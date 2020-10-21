package com.yd.api.service.common;

import com.yd.api.wx.bean.WxMessageRequest;

public interface AutoReplyMessageService {

	String reply(WxMessageRequest mr);

}
