package com.yg.web.entity;

import com.yg.core.utils.StringUtil;
import com.yg.web.util.ControllerUtil;
import com.yg.web.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 *                       
 * @Filename SiteContext.java
 *
 * @Description m站上下文. m站很多地方使用cookie作为临时数据控制网站的一些功能，但是随着系统Cookie数据的增多，越来越难以管理。
 * SiteContext的主要目的即对关系到“网站功能”方面的cookie进行统一的管理
 *
 * @History
 *
 */
public class SiteContext {
	
	public static final String	COOKIE_PAY_METHODS	= "pay_methods";		//cookie记录限定的支付方式
																			
	private HttpServletRequest	request;
	private HttpServletResponse	response;
	
	private PassportContext passport;									//登录用户信息
																			
																			
	private String				payMethods;								//指定支付方式
																			
	private String 				platform;								//平台
																			
																			
	private String				remoteIp;									//远程用户ID
																			
	private String				browser;									//浏览器类型
	
	private String 				referer;
	private Map<String, String>	requestHeader;
	private Map<String, String>	requestBody;
																			
	public void init(HttpServletRequest req, HttpServletResponse res) {
		this.request = req;
		this.response = res;
		
		String ip = WebUtil.getVisitorIp(req);
		this.setRemoteIp(ip);
		
		//登录状态
		PassportContext passport = ControllerUtil.getPasspotContext(req,res);
		if (passport != null ) {
			setPassport(passport, false);
		}
		
		//wrap信息
		String appPlatform = request.getHeader(ControllerUtil.APP_PLATFORM);
		if (StringUtil.isEmpty(appPlatform)) {
			appPlatform = ControllerUtil.getCookieValue(request, "platform");//从cookie里面去一次platform
		}
		if (!StringUtil.isEmpty(appPlatform)) {
			setPlatform(appPlatform);
		}else if(!StringUtil.isEmpty(ControllerUtil.getCookieValue(request, "passpotPlatform"))){ 
			setPlatform(ControllerUtil.getCookieValue(request, "passpotPlatform"));
		}else{
			setPlatform("wap");
		}
		
		String userAgent = request.getHeader("User-Agent");
		//优先从cookie里去取平台
		
		setRequestBody(buildHttpBodytMap(req));
		
		
		setRequestHeader(buildHttpHeaderMap(req));
	}
	
	
	public String getPlatform() {
		return platform;
	}


	public void setPlatform(String platform) {
		this.platform = platform;
	}


	public PassportContext getPassport() {
		return passport;
	}
	
	public void setPassport(PassportContext passport, boolean updateCookie) {
		this.passport = passport;
		if (updateCookie) {
			ControllerUtil.addUserCookie(response, passport);//保存cookie
		}
	}
	
	public String getPayMethods() {
		return payMethods;
	}
	
	public void setPayMethods(String payMethods, boolean updateCookie) {
		this.payMethods = payMethods;
		if (updateCookie) {
			ControllerUtil.addCookie(response, COOKIE_PAY_METHODS, payMethods, 24 * 60 * 60);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, String> buildHttpHeaderMap(HttpServletRequest httpRequest) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration names = httpRequest.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = httpRequest.getHeader(name);
			map.put(name, value);
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, String> buildHttpBodytMap(HttpServletRequest httpRequest) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration names = httpRequest.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = httpRequest.getParameter(name);
			map.put(name, value);
		}
		return map;
	}
	
	
	
	public String getRemoteIp() {
		return remoteIp;
	}
	
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	
	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}


	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}


	public Map<String, String> getRequestBody() {
		return requestBody;
	}


	public void setRequestBody(Map<String, String> requestBody) {
		this.requestBody = requestBody;
	}


	public String getReferer() {
		return referer;
	}


	public void setReferer(String referer) {
		this.referer = referer;
	}

	@Override
	public String toString() {
		return String
			.format(
				"SiteContext [request=%s, response=%s, passport=%s,  payMethods=%s, remoteIp=%s, browser=%s]",
				request, response,  passport,  payMethods,
				 remoteIp, browser);
	}
	
}
