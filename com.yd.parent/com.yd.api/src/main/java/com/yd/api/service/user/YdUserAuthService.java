package com.yd.api.service.user;

import java.util.List;

import com.yd.api.result.user.YdUserAuthResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户授权表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 11:34:57
 * @Version:1.1.0
 */
public interface YdUserAuthService {

	/**
	 * 通过id得到用户授权表YdUserAuth
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserAuthResult getYdUserAuthById(Integer id);

	/**
	 * 分页查询用户授权表YdUserAuth
	 * @param ydUserAuthResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdUserAuthResult> findYdUserAuthListByPage(YdUserAuthResult ydUserAuthResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有用户授权表YdUserAuth
	 * @param ydUserAuthResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserAuthResult> getAll(YdUserAuthResult ydUserAuthResult);

	/**
	 * 添加用户授权表YdUserAuth
	 * @param ydUserAuthResult
	 * @Description:
	 */
	public void insertYdUserAuth(YdUserAuthResult ydUserAuthResult) throws BusinessException;

	/**
	 * 通过id修改用户授权表YdUserAuth throws BusinessException;
	 * @param ydUserAuthResult
	 * @Description:
	 */
	public void updateYdUserAuth(YdUserAuthResult ydUserAuthResult)throws BusinessException;

	public YdUserAuthResult addUserAuth(String openId, String accessToken);
	
}
