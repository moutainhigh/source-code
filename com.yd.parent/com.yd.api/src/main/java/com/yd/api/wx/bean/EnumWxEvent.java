package com.yd.api.wx.bean;

public enum EnumWxEvent {
	subscribe("subscribe","关注"),
	unsubscribe("unsubscribe","取消订阅"),
	scan("scan","已关注的再次扫描"),
	location("location","上报地理位置"),
	click("click","自定义菜单事件"),
	view("view","点击菜单跳转链接时的事件推送");
	private String code;
	private String name;
	private EnumWxEvent(String code,String name) {
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
