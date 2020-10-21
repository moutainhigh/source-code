package com.yd.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.yd.api.service.LoginService;

@Service(dynamic = true)
public class LoginServiceImpl implements LoginService {

	@Override
	public boolean isLoginMerchant(String merchantId, String sessionId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoginUser(String userId, String sessionId) {
		// TODO Auto-generated method stub
		return false;
	}

}
