package com.yd.api.service.market;

import java.util.List;

import com.yd.api.result.market.YdMerchantGiftRateResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:门店礼品占比Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:09
 * @Version:1.1.0
 */
public interface YdMerchantGiftRateService {

	/**
	 * 通过id得到门店礼品占比YdMerchantGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftRateResult getYdMerchantGiftRateById(Integer id);

	/**
	 * 分页查询门店礼品占比YdMerchantGiftRate
	 * @param ydMerchantGiftRateResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantGiftRateResult> findYdMerchantGiftRateListByPage(YdMerchantGiftRateResult ydMerchantGiftRateResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店礼品占比YdMerchantGiftRate
	 * @param ydMerchantGiftRateResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftRateResult> getAll(YdMerchantGiftRateResult ydMerchantGiftRateResult);

	/**
	 * 更新设置礼品比率
	 * @param type	store | first | second | item
	 * @param merchantId	礼品比率
	 * @param rate	礼品比率
	 * @param firstCategoryId 一级分类id
	 * @param secondCategoryId	二级分类id
	 * @param merchantItemId	商户商品id
	 * @return
	 */
    void updateGiftRate(String type, Integer merchantId, Integer rate, Integer firstCategoryId, Integer secondCategoryId, Integer merchantItemId) throws BusinessException;
}
