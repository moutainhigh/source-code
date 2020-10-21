package com.yd.api.wx.bean;

public enum EnumWxMenuType {
	CLICK("click"),
	VIEW("view"),
	MINI_APP("miniprogram");
	
	private String code;
	private EnumWxMenuType(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		System.out.println("--toString :"+code);
		return code;
	}
}
