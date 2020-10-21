package com.yd.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.web.util.LinkedMallRSAUtil;

@Controller
public class LinkedMallNotifyController {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String str=FileUtils.readFileToString(new File("d:\\11.txt"));
		JSONObject json=JSONObject.parseObject(str);
		System.out.println(json.toJSONString());
		TreeMap<String,String> params=jsonToMap(json);
		String sign=params.get("signature");
		LOG.info("==========sign:"+sign);
		System.out.println("==========sign:"+sign);
		String dataToBeSigned=getSignPlainStr(params);
		LOG.info("==========dataToBeSigned:"+dataToBeSigned);
		System.out.println("==========dataToBeSigned:"+dataToBeSigned);
		boolean verify = LinkedMallRSAUtil.verify(dataToBeSigned, sign, LINK_MAIL_PUBLIC_KEY);
		
		System.out.println("===========verify===="+verify);
		
	}
	private static final String LINK_MAIL_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmB2d4NasdZEQ//lg7/h7ZFwuiyBn86a9SoE0gfquIkdFmEv2+8dj7AwxlsXidzxI4Ta9zkiZFHqgC3bmtlRuF6BgtS1+ubs7ksd3YG+kyk+H6dAb6LnhGf7rv7PTUxSb8WN8ytZbl/5li2NYJva2igiWhOQ9VITPFobYcbZLiaaRfRRUmkPGgbuP2ScgrKQJB6cy34/wpc0bYMoqLETTCKctZRnfX1G1d1E8meCKdWWHmQqsRFkA8+OxzBKMeKhrJYT3fa2lDdA9yQDQsWj+jbmMd42NE6VnOQWpI/afsCNalFBVOM/RTYY2yLjhmX20P0ytVfs4Ep1h2SM4g9PP8wIDAQAB";
	private static final Logger LOG = LoggerFactory.getLogger(LinkedMallNotifyController.class);
	@ResponseBody
    @RequestMapping("/api/linkedMall/notify/{apiName}")
    public Map<String,String> linkedMallNotify(HttpServletRequest request, HttpServletResponse response,@PathVariable String apiName,@RequestBody(required=false) JSONObject json) {
		LOG.info("=============/api/linkedMall/notify/"+apiName);
    	System.out.println("=============/api/linkedMall/notify/"+apiName);
    	
    	LOG.info("=============/api/linkedMall/notify/"+apiName+",json:"+json.toJSONString());
    	System.out.println("=============/api/linkedMall/notify/"+apiName+",json:"+json.toJSONString());
    	if("syncItemIncrement".equalsIgnoreCase(apiName)) {
    		LOG.info("==============添加商品信息===============");
    		System.out.println("==============添加商品信息===============");
    	}else if("syncItemDeletion".equalsIgnoreCase(apiName)) {
    		LOG.info("==============删除商品信息===============");
    		System.out.println("==============删除商品信息===============");
    	}else if("syncItemModification".equalsIgnoreCase(apiName)) {
    		LOG.info("==============同步商品修改信息===============");
    		System.out.println("==============同步商品修改信息===============");
    	}
    	
    	
		String requestId=json.getString("requestId");
		LOG.info("===========requestId:"+requestId);
    	System.out.println("===========requestId:"+requestId);
		String sign=json.getString("signature");
		LOG.info("==========sign:"+sign);
		System.out.println("==========sign:"+sign);
		String dataToBeSigned=getSignPlainStr(json);
		LOG.info("==========dataToBeSigned:"+dataToBeSigned);
		System.out.println("==========dataToBeSigned:"+dataToBeSigned);
		boolean verify = LinkedMallRSAUtil.verify(dataToBeSigned, sign, LINK_MAIL_PUBLIC_KEY);
		LOG.info("==============verify:"+verify);
    	System.out.println("==============verify:"+verify);
    	
		
		Map<String,String> retMap=new HashMap<String,String>();
		retMap.put("Code", "SUCCESS");
		retMap.put("Message", "SUCCESS");
		retMap.put("RequestId", requestId);
		LOG.info("=============/api/linkedMall/notify/"+apiName+"=================end======");
    	System.out.println("=============/api/linkedMall/notify/"+apiName+"=================end======");
    	return retMap;
    }
	private static TreeMap<String,String> jsonToMap(JSONObject json){
		TreeMap<String,String> map=new TreeMap<String,String>();
		for(Entry<String,Object> entry:json.entrySet()) {
			map.put(entry.getKey(), entry.getValue()+"");
		}
		return map;
	}

	private static String getSignPlainStr(TreeMap<String, String> params) {
		String dataToBeSigned = "";
		for (Entry<String,String> entry:params.entrySet()){
			String keyName=entry.getKey();
			String keyValue=entry.getValue();
			if(!"signature".equalsIgnoreCase(keyName) && !"signatureMethod".equalsIgnoreCase(keyName)) {
				dataToBeSigned = dataToBeSigned + (dataToBeSigned.equals("") ? "" : "&") + keyName + "=" + keyValue;
			}
        }
		
		return dataToBeSigned;
	}
	
	private static String getSignPlainStr(JSONObject json) {
		
		TreeMap<String, String> params=new TreeMap<String,String>();
		for(Entry<String,Object> entry:json.entrySet()) {
			String key=entry.getKey();
			String value=entry.getValue().toString();
			params.put(key, value);
		}
		
		return getSignPlainStr(params);
	}	
	
}
