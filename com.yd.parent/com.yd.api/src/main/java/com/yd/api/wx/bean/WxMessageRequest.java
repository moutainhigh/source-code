package com.yd.api.wx.bean;

import java.io.Serializable;

public class WxMessageRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	private String fromUserName;
	private String toUserName;
	private String msgType;//text	image	voice	video	shortvideo	location	link	
	private String content;
	private String msgId;
	private String event;
	private String eventKey;
	private String requestIp;
	
	private String mpWeixinPinyin;
	
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public String getMpWeixinPinyin() {
		return mpWeixinPinyin;
	}
	public void setMpWeixinPinyin(String mpWeixinPinyin) {
		this.mpWeixinPinyin = mpWeixinPinyin;
	}


}
