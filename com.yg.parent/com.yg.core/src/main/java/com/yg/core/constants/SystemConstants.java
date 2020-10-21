package com.yg.core.constants;

/**
 * cookie相关的常量
 *
 * @author Xulg
 * Created in 2019-06-03 13:51
 */
public class SystemConstants {


    public static final String YD_PWD_SALT = "!@#$$#@!";

    public static final String YD_MERCHANT_DEFAULT_PASSWORD = "88888888";

    /**
     * 存储admin用户信息的cookie的key
     */
    public static final String YD_ADMIN_PLATFORM = "YdAdminPlatformUserInfo";

    /**
     * 后端商户登录 默认的登录信息cookie有效期
     * 12小时
     */
    public static final int ADMIN_DEFAULT_LOGIN_COOKIE_EXPIRY_SECONDS = 12 * 60 * 60;

    /**
     * 存储图形验证码信息的cookie的key
     */
    public static final String BENEFIT_PICTURE_VERIFICATION_CODE = "BenefitPvcUniqueId";
}
