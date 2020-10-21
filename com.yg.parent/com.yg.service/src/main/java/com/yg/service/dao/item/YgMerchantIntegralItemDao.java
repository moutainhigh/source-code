package com.yg.service.dao.item;

import java.util.List;
import com.yg.service.bean.item.YgMerchantIntegralItem;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户积分商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-27 13:34:01
 * @Version:1.1.0
 */
public interface YgMerchantIntegralItemDao {

	/**
	 * 通过id得到商户积分商品YgMerchantIntegralItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchantIntegralItem getYgMerchantIntegralItemById(Integer id);
	
	/**
	 * 获取数量
	 * @param ygMerchantIntegralItem
	 * @return 
	 * @Description:
	 */
	public int getYgMerchantIntegralItemCount(YgMerchantIntegralItem ygMerchantIntegralItem);
	
	/**
	 * 分页获取数据
	 * @param ygMerchantIntegralItem
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantIntegralItem> findYgMerchantIntegralItemListByPage(@Param("params") YgMerchantIntegralItem ygMerchantIntegralItem,
                                                                             @Param("pageStart") Integer pageStart,
                                                                             @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItem
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantIntegralItem> getAll(YgMerchantIntegralItem ygMerchantIntegralItem);

	/**
	 * 添加商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItem
	 * @Description:
	 */
	public void insertYgMerchantIntegralItem(YgMerchantIntegralItem ygMerchantIntegralItem);
	
	/**
	 * 通过id修改商户积分商品YgMerchantIntegralItem
	 * @param ygMerchantIntegralItem
	 * @Description:
	 */
	public void updateYgMerchantIntegralItem(YgMerchantIntegralItem ygMerchantIntegralItem);
	
}
