package com.yd.web.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.StringUtil;
import com.yd.web.entity.PassportContext;


public class ControllerUtil {
	public static final String	APP_LOGIN				= "applogin";
	public static final String	APP_PLATFORM			= "APP_PLATFORM";
	public static final String	APP_PAY_METHODS			= "APP_PAY_METHODS";
	
	public static final String	COOKIE_CLIENT_NAME	= PropertiesHelp.getProperty("admin.cookie.client.name", "456");
	public static final String	COOKIE_PASSPORT_NAME	= PropertiesHelp.getProperty("admin.cookie.passport.name", "123");
	public static final String	COOKIE_DOMAIN			= PropertiesHelp.getProperty("admin.cookie.domain", "456");

	public static final String KQ_OPEN_ID = "kqOpenId";
	
	public static String getCookieValue(HttpServletRequest request, String name) {
		if (StringUtils.isNotEmpty(name) && request != null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static String getCookieValue(HttpServletRequest request, String name,String defaultValue) {
		if (StringUtils.isNotEmpty(name) && request != null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * 获取用户信息
	 * @param request
	 * @return
	 */
	public static PassportContext getPasspotContext(HttpServletRequest request, HttpServletResponse res) {
		PassportContext context = new PassportContext();
		
		//优先尝试读取APP head带来的数据
		String loginData = request.getHeader(APP_LOGIN);
		//再次尝试读取cookie数据
		if (StringUtil.isEmpty(loginData)) {
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					String name = cookie.getName();
					//log4JUtils.info("cookie name : "+name+"value "+cookie.getValue());
					if (COOKIE_PASSPORT_NAME.equals(name)) {
						loginData = cookie.getValue();
					}
				}
			}
		}
		
		if (!StringUtil.isEmpty(loginData)) {
			context.fill(loginData);
		}
		
		return context;
	}
	
	public static void delTempPasspotContext(HttpServletResponse response) {
		String cookieName = "mcart_tmp_passport";
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public static PassportContext getTempPasspotContext(HttpServletRequest request) {
		PassportContext context = new PassportContext();
		String loginData = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				String name = cookie.getName();
				if ("mcart_tmp_passport".equals(name)) {
					loginData = cookie.getValue();
				}
			}
		}
		if (!StringUtil.isEmpty(loginData)) {
			context.fill(loginData);
		}
		return context;
	}
	
	public static void addTempUserCookie(HttpServletResponse response, PassportContext passport) {
		String cookieName = "mcart_tmp_passport";
		Cookie cookie = new Cookie(cookieName, passport.toCookieValue());
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(24 * 60 * 60);
		response.addCookie(cookie);
	}
	
	/**
	 * 将用户信息发送到手机客户的cookie中
	 */
	public static void addUserCookie(HttpServletResponse response, PassportContext passport) {
		String cookieName = COOKIE_PASSPORT_NAME;
		System.out.println(JSON.toJSONString(passport));
		addCookie(response, cookieName, passport.toCookieValue(), 365 * 24 * 60 * 60);
	}

	/**
	 * 将用户信息发送到手机客户的cookie中
	 */
	public static void addAppCookie(HttpServletResponse response, PassportContext passport) {
		Cookie cookie = new Cookie(APP_LOGIN, passport.toCookieValue());
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(7 * 24 * 60 * 60);
		response.addCookie(cookie);
	}
	
	public static void addCookie(HttpServletResponse response, String cookieName, String value) {
		addCookie(response, cookieName, value, 365 * 24 * 60 * 60);
	}
	public static void addCookie7Day(HttpServletResponse response, String cookieName, String value) {
		addCookie(response, cookieName, value, 7 * 24 * 60 * 60);
	}	
	public static void addCookie(HttpServletResponse response, String cookieName, String value,
									int maxAge) {
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
		System.out.println("cookie: " + JSON.toJSONString(cookie));
		System.out.println("cookieName:" + cookieName);
		System.out.println("cookieDomain:" + COOKIE_DOMAIN);
	}
	
	public static void delUserCookie(HttpServletResponse response) {
		String cookieName = COOKIE_PASSPORT_NAME;
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public static void delCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setPath("/");
		cookie.setDomain(COOKIE_DOMAIN);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	
	
	
}
