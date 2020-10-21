package com.yg.core.enums;

/**
* 角色枚举  todo 暂时不用
* @author wuyc
* created 2019/12/11 15:16
**/
public enum YdRoleTypeEnum {

    ROLE_ADMIN_SUPER_MANAGER(1, "平台超级管理员"),
    ROLE_ADMIN_NORMAL_MANAGER(2, "平台普通管理员"),
    ROLE_SUPPLIER(3, "供应商"),
    ROLE_STORE(4, "门店"),
    ROLE_OPERATOR(5, "操作员");

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
