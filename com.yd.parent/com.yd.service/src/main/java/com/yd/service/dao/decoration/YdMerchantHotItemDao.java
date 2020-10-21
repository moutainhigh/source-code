package com.yd.service.dao.decoration;

import java.util.List;
import com.yd.service.bean.decoration.YdMerchantHotItem;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店首页热门商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:09:00
 * @Version:1.1.0
 */
public interface YdMerchantHotItemDao {

	/**
	 * 通过id得到门店首页热门商品YdMerchantHotItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantHotItem getYdMerchantHotItemById(Integer id);

	/**
	 * 通过merchantId和merchantItemId得到门店首页热门商品YdMerchantHotItem
	 * @param merchantId
	 * @param merchantItemId
	 * @return
	 */
	public YdMerchantHotItem getYdMerchantHotItemByItemIdAndMerchantId(@Param("merchantId") Integer merchantId,
																	   @Param("merchantItemId") Integer merchantItemId);

	/**
	 * 通过merchantId得到门店首页热门商品YdMerchantHotItem
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public List<YdMerchantHotItem> getYdMerchantHotItemByMerchantId(Integer merchantId);
	
	/**
	 * 获取数量
	 * @param ydMerchantHotItem
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantHotItemCount(YdMerchantHotItem ydMerchantHotItem);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantHotItem
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantHotItem> findYdMerchantHotItemListByPage(@Param("params") YdMerchantHotItem ydMerchantHotItem,
                                                                   @Param("pageStart") Integer pageStart,
                                                                   @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItem
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantHotItem> getAll(YdMerchantHotItem ydMerchantHotItem);


	/**
	 * 添加门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItem
	 * @Description:
	 */
	public void insertYdMerchantHotItem(YdMerchantHotItem ydMerchantHotItem);
	

	/**
	 * 通过id修改门店首页热门商品YdMerchantHotItem
	 * @param ydMerchantHotItem
	 * @Description:
	 */
	public void updateYdMerchantHotItem(YdMerchantHotItem ydMerchantHotItem);

	/**
	 * 通过id删除门店首页热门商品YdMerchantHotItem
	 * @param id
	 * @Description:
	 */
	public void deleteYdMerchantHotItem(Integer id);

}
