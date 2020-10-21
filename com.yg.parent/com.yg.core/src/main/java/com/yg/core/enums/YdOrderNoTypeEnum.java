package com.yg.core.enums;

/**
* 订单规则枚举
* @author wuyc
* created 2019/12/11 15:16
**/
public enum YdOrderNoTypeEnum {

    ITEM_ORDER("00", "商品交易订单"),
    GIFT_IN("01", "礼品入库订单"),
    GIFT_OUT("02", "礼品出库订单");

    /** 订单规格编码 */
    private String code;

    /** 订单规则描述 */
    private String desc;

    YdOrderNoTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YdOrderNoTypeEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YdOrderNoTypeEnum value : values()) {
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
