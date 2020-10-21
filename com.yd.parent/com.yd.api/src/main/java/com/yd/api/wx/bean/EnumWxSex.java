package com.yd.api.wx.bean;

public enum EnumWxSex {
	MALE("1","男"),FEMALE("2","女"),UNKNOW("0","");
	private String code;
	private String name;
	private EnumWxSex(String code,String name) {
		this.code=code;
		this.name=name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static EnumWxSex getByCode(String code) {
		for(EnumWxSex item:values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}
		return UNKNOW;
	}
}
