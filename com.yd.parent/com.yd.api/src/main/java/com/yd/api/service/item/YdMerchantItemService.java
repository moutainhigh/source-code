package com.yd.api.service.item;

import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.List;

/**
 * @Title:商户商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:30:19
 * @Version:1.1.0
 */
public interface YdMerchantItemService {

	/**
	 * 通过id得到商户商品YdShopMerchantItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemResult getYdShopMerchantItemById(Integer id);

	/**
	 * 得到所有商户商品YdShopMerchantItem
	 * @param ydShopMerchantItemResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemResult> getAll(YdMerchantItemResult ydShopMerchantItemResult);

	/**
	 * 分页查询商品列表
	 * @param params
	 * @param pagerInfo
	 * @return
	 * @throws BusinessException
	 */
	Page<YdMerchantItemResult> findMerchantItemListByPage(YdMerchantItemResult params, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 获取商户商品详情
	 * @param merchantId	商户id
	 * @param id			商户商品id
	 * @param itemId		商品库商品id
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantItemResult getMerchantItemDetail(Integer merchantId, Integer id, Integer itemId) throws BusinessException;

	/**
	 * 获取前台商品详情
	 * @param merchantItemId
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantItemResult getFrontMerchantItemDetail(Integer merchantItemId) throws BusinessException;

	/**
	 *
	 * @param itemInfo
	 * @throws BusinessException
	 */
	void saveOrUpdate(YdMerchantItemResult itemInfo) throws BusinessException;

	/**
	 * 删除商家商品
	 * @param merchantId		商户id
	 * @param merchantItemId	商户商品id
	 * @throws BusinessException
	 */
    void deleteMerchantItem(Integer merchantId, Integer merchantItemId) throws BusinessException;

	/**
	 * 商户上下架商品
	 * @param merchantId
	 * @param merchantItemId
	 * @param type	up | down
	 * @throws BusinessException
	 */
	void upOrDownMerchantItem(Integer merchantId, Integer merchantItemId, String type) throws BusinessException;

	/**
	 * 分页查询商城商品 (包含销量)
	 * @param merchantId 商户id
	 * @param brandId	 品牌id
	 * @param type		 time(时间倒序) | sales(销量倒序) | price(价格排序)
	 * @param sort		 asc(价格升序) | desc (价格降序)
	 * @param pagerInfo
	 * @return
	 * @throws BusinessException
	 */
	Page<YdMerchantItemResult> findFrontMerchantItemList(Integer merchantId, Integer brandId, String type, String sort,
														 PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 获取比价商品列表
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	List<YdMerchantItemResult> findFrontCompareItemList(Integer merchantId) throws BusinessException;

	/**
	 * 用户根据购物车或者商品查询商品规格列表
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param skuId		 商户商品规格id
	 * @param num		 商品数量
	 * @param carIds	 购物车ids
	 * @param type	 	 car | item
	 * @return
	 * @throws BusinessException
	 */
	List<YdMerchantItemSkuResult> findMerchantItemListByCarIdList(Integer userId, Integer merchantId, Integer skuId,
																  Integer num, String carIds, String type) throws BusinessException;

	/**
	 * 订单下单减库存
	 * @param skuList
	 */
	void reduceItemSkuStock(List<YdMerchantItemSkuResult> skuList) throws BusinessException;

	/**
	 * 商品下单失败退还库存
	 * @param skuList
	 */
	void addItemSkuStock(List<YdMerchantItemSkuResult> skuList) throws BusinessException;

	/**
	 * 统计平台商品， 平台商品规格，
	 * 商户商品， 商户商品规格销量
	 */
	void synItemSales();

}
