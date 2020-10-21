package com.yd.api.wx.bean;

public class WxTag {
	private WxTagItem tag;

	public WxTag(){
		
	}
	
	public WxTag(String name){
		WxTagItem tagItem=new WxTagItem();
		tagItem.setName(name);
		this.tag=tagItem;
	}
	public WxTagItem getTag() {
		return tag;
	}

	public void setTag(WxTagItem tag) {
		this.tag = tag;
	}

	public static class WxTagItem{
		private Integer id;
		private String name;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}

