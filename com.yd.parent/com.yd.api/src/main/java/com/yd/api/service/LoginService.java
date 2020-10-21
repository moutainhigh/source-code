package com.yd.api.service;

public interface LoginService {

	boolean isLoginMerchant(String merchantId, String sessionId);

	boolean isLoginUser(String userId, String sessionId);

}
