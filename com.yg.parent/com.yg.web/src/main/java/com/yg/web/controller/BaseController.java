package com.yg.web.controller;

import com.yg.core.utils.PagerInfo;
import com.yg.web.entity.SiteContextThreadLocal;
import com.yg.web.util.ControllerUtil;
import com.yg.web.util.WebUtil;
import com.yg.web.entity.SiteContextThreadLocal;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	public Integer getCurrMerchantId(HttpServletRequest request){
		String merchantId = SiteContextThreadLocal.get().getPassport().getPairMap().get("merchantId");
		return merchantId == null ? null : new Integer(merchantId);
	}
	
	public Integer getCurrUserId(HttpServletRequest request) {
		String userId = SiteContextThreadLocal.get().getPassport().getPairMap().get("userId");
		return userId == null ? null : new Integer(userId);
	}
	
	public Integer getUtmSource(HttpServletRequest request) {
		Integer utmSource=0;
		String utmSourceStr=getParamFromCookieOrRequest(request, "utmSource");
		if(StringUtils.isNotEmpty(utmSourceStr) && StringUtils.isNumeric(utmSourceStr)) {
			utmSource=Integer.parseInt(utmSourceStr);
		}
		
		return utmSource;
	}
	
	public Integer getIntAttr(HttpServletRequest request,String name){
		String value=WebUtil.getParameter(request, name);
		if(StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)){
			return Integer.parseInt(value);
		}
		return null;
	}
	public Integer getIntAttr(HttpServletRequest request,String name,Integer defaultValue){
		String value=WebUtil.getParameter(request, name);
		if(StringUtils.isEmpty(value) || !StringUtils.isNumeric(value)){
			return defaultValue;
		}
		return Integer.parseInt(value);
	}
	
	
	public String getParamFromCookieOrRequest(HttpServletRequest request,String name) {
		String value = WebUtil.getParameter(request, name);
		if(StringUtils.isNotEmpty(value)) {
			return value;
		}
		return ControllerUtil.getCookieValue(request, name);
	}
	
	public PagerInfo getPageInfo(HttpServletRequest request) {
		int pageIndex = getIntAttr(request, "pageIndex", 1);
		int pageSize = getIntAttr(request, "pageSize", 10);
		return new PagerInfo(pageSize, pageIndex);
	}

	public PagerInfo getPageInfo(HttpServletRequest request, Integer pageIndex, Integer pageSize) {
		pageIndex = getIntAttr(request, "pageIndex", pageIndex);
		pageSize = getIntAttr(request, "pageSize", pageSize);
		return new PagerInfo(pageSize, pageIndex);
	}
}
