package com.yd.api.enums;

public enum EnumSiteGroup {
	SYS("sys","系统"),
	MERCHANT("merchant","商户"),
	SUPPLIER("supplier","供应商"),
	UNKNOW("unknow","未知");
	
	private String code;
	private String name;
	private EnumSiteGroup(String code,String name) {
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
	
	public static EnumSiteGroup getByCode(String code) {
		for(EnumSiteGroup item:values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}

		return null;
	}
}
