package com.yg.api.service.item;

import java.util.List;
import com.yg.api.result.item.YgMerchantIntegralItemResult;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;

/**
 * @Title:商户积分商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-27 13:34:01
 * @Version:1.1.0
 */
public interface YgMerchantIntegralItemService {

	/**
	 * 通过id得到商户积分商品YgMerchantIntegralItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchantIntegralItemResult getYgMerchantIntegralItemById(Integer id);

	/**
	 * 分页查询商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItemResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YgMerchantIntegralItemResult> findYgMerchantIntegralItemListByPage(YgMerchantIntegralItemResult ygMerchantIntegralItemResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItemResult
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantIntegralItemResult> getAll(YgMerchantIntegralItemResult ygMerchantIntegralItemResult);

	/**
	 * 添加商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItemResult
	 * @Description:
	 */
	public void insertYgMerchantIntegralItem(YgMerchantIntegralItemResult ygMerchantIntegralItemResult) throws BusinessException;
	
	/**
	 * 通过id修改商户积分商品YgMerchantIntegralItem throws BusinessException;
	 * @param ygMerchantIntegralItemResult
	 * @Description:
	 */
	public void updateYgMerchantIntegralItem(YgMerchantIntegralItemResult ygMerchantIntegralItemResult) throws BusinessException;

	/**
	 * 上下架商品
	 * @param merchantId
	 * @param isEnable
	 * @throws BusinessException
	 */
	void deleteMerchantItemStatus(Integer merchantId, String isEnable) throws BusinessException;

	/**
	 * 删除商品
	 * @param merchantId
	 * @param isEnable
	 * @throws BusinessException
	 */
	void updateMerchantItemStatus(Integer merchantId, String isEnable) throws BusinessException;
}
