package com.yd.api.service.user;

import java.util.List;

import com.yd.api.result.user.YdUserAddressResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户收货地址Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-28 10:17:22
 * @Version:1.1.0
 */
public interface YdUserAddressService {

	/**
	 * 通过id得到用户收货地址YdUserAddress
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserAddressResult getYdUserAddressById(Integer id);

	/**
	 * 分页查询用户收货地址YdUserAddress
	 * @param ydUserAddressResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdUserAddressResult> findYdUserAddressListByPage(YdUserAddressResult ydUserAddressResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有用户收货地址YdUserAddress
	 * @param ydUserAddressResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserAddressResult> getAll(YdUserAddressResult ydUserAddressResult);

	/**
	 * 添加用户收货地址YdUserAddress
	 * @param ydUserAddressResult
	 * @Description:
	 */
	public void insertYdUserAddress(YdUserAddressResult ydUserAddressResult) throws BusinessException;
	
	/**
	 * 通过id修改用户收货地址YdUserAddress throws BusinessException;
	 * @param ydUserAddressResult
	 * @Description:
	 */
	public void updateYdUserAddress(YdUserAddressResult ydUserAddressResult) throws BusinessException;

	/**
	 * 删除用户收货地址
	 * @param userId	 用户id
	 * @param addressId	 收货地址id
	 * @throws BusinessException
	 */
	void deleteYdUserAddress(Integer userId, Integer addressId) throws BusinessException;

	/**
	 * 校验收货地址
	 * @param addressId
	 * @throws BusinessException
	 */
	YdUserAddressResult checkAddressId(Integer addressId) throws BusinessException;
}
