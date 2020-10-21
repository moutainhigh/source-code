package com.yd.api.wx.bean;

public enum EnumWeixinAuthorizeScope {
	BASE("snsapi_base"),
	USER_INFO("snsapi_userinfo");
	private String code;
	private EnumWeixinAuthorizeScope(String code) {
		this.code=code;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
