package com.yd.core.enums;

/**
 * 短信来源枚举
 * @author wuyc
 * created 2019/10/24 14:39
 **/
public enum YdSmsResourceEnum {

    MERCHANT_FORGET_PASSWORD("merchant_forget_password", "商户忘记密码", 10),

    MERCHANT_SET_PAY_PASSWORD("merchant_set_pay_password", "商户设置支付密码", 10),

    MERCHANT_UPDATE_MOBILE("merchant_update_mobile", "商户更换手机号", 10),

    SHOP_USER_BIND_MOBILE("shop_user_bind_mobile", "商城用户绑定手机号", 10),

    MERCHANT_REGISTER_MOBILE("merchant_register_mobile", "商户注册绑定", 10),

    YD_SMS_FREE_SIGN_NAME("快抢商城", "短信验证码签名", 0),

    YD_SMS_CODE_TEMPLATE("SMS_186942914", "短信验证码模板", 0);


    private String	code;

    private String	message;

    /**
     * 短信验证码有效时间(分钟)
     */
    private Integer validTime;

    private YdSmsResourceEnum(String code, String message, Integer validTime) {
        this.code = code;
        this.message = message;
        this.validTime = validTime;
    }

    public static YdSmsResourceEnum getByCode(String code) {
        for (YdSmsResourceEnum afe : values()) {
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
