package com.yd.api.service.market;

import java.util.List;

import com.yd.api.result.market.YdMerchantItemGiftRateResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:门店商品礼品占比Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:59
 * @Version:1.1.0
 */
public interface YdMerchantItemGiftRateService {

	/**
	 * 通过id得到门店商品礼品占比YdMerchantItemGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemGiftRateResult getYdMerchantItemGiftRateById(Integer id);

	/**
	 * 分页查询门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRateResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantItemGiftRateResult> findYdMerchantItemGiftRateListByPage(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRateResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemGiftRateResult> getAll(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult);

	/**
	 * 添加门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRateResult
	 * @Description:
	 */
	public void insertYdMerchantItemGiftRate(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult) throws BusinessException;
	

	/**
	 * 通过id修改门店商品礼品占比YdMerchantItemGiftRate throws BusinessException;
	 * @param ydMerchantItemGiftRateResult
	 * @Description:
	 */
	public void updateYdMerchantItemGiftRate(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult)throws BusinessException;
	
	
}
