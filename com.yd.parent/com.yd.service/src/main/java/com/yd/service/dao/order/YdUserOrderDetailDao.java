package com.yd.service.dao.order;

import java.util.List;

import com.yd.service.bean.order.YdUserOrderDetail;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户订单详情Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:37:13
 * @Version:1.1.0
 */
public interface YdUserOrderDetailDao {

	/**
	 * 通过id得到商户订单详情YdShopOrderDetail
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserOrderDetail getYdShopOrderDetailById(Integer id);

	/**
	 * 通过id得到商户订单详情YdShopOrderDetail
	 * @param orderId
	 * @return
	 * @Description:
	 */
	public List<YdUserOrderDetail> getYdShopOrderDetailByOrderId(Integer orderId);

	/**
	 * 得到商户用户订单详情数量
	 * @param ydUserOrderDetail
	 * @return
	 */
	int getOrderDetailCount(YdUserOrderDetail ydUserOrderDetail);

	/**
	 * 得到商户用户所有的订单详情
	 * @param ydUserOrderDetail
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<YdUserOrderDetail> findOrderDetailListByPage(@Param("params") YdUserOrderDetail ydUserOrderDetail,
													  @Param("pageStart") Integer pageStart,
													  @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetail
	 * @return 
	 * @Description:
	 */
	public List<YdUserOrderDetail> getAll(YdUserOrderDetail ydShopOrderDetail);


	/**
	 * 添加商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetail
	 * @Description:
	 */
	public void insertYdShopOrderDetail(YdUserOrderDetail ydShopOrderDetail);
	

	/**
	 * 通过id修改商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetail
	 * @Description:
	 */
	public void updateYdShopOrderDetail(YdUserOrderDetail ydShopOrderDetail);

	/**
	 * 查询销量排行
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 */
	List<YdUserOrderDetail> findMerchantMaxSalesItemData(@Param("merchantId") Integer merchantId,
                                                         @Param("startTime") String startTime,
                                                         @Param("endTime") String endTime);

	/**
	 * 查询已经完成的订单
	 * @param startTime 开始时间
	 * @param endTime	结束时间
	 * @return	订单详情list
	 */
	List<YdUserOrderDetail> findCompleteOrderList(@Param("startTime") String startTime,
												  @Param("endTime") String endTime);

	/**
	 * 获取app首页 商品营收数据 (时间段内售卖成功的子订单列表)
	 * @param merchantId	商户id
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @param type			offline | online
	 * @return
	 */
    List<YdUserOrderDetail> getAppSaleData(@Param("merchantId") Integer merchantId,
										   @Param("startTime") String startTime,
										   @Param("endTime") String endTime,
										   @Param("type") String type);
}
