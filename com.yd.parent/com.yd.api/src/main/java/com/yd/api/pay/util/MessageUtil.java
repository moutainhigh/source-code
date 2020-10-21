package com.yd.api.pay.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yd.api.wx.res.Article;
import com.yd.api.wx.res.ImageMessage;
import com.yd.api.wx.res.NewsMessage;
import com.yd.api.wx.res.TextMessage;

/**
 * 消息工具类
 */
public class MessageUtil {

	public static String payStatusToXml(PayStatus statusMessage) {
		StringBuffer sb=new StringBuffer();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA["+statusMessage.getReturnCode()+"]]></return_code>");
		sb.append("<return_msg><![CDATA["+statusMessage.getReturnMsg()+"]]></return_msg>");
		sb.append("</xml>");
		
		return sb.toString();
	}
	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}	
	
	
	
	public static Map<String, String> parseXml(String xml) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		Document document =  DocumentHelper.parseText(xml);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}	

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String messageToXml(TextMessage textMessage) {
		StringBuffer sb=new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA["+textMessage.getToUserName()+"]]></ToUserName>");
		sb.append("<FromUserName><![CDATA["+textMessage.getFromUserName()+"]]></FromUserName>");
		sb.append("<CreateTime>"+textMessage.getCreateTime()+"</CreateTime>");
		sb.append("<MsgType><![CDATA["+textMessage.getMsgType()+"]]></MsgType>");
		sb.append("<Content><![CDATA["+textMessage.getContent()+"]]></Content>");
		sb.append("</xml>");
		return sb.toString();
	}


	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String messageToXml(NewsMessage newsMessage) {
		StringBuffer sb=new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA["+newsMessage.getToUserName()+"]]></ToUserName>");
		sb.append("<FromUserName><![CDATA["+newsMessage.getFromUserName()+"]]></FromUserName>");
		sb.append("<CreateTime>"+newsMessage.getCreateTime()+"</CreateTime>");
		sb.append("<MsgType><![CDATA["+newsMessage.getMsgType()+"]]></MsgType>");
		sb.append("<ArticleCount>"+newsMessage.getArticleCount()+"</ArticleCount>");
		sb.append("<Articles>");
		for(Article article:newsMessage.getArticles()) {
			sb.append("<item>");
			sb.append("<Title><![CDATA["+article.getTitle()+"]]></Title>");
			sb.append("<Description><![CDATA["+article.getDescription()+"]]></Description>");
			sb.append("<PicUrl><![CDATA["+article.getPicUrl()+"]]></PicUrl>");
			sb.append("<Url><![CDATA["+article.getUrl()+"]]></Url>");
			sb.append("</item>");
		}
		sb.append("</Articles>");
		sb.append("</xml>");
		return sb.toString();
	}


	public static String messageToXml(ImageMessage message) {
		StringBuffer sb=new StringBuffer();
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA["+message.getToUserName()+"]]></ToUserName>");
		sb.append("<FromUserName><![CDATA["+message.getFromUserName()+"]]></FromUserName>");
		sb.append("<CreateTime>"+message.getCreateTime()+"</CreateTime>");
		sb.append("<MsgType><![CDATA["+message.getMsgType()+"]]></MsgType>");
		sb.append("<Image>");
		sb.append("<MediaId><![CDATA["+message.getMediaId()+"]]></MediaId>");
		sb.append("</Image>");
		sb.append("</xml>");
		return sb.toString();
	}
}

