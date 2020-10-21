package com.yg.api.enums;

public enum YgSiteGroupEnum {

	SYS("sys","系统"),

	MERCHANT("merchant","商户"),

	UNKNOW("unknow","未知");

	private String code;
	private String name;

	private YgSiteGroupEnum(String code, String name) {
		this.code=code;
		this.name=name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static YgSiteGroupEnum getByCode(String code) {
		for(YgSiteGroupEnum item:values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
}
