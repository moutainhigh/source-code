package com.yd.service.impl.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.MessageUtil;
import com.yd.api.service.common.AutoReplyMessageService;
import com.yd.api.wx.bean.EnumWxEvent;
import com.yd.api.wx.bean.EnumWxReqMsgType;
import com.yd.api.wx.bean.EnumWxResMsgType;
import com.yd.api.wx.bean.WxMessageRequest;
import com.yd.api.wx.bean.WxUserInfo;
import com.yd.api.wx.res.Article;
import com.yd.api.wx.res.ImageMessage;
import com.yd.api.wx.res.NewsMessage;
import com.yd.api.wx.res.TextMessage;
import com.yd.api.wx.util.WeixinHelper;
import com.yd.service.bean.common.WbWeixinAccount;
import com.yd.service.bean.common.WxKeywords;
import com.yd.service.bean.common.WxKeywordsArticle;
import com.yd.service.bean.common.WxVisitRecord;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.dao.common.WbWeixinAccountDao;
import com.yd.service.dao.common.WxKeywordsArticleDao;
import com.yd.service.dao.common.WxKeywordsDao;
import com.yd.service.dao.common.WxVisitRecordDao;
import com.yd.service.dao.merchant.YdMerchantDao;

@Service(dynamic = true)
public class AutoReplyMessageServiceImpl implements AutoReplyMessageService {
	private final static Logger LOG = LoggerFactory.getLogger(AutoReplyMessageServiceImpl.class);
	
	@Resource
	private WxVisitRecordDao	wxVisitRecordDao;
	@Resource
	private WxKeywordsDao	wxKeywordsDao;
	@Resource
	private WxKeywordsArticleDao	wxKeywordsArticleDao;
	@Resource
	private WbWeixinAccountDao	wbWeixinAccountDao;
	@Resource
	private YdMerchantDao	ydMerchantDao;

