package com.yd.service.dao.order;

import java.util.List;

import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.service.bean.order.YdGiftOrder;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:礼品订单主表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:53:33
 * @Version:1.1.0
 */
public interface YdGiftOrderDao {

	/**
	 * 通过id得到礼品订单主表YdGiftOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftOrder getYdGiftOrderById(Integer id);

	/**
	 * 通过outOrderId得到礼品订单主表YdGiftOrder
	 * @param outOrderId
	 * @return
	 * @Description:
	 */
	public YdGiftOrder getYdGiftOrderByOutOrderId(String outOrderId);
	
	/**
	 * 获取数量
	 * @param ydGiftOrder
	 * @return 
	 * @Description:
	 */
	public int getYdGiftOrderCount(YdGiftOrder ydGiftOrder);
	
	/**
	 * 分页获取数据
	 * @param ydGiftOrder
	 * @return 
	 * @Description:
	 */
	public List<YdGiftOrder> findYdGiftOrderListByPage(@Param("params") YdGiftOrder ydGiftOrder,
                                                       @Param("pageStart") Integer pageStart,
                                                       @Param("pageSize") Integer pageSize);

	/**
	 * 查询供应商订单数量
	 * @param params
	 * @return
	 */
	int getSupplerGiftOrderCount(YdGiftOrderResult params);

	/**
	 * 分页获取数据
	 * @param ydGiftOrder
	 * @return
	 * @Description:
	 */
	public List<YdGiftOrder> findSupplerGiftOrderListByPage(@Param("params") YdGiftOrder ydGiftOrder,
															@Param("pageStart") Integer pageStart,
															@Param("pageSize") Integer pageSize);

	/**
	 * 得到所有礼品订单主表YdGiftOrder
	 * @param ydGiftOrder
	 * @return 
	 * @Description:
	 */
	public List<YdGiftOrder> getAll(YdGiftOrder ydGiftOrder);


	/**
	 * 添加礼品订单主表YdGiftOrder
	 * @param ydGiftOrder
	 * @Description:
	 */
	public void insertYdGiftOrder(YdGiftOrder ydGiftOrder);
	

	/**
	 * 通过id修改礼品订单主表YdGiftOrder
	 * @param ydGiftOrder
	 * @Description:
	 */
	public void updateYdGiftOrder(YdGiftOrder ydGiftOrder);

}
