package com.yd.api.pay.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;


public class WeixinSignHelper {
	/**
	 * 微信支付签名算法sign
	 * 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<String, String> parameters,String signKey) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,String>> es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String,String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> entry = it.next();
			String k = (String) entry.getKey();
			String v = entry.getValue();
			if (StringUtils.isNotEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key="+signKey);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
}
