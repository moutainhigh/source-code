package com.yg.core.utils;

public class PasswordUtil {
	private static final String	ENCRYPT_AUTH_KEY	= "{_ENCRYPT_AUTN_KEY_}";
	
	public static String encryptPassword(String password) {
		if (StringUtil.isEmpty(password)) {
			throw new RuntimeException("密码不能为空");
		}
		password = password.trim() + ENCRYPT_AUTH_KEY;
		return MD5Util.getMD5Str(password);
	}
	
	public static void main(String[] args) {
		System.out.println(encryptPassword("e10adc3949ba59abbe56e057f20f883e"));
	}
}