package com.yd.service.dao.decoration;

import java.util.List;
import com.yd.service.bean.decoration.YdMerchantCompareItem;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户配置比价商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:29:13
 * @Version:1.1.0
 */
public interface YdMerchantCompareItemDao {

	/**
	 * 通过id得到商户配置比价商品YdMerchantCompareItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantCompareItem getYdMerchantCompareItemById(Integer id);

	public List<YdMerchantCompareItem> findListByMerchant(Integer merchantId);

	public void deleteByMerchantItemId(Integer merchantItemId);
	
	/**
	 * 获取数量
	 * @param ydMerchantCompareItem
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantCompareItemCount(YdMerchantCompareItem ydMerchantCompareItem);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantCompareItem
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCompareItem> findYdMerchantCompareItemListByPage(@Param("params") YdMerchantCompareItem ydMerchantCompareItem,
                                                                           @Param("pageStart") Integer pageStart,
                                                                           @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItem
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCompareItem> getAll(YdMerchantCompareItem ydMerchantCompareItem);

	/**
	 * 添加商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItem
	 * @Description:
	 */
	public void insertYdMerchantCompareItem(YdMerchantCompareItem ydMerchantCompareItem);
	
	/**
	 * 通过id修改商户配置比价商品YdMerchantCompareItem
	 * @param ydMerchantCompareItem
	 * @Description:
	 */
	public void updateYdMerchantCompareItem(YdMerchantCompareItem ydMerchantCompareItem);

	public void deleteYdMerchantCompareItem(Integer merchantId);
}
