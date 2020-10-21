package com.yd.api.wx.util;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yd.api.pay.util.HttpClientUtil;
import com.yd.api.result.common.WbWeixinAccountResult;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.wx.bean.EnumWeixinAuthorizeScope;
import com.yd.api.wx.bean.WxAuthorizeAccessToken;
import com.yd.api.wx.bean.WxKefeTextMessage;
import com.yd.api.wx.bean.WxMenu;
import com.yd.api.wx.bean.WxQrCode;
import com.yd.api.wx.bean.WxQrCodeReq;
import com.yd.api.wx.bean.WxTag;
import com.yd.api.wx.bean.WxTag.WxTagItem;
import com.yd.api.wx.bean.WxUserInfo;
import com.yd.core.utils.BusinessException;

public class WeixinHelper {
	private final static Logger LOG = LoggerFactory.getLogger(WeixinHelper.class);

	/**
	 * 获取授权路径
	 * 
	 * @param appId
	 * @param redirectUrl
	 * @param scope
	 * @return
	 */
	public static String getAuthorizeUrl(String loginUrl,String appId, String goUrl, EnumWeixinAuthorizeScope scope) {
		String redirectUrl=loginUrl+"?returnUrl="+URLEncoder.encode(goUrl);
		String formatUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STATE#wechat_redirect";
		String returnUrl = String.format(formatUrl, appId, URLEncoder.encode(redirectUrl), scope.getCode());
		System.out.println("=================returnUrl:"+returnUrl);
		return returnUrl;
	}

	public static WxAuthorizeAccessToken getAccessTokenByCode(String code, WbWeixinAccountResult account) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appid", account.getAppId());
		paramMap.put("secret", account.getAppSecret());
		paramMap.put("code", code);
		paramMap.put("grant_type", "authorization_code");
		LOG.info("======================================");
		LOG.info(JSONObject.toJSONString(paramMap));
		String ret = HttpClientUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token", paramMap);
		WxAuthorizeAccessToken accessToken = JSONObject.parseObject(ret, WxAuthorizeAccessToken.class);
		
