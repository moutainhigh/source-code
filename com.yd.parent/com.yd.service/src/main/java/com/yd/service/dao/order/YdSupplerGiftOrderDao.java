package com.yd.service.dao.order;

import java.util.List;
import com.yd.service.bean.order.YdSupplerGiftOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:供货商礼品订单Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 17:26:44
 * @Version:1.1.0
 */
public interface YdSupplerGiftOrderDao {

	/**
	 * 通过id得到供货商礼品订单YdSupplerGiftOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdSupplerGiftOrder getYdSupplerGiftOrderById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydSupplerGiftOrder
	 * @return 
	 * @Description:
	 */
	public int getYdSupplerGiftOrderCount(YdSupplerGiftOrder ydSupplerGiftOrder);
	
	/**
	 * 分页获取数据
	 * @param ydSupplerGiftOrder
	 * @return 
	 * @Description:
	 */
	public List<YdSupplerGiftOrder> findYdSupplerGiftOrderListByPage(@Param("params") YdSupplerGiftOrder ydSupplerGiftOrder,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrder
	 * @return 
	 * @Description:
	 */
	public List<YdSupplerGiftOrder> getAll(YdSupplerGiftOrder ydSupplerGiftOrder);


	/**
	 * 添加供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrder
	 * @Description:
	 */
	public void insertYdSupplerGiftOrder(YdSupplerGiftOrder ydSupplerGiftOrder);
	

	/**
	 * 通过id修改供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrder
	 * @Description:
	 */
	public void updateYdSupplerGiftOrder(YdSupplerGiftOrder ydSupplerGiftOrder);
	
}
