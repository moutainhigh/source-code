package com.yd.api.service.decoration;

import java.util.List;

import com.yd.api.result.decoration.YdMerchantCompareItemResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户配置比价商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:29:13
 * @Version:1.1.0
 */
public interface YdMerchantCompareItemService {

	/**
	 * 通过id得到商户配置比价商品YdMerchantCompareItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantCompareItemResult getYdMerchantCompareItemById(Integer id) throws BusinessException;

	/**
	 * 通过merchantItemId删除商户配置比价商品YdMerchantCompareItem
	 * @param merchantItemId
	 * @return
	 * @Description:
	 */
	public void deleteYdMerchantCompareItem(Integer merchantItemId) throws BusinessException;

	/**
	 * 分页查询商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItemResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantCompareItemResult> findYdMerchantCompareItemListByPage(YdMerchantCompareItemResult ydMerchantCompareItemResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItemResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCompareItemResult> getAll(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException;

	/**
	 * 添加商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItemResult
	 * @Description:
	 */
	public void insertYdMerchantCompareItem(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException;
	
	/**
	 * 通过id修改商户配置比价商品YdMerchantCompareItem throws BusinessException;
	 * @param ydMerchantCompareItemResult
	 * @Description:
	 */
	public void updateYdMerchantCompareItem(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException;

	/**
	 * 列表排序
	 * @param merchantId
	 * @param dataList
	 * @throws BusinessException
	 */
    void listSort(Integer merchantId, List<YdMerchantCompareItemResult> dataList) throws BusinessException;

	/**
	 * 批量保存
	 * @param currMerchantId
	 * @param merchantItemIds
	 */
	void batchInsertYdMerchantCompareItem(Integer currMerchantId, String merchantItemIds) throws BusinessException;
}
