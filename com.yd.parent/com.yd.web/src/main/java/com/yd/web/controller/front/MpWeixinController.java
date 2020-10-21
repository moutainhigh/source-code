package com.yd.web.controller.front;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.MessageUtil;
import com.yd.api.pay.util.SHA1;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.service.common.AutoReplyMessageService;
import com.yd.api.service.common.WeixinService;
import com.yd.api.wx.bean.WxMessageRequest;
import com.yd.web.util.WebUtil;

@Controller
public class MpWeixinController {
	private final static Logger LOG = LoggerFactory.getLogger(MpWeixinController.class);
	
	@Reference
	private WeixinService	weixinService;
	@Reference
	private AutoReplyMessageService		autoReplyMessageService;

	/**
	 * 微信验证 和 消息入口
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	
	@ResponseBody
	@RequestMapping("/mp/weixin/{weixinPinyin}/verification")
	public void verification(HttpServletRequest request, HttpServletResponse response, @PathVariable String weixinPinyin) throws IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		LOG.info("==========/mp/weixin/{weixinPinyin}/verification=============");
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		if (isGet) {
			// 验证URL真实性
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			String echostr = request.getParameter("echostr");// 随机字符串
			if (StringUtils.isNotEmpty(signature) && StringUtils.isNotEmpty(timestamp) && StringUtils.isNotEmpty(nonce) && StringUtils.isNotEmpty(echostr)) {
				List<String> params = new ArrayList<String>();
				// 这个Token是给微信开发者接入时填的
				// 可以是任意英文字母或数字，长度为3-32字符
				WbWeixinAccountResult weixinAccount=weixinService.getByWeixinAccountByType(weixinPinyin);
				params.add(weixinAccount.getToken());
				params.add(timestamp);
				params.add(nonce);
				// 1. 将token、timestamp、nonce三个参数进行字典序排序
				Collections.sort(params, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
				String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
				if (temp.equals(signature)) {
					response.getWriter().write(echostr);
					return;
				}
			}
			
			return ;
		}

		// 调用核心业务类接收消息、处理消息
		String respMessage = null;

		try {
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content = requestMap.get("Content");

			String event = requestMap.get("Event");
			String eventKey = requestMap.get("EventKey");
			
			
			WxMessageRequest mr=new WxMessageRequest();
			mr.setFromUserName(fromUserName);
			mr.setToUserName(toUserName);
			mr.setMsgType(msgType);
			mr.setContent(content);
			mr.setEvent(event);
			if(StringUtils.isNotEmpty(eventKey)) {
				eventKey=eventKey.replaceAll("qrscene_", "");
			}
			mr.setEventKey(eventKey);
			
			mr.setRequestIp(WebUtil.getVisitorIp(request));
			mr.setMpWeixinPinyin(weixinPinyin);

			LOG.info("===============mr:"+JSONObject.toJSONString(mr));
			respMessage = autoReplyMessageService.reply(mr);
			LOG.info("============respMessage:"+respMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.flush();
		out.close();
	}
}
