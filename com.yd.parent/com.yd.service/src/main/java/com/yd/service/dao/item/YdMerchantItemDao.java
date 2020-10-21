package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdMerchantItem;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:30:19
 * @Version:1.1.0
 */
public interface YdMerchantItemDao {

	/**
	 * 通过id得到商户商品YdShopMerchantItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItem getYdShopMerchantItemById(Integer id);

	/**
	 * 通过itemId得到商户商品YdShopMerchantItem
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public YdMerchantItem getYdShopMerchantItemByItemId(@Param("merchantId") Integer merchantId,
														@Param("itemId") Integer itemId);

	/**
	 * 通过idList得到商户商品YdShopMerchantItem
	 * @param idList
	 * @return
	 * @Description:
	 */
	List<YdMerchantItem> findYdMerchantItemListByIds(@Param("list") List<Integer> idList);

	/**
	 * 得到所有商户商品YdShopMerchantItem
	 * @param ydShopMerchantItem
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItem> getAll(YdMerchantItem ydShopMerchantItem);

	/**
	 * 条件查询商户商品数量
	 * @param ydMerchantItem
	 * @return
	 */
	int getMerchantItemCount(YdMerchantItem ydMerchantItem);

	/**
	 * 分页查询商户商品
	 * @param ydMerchantItem
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<YdMerchantItem> findMerchantItemListByPage(@Param("params") YdMerchantItem ydMerchantItem,
													@Param("pageStart") Integer pageStart,
													@Param("pageSize") Integer pageSize);

	/**
	 * 添加商户商品YdShopMerchantItem
	 * @param ydShopMerchantItem
	 * @Description:
	 */
	public void insertYdShopMerchantItem(YdMerchantItem ydShopMerchantItem);

	/**
	 * 通过id修改商户商品YdShopMerchantItem
	 * @param ydShopMerchantItem
	 * @Description:
	 */
	public void updateYdShopMerchantItem(YdMerchantItem ydShopMerchantItem);

	/**
	 * 删除商户商品
	 * @param merchantId
	 * @param id
	 */
	void deleteMerchantItemById(@Param("merchantId") Integer merchantId,
								@Param("id") Integer id);

	/**
	 *
	 * @param merchantId
	 * @param brandId
	 * @return
	 */
	int getFrontMerchantItemCount(@Param("merchantId") Integer merchantId, @Param("brandId") Integer brandId);

	/**
	 * 分页查询商城商品 (包含销量)
	 * @param brandId	商户id
	 * @param brandId	品牌id
	 * @param type		time | sales | price
	 * @param sort		asc(价格升序) | desc (价格降序)
	 * @param pageSize
	 * @return
	 */
	List<YdMerchantItem> findFrontMerchantItemList(@Param("merchantId") Integer merchantId, @Param("brandId") Integer brandId,
												   @Param("type") String type, @Param("sort") String sort,
												   @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);


}
