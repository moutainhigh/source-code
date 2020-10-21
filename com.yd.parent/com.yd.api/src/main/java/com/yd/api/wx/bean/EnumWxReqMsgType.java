package com.yd.api.wx.bean;

public enum EnumWxReqMsgType {
	text("text","文本消息"),
	image("image","图片消息"),
	voice("voice","语音消息"),
	video("video","视频消息"),
	shortvideo("shortvideo","小视频消息"),
	location("location","地理位置消息"),
	link("link","链接消息"),
	event("event","事件消息");
	
	private String code;
	private String name;
	private EnumWxReqMsgType(String code,String name) {
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
