package com.yd.api.service.user;

import com.yd.api.result.user.YdUserResult;
import com.yd.core.utils.BusinessException;

import java.util.List;


/**
 * @Title:用户Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:54:35
 * @Version:1.1.0
 */
public interface YdUserService {

	/**
	 * 通过id得到用户YdShopUser
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdUserResult getYdShopUserById(Integer id);

	/**
	 * 得到所有用户YdShopUser
	 * @param ydShopUser
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdUserResult> getAll(YdUserResult ydShopUser);

	/**
	 * 添加用户YdShopUser
	 * @param ydShopUser
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdShopUser(YdUserResult ydShopUser);

	/**
	 * 修改用户头像
	 * @param userId	用户id
	 * @param imageUrl	头像地址
	 */
	void updateUserImage(Integer userId, String imageUrl) throws BusinessException;

	/**
	 * 用户绑定手机号
	 * @param userId	用户id
	 * @param mobile	手机号
	 * @param code		短信验证码
	 */
	void userBindMobile(Integer userId, String mobile, String code) throws BusinessException;

	/**
	 * 用户绑定手机号发送验证码
	 * @param userId	用户id
	 * @param mobile	手机号
	 */
	void sendUserBindMobileSms(Integer userId, String mobile) throws BusinessException;

	/**
	 * 校验用户信息
	 * @param userId
	 * @return 返回用户信息
	 * @throws BusinessException
	 */
	YdUserResult checkUserInfo(Integer userId) throws BusinessException;
}
