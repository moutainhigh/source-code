package com.yg.service.impl.login;

import com.alibaba.fastjson.JSON;
import com.yg.api.result.login.LoginUserResult;
import com.yg.api.service.login.YgLoginService;
import com.yg.api.service.merchant.YgMerchantService;
import com.yg.api.service.sms.YgSmsCodeService;
import com.yg.core.enums.YgLoginUserSourceEnums;
import com.yg.core.enums.YgSmsResourceEnum;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.PasswordUtil;
import com.yg.core.utils.UUIDUtils;
import com.yg.core.utils.ValidateBusinessUtils;
import com.yg.service.bean.login.YgLoginSession;
import com.yg.service.bean.merchant.YgMerchant;
import com.yg.service.dao.login.YgLoginSessionDao;
import com.yg.service.dao.merchant.YgMerchantDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wuyc
 * created 2019/10/17 9:39
 **/
@Service(dynamic = true)
public class YgAdminLoginServiceImpl implements YgLoginService {

    private static final Logger logger = LoggerFactory.getLogger(YgAdminLoginServiceImpl.class);

    @Resource
    private YgMerchantDao ygMerchantDao;

    @Resource
    private YgSmsCodeService ygSmsCodeService;

    @Resource
    private YgLoginSessionDao ydLoginSessionDao;

    @Resource
    private YgMerchantService ygMerchantService;

    /**
     * 后台管理商户登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginUserResult adminLogin(String mobile, String password) throws BusinessException {
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile), "err_no_username", "登录用户名不能为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(password), "err_no_password", "登录密码不能为空");

        logger.info("====后台登录mobile=" + mobile + " ,password=" + password);
        YgMerchant ydMerchant = new YgMerchant();
        ydMerchant.setPassword(PasswordUtil.encryptPassword(password));
        ydMerchant.setMobile(mobile);
        List<YgMerchant> merchantList = ygMerchantDao.getAll(ydMerchant);
        ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(merchantList),
                "err_login", "用户名或密码错误");

        YgMerchant adminMerchant = merchantList.get(0);

        ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(adminMerchant.getIsEnable()),
                "error_is_flag", "账号已被禁用,不可以登录");

        // 保存登录session
        YgLoginSession loginSession = saveLoginUserSession(adminMerchant.getId(), YgLoginUserSourceEnums.YG_ADMIN_MERCHANT.getCode());

        // 返回session信息
        LoginUserResult loginUser = new LoginUserResult();
        loginUser.setMerchantId(adminMerchant.getId());
        loginUser.setSessionId(loginSession.getSessionId());
        return loginUser;
    }

    /**
     * 判断用户是否登录
     * @param ydLoginUserSourceEnums
     * @param loginUserId   登录用户id
     * @param sessionId     session
     * @return true | false
     */
    @Override
    public boolean checkUserLogin(YgLoginUserSourceEnums ydLoginUserSourceEnums, String loginUserId, String sessionId) {
        YgLoginSession ygLoginSession = new YgLoginSession();
        ygLoginSession.setSessionId(sessionId);
        ygLoginSession.setLoginUserId(Integer.valueOf(loginUserId));
        ygLoginSession.setUserSource(ydLoginUserSourceEnums.getCode());
        List<YgLoginSession> loginSessionList = ydLoginSessionDao.getAll(ygLoginSession);
        if (loginSessionList.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 商户忘记密码
     * @param mobile    用户手机号
     * @param password  用户密码
     * @param smsCode   手机验证码
     */
    @Override
    public void forgetPassword(String mobile, String password, String smsCode) {
        logger.info("====商户忘记密码入参=mobile" + mobile + ", password=" + password);
        YgMerchant ygMerchant = ygMerchantDao.getYdMerchantByMobile(mobile);
        ValidateBusinessUtils.assertFalse(ygMerchant == null,
                "err_no_merchant", "商户不存在");

        // 查询验证码是否有效
        if (!ygSmsCodeService.getLastSmsCode(mobile, smsCode, YgLoginUserSourceEnums.YG_ADMIN_MERCHANT.getCode(),
                YgSmsResourceEnum.MERCHANT_FORGET_PASSWORD.getCode())) {
            ValidateBusinessUtils.assertFalse(true, "err_smsCode", "验证码错误");
        }

        String newPassword = PasswordUtil.encryptPassword(password);
        ygMerchant.setPassword(newPassword);
        ygMerchantDao.updateYgMerchant(ygMerchant);
    }

    /**
     * 保存用户登录session
     * @param loginUserId   登录用户ID
     * @param resource      登录来源
     * @return YdLoginSession
     */
    private YgLoginSession saveLoginUserSession(Integer loginUserId, String resource) {
        Date nowDate = new Date();
        YgLoginSession loginSession = new YgLoginSession();
        loginSession.setCreateTime(nowDate);
        loginSession.setUpdateTime(nowDate);
        loginSession.setLoginTime(nowDate);
        loginSession.setLoginUserId(loginUserId);
        loginSession.setSessionId(UUIDUtils.getUUID());
        loginSession.setUserSource(resource);
        ydLoginSessionDao.insertYdLoginSession(loginSession);
        return loginSession;
    }

}
