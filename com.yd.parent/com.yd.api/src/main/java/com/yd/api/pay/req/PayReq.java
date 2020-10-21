package com.yd.api.pay.req;


import java.io.Serializable;

import com.yd.api.pay.EnumPayMethod;

public class PayReq implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer merchantId;
	private Double amount;
	private String subject;
	private String outOrderId;
	private String prepareId;
	private String notifyUrl;
	private String returnUrl;
	private EnumPayMethod payMethod;
	private String userIp;
	private String openId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getOutOrderId() {
		return outOrderId;
	}
	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
	
	public String getPrepareId() {
		return prepareId;
	}
	public void setPrepareId(String prepareId) {
		this.prepareId = prepareId;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public EnumPayMethod getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(EnumPayMethod payMethod) {
		this.payMethod = payMethod;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
