package com.yd.api.wx.bean;

public enum EnumWxResMsgType {
	text("text","文本消息"),
	image("image","图片消息"),
	news("news","图文消息");
	
	private String code;
	private String name;
	private EnumWxResMsgType(String code,String name) {
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
