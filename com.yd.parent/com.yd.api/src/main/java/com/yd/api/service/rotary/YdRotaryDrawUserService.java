package com.yd.api.service.rotary;

import com.yd.api.result.rotary.YdRotaryDrawUserResult;
import com.yd.core.utils.BusinessException;

import java.util.List;

/**
 * @Title:用户Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-18 11:46:36
 * @Version:1.1.0
 */
public interface YdRotaryDrawUserService {

	/**
	 * 查询用户剩余抽奖次数
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws BusinessException
	 */
	Integer getUserCanUseDrawCount(Integer userId, String uuid) throws BusinessException;

	/**
	 * 扣除用户抽奖次数
	 * @param userId
	 * @param uuid
	 * @return
	 * @throws BusinessException
	 */
	Integer reduceUserDrawCount(Integer userId, String uuid) throws BusinessException;

	/**
	 * 通过id得到用户YdRotaryDrawUser
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawUserResult getYdRotaryDrawUserById(Integer id);

	/**
	 * 得到所有用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawUserResult> getAll(YdRotaryDrawUserResult ydRotaryDrawUser);;

	/**
	 * 添加用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawUser(YdRotaryDrawUserResult ydRotaryDrawUser);;
	
	/**
	 * 通过id修改用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawUser(YdRotaryDrawUserResult ydRotaryDrawUser);;


}
