package com.yd.api.wx.bean;

import java.util.ArrayList;
import java.util.List;

public class WxMenuButton {
	private String name;
	private String type;
	private String key;
	private List<WxMenuButton> sub_button;
	private String url;
	private String appid;
	private String pagepath;
	
	public static WxMenuButton initParentButton(String name){
		WxMenuButton button=new WxMenuButton();
		button.setName(name);
		button.setSub_button(new ArrayList<WxMenuButton>());
		
		return button;
	}
	
	public static WxMenuButton initClickButton(String name,String key){
		WxMenuButton button=new WxMenuButton();
		button.setType(EnumWxMenuType.CLICK.getCode());
		button.setName(name);
		button.setKey(key);
		
		return button;
	}
	
	public static WxMenuButton initViewButton(String name,String url){
		WxMenuButton button=new WxMenuButton();
		button.setType(EnumWxMenuType.VIEW.getCode());
		button.setName(name);
		button.setUrl(url);
		
		return button;
	}
	
	public static WxMenuButton initMiniAppButton(String name,String url,String appid,String pagepath){
		WxMenuButton button=new WxMenuButton();
		button.setType(EnumWxMenuType.MINI_APP.getCode());
		button.setAppid(appid);
		button.setPagepath(pagepath);
		button.setUrl(url);
		button.setName(name);
		
		return button;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<WxMenuButton> getSub_button() {
		return sub_button;
	}
	public void setSub_button(List<WxMenuButton> sub_button) {
		this.sub_button = sub_button;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPagepath() {
		return pagepath;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}
	
	
}
