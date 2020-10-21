package com.yg.core.enums;

public enum YgLoginUserSourceEnums {

    /** B端 */
    YG_ADMIN_MERCHANT("YG_ADMIN_MERCHANT", "印鸽商城B端管理后台用户"),

    /** C端 */
    YG_SHOP_USER("YG_SHOP_USER", "印鸽商城C端用户");

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 枚举描述
     */
    private String description;

    YgLoginUserSourceEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YgLoginUserSourceEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YgLoginUserSourceEnums value : values()) {
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
