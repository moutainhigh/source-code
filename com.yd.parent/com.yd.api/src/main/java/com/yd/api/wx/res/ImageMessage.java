package com.yd.api.wx.res;

import com.yd.api.wx.bean.EnumWxResMsgType;

public class ImageMessage  extends BaseMessage{
	private String mediaId;
	public ImageMessage(){
		super.setMsgType(EnumWxResMsgType.image.getCode());
	}
	public ImageMessage(BaseMessage message){
		super.setCreateTime(message.getCreateTime());
		super.setFromUserName(message.getFromUserName());
		super.setToUserName(message.getToUserName());
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	
}
