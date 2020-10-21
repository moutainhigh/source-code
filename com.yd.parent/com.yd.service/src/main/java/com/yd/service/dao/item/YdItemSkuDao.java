package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemSku;


/**
 * @Title:平台商品skuDao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:31:49
 * @Version:1.1.0
 */
public interface YdItemSkuDao {

	/**
	 * 通过id得到平台商品skuYdItemSku
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemSku getYdItemSkuById(Integer id);

	/**
	 * 通过itemId得到平台商品skuYdItemSku
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public List<YdItemSku> findItemSkuListByItemId(Integer itemId);

	/**
	 * 得到所有平台商品skuYdItemSku
	 * @param ydItemSku
	 * @return 
	 * @Description:
	 */
	public List<YdItemSku> getAll(YdItemSku ydItemSku);


	/**
	 * 添加平台商品skuYdItemSku
	 * @param ydItemSku
	 * @Description:
	 */
	public void insertYdItemSku(YdItemSku ydItemSku);
	

	/**
	 * 通过id修改平台商品skuYdItemSku
	 * @param ydItemSku
	 * @Description:
	 */
	public void updateYdItemSku(YdItemSku ydItemSku);

	/**
	 * 删除平台商品对应的sku
	 * @param itemId
	 */
    void deleteItemSkuByItemId(Integer itemId);
}
