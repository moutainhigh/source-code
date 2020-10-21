package com.yd.api.wx.res;

import com.yd.api.pay.util.XStreamCDATA;
import com.yd.api.wx.bean.EnumWxReqMsgType;

/**
 * 文本消息
 */
public class TextMessage extends BaseMessage {
	public TextMessage() {
		super.setMsgType(EnumWxReqMsgType.text.getCode());
	}

	public TextMessage(BaseMessage message) {
		super.setCreateTime(message.getCreateTime());
		super.setFromUserName(message.getFromUserName());
		super.setToUserName(message.getToUserName());
		super.setMsgType(EnumWxReqMsgType.text.getCode());
	}

	// 回复的消息内容
	@XStreamCDATA
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
