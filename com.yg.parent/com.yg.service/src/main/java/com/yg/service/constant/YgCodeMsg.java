package com.yg.service.constant;

/**
 * @Title:定义错误信息
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-29 20:23:18
 * @Version:1.1.0
 */
public class YgCodeMsg {

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public YgCodeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 登录模块 1000XX
    public static YgCodeMsg SMS_CODE_ERROR = new YgCodeMsg("10000", "短信验证码验证码错误");

    // 登录模块 1001XX
    public static YgCodeMsg LOGIN_MOBILE_EMPTY = new YgCodeMsg("10100", "登录手机号不可以为空");
    public static YgCodeMsg LOGIN_PASSWORD_EMPTY = new YgCodeMsg("10101", "登录密码不可以为空");
    public static YgCodeMsg LOGIN_ACCOUNT_ERROR = new YgCodeMsg("10102", "用户名或者密码错误");
    public static YgCodeMsg LOGIN_MERCHANT_NOT_ENABLE = new YgCodeMsg("10103", "账号已被禁用,不可以登录");
    public static YgCodeMsg LOGIN_MERCHANT_NOT_EXIST = new YgCodeMsg("10104", "商户不存在");

    // 商户模块 1002XX
    public static YgCodeMsg MERCHANT_ID_EMPTY = new YgCodeMsg("10200", "商户ID不可以为空");



}
