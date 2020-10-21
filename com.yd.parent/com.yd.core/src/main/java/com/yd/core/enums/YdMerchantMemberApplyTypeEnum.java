package com.yd.core.enums;

/**
* 商户会员申请类型枚举
* @author wuyc
* created 2020/03/25 10:16
**/
public enum YdMerchantMemberApplyTypeEnum {

    MERCHANT_MEMBER_HYZC("HYZC", "会员注册"),

    MERCHANT_MEMBER_HYXF("HYXF", "会员续费"),

    MERCHANT_MEMBER_HYSJ("HYSJ", "会员升级"),

    MERCHANT_MEMBER_CXKT("CXKT", "重新开通"),

    MEMBER_APPLY_METHOD_ZDKT("ZDKT", "自动开通"),

    MEMBER_APPLY_METHOD_SDKT("SDKT", "手动开通");

    private String code;

    private String desc;

    YdMerchantMemberApplyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YdMerchantMemberApplyTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YdMerchantMemberApplyTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
