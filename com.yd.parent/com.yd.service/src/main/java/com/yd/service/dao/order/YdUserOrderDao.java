package com.yd.service.dao.order;

import java.util.List;
import com.yd.service.bean.order.YdUserOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户订单Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:35:32
 * @Version:1.1.0
 */
public interface YdUserOrderDao {

	/**
	 * 通过id得到商户订单YdShopOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserOrder getYdUserOrderById(Integer id);

	/**
	 * 得到所有商户订单YdShopOrder
	 * @param ydShopOrder
	 * @return 
	 * @Description:
	 */
	public List<YdUserOrder> getAll(YdUserOrder ydShopOrder);

	/**
	 * 查询商户订单列表中数量
	 * @param merchantId
	 * @param orderId
	 * @param itemTitle
	 * @param orderStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int findOrderListCount(@Param("merchantId") Integer merchantId,
						   @Param("orderId") String orderId,
						   @Param("itemTitle") String itemTitle,
						   @Param("orderStatus") String orderStatus,
						   @Param("startTime") String startTime,
						   @Param("endTime") String endTime,
						   @Param("payStartTime") String payStartTime,
						   @Param("payEndTime") String payEndTime);

	/**
	 * 分页查询商户总订单
	 * @param merchantId
	 * @param orderId
	 * @param itemTitle
	 * @param orderStatus
	 * @param startTime
	 * @param endTime
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<YdUserOrder> findOrderListByPage(@Param("merchantId") Integer merchantId,
										  @Param("orderId") String orderId,
										  @Param("itemTitle") String itemTitle,
										  @Param("orderStatus") String orderStatus,
										  @Param("startTime") String startTime,
										  @Param("endTime") String endTime,
										  @Param("payStartTime") String payStartTime,
										  @Param("payEndTime") String payEndTime,
										  @Param("pageStart") Integer pageStart,
										  @Param("pageSize") Integer pageSize);

	/**
	 * 分页查询商户完成订单数量
	 * @param merchantId
	 * @param orderId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int findOrderTransDetailListCount(@Param("merchantId") Integer merchantId,
									  @Param("orderId") String orderId,
									  @Param("startTime") String startTime,
									  @Param("endTime") String endTime);

	/**
	 * 分页查询商户完成订单
	 * @param merchantId
	 * @param orderId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<YdUserOrder> findOrderTransDetailListByPage(@Param("merchantId") Integer merchantId,
													 @Param("orderId") String orderId,
													 @Param("startTime") String startTime,
													 @Param("endTime") String endTime,
													 @Param("pageStart") Integer pageStart,
													 @Param("pageSize") Integer pageSize);

	/**
	 * 添加商户订单YdShopOrder
	 * @param ydShopOrder
	 * @Description:
	 */
	public void insertYdShopOrder(YdUserOrder ydShopOrder);
	

	/**
	 * 通过id修改商户订单YdShopOrder
	 * @param ydShopOrder
	 * @Description:
	 */
	public void updateYdUserOrder(YdUserOrder ydShopOrder);

    /**
     *  确认订单
     * @param merchantId
     * @param orderId
     * @param manualReducePrice
     */
    int confirmOrder(@Param("merchantId") Integer merchantId,
                     @Param("orderId")  Integer orderId,
                     @Param("manualReducePrice")  Double manualReducePrice);

	/**
	 * 设置其它优惠券金额
	 * @param merchantId
	 * @param orderId
	 * @param otherPrice
	 */
	int updateOrderOtherPrice(@Param("merchantId") Integer merchantId,
							  @Param("orderId")  Integer orderId,
							  @Param("otherPrice")  Double otherPrice);

	/**
	 * 设置旧机抵扣金额
	 * @param merchantId
	 * @param orderId
	 * @param price
	 */
	int updateOldMachinePrice(@Param("merchantId") Integer merchantId,
							  @Param("orderId")  Integer orderId,
							  @Param("price")  Double price,
							  @Param("mobileName")  String mobileName);

    int updateMerchantAutoCancelTime(@Param("merchantId") Integer merchantId,
									 @Param("hoursCount") Integer hoursCount);

	/**
	 * 查询完成订单的用户数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getCompleteOrderUserCount(@Param("userId") Integer userId,
								  @Param("merchantId") Integer merchantId,
								  @Param("orderStatus") String orderStatus,
								  @Param("startTime") String startTime,
								  @Param("endTime") String endTime);
	/**
	 * 根据订单状态查询订单数量
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderStatus 订单状态
	 * @return 订单数量
	 */
    Integer getUserOrderNumByOrderStatus(@Param("userId") Integer userId,
										 @Param("merchantId") Integer merchantId,
										 @Param("orderStatus") String orderStatus);

	List<YdUserOrder> findFrontOrderListByPage(@Param("userId") Integer userId,
											   @Param("merchantId") Integer merchantId,
											   @Param("orderStatus") String orderStatus,
											   @Param("pageStart") Integer pageStart,
											   @Param("pageSize") Integer pageSize);

	/**
	 * app首页获取营收统计
	 * @param merchantId  	商户id
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @param type			offline | online
	 * @return
	 */
    List<YdUserOrder> getAppMarketData(@Param("merchantId") Integer merchantId,
									   @Param("startTime") String startTime,
									   @Param("endTime") String endTime,
									   @Param("type") String type);

	List<YdUserOrder> findNoPayOrderList(YdUserOrder ydUserOrder);

	/**
	 * 取消订单
	 * @param id
	 */
	void cancelNoPayOrder(Integer id);

	/**
	 * 重试次数+1
	 * @param id
	 */
    void updatePayEntryCount(Integer id);
}
