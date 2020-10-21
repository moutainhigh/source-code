package com.yd.core.enums;

/**
 * @author Wuyc
 * Created in 2019-06-20 11:38
 */
public enum YdLoginUserSourceEnums {

    /** B端PC端 */
    YD_ADMIN_MERCHANT("YD_ADMIN_MERCHANT", "优度商城B端管理后台用户"),

    /** B端PC端 */
    YD_APP_MERCHANT("YD_APP_MERCHANT", "优度商城B端管理用户app登录"),

    /** C端 */
    YD_SHOP_USER("YD_SHOP_USER", "优度商城C端用户");

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 枚举描述
     */
    private String description;

    YdLoginUserSourceEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YdLoginUserSourceEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YdLoginUserSourceEnums value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
