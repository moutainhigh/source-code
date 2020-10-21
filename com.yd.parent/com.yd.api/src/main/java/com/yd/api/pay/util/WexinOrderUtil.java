package com.yd.api.pay.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.yd.api.result.common.WbPayOrderResult;
import com.yd.api.result.common.WbWeixinAccountResult;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yd.core.utils.BusinessException;

public class WexinOrderUtil {
	private final static Logger LOG = LoggerFactory.getLogger(WexinOrderUtil.class);
	
	public static Map<String,String> wechatPay(WbWeixinAccountResult wxPayAccount, WbPayOrderResult payOrder, String userIp, String openId, String notifyUrl) throws BusinessException{
		// step 2:去微信那边调用统一下单接口
		String appid = wxPayAccount.getAppId();
		String mchId = wxPayAccount.getMchId();
		String deviceInfo = "WEB";
		String body = payOrder.getTitle();
		String nonceStr = RandomHelper.getNonceStr();
		String tradeType = "JSAPI";
		String weixinSignKey = wxPayAccount.getSignKey();

		Integer totalFee=new BigDecimal(""+payOrder.getMoney()).multiply(new BigDecimal("100")).intValue();
		
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", appid);
		parameters.put("mch_id", mchId);
		parameters.put("body", body);
		parameters.put("device_info", deviceInfo);
		parameters.put("nonce_str", nonceStr);
		parameters.put("out_trade_no", "payOrder-"+payOrder.getId());
		parameters.put("spbill_create_ip", userIp);
		parameters.put("total_fee", totalFee+"");
		parameters.put("trade_type", tradeType);
		parameters.put("openid", openId);
		parameters.put("notify_url", notifyUrl);
		String characterEncoding = "UTF-8";
		String mySign = WeixinSignHelper.createSign(characterEncoding, parameters, weixinSignKey);

		UnifiedOrder order = new UnifiedOrder();
		order.setAppid(appid);
		order.setMch_id(mchId);
		order.setNonce_str(nonceStr);
		order.setSign(mySign);
		order.setBody(body);
		order.setDevice_info(deviceInfo);
		order.setNotify_url(notifyUrl);
		order.setOut_trade_no("payOrder-"+payOrder.getId());
		order.setTotal_fee(totalFee);
		order.setTrade_type(tradeType);
		order.setSpbill_create_ip(userIp);
		order.setOpenid(openId);
		LOG.info("===去微信那下订单：客户订单号:" + "payOrder-"+payOrder.getId());
		long begin = System.currentTimeMillis();
		try {
			String ret = HttpClientUtil.postBody("https://api.mch.weixin.qq.com/pay/unifiedorder", order.toXml());
			long end = System.currentTimeMillis();
			LOG.info("去微信支付=" + "payOrder-"+payOrder.getId() + "去微信下订单花费时间：" + (end - begin));
			LOG.info(ret);
			// 下单成功后
			Map<String, String> retMap = MessageUtil.parseXml(ret);
			
			if ("SUCCESS".equals(retMap.get("return_code")) && "SUCCESS".equals(retMap.get("result_code"))) {
				// 统一下单成功
				String prepayId = retMap.get("prepay_id");
				SortedMap<String, String> model = new TreeMap<String, String>();
				model.put("appId", appid);
				model.put("timeStamp", new Date().getTime() / 1000 + "");
				model.put("nonceStr", RandomHelper.getNonceStr());
				model.put("package", "prepay_id=" + prepayId);
				model.put("signType", "MD5");
				String modelSign = WeixinSignHelper.createSign(characterEncoding, model, weixinSignKey);
				model.put("paySign", modelSign);

				
				Map<String, String> map = new HashMap<String, String>();
				map.put("appId", model.get("appId"));
				map.put("timeStamp", model.get("timeStamp"));
				map.put("nonceStr", model.get("nonceStr"));
				map.put("package", model.get("package"));
				map.put("signType", model.get("signType"));
				map.put("paySign", model.get("paySign"));
				map.put("returnUrl", payOrder.getReturnUrl());
				
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
