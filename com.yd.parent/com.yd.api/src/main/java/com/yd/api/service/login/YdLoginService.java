package com.yd.api.service.login;

import com.yd.api.LoginUser;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.utils.BusinessException;

/**
 * 商户登录
 * @author wuyc
 * created 2019/10/16 18:14
 **/
public interface YdLoginService {

    /**
     * 后台用户登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    LoginUser adminLogin(String mobile, String password) throws BusinessException;

    /**
     * 公众号用户登录
     * @param userId
     * @return
     * @throws BusinessException
     */
    LoginUser frontLogin(Integer userId) throws BusinessException;

    /**
     * 后台用户登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    LoginUser appLogin(String mobile, String password) throws BusinessException;

    /**
     * 前台登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    LoginUser frontLogin(String mobile, String password) throws BusinessException;

    /**
     * 判断用户是否登录
     * @param source    admin | shop
     * @param loginUserId   登录用户id
     * @param sessionId     session
     * @return true | false
     */
    boolean checkUserLogin(YdLoginUserSourceEnums ydLoginUserSourceEnums, String loginUserId, String sessionId) throws BusinessException;

    /**
     * 修改商户密码
     * @param mobile
     * @param password
     * @param smsCode
     */
    void forgetPassword(String mobile, String password, String smsCode) throws BusinessException;

    /**
     * app开通会员成功后的自动登录
     * @param memberPayId
     * @return
     * @throws BusinessException
     */
    LoginUser appRegisterSuccessLogin(Integer memberPayId) throws BusinessException;
}
