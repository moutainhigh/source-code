package com.yd.api.pay.util;

import org.apache.commons.lang.RandomStringUtils;

public class RandomHelper {
	public static String getNonceStr(){
		String nonceStr = RandomStringUtils.random(8, "123456789");
		
		return nonceStr;
	}
}