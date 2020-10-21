package com.yd.api.crawer.enums;

public enum EnumCrawerSite {
	ZOL("zol","中关村");
	private String code;
	private String name;
	private EnumCrawerSite(String code,String name) {
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
}
