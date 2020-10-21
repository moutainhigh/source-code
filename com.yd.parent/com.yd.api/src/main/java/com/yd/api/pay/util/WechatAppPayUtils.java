package com.yd.api.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.core.utils.BusinessException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * 微信公众号支付工具类
 * @author wuyc
 * created 2019/12/25 18:32
 **/
public class WechatAppPayUtils {

    private final static Logger LOG = LoggerFactory.getLogger(WechatAppPayUtils.class);

    private static String TRADE_TYPE = "APP";

    private final static String DEVICE_INFO = "WEB";

    private final static String URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static Map<String,String> wechatAppPay(String appId, String macId, String signKey, String orderId, String title,
                                                  String userIp, String returnUrl, String notifyUrl, Integer totalFee) {
        String randomStr = RandomHelper.getNonceStr();
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("appid", appId);
        parameters.put("mch_id", macId);
        parameters.put("device_info", DEVICE_INFO);
        parameters.put("nonce_str", randomStr);
        parameters.put("body", title);
        parameters.put("out_trade_no", orderId);
        parameters.put("total_fee", totalFee + "");
        parameters.put("spbill_create_ip", userIp);
        parameters.put("notify_url", notifyUrl);
        parameters.put("trade_type", TRADE_TYPE);

        String characterEncoding = "UTF-8";
        String mySign = WeixinSignHelper.createSign(characterEncoding, parameters, signKey);
        UnifiedOrder order = new UnifiedOrder();
        order.setAppid(appId);
        order.setMch_id(macId);
        order.setNonce_str(randomStr);
        order.setSign(mySign);
        order.setBody(title);
        order.setDevice_info(DEVICE_INFO);
        order.setNotify_url(notifyUrl);
        order.setOut_trade_no(orderId);
        order.setTotal_fee(totalFee);
        order.setTrade_type(TRADE_TYPE);
        order.setSpbill_create_ip(userIp);
        long begin = System.currentTimeMillis();
        try {
            LOG.info("去微信APP支付outOrderId=" + orderId + "入参为" + JSON.toJSONString(order.toXml()));
            String ret = HttpClientUtil.postBody(URL, order.toXml());
            long end = System.currentTimeMillis();
            LOG.info("去微信APP支付outOrderId=" + orderId + "下订单花费时间：" + (end - begin) + ",返回值=" + ret);
            Map<String, String> retMap = MessageUtil.parseXml(ret);
            if ("SUCCESS".equals(retMap.get("return_code")) && "SUCCESS".equals(retMap.get("result_code"))) {
                String prepayId = retMap.get("prepay_id");
                SortedMap<String, String> model = new TreeMap<>();
                model.put("appid", appId);
                model.put("timestamp", new Date().getTime() / 1000 + "");
                model.put("prepayid", prepayId);
                model.put("noncestr", RandomHelper.getNonceStr());
                model.put("package", "Sign=WXPay");
                model.put("partnerid", macId);
                String modelSign = WeixinSignHelper.createSign(characterEncoding, model, signKey);
                model.put("paysign", modelSign);

                Map<String, String> map = new HashMap<String, String>();
                map.put("appId", model.get("appid"));
                map.put("timeStamp", model.get("timestamp"));
                map.put("nonceStr", model.get("noncestr"));
                map.put("package", model.get("package"));
                map.put("paySign", model.get("paysign"));
                map.put("prepayId", model.get("prepayid"));
                map.put("partnerId", model.get("partnerid"));
                return map;
            } else {
                LOG.error("=============微信APP统一下单失败=========="+JSONObject.toJSONString(retMap));
                throw new BusinessException("err_unifiedorder:" + retMap.get("return_code"), "微信APP统一下单失败");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new BusinessException("err_ClientProtocolException", "ClientProtocolException");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("err_IOException", "IOException");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("err_Exception", "Exception");
        }
    }

    public static void main(String[] args) {
        String appId = "wxa86f8b4f13451660";
        String macId = "1502794471";
        String signKey = "BsQqimhyZW7rAllvv6kK30GpSMExTXG6";
        String orderId = "guijiApp-002";
        String title = "测试app支付002";
        String userIp = "127.0.0.1";
        Integer totalFee = 1;
        String notifyUrl = "http://prev-saas.guijitech.com/yd/callback//weixin/merchantMemberPay/notify";
        String returnUrl = "http://prev-saas.guijitech.com/store/front/" + 10049 + "/index/my";
        Map<String, String> resultMap = wechatAppPay(appId, macId, signKey, orderId, title, userIp, returnUrl, notifyUrl, totalFee);
        System.out.println(JSON.toJSONString(resultMap));
    }

}
