package com.yd.api.wx.bean;

public class WxKefeTextMessage {
	private String touser;
	private String msgtype="text";
	private WxKefeText text;
	
	public WxKefeTextMessage(String openId,String content){
		this.touser=openId;
		WxKefeText t=new WxKefeText();
		t.setContent(content);
		this.text=t;
	}
	
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public WxKefeText getText() {
		return text;
	}

	public void setText(WxKefeText text) {
		this.text = text;
	}

	public static class WxKefeText{
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
	}
}
