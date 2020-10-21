package com.yd.core.enums;

public enum YdUserAuthEnum {

    WEIXIN(1, "微信");

    /** 授权编码 */
    private Integer code;

    /** 授权来源 */
    private String description;

    YdUserAuthEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YdUserAuthEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (YdUserAuthEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
