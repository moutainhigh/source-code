package com.yg.core.enums;

/**
 * 短信来源枚举
 * @author wuyc
 * created 2019/10/24 14:39
 **/
public enum YgSmsResourceEnum {

    MERCHANT_FORGET_PASSWORD("merchant_forget_password", "商户忘记密码", 3),

    MERCHANT_SET_PAY_PASSWORD("merchant_set_pay_password", "商户设置支付密码", 3),

    MERCHANT_UPDATE_MOBILE("merchant_update_mobile", "商户更换手机号", 3),

    SHOP_USER_BIND_MOBILE("shop_user_bind_mobile", "商城用户绑定手机号", 3);


    private String	code;

    private String	message;

    /**
     * 短信验证码有效时间(分钟)
     */
    private Integer validTime;

    private YgSmsResourceEnum(String code, String message, Integer validTime) {
        this.code = code;
        this.message = message;
        this.validTime = validTime;
    }

    public static YgSmsResourceEnum getByCode(String code) {
        for (YgSmsResourceEnum afe : values()) {
            if (afe.getCode().equals(code)) {
                return afe;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getValidTime() {
        return validTime;
    }
}
