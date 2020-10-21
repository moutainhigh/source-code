package com.yd.api.pay.result;


import java.io.Serializable;
import java.util.Map;

public class PayParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String outOrderId;
	private String prepareId;
	private double amount;
	private String subject;
	private String payMethod;
	private String goUrl;
	private Map<String,String> weixinOrderInfo;
	
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getGoUrl() {
		return goUrl;
	}
	public void setGoUrl(String goUrl) {
		this.goUrl = goUrl;
	}
	public Map<String, String> getWeixinOrderInfo() {
		return weixinOrderInfo;
	}
	public void setWeixinOrderInfo(Map<String, String> weixinOrderInfo) {
		this.weixinOrderInfo = weixinOrderInfo;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("PayParam{");
		sb.append("outOrderId='").append(outOrderId).append('\'');
		sb.append(", prepareId='").append(prepareId).append('\'');
		sb.append(", amount=").append(amount);
		sb.append(", subject='").append(subject).append('\'');
		sb.append(", payMethod='").append(payMethod).append('\'');
		sb.append(", goUrl='").append(goUrl).append('\'');
		sb.append(", weixinOrderInfo=").append(weixinOrderInfo);
		sb.append('}');
		return sb.toString();
	}
}
