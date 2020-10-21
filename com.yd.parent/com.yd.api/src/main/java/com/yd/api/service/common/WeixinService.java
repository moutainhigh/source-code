package com.yd.api.service.common;

import com.yd.api.result.common.WbWeixinAccountResult;

public interface WeixinService {

	WbWeixinAccountResult getByWeixinAccountByType(String type);

	void updateAccessTokenWithTicket(WbWeixinAccountResult account);

}
