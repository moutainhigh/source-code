package com.yd.core.enums;

/**
 * 用户订单状态枚举
 * @author wuyc
 * created 2019/12/4 21:31
 **/
public enum YdUserOrderStatusEnum {

    WAIT_PAY("WAIT_PAY", "待付款"),
    CANCEL("CANCEL", "订单取消"),
    WAIT_DELIVER("WAIT_DELIVER", "待发货"),
    WAIT_HANDLE("WAIT_HANDLE", "待处理"),
    WAIT_GOODS("WAIT_GOODS", "待收货"),
    SUCCESS("SUCCESS", "已完成");

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 枚举描述
     */
    private String description;

    YdUserOrderStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static YdUserOrderStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (YdUserOrderStatusEnum value : values()) {
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
