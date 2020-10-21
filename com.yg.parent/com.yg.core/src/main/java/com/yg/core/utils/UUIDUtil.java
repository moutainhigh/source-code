package com.yg.core.utils;

import java.util.UUID;

public class UUIDUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getRandomSn() {
		return String.valueOf(System.currentTimeMillis());
	}
}
