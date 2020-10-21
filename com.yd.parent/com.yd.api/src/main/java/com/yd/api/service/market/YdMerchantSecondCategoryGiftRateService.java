package com.yd.api.service.market;

import java.util.List;

import com.yd.api.result.market.YdMerchantSecondCategoryGiftRateResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:门店二级分类礼品占比Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:52:52
 * @Version:1.1.0
 */
public interface YdMerchantSecondCategoryGiftRateService {

	/**
	 * 通过id得到门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantSecondCategoryGiftRateResult getYdMerchantSecondCategoryGiftRateById(Integer id);

	/**
	 * 分页查询门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRateResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantSecondCategoryGiftRateResult> findYdMerchantSecondCategoryGiftRateListByPage(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRateResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantSecondCategoryGiftRateResult> getAll(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult);

	/**
	 * 添加门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRateResult
	 * @Description:
	 */
	public void insertYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult) throws BusinessException;
	

	/**
	 * 通过id修改门店二级分类礼品占比YdMerchantSecondCategoryGiftRate throws BusinessException;
	 * @param ydMerchantSecondCategoryGiftRateResult
	 * @Description:
	 */
	public void updateYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult)throws BusinessException;
	
	
}
