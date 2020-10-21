package com.yg.api.service.login;

import com.yg.api.result.login.LoginUserResult;
import com.yg.core.enums.YgLoginUserSourceEnums;
import com.yg.core.utils.BusinessException;

/**
 * 商户登录
 * @author wuyc
 * created 2019/10/16 18:14
 **/
public interface YgLoginService {

    /**
     * 后台用户登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    LoginUserResult adminLogin(String mobile, String password) throws BusinessException;

    /**
     * 判断用户是否登录
     * @param ydLoginUserSourceEnums    admin | shop
     * @param loginUserId   登录用户id
     * @param sessionId     session
     * @return true | false
     */
    boolean checkUserLogin(YgLoginUserSourceEnums ydLoginUserSourceEnums, String loginUserId, String sessionId) throws BusinessException;

    /**
     * 修改商户密码
     * @param mobile
     * @param password
     * @param smsCode
     */
    void forgetPassword(String mobile, String password, String smsCode) throws BusinessException;
}
