package com.yd.api.pay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.core.utils.BusinessException;
import org.apache.log4j.Logger;

public class MD5Util {
	private static Logger	LOG	= Logger.getLogger("debugAppender");
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	 public static void main(String[] args) {
//		System.out.println(getMD5Str(getMD5Str("111111")));
		 getAllocation("15166795821");
	}
	/**
	* MD5 加密  
	* @param str 字符串
	* @return 返回加密后的字符串
	*/
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			
			messageDigest.reset();
			
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			LOG.info("getMD5Str error", e);
		} catch (UnsupportedEncodingException e) {
			LOG.info("getMD5Str erro", e);
		}
		
		byte[] byteArray = messageDigest.digest();
		
		StringBuffer md5StrBuff = new StringBuffer();
		
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		
		return md5StrBuff.toString();
	}
	
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	
	
	/**
	 * chck password
	 * @param newStr
	 *        no encrypt password
	 * @param oldMD5Str
	 * 
	 * @return
	 */
	public final static boolean checkMD5(String newStr, String oldMD5Str) {
		String temp = getMD5Str(newStr);
		if (temp != null && temp.equals(oldMD5Str)) {
			return true;
		} else {
			return false;
		}
	}


	public static void getAllocation(String mobile) throws BusinessException {
		String url = "http://apis.juhe.cn/mobile/get";// 查询手机归属地的聚合接口地址
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", mobile);
		params.put("key", "7ecc6080360bb8430048cdc03391d971");
		String rStr = HttpClientUtil.get(url, params);
		JSONObject rsJson = JSONObject.parseObject(rStr);
		String resultcode = rsJson.getString("resultcode");
		System.out.println(JSON.toJSONString(resultcode));
		if (resultcode.equals("200")) {
//            MobileArea mobileArea = JSONObject.parseObject(rsJson.getString("result"), MobileArea.class);
			return ;
		}
		throw new BusinessException(rsJson.getString("reason"));
	}
}
