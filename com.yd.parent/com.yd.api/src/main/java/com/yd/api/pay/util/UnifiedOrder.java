package com.yd.api.pay.util;

import org.apache.commons.lang.StringUtils;

public class UnifiedOrder {
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String body;
	private String device_info;
	private String attach;
	private String out_trade_no;
	private int total_fee;
	private String spbill_create_ip;
	private String notify_url;
	private String trade_type = "JSAPI";
	private String openid;

	public String toXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<appid>" + this.appid + "</appid>");

		if(StringUtils.isNotEmpty(this.body)){
			sb.append("<body>" + this.body + "</body>");
		}
		if(StringUtils.isNotEmpty(this.device_info)){
			sb.append("<device_info>"+this.device_info+"</device_info>");
		}

		if(StringUtils.isNotEmpty(this.mch_id)){
			sb.append("<mch_id>" + this.mch_id + "</mch_id>");
		}

		if(StringUtils.isNotEmpty(this.nonce_str)){
			sb.append("<nonce_str>" + this.nonce_str + "</nonce_str>");
		}

		if(StringUtils.isNotEmpty(this.notify_url)){
			sb.append("<notify_url>" + this.notify_url + "</notify_url>");
		}
		
		if(StringUtils.isNotEmpty(this.openid)){
			sb.append("<openid>" + this.openid + "</openid>");
		}

		if(StringUtils.isNotEmpty(this.out_trade_no)){
			sb.append("<out_trade_no>" + this.out_trade_no + "</out_trade_no>");
		}

		if(StringUtils.isNotEmpty(this.spbill_create_ip)){
			sb.append("<spbill_create_ip>" + this.spbill_create_ip + "</spbill_create_ip>");
		}
		
		sb.append("<total_fee>" + this.total_fee + "</total_fee>");
		
		if(StringUtils.isNotEmpty(this.trade_type)){
			sb.append("<trade_type>" + this.trade_type + "</trade_type>");
		}

		if(StringUtils.isNotEmpty(this.sign)){
			sb.append("<sign>" + this.sign + "</sign>");
		}
		sb.append("</xml>");

		return sb.toString();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}

