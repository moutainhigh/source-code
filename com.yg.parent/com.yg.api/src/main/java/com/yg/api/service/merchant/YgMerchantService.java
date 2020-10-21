package com.yg.api.service.merchant;

import com.yg.api.result.merchant.YgMerchantResult;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;
import java.util.List;

/**
 * @Title:商户信息Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-25 16:44:35
 * @Version:1.1.0
 */
public interface YgMerchantService {

	/**
	 * 通过id得到商户信息YgMerchant
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchantResult getYgMerchantById(Integer id);

	/**
	 * 分页查询商户信息YgMerchant
	 * @param ygMerchantResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YgMerchantResult> findYgMerchantListByPage(YgMerchantResult ygMerchantResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户信息YgMerchant
	 * @param ygMerchantResult
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantResult> getAll(YgMerchantResult ygMerchantResult);

	/**
	 * 添加商户信息YgMerchant
	 * @param ygMerchantResult
	 * @Description:
	 */
	public void insertYgMerchant(YgMerchantResult ygMerchantResult) throws BusinessException;

	/**
	 * 通过id修改商户信息YgMerchant throws BusinessException;
	 * @param ygMerchantResult
	 * @Description:
	 */
	public void updateYgMerchant(YgMerchantResult ygMerchantResult) throws BusinessException;

	/**
	 * 启用禁用商户
	 * @param merchantId
	 * @param isEnable 	Y启用  N禁用
	 * @throws BusinessException
	 */
    void updateMerchantStatus(Integer merchantId, String isEnable) throws BusinessException;
}
