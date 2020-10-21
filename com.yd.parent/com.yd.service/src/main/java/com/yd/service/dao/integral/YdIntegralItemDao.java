package com.yd.service.dao.integral;

import java.util.List;
import com.yd.service.bean.integral.YdIntegralItem;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:积分商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:16:34
 * @Version:1.1.0
 */
public interface YdIntegralItemDao {

	/**
	 * 通过id得到积分商品YdIntegralItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralItem getYdIntegralItemById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydIntegralItem
	 * @return 
	 * @Description:
	 */
	public int getYdIntegralItemCount(YdIntegralItem ydIntegralItem);
	
	/**
	 * 分页获取数据
	 * @param ydIntegralItem
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralItem> findYdIntegralItemListByPage(@Param("params") YdIntegralItem ydIntegralItem,
                                                             @Param("pageStart") Integer pageStart,
                                                             @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有积分商品YdIntegralItem
	 * @param ydIntegralItem
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralItem> getAll(YdIntegralItem ydIntegralItem);


	/**
	 * 添加积分商品YdIntegralItem
	 * @param ydIntegralItem
	 * @Description:
	 */
	public void insertYdIntegralItem(YdIntegralItem ydIntegralItem);

	/**
	 * 通过id修改积分商品YdIntegralItem
	 * @param ydIntegralItem
	 * @Description:
	 */
	public void updateYdIntegralItem(YdIntegralItem ydIntegralItem);

    void deleteIntegralItem(Integer itemId);

	/**
	 * 修改商品结算比率
	 * @param rate
	 */
	void updateIntegralItemSettlePrice(Double rate);
}
