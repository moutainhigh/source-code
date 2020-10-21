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
public class WexinPublicPayUtils {

    private final static Logger LOG = LoggerFactory.getLogger(WexinPublicPayUtils.class);

    private static String TRADE_TYPE = "JSAPI";

    private final static String DEVICE_INFO = "WEB";

    /**
     * 微信公众号下单接口
     * @param totalFee  支付金额
     * @param appId     appId
     * @param macId     matchId
     * @param signKey   微信签名key
     * @param orderId   系统内部订单号
     * @param title     标题
     * @param userIp    用户id
     * @param openId    用户openId
     * @param returnUrl 回调地址
     * @param notifyUrl 通知地址
     * @return
     * @throws BusinessException
     */
    public static Map<String,String> wechatPay(Integer totalFee, String appId, String macId, String signKey, String orderId, String title,
                                               String userIp, String openId, String returnUrl, String notifyUrl) throws BusinessException {
        // 去微信那边调用统一下单接口
        String nonceStr = RandomHelper.getNonceStr();

        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", appId);
        parameters.put("mch_id", macId);
        parameters.put("body", title);
        parameters.put("device_info", DEVICE_INFO);
        parameters.put("nonce_str", nonceStr);
        parameters.put("out_trade_no", orderId);
        parameters.put("spbill_create_ip", userIp);
        parameters.put("total_fee", totalFee + "");
        parameters.put("trade_type", TRADE_TYPE);
        parameters.put("openid", openId);
        parameters.put("notify_url", notifyUrl);
        String characterEncoding = "UTF-8";
        String mySign = WeixinSignHelper.createSign(characterEncoding, parameters, signKey);

        UnifiedOrder order = new UnifiedOrder();
        order.setAppid(appId);
        order.setMch_id(macId);
        order.setNonce_str(nonceStr);
        order.setSign(mySign);
        order.setBody(title);
        order.setDevice_info(DEVICE_INFO);
        order.setNotify_url(notifyUrl);
        order.setOut_trade_no(orderId);
        order.setTotal_fee(totalFee);
        order.setTrade_type(TRADE_TYPE);
        order.setSpbill_create_ip(userIp);
        order.setOpenid(openId);
        LOG.info("===去微信那下订单：客户订单号:" + orderId);
        long begin = System.currentTimeMillis();
        try {
            LOG.info("去微信支付outOrderId=" + orderId + "入参为" + JSON.toJSONString(order.toXml()));
            String ret = HttpClientUtil.postBody("https://api.mch.weixin.qq.com/pay/unifiedorder", order.toXml());
            long end = System.currentTimeMillis();
            LOG.info("去微信支付outOrderId=" + orderId + "下订单花费时间：" + (end - begin));
            LOG.info("去微信支付outOrderId=" + orderId + "返回值=" + ret);

            // 下单成功后
            Map<String, String> retMap = MessageUtil.parseXml(ret);

            if ("SUCCESS".equals(retMap.get("return_code")) && "SUCCESS".equals(retMap.get("result_code"))) {
                // 统一下单成功
                String prepayId = retMap.get("prepay_id");
                SortedMap<String, String> model = new TreeMap<String, String>();
                model.put("appId", appId);
                model.put("timeStamp", new Date().getTime() / 1000 + "");
                model.put("nonceStr", RandomHelper.getNonceStr());
                model.put("package", "prepay_id=" + prepayId);
                model.put("signType", "MD5");
                String modelSign = WeixinSignHelper.createSign(characterEncoding, model, signKey);
                model.put("paySign", modelSign);

                Map<String, String> map = new HashMap<String, String>();
                map.put("appId", model.get("appId"));
                map.put("timeStamp", model.get("timeStamp"));
                map.put("nonceStr", model.get("nonceStr"));
                map.put("package", model.get("package"));
                map.put("signType", model.get("signType"));
                map.put("paySign", model.get("paySign"));
                map.put("returnUrl", returnUrl);
                return map;
            } else {
                LOG.error("=============统一下单失败=========="+JSONObject.toJSONString(retMap));
                throw new BusinessException("err_unifiedorder:"+retMap.get("return_code"), "统一下单失败");
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

}
