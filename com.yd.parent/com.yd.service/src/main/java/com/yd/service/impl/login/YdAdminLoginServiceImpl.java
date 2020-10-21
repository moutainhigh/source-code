package com.yd.service.impl.login;

import com.alibaba.fastjson.JSON;
import com.yd.api.LoginUser;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.member.YdMerchantMemberPayRecordResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.login.YdLoginService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PasswordUtil;
import com.yd.core.utils.UUIDUtils;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.service.bean.login.YdLoginSession;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.user.YdUser;
import com.yd.service.dao.login.YdLoginSessionDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.user.YdUserDao;
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
public class YdAdminLoginServiceImpl implements YdLoginService {

    private static final Logger logger = LoggerFactory.getLogger(YdAdminLoginServiceImpl.class);

    @Resource
    private YdUserDao ydUserDao;

    @Resource
    private YdMerchantDao ydMerchantDao;

    @Resource
    private YdUserDao ydShopUserDao;

    @Resource
    private YdLoginSessionDao ydLoginSessionDao;

    @Resource
    private YdSmsCodeService ydSmsCodeService;

    @Resource
    private YdMerchantService ydMerchantService;

    @Resource
    private YdMerchantMemberPayRecordService ydMerchantMemberPayRecordService;

    /**
     * 后台管理商户登录
     * @param mobile
     * @param password
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginUser adminLogin(String mobile, String password) throws BusinessException {
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile), "err_no_username", "登录用户名不能为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(password), "err_no_password", "登录密码不能为空");

        logger.info("====后台登录mobile=" + mobile + " ,password=" + password);
        YdMerchant ydAdminMerchant = new YdMerchant();
        ydAdminMerchant.setPassword(PasswordUtil.encryptPassword(password));
        ydAdminMerchant.setMobile(mobile);
        List<YdMerchant> adminMerchantList = ydMerchantDao.getAll(ydAdminMerchant);
        logger.info("====后台登录adminMerchantList" + JSON.toJSONString(adminMerchantList));
        ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(adminMerchantList),
                "err_login", "用户名或密码错误");

        YdMerchant adminMerchant = adminMerchantList.get(0);

        ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(adminMerchant.getIsFlag()),
                "error_is_flag", "账号已被删除或者禁用,不可以登录");

        // 如果是商户或者供应商, 校验会员是否到期
        long currentTimeMillis = System.currentTimeMillis();
        if (EnumSiteGroup.MERCHANT.getCode().equalsIgnoreCase(adminMerchant.getGroupCode())) {
            YdMerchantResult storeMerchant = ydMerchantService.getStoreInfo(adminMerchant.getId());
            ValidateBusinessUtils.assertFalse(storeMerchant.getMemberValidTime() == null ||
                            currentTimeMillis > storeMerchant.getMemberValidTime().getTime(),
                    "error_member_valid_time", "请联系管理人员充值会员");
        } else if (EnumSiteGroup.SUPPLIER.getCode().equalsIgnoreCase(adminMerchant.getGroupCode())) {
            ValidateBusinessUtils.assertFalse(adminMerchant.getMemberValidTime() == null ||
                            currentTimeMillis > adminMerchant.getMemberValidTime().getTime(),
                    "error_member_valid_time", "请联系管理人员充值会员");
        }

        // 保存登录session
        YdLoginSession loginSession = saveLoginUserSession(adminMerchant.getId(), YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode());

        // 返回session信息
        LoginUser loginUser = new LoginUser();
        loginUser.setMerchantId(adminMerchant.getId());
        loginUser.setSessionId(loginSession.getSessionId());
        return loginUser;
    }

    @Override
    public LoginUser frontLogin(Integer userId) throws BusinessException {
        ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
                "err_empty_user_id", "用户id不可以为空");

        YdUser ydUser = ydUserDao.getYdUserById(userId);
        ValidateBusinessUtils.assertFalse(ydUser == null,
                "err_not_exist_user", "用户不存在");

        // 保存登录session
        YdLoginSession loginSession = saveLoginUserSession(userId, YdLoginUserSourceEnums.YD_SHOP_USER.getCode());

        // 返回session信息
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setSessionId(loginSession.getSessionId());
        return loginUser;
    }

    @Override
    public LoginUser appLogin(String mobile, String password) throws BusinessException {
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile), "err_no_username", "登录用户名不能为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(password), "err_no_password", "登录密码不能为空");
        logger.info("====app登录入参=mobile" + mobile + ", password=" + password);

        YdMerchant ydAdminMerchant = new YdMerchant();
        ydAdminMerchant.setPassword(PasswordUtil.encryptPassword(password));
        ydAdminMerchant.setMobile(mobile);
        List<YdMerchant> adminMerchantList = ydMerchantDao.getAll(ydAdminMerchant);
        logger.info("====adminMerchantList=" + JSON.toJSONString(adminMerchantList));
        ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(adminMerchantList),
                "err_login", "用户名或密码错误");

        YdMerchant adminMerchant = adminMerchantList.get(0);

        ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(adminMerchant.getIsFlag()),
                "error_is_flag", "账号已被删除或者禁用,不可以登录");

        ValidateBusinessUtils.assertFalse(!EnumSiteGroup.MERCHANT.getCode().equalsIgnoreCase(adminMerchant.getGroupCode()),
                "error_site_group", "只有门店和操作员才可以登录");

        long currentTimeMillis = System.currentTimeMillis();
        YdMerchantResult storeMerchant = ydMerchantService.getStoreInfo(adminMerchant.getId());
        ValidateBusinessUtils.assertFalse(storeMerchant.getMemberValidTime() == null ||
                        currentTimeMillis > storeMerchant.getMemberValidTime().getTime(),
                "error_member_valid_time", "请联系管理人员充值会员");

        // 保存登录session
        YdLoginSession loginSession =  saveLoginUserSession(adminMerchant.getId(), YdLoginUserSourceEnums.YD_APP_MERCHANT.getCode());

        // 返回session信息
        LoginUser loginUser = new LoginUser();
        loginUser.setMerchantId(adminMerchant.getId());
        loginUser.setSessionId(loginSession.getSessionId());
        return loginUser;
    }

    @Override
    public LoginUser frontLogin(String mobile, String password) throws BusinessException {
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile), "err_no_username", "登录用户名不能为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(password), "err_no_password", "登录密码不能为空");

        YdUser ydShopUser = new YdUser();
        ydShopUser.setMobile(mobile);
        List<YdUser> shopUserList = ydShopUserDao.getAll(ydShopUser);
        logger.info("----shopUserList" + JSON.toJSONString(shopUserList));
        ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(shopUserList), "err_login", "用户名或密码错误");

        // 保存用户session
        ydShopUser =  shopUserList.get(0);
        YdLoginSession loginSession =  saveLoginUserSession(ydShopUser.getId(), YdLoginUserSourceEnums.YD_SHOP_USER.getCode());

        LoginUser loginUser = new LoginUser();
        loginUser.setMerchantId(ydShopUser.getId());
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
    public boolean checkUserLogin(YdLoginUserSourceEnums ydLoginUserSourceEnums, String loginUserId, String sessionId) {
        YdLoginSession ydLoginSession = new YdLoginSession();
        ydLoginSession.setSessionId(sessionId);
        ydLoginSession.setLoginUserId(Integer.valueOf(loginUserId));
        ydLoginSession.setUserSource(ydLoginUserSourceEnums.getCode());
        List<YdLoginSession> loginSessionList = ydLoginSessionDao.getAll(ydLoginSession);
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
        YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(mobile);
        ValidateBusinessUtils.assertFalse(ydMerchant == null,
                "err_no_merchant", "商户不存在");

        // 查询验证码是否有效
        if (!ydSmsCodeService.getLastSmsCode(mobile, smsCode, YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
                YdSmsResourceEnum.MERCHANT_FORGET_PASSWORD.getCode())) {
            ValidateBusinessUtils.assertFalse(true, "err_smsCode", "验证码错误");
        }

        String newPassword = PasswordUtil.encryptPassword(password);
        ydMerchant.setPassword(newPassword);
        ydMerchantDao.updateYdMerchant(ydMerchant);
    }

    /**
     * app开通会员成功后的自动登录
     * @param memberPayId
     * @return
     * @throws BusinessException
     */
    @Override
    public LoginUser appRegisterSuccessLogin(Integer memberPayId) throws BusinessException {
        ValidateBusinessUtils.assertFalse(memberPayId == null || memberPayId <= 0,
                "err_no_merchant", "app会员注册成功后登录id不可以为空");
        YdMerchantMemberPayRecordResult payRecord = ydMerchantMemberPayRecordService.getYdMerchantMemberPayRecordById(memberPayId);
        ValidateBusinessUtils.assertFalse(payRecord == null,
                "err_no_merchant", "app会员注册记录不存在");
        ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(payRecord.getIsPay()),
                "err_no_merchant", "请先去支付或者刷新页面重新登录");

        YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(payRecord.getMobile());

        // 保存登录session
        YdLoginSession loginSession =  saveLoginUserSession(ydMerchant.getId(), YdLoginUserSourceEnums.YD_APP_MERCHANT.getCode());

        // 返回session信息
        LoginUser loginUser = new LoginUser();
        loginUser.setMerchantId(ydMerchant.getId());
        loginUser.setSessionId(loginSession.getSessionId());
        return loginUser;
    }

    /**
     * 保存用户登录session
     * @param loginUserId   登录用户ID
     * @param resource      登录来源
     * @return YdLoginSession
     */
    private YdLoginSession saveLoginUserSession(Integer loginUserId, String resource) {
        Date nowDate = new Date();
        YdLoginSession loginSession = new YdLoginSession();
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
