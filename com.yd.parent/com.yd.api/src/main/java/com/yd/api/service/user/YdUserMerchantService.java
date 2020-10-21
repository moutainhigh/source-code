package com.yd.api.service.user;

import java.util.List;

import com.yd.api.result.user.YdUserMerchantResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户商户绑定关系表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-29 10:11:01
 * @Version:1.1.0
 */
public interface YdUserMerchantService {

	/**
	 * 通过id得到用户商户绑定关系表YdUserMerchant
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserMerchantResult getYdUserMerchantById(Integer id) throws BusinessException;

	/**
	 * 通过userId得到用户商户绑定关系表YdUserMerchant
	 * @param userId
	 * @return
	 * @Description:
	 */
	public YdUserMerchantResult getYdUserMerchantByUserId(Integer userId) throws BusinessException;

	/**
	 * 得到所有用户商户绑定关系表YdUserMerchant
	 * @param ydUserMerchantResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserMerchantResult> getAll(YdUserMerchantResult ydUserMerchantResult) throws BusinessException;

	/**
	 * 添加用户商户绑定关系表YdUserMerchant
	 * @param ydUserMerchantResult
	 * @Description:
	 */
	public void insertYdUserMerchant(YdUserMerchantResult ydUserMerchantResult) throws BusinessException;
	
	/**
	 * 通过id修改用户商户绑定关系表YdUserMerchant throws BusinessException;
	 * @param ydUserMerchantResult
	 * @Description:
	 */
	public void updateYdUserMerchant(YdUserMerchantResult ydUserMerchantResult) throws BusinessException;

	/**
	 * 添加修改用户门店绑定关系
	 * @param userId
	 * @param merchantId
	 * @throws BusinessException
	 */
	public void saveOrUpdate(Integer userId, Integer merchantId) throws BusinessException;
	
}
