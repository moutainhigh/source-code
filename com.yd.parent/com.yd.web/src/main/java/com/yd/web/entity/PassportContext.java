package com.yd.web.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yd.core.utils.StringUtil;

public class PassportContext {
	private static Logger LOG=Logger.getLogger(PassportContext.class);
	private Map<String, String>	pairMap;
	
	
	private String				cookieName;
	
	public static PassportContext init(JSONObject object) {
		PassportContext context = new PassportContext();
		for (String key : object.keySet()) {
			context.addPairMap(key, object.getString(key));
		}
		return context;
	}
	
	public String getCookieName() {
		return cookieName;
	}
	
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	
	public String getCookieDomain() {
		return cookieDomain;
	}
	
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}
	
	private String	cookieDomain;
	
	public PassportContext() {
		this.pairMap = new HashMap<String, String>();
	}
	
	public Map<String, String> fill(String cookieValue) {
		return StringUtil.urlStringToMap(cookieValue, this.pairMap);
	}
	
	public String toCookieValue() {
		String cookieValue = StringUtil.mapToUrlString(this.pairMap);
		return cookieValue;
	}
	
	public void addPairMap(String key, String value) {
		if(StringUtil.isEmpty(value)){
			return;
		}
		this.pairMap.put(key, value);
	}
	
	public Map<String, String> getPairMap() {
		return pairMap;
	}
	
	public void setPairMap(Map<String, String> pairMap) {
		this.pairMap = pairMap;
	}
	
	
	@Override
	public String toString() {
		return String.format(
			"PassportContext [pairMap=%s, cookieName=%s, cookieDomain=%s]", pairMap, 
			 cookieName, cookieDomain);
	}
	
}