	@Override
	public String reply(WxMessageRequest mr) {
		LOG.info("=========reply======="+JSONObject.toJSONString(mr));
		try {
			if(StringUtils.isEmpty(mr.getRequestIp())){
				mr.setRequestIp("127.0.0.1");
			}
			//记录访问日志
			WxVisitRecord visitlog=new WxVisitRecord();
			visitlog.setCreateTime(new Date());
			if(mr.getEventKey()==null){
				visitlog.setEventKey("0");
			}else{
				visitlog.setEventKey(mr.getEventKey());
			}
			visitlog.setOpenId(mr.getFromUserName());
			visitlog.setMpType(mr.getMpWeixinPinyin());
			wxVisitRecordDao.insert(visitlog);
			
			
		    System.out.println(JSONObject.toJSONString(mr));
		    String msgType=mr.getMsgType();
		    WxKeywords keywordRecord=null;
			// 文本消息
			if (msgType.equals(EnumWxReqMsgType.text.getCode())) {
				String keywords=mr.getContent();
				keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords(keywords);
			}
			// 事件推送
			else if (msgType.equals(EnumWxReqMsgType.event.getCode())) {
				// 事件类型
				String eventType = mr.getEvent();
				String eventKey=mr.getEventKey();
				if(StringUtils.isNotEmpty(eventKey)) {
					eventKey=eventKey.replaceAll("qrscene_", "");
				}

				// 订阅,首次关注
				if (eventType.equalsIgnoreCase(EnumWxEvent.subscribe.getCode())) {
					
					if(StringUtils.isNotEmpty(eventKey)) {
						String openId=mr.getFromUserName();

						Map<String,String> map=new HashMap<String,String>();
						map.put("openId", openId);
						if(eventKey.startsWith("bindWithdraw-") || eventKey.startsWith("reBindWithdraw-")) {
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							LOG.info("===merchantId:"+merchantId+" 绑定微信 号 openId： "+openId);
							WbWeixinAccount weixinAccount=wbWeixinAccountDao.getByWeixinAccountByType(mr.getMpWeixinPinyin());
							WxUserInfo userInfo=WeixinHelper.getFollowWxUser(weixinAccount.getAccessToken(), openId);
							
							map.put("nickname", userInfo.getNickname());
							map.put("headImage", userInfo.getHeadimgurl());
						}
						
						
						if(eventKey.startsWith("bindWithdraw-")) {
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							YdMerchant merchant=ydMerchantDao.getYdMerchantById(merchantId);
							if(merchant!=null && StringUtils.isEmpty(merchant.getWxOpenId())) {
								ydMerchantDao.updateMerchantBindWeixin(merchantId,openId,JSONObject.toJSONString(map));
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户绑定微信账户成功");
							}else {
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户绑定微信账户失败");
							}
						}else if(eventKey.startsWith("reBindWithdraw-")){
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							LOG.info("===merchantId:"+merchantId+" 绑定微信 号 openId： "+openId);
							YdMerchant merchant=ydMerchantDao.getYdMerchantById(merchantId);
							if(merchant!=null) {
								ydMerchantDao.updateMerchantBindWeixin(merchantId,openId,JSONObject.toJSONString(map));
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户重新绑定微信账户成功");
							}
						}
					}else {
						keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords(eventKey);
					}
				}
				// 取消订阅
				else if (eventType.equalsIgnoreCase(EnumWxEvent.unsubscribe.getCode())) {
					
				}
				// 自定义菜单点击事件
				else if (eventType.equalsIgnoreCase(EnumWxEvent.click.getCode())) {
					//1.根据EventKey查询对应的message
					//2.根据message的消息类型回复图文消息or文字消息
					keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("点击菜单"+eventKey);
				}
				//扫描事件
				else if(eventType.equalsIgnoreCase(EnumWxEvent.scan.getCode())){
					if(StringUtils.isNotEmpty(eventKey)) {
						String openId=mr.getFromUserName();

						Map<String,String> map=new HashMap<String,String>();
						map.put("openId", openId);
						if(eventKey.startsWith("bindWithdraw-") || eventKey.startsWith("reBindWithdraw-")) {
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							LOG.info("===merchantId:"+merchantId+" 绑定微信 号 openId： "+openId);
							WbWeixinAccount weixinAccount=wbWeixinAccountDao.getByWeixinAccountByType(mr.getMpWeixinPinyin());
							WxUserInfo userInfo=WeixinHelper.getFollowWxUser(weixinAccount.getAccessToken(), openId);
							LOG.info("============userInfo:"+JSONObject.toJSONString(userInfo));
							map.put("nickname", userInfo.getNickname());
							map.put("headImage", userInfo.getHeadimgurl());
						}
						
						
						if(eventKey.startsWith("bindWithdraw-")) {
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							YdMerchant merchant=ydMerchantDao.getYdMerchantById(merchantId);
							if(merchant!=null && StringUtils.isEmpty(merchant.getWxOpenId())) {
								ydMerchantDao.updateMerchantBindWeixin(merchantId,openId,JSONObject.toJSONString(map));
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户绑定微信账户成功");
							}else {
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户绑定微信账户失败");
							}
						}else if(eventKey.startsWith("reBindWithdraw-")){
							Integer merchantId=Integer.parseInt(eventKey.split("-")[1]);
							LOG.info("===merchantId:"+merchantId+" 绑定微信 号 openId： "+openId);
							YdMerchant merchant=ydMerchantDao.getYdMerchantById(merchantId);
							if(merchant!=null) {
								ydMerchantDao.updateMerchantBindWeixin(merchantId,openId,JSONObject.toJSONString(map));
								keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("商户重新绑定微信账户成功");
							}
						}						
					}else {
						keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords(eventKey);
					}
				}
			}
			
			if(keywordRecord==null) {
				keywordRecord=wxKeywordsDao.findWxKeywordsByKeyWords("未识别");
			}
			if(EnumWxResMsgType.text.getCode().equalsIgnoreCase(keywordRecord.getType())) {
				TextMessage message=new TextMessage();
				message.setContent(keywordRecord.getContent());
				message.setCreateTime(new Date().getTime());
				message.setFromUserName(mr.getToUserName());
				message.setToUserName(mr.getFromUserName());
				return MessageUtil.messageToXml(message);
			}else if(EnumWxResMsgType.image.getCode().equalsIgnoreCase(keywordRecord.getType())) {
				ImageMessage message=new ImageMessage();
				message.setCreateTime(new Date().getTime());
				message.setFromUserName(mr.getToUserName());
				message.setToUserName(mr.getFromUserName());
				message.setMediaId(keywordRecord.getMediaId());
				return MessageUtil.messageToXml(message);
				
			}else if(EnumWxResMsgType.news.getCode().equalsIgnoreCase(keywordRecord.getType())){
				NewsMessage newsMessage=keywordsIdToNewsMessage(keywordRecord.getId());
				newsMessage.setFromUserName(mr.getToUserName());
				newsMessage.setToUserName(mr.getFromUserName());
				newsMessage.setCreateTime(new Date().getTime());
				return MessageUtil.messageToXml(newsMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.toString());
		}
		return null;
	}
	
	
	


	private NewsMessage keywordsIdToNewsMessage(Integer id) {
		WxKeywordsArticle param=new WxKeywordsArticle();
		param.setKeywordsId(id);
		List<WxKeywordsArticle> list=wxKeywordsArticleDao.findWxKeywordsArticlesByPage(param, 0, 6);
		
		List<Article> articles=new ArrayList<Article>();
		for(int i=0;i<list.size();i++) {
			WxKeywordsArticle item=list.get(i);
			Article article=new Article();
			article.setTitle(item.getTitle());
			article.setDescription(item.getIntro());
			if(i==0) {
				article.setPicUrl(item.getCover());
			}else {
				article.setPicUrl(item.getSmallCover());
			}
			article.setUrl(item.getLinkUrl());
			articles.add(article);
		}
		
		NewsMessage newsMessage=new NewsMessage();
		newsMessage.setArticleCount(list.size());
		newsMessage.setArticles(articles);
		
		return newsMessage;
	}
}