		return accessToken;
	}
	
	public static WxUserInfo getUserInfo(WxAuthorizeAccessToken p) {
		Map<String,String> param=new HashMap<String,String>();
		param.put("access_token", p.getAccessToken());
		param.put("openid", p.getOpenid());
		param.put("lang", "zh_CN");
		String userInfoStr=HttpClientUtil.get("https://api.weixin.qq.com/sns/userinfo", param);
		WxUserInfo wxUserInfo=JSONObject.parseObject(userInfoStr, WxUserInfo.class);
		
		return wxUserInfo;
	}
	
	
	public static WxUserInfo getFollowWxUser(String accessToken,String openId) {
		Map<String,String> param=new HashMap<String,String>();
		param.put("access_token", accessToken);
		param.put("openid", openId);
		param.put("lang", "zh_CN");
		String userInfoStr=HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/user/info", param);
		LOG.info("============userInfoStr===:"+userInfoStr);
		WxUserInfo wxUserInfo=JSONObject.parseObject(userInfoStr, WxUserInfo.class);
		return wxUserInfo;
		
	}
	
	
	public static WxQrCode createScene(String sceneStr,String accessToken) {
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		WxQrCodeReq	wxQrCodeReq=new WxQrCodeReq();
		wxQrCodeReq.setAction_name("QR_LIMIT_STR_SCENE");
		wxQrCodeReq.getAction_info().setScene(sceneStr);
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(wxQrCodeReq));
			System.out.println("ret:"+ret);
			JSONObject json=JSONObject.parseObject(ret);
			String ticket=json.getString("ticket");
			String content=json.getString("url");
			String qrCode="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
			
			WxQrCode result=new WxQrCode();
			result.setContent(content);
			result.setTicket(ticket);
			result.setQrCode(qrCode);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static WxQrCode createSceneTmp(String sceneStr,String accessToken,int seconds) {
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		WxQrCodeReq	wxQrCodeReq=new WxQrCodeReq();
		wxQrCodeReq.setAction_name("QR_STR_SCENE");
		wxQrCodeReq.setExpire_seconds(seconds);
		wxQrCodeReq.getAction_info().setScene(sceneStr);
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(wxQrCodeReq));
			System.out.println("=======createSceneTmp: param=="+JSONObject.toJSONString(wxQrCodeReq));
			System.out.println("=======createSceneTmp: ret=="+ret);
			JSONObject json=JSONObject.parseObject(ret);
			String ticket=json.getString("ticket");
			String content=json.getString("url");
			String qrCode="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
			
			WxQrCode result=new WxQrCode();
			result.setContent(content);
			result.setTicket(ticket);
			result.setQrCode(qrCode);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void createMenu(WxMenu menu,String accessToken) throws BusinessException{
		String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(menu));
			System.out.println("ret:"+ret);
			JSONObject json=JSONObject.parseObject(ret);
			Integer errcode=json.getInteger("errcode");
			String errMsg=json.getString("errmsg");
			if(errcode!=0){
				throw new BusinessException("err_"+errcode, errMsg);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createConditionalMenu(WxMenu menu,String accessToken) throws BusinessException{
		String url="https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="+accessToken;
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(menu));
			System.out.println("ret:"+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delConditionalMenu(String accessToken,int menuId) {
		String url="https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token="+accessToken;
		JSONObject json=new JSONObject();
		json.put("menuid", menuId);
		
		try {
			String ret=HttpClientUtil.postBody(url, json.toJSONString());
			System.out.println("ret:"+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void getMenuAll(String accessToken) {
		String url="https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+accessToken;
		
		String ret=HttpClientUtil.get(url, null);
		System.out.println("ret:"+ret);
	}
	
	
	public static WxTagItem createTag(String name,String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/tags/create?access_token="+accessToken;
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(new WxTag(name)));
			System.out.println("ret:"+ret);
			WxTag wxTagReturn=JSONObject.parseObject(ret,WxTag.class);
			
			return wxTagReturn.getTag();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void getTags(String accessToken) {
		String url="https://api.weixin.qq.com/cgi-bin/tags/get?access_token="+accessToken;
		String ret=HttpClientUtil.get(url, null);
		System.out.println("ret=="+ret);
		JSONObject json=JSONObject.parseObject(ret);
		JSONArray arr=json.getJSONArray("tags");
		for(int i=0;i<arr.size();i++) {
			JSONObject item=arr.getJSONObject(i);
			
			Integer tagId=item.getInteger("id");
			String name=item.getString("name");
			Integer count=item.getInteger("count");
		}
	}
	
	
	public static void addTagToUser(String openId,Integer tagId,String accessToken) {
		JSONObject json=new JSONObject();
		List<String> openidList=new ArrayList<String>();
		openidList.add(openId);
		json.put("openid_list", openidList);
		json.put("tagid", tagId);
		String url="https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token="+accessToken;
		try {
			String ret=HttpClientUtil.postBody(url, json.toJSONString());
			System.out.println("ret=="+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delTagToUser(String openId,Integer tagId,String accessToken) {
		JSONObject json=new JSONObject();
		List<String> openidList=new ArrayList<String>();
		openidList.add(openId);
		json.put("openid_list", openidList);
		json.put("tagid", tagId);
		String url="https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token="+accessToken;
		try {
			String ret=HttpClientUtil.postBody(url, json.toJSONString());
			System.out.println("ret=="+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getTagToUser(String openId,String accessToken) {
		String url="https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token="+accessToken;
		JSONObject json=new JSONObject();
		json.put("openid", openId);
		try {
			String ret=HttpClientUtil.postBody(url, json.toJSONString());
			System.out.println("ret=="+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendKefuMessage(String accessToken,String openId,String content){
		String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
		
		WxKefeTextMessage message=new WxKefeTextMessage(openId,content);
		try {
			String ret=HttpClientUtil.postBody(url, JSONObject.toJSONString(message));
			System.out.println("ret="+ret);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		String accessToken="27_QUzcfd0BBQyTzi46Ptyj0c8T_W9rQJMDIqozpOs-LyQ5PkQY25oow3r5DVKIg55nGgPsAEQBD9bCCvrRNfoMFleWp4qqsKNSoqlbSqGq6bO9b7hsDyXeyQswzPj-1ep4GhxNwFWLuX4crxfKZLJgAHAOAY";
		//{"tags":[{"id":2,"name":"星标组","count":0},{"id":100,"name":"用户","count":0},{"id":101,"name":"商户","count":1}]}
		//getTags(accessToken);
		
//		String openId="oPzVdwlfF-4Nld93cmJFU2mmBrNw";
//		String content="亲爱的白苏,当你收到这条消息时，你已经整整两天没有搭理我了\n\n";
//		content+="由于微信平台规则限制，小蒙将无法继续为你推荐儿童教育相关知识和课程\n\n";
//		content+="<a href=\"weixin://bizmsgmenu?msgmenucontent=撩一撩小蒙&msgmenuid=1\">撩一撩小蒙</a>\n\n";
//		content+="<a href=\"weixin://bizmsgmenu?msgmenucontent=取消限制&msgmenuid=2\">取消限制</a>\n\n";
//		sendKefuMessage(accessToken, openId, content);
//		List<WxMenuButton> buttonList=new ArrayList<WxMenuButton>();
//		buttonList.add(WxMenuButton.initViewButton("优课中心", "http://m.linjiamei.cn/wechat/page/recommend"));
//		buttonList.add(WxMenuButton.initViewButton("已购课程", "http://m.linjiamei.cn/wechat/page/recommend"));
//		WxMenuButton userCenter=WxMenuButton.initParentButton("个人中心");
//		userCenter.getSub_button().add(WxMenuButton.initViewButton("个人中心", "http://m.linjiamei.cn/wechat/page/recommend"));
//		userCenter.getSub_button().add(WxMenuButton.initViewButton("低价秒杀", "http://m.linjiamei.cn/wechat/page/recommend"));
//		userCenter.getSub_button().add(WxMenuButton.initViewButton("今日签到", "http://m.linjiamei.cn/wechat/page/recommend"));
//		buttonList.add(userCenter);
//		WxMenu menu=new WxMenu();
//		menu.setButton(buttonList);
//		WxMatchrule matchrule=new WxMatchrule();
//		matchrule.setTag_id("100");
//		menu.setMatchrule(matchrule);
//		try {
//			//{"menuid":533256893}  {"menuid":533256895}
//			createConditionalMenu(menu, accessToken);
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
		
		//delConditionalMenu(accessToken, 533256895);
		//getMenuAll(accessToken);
		//addTagToUser("oPzVdwlfF-4Nld93cmJFU2mmBrNw", 101, accessToken);
		
		uploadMedia(accessToken);
	}
	
	
	public static void uploadMedia(String accessToken) {
		String url="https://api.weixin.qq.com/cgi-bin/material/add_material?&type=image&access_token="+accessToken;
		File file=new File("C:\\58.png");
		String ret=WeixinUpload.formUpload(url, file);
		System.out.println(ret);
		
	}
}
