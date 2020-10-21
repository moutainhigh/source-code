package com.yd.service.crawer.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 模拟用户浏览页面 仅仅浏览产生浏览记录和用户cookei
 * 
 * @author Administrator
 *
 */
public class MockViewHelper {
	private static final Logger LOG = Logger.getLogger(MockViewHelper.class);
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

	
	public static MockViewRes views(String url) {
		Connection con = Jsoup.connect(url);
		con.header("User-Agent", userAgent);

		MockViewRes res=new MockViewRes();
		Map<String,String> cookies=new HashMap<String,String>();
		try {
			Response articleRes = con.ignoreContentType(true).method(Method.GET).cookies(cookies).execute();
			cookies.putAll(articleRes.cookies());
			String body=articleRes.body();
			Document document=articleRes.parse();
			res.setBody(body);
			res.setDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static MockViewRes views(String url,String referrer,Map<String,String> cookies) {
		Connection con = Jsoup.connect(url);
		con.header("User-Agent", userAgent);
		con.referrer(referrer);

		MockViewRes res=new MockViewRes();
		if(cookies==null) {
			cookies=new HashMap<String,String>();
		}
		try {
			Response articleRes = con.ignoreContentType(true).method(Method.GET).cookies(cookies).execute();
			cookies.putAll(articleRes.cookies());
			String body=articleRes.body();
			Document document=articleRes.parse();
			res.setBody(body);
			res.setDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static MockViewRes views(String url, Map<String, String> cookies) {
		Connection con = Jsoup.connect(url);
		con.header("User-Agent", userAgent);

		MockViewRes res=new MockViewRes();
		try {
			Response articleRes = con.ignoreContentType(true).method(Method.GET).cookies(cookies).execute();
			cookies.putAll(articleRes.cookies());
			String body=articleRes.body();
			Document document=articleRes.parse();
			res.setBody(body);
			res.setDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static class MockViewRes{
		private String body;
		private Document document;
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public Document getDocument() {
			return document;
		}
		public void setDocument(Document document) {
			this.document = document;
		}
	}
	
	public static String doBodySubmit(String url, Map<String, String> cookies,String body){
		return doSubmit(Method.POST, url, null, cookies, null, null, null, body);
	}
	public static String doSubmit(Method method, String url, Map<String, String> headers, Map<String, String> cookies,
			Map<String, String> datas,String host,Integer port,String body) {
		Connection conn = Jsoup.connect(url).cookies(cookies).ignoreContentType(true).requestBody(body);
		if(datas!=null){
			conn=conn.data(datas);
		}
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put("User-Agent", userAgent);
		if(StringUtils.isNotEmpty(body)){
			headers.put("Content-Type", "application/json");
		}
		
		conn = conn.headers(headers);
		conn = conn.method(method);
		
		Response res;
		try {
			res = conn.execute();
			cookies.putAll(res.cookies());
			return res.body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> urlParamsToMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			params.put(name, valueStr);
		}

		return params;
	}

	public static Map<String, String> stringToMap(String str) {
		if (StringUtils.isEmpty(str)) {
			return new HashMap<String, String>();
		}
		JSONObject json = JSONObject.parseObject(str);
		return jsonToMap(json);

	}
	private static Map<String, String> jsonToMap(JSONObject jsonObj) {
		Map<String, String> map = new HashMap<String, String>();
		for (String key : jsonObj.keySet()) {
			map.put(key, jsonObj.getString(key));
		}
		return map;
	}
}