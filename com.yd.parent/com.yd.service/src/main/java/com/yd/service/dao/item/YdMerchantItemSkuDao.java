package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdMerchantItemSku;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户商品skuDao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:39:56
 * @Version:1.1.0
 */
public interface YdMerchantItemSkuDao {

	/**
	 * 通过id得到商户商品skuYdMerchantItemSku
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemSku getYdMerchantItemSkuById(Integer id);

	/**
	 * 通过idList得到商户商品skuYdMerchantItemSku
	 * @param skuIdList
	 * @return
	 * @Description:
	 */
	List<YdMerchantItemSku> findYdMerchantItemSkuListByIds(@Param("list") List<Integer> skuIdList);

	/**
	 * 通过merchantItemId得到商户商品skuYdMerchantItemSku
	 * @param merchantItemId
	 * @param isEnable
	 * @return
	 * @Description:
	 */
	List<YdMerchantItemSku> findSkuListByMerchantItemId(@Param("merchantItemId") Integer merchantItemId,
														@Param("isEnable") String isEnable);

	/**
	 * 得到所有商户商品skuYdMerchantItemSku
	 * @param ydMerchantItemSku
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemSku> getAll(YdMerchantItemSku ydMerchantItemSku);


	/**
	 * 添加商户商品skuYdMerchantItemSku
	 * @param ydMerchantItemSku
	 * @Description:
	 */
	public void insertYdMerchantItemSku(YdMerchantItemSku ydMerchantItemSku);
	

	/**
	 * 通过id修改商户商品skuYdMerchantItemSku
	 * @param ydMerchantItemSku
	 * @Description:
	 */
	public void updateYdMerchantItemSku(YdMerchantItemSku ydMerchantItemSku);

	void deleteMerchantItemSkuByMerchantItemId(@Param("merchantItemId") Integer merchantItemId);

	int reduceItemSkuStock(@Param("skuId") Integer skuId, @Param("num") Integer num);

	int addItemSkuStock(@Param("skuId") Integer skuId, @Param("num") Integer num);

}
