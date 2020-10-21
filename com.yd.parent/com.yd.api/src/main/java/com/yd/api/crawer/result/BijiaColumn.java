package com.yd.api.crawer.result;

import java.io.Serializable;

public class BijiaColumn implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String siteName;
	private String title;
	private String cover;
	private String price;
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
