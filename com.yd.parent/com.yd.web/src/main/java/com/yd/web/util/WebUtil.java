package com.yd.web.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class WebUtil {
	public static final String EMPTY_STRING = "";

	/**
	 * 获取url传输的参数
	 * @param request
	 * @param name
	 * @return
	 */
	public static final String getParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if(StringUtils.isEmpty(value)){
			return null;
		}
		return value;
	}
	public static final String getParameter(HttpServletRequest request, String name,String defaultValue) {
		String value = request.getParameter(name);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}
		return value;
	}	
	/**
	 * 获取cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie ck : cookies) {
				if (ck.getName().equals(name)) {
					return ck.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getVisitorIp(HttpServletRequest request) {
		String ipAddress = null;
		// // A10 指定
		// String clientIp = request.getHeader("Client_IP");
		// if (clientIp != null && !"".equals(clientIp)) {
		// return clientIp;
		// }
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP;
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public static boolean isRobot(HttpServletRequest req) {
		String ua = req.getHeader("user-agent");

		if (StringUtils.isBlank(ua)) {
			return false;
		}
		if (ua != null) {
			if ((ua.indexOf("Baiduspider") != -1) || (ua.indexOf("Googlebot") != -1) || (ua.indexOf("sogou") != -1)
					|| (ua.indexOf("sina") != -1) || (ua.indexOf("iaskspider") != -1) || (ua.indexOf("ia_archiver") != -1)
					|| (ua.indexOf("Sosospider") != -1) || (ua.indexOf("YoudaoBot") != -1) || (ua.indexOf("yahoo") != -1)
					|| (ua.indexOf("yodao") != -1) || (ua.indexOf("MSNBot") != -1) || (ua.indexOf("spider") != -1)
					|| (ua.indexOf("Twiceler") != -1) || (ua.indexOf("Sosoimagespider") != -1) || (ua.indexOf("naver.com/robots") != -1)
					|| (ua.indexOf("Nutch") != -1) || (ua.indexOf("spider") != -1))
				return true;
		}
		return false;
	}

	/**
	 * 禁止缓存
	 * 
	 * @param request
	 * @param response
	 */
	public static void setNoCache(HttpServletRequest request, HttpServletResponse response) {
		// 禁止缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");
	}

	public static final String getString(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		String stringValue = EMPTY_STRING;
		if (value != null) {
			/*
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8").trim();
			} catch (UnsupportedEncodingException e) {
				// Ignore
			}
			*/
			return value;
		}
		return stringValue;
	}
}
