package com.yd.service.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.service.common.WeixinService;
import com.yd.core.utils.BusinessException;

public class WeixinAccessTokenJob  extends QuartzJobBean{
	private final static Logger LOG = LoggerFactory.getLogger(WeixinAccessTokenJob.class);
	
	@Resource
	private WeixinService	weixinService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("==============WeixinAccessTokenJob:"+new Date());
		
		List<String> typeList=new ArrayList<String>();
		typeList.add("91kuaiqiang");
		
		for(String type:typeList) {
			executeItem(type);
		}
		
	}
	
	private void executeItem(String type) {
		WbWeixinAccountResult account=weixinService.getByWeixinAccountByType(type);
		if(account==null) {
			return ;
		}
		//获取微信访问 accessToken
		try {
			String accessToken=getWeixinAccessToken(account);
			String jsapiTicket=getWeixinTicket(accessToken);
			account.setAccessToken(accessToken);
			account.setJsapiTicket(jsapiTicket);
			account.setExpireTime(DateTime.now().plusHours(2).toDate());
			weixinService.updateAccessTokenWithTicket(account);
		}catch(BusinessException e) {
			LOG.error("===============errCode:"+e.getCode()+",errMsg:"+e.getMessage());
		}
	}
	
	private static String getWeixinAccessToken(WbWeixinAccountResult account) throws BusinessException{
		Map<String,String> params=new HashMap<String,String>();
		params.put("grant_type", "client_credential");
		params.put("appid", account.getAppId());
		params.put("secret", account.getAppSecret());
		String accessTokenRet = HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/token", params);
		LOG.info("==============accessTokenRet:"+accessTokenRet);
		JSONObject accessJson=JSONObject.parseObject(accessTokenRet);
		if(accessJson.getInteger("errcode")!=null) {
			LOG.error("=========================================");
			LOG.error("========获取微信 accessToken 出错===========");
			LOG.error("===appId:"+account.getAppId()+",appSecret:"+account.getAppSecret());
			Integer errCode=accessJson.getInteger("errcode");
			String errMsg=accessJson.getString("errmsg");
			LOG.error("=====errCode:"+errCode+",errMsg:"+errMsg);
			LOG.error("=========================================");
			
			throw new BusinessException("err_"+errCode, errMsg);
		}
		return accessJson.getString("access_token");
	}

	private static String getWeixinTicket(String accessToken) throws BusinessException{
		Map<String,String> param=new HashMap<String,String>();
		param.put("type", "jsapi");
		param.put("access_token", accessToken);
		String getticketStr=HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket", param);
		//获取微信 jsapi
		LOG.info("==============getticketStr:"+getticketStr);
		JSONObject ticketJson = JSONObject.parseObject(getticketStr);
		if (0 != ticketJson.getInteger("errcode")) {
			LOG.error("=========================================");
			LOG.error("========获取微信 accessToken 出错===========");
			Integer errCode=ticketJson.getInteger("errcode");
			String errMsg=ticketJson.getString("errmsg");
			LOG.error("=====errCode:"+errCode+",errMsg:"+errMsg);
			LOG.error("=========================================");
			throw new BusinessException("err_"+errCode, errMsg);
		}
		return ticketJson.getString("ticket");
	}
}
