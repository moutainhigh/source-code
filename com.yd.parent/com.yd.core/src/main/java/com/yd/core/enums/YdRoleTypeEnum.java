package com.yd.core.enums;

/**
* 角色枚举
* @author wuyc
* created 2019/12/11 15:16
**/
public enum YdRoleTypeEnum {

    ROLE_PLATFORM_MANAGER(1, "平台管理员"),
    ROLE_SUPPLIER(2, "供应商"),
    ROLE_MERCHANT_LEVEL_01(3, "普通版商户"),
    ROLE_OPERATOR(4, "操作员");

    /** 角色编码 */
    private Integer code;

    /** 角色描述 */
    private String desc;

    YdRoleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YdRoleTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (YdRoleTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
