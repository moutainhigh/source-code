package com.yd.service.impl.user;

import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.user.YdUserResult;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.service.bean.user.YdUser;
import com.yd.service.dao.user.YdUserDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Title:用户Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:54:35
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserServiceImpl implements YdUserService {

	@Resource
	private YdUserDao ydUserDao;

	@Resource
	private YdSmsCodeService ydSmsCodeService;

	@Override
	public YdUserResult getYdShopUserById(Integer id) {
		if (id == null || id < 0) {
			return null;
		}
		YdUser shopUser = ydUserDao.getYdUserById(id);
		YdUserResult shopUserResult = null;
		if (shopUser != null) {
			shopUserResult = new YdUserResult();
			BeanUtilExt.copyProperties(shopUserResult, shopUser);
		}
		return shopUserResult;
	}

	@Override
	public List<YdUserResult> getAll(YdUserResult ydShopUser) {
		return null;
	}

	@Override
	public void insertYdShopUser(YdUserResult ydShopUser) {

	}

	/**
	 * 修改用户头像
	 * @param userId	用户id
	 * @param imageUrl	头像地址
	 * @throws BusinessException
	 */
	@Override
	public void updateUserImage(Integer userId, String imageUrl) throws BusinessException {
		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
				"err_user_id", "用户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(imageUrl),
				"err_image_url", "头像地址不可以为空");
		YdUser ydUser = ydUserDao.getYdUserById(userId);
		ValidateBusinessUtils.assertFalse(ydUser == null,
				"err_user_empty", "用户不存在");
		ydUser.setImage(imageUrl);
		this.ydUserDao.updateYdUser(ydUser);
	}

	/**
	 * 用户绑定手机号
	 * @param userId	用户id
	 * @param mobile	手机号
	 * @param code		短信验证码
	 * @throws BusinessException
	 */
	@Override
	public void userBindMobile(Integer userId, String mobile, String code) throws BusinessException {
		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
				"err_user_id", "用户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile),
				"err_mobile_empty", "手机号不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(code),
				"err_code_empty", "短信验证码不可以为空");

		YdUser ydUser = ydUserDao.getYdUserById(userId);
		ValidateBusinessUtils.assertFalse(ydUser == null,
				"err_user_empty", "用户不存在");

		YdUser userMobile = ydUserDao.getYdUserByMobile(mobile);
		ValidateBusinessUtils.assertFalse(userMobile != null && !ydUser.getId().equals(userMobile.getId()),
				"err_exist_mobile", "手机号已经绑定");

		boolean checkResult = ydSmsCodeService.getLastSmsCode(mobile, code, YdLoginUserSourceEnums.YD_SHOP_USER.getCode(), YdSmsResourceEnum.SHOP_USER_BIND_MOBILE.getCode());
		ValidateBusinessUtils.assertFalse(!checkResult,  "err_sms_code", "短信验证码错误");

		ydUser.setMobile(mobile);
		this.ydUserDao.updateYdUser(ydUser);
	}

	/**
	 * 用户绑定手机号发送验证码
	 * @param userId	用户id
	 * @param mobile	手机号
	 * @throws BusinessException
	 */
	@Override
	public void sendUserBindMobileSms(Integer userId, String mobile) throws BusinessException {
		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
				"err_user_id", "用户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile),
				"err_mobile_empty", "手机号不可以为空");

		YdUser ydUser = ydUserDao.getYdUserById(userId);
		ValidateBusinessUtils.assertFalse(ydUser == null,
				"err_user_empty", "用户不存在");

		YdUser userMobile = ydUserDao.getYdUserByMobile(mobile);
		ValidateBusinessUtils.assertFalse(userMobile != null && userMobile.getId().equals(ydUser.getId()),
				"err_exist_mobile", "手机号已经绑定");

		// 发送短信验证码
		ydSmsCodeService.sendSmsCode(mobile, YdLoginUserSourceEnums.YD_SHOP_USER.getCode(), YdSmsResourceEnum.SHOP_USER_BIND_MOBILE);
	}

	/**
	 * 校验用户信息
	 * @param userId
	 * @return 返回用户信息
	 * @throws BusinessException
	 */
	@Override
	public YdUserResult checkUserInfo(Integer userId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(userId, "err_empty_user_id", "用户id不可以为空");

		YdUser ydUser = ydUserDao.getYdUserById(userId);
		ValidateBusinessUtils.assertNonNull(ydUser,"err_not_exist_user", "用户不存在");

		YdUserResult ydUserResult = new YdUserResult();
		BeanUtilExt.copyProperties(ydUserResult, ydUser);
		return ydUserResult;
	}
}

