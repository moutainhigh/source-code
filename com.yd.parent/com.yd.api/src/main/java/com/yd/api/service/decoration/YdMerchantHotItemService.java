package com.yd.api.service.decoration;

import java.util.List;

import com.yd.api.result.decoration.YdMerchantHotItemResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:门店首页热门商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:09:00
 * @Version:1.1.0
 */
public interface YdMerchantHotItemService {

	/**
	 * 通过id得到门店首页热门商品YdMerchantHotItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantHotItemResult getYdMerchantHotItemById(Integer id);

	/**
	 * 分页查询门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItemResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantHotItemResult> findYdMerchantHotItemListByPage(YdMerchantHotItemResult ydMerchantHotItemResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItemResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantHotItemResult> getAll(YdMerchantHotItemResult ydMerchantHotItemResult);

	/**
	 * 门店热门商品排序
	 * @param hotItemList
	 * @throws BusinessException
	 */
	public void sortHotItem(List<YdMerchantHotItemResult> hotItemList) throws BusinessException;

	/**
	 * 添加门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItemResult
	 * @Description:
	 */
	public void insertYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult) throws BusinessException;

	/**
	 * 批量添加门店首页热门商品YdMerchantHotItem
	 * @param merchantId
	 * @param hotItemResultList
	 * @throws BusinessException
	 */
	public void insertYdMerchantHotItemList(Integer merchantId, List<YdMerchantHotItemResult> hotItemResultList) throws BusinessException;
	
	/**
	 * 通过id修改门店首页热门商品YdMerchantHotItem throws BusinessException;
	 * @param ydMerchantHotItemResult
	 * @Description:
	 */
	public void updateYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult)throws BusinessException;

	/**
	 * 通过id修改门店首页热门商品YdMerchantHotItem throws BusinessException;
	 * @param ydMerchantHotItemResult
	 * @Description:
	 */
	public void deleteYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult)throws BusinessException;


	/**
	 * 查询商城热门商品
	 * @param merchantId
	 * @return
	 */
    List<YdMerchantHotItemResult> findFrontHotItemList(Integer merchantId);
}
