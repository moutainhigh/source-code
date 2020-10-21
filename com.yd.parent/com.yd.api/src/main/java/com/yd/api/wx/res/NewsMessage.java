package com.yd.api.wx.res;

import java.util.List;

import com.yd.api.wx.bean.EnumWxResMsgType;

/**
 * 文本消息
 */
public class NewsMessage extends BaseMessage {
	
	public NewsMessage(){
		super.setMsgType(EnumWxResMsgType.news.getCode());
	}
	public NewsMessage(BaseMessage message){
		super.setCreateTime(message.getCreateTime());
		super.setFromUserName(message.getFromUserName());
		super.setToUserName(message.getToUserName());
	}
	
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
}