package com.yd.api.service.market;

import java.util.List;

import com.yd.api.result.market.YdMerchantFirstCategoryGiftRateResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:门店一级分类礼品占比Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 14:12:58
 * @Version:1.1.0
 */
public interface YdMerchantFirstCategoryGiftRateService {

	/**
	 * 通过id得到门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantFirstCategoryGiftRateResult getYdMerchantFirstCategoryGiftRateById(Integer id);

	/**
	 * 分页查询门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRateResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantFirstCategoryGiftRateResult> findYdMerchantFirstCategoryGiftRateListByPage(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRateResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantFirstCategoryGiftRateResult> getAll(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult);

	/**
	 * 添加门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRateResult
	 * @Description:
	 */
	public void insertYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult) throws BusinessException;
	

	/**
	 * 通过id修改门店一级分类礼品占比YdMerchantFirstCategoryGiftRate throws BusinessException;
	 * @param ydMerchantFirstCategoryGiftRateResult
	 * @Description:
	 */
	public void updateYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult)throws BusinessException;
	
	
}
