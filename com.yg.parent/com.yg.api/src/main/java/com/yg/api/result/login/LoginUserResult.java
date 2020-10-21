package com.yg.api.result.login;

import java.io.Serializable;

public class LoginUserResult implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer userId;
	private Integer merchantId;
	private String sessionId;
	
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
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
