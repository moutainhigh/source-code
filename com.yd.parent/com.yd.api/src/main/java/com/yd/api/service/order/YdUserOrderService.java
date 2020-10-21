package com.yd.api.service.order;

import com.yd.api.req.MerchantGiftReq;
import com.yd.api.result.cart.SettlementDetailResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.List;
import java.util.Map;

/**
 * @Title:商户订单Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:35:32
 * @Version:1.1.0
 */
public interface YdUserOrderService {

	/**
	 * 通过id得到商户订单YdShopOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserOrderResult getYdShopOrderById(Integer id);

	/**
	 * 根据用户获取订单详情
	 * @param userId  	用户id
	 * @param orderId	订单id
	 * @return
	 */
	public YdUserOrderResult getFrontOrderDetail(Integer userId, Integer orderId);

	/**
	 * 得到所有商户订单YdShopOrder
	 * @param ydShopOrderResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserOrderResult> getAll(YdUserOrderResult ydShopOrderResult);

	/**
	 * 前台订单分页列表
	 * @param ydUserOrderResult
	 * @param pagerInfo
	 */
	Page<YdUserOrderResult> findFrontOrderListByPage(YdUserOrderResult ydUserOrderResult, PagerInfo pagerInfo);

	/**
	 * 分页查询所有订单,包含订单子订单
	 * @param merchantId
	 * @param orderId
	 * @param orderStatus
	 * @param startTime
	 * @param endTime
	 * @param pagerInfo
	 * @return
	 */
	public Page<YdUserOrderResult> findOrderListByPage(Integer merchantId, String orderId, String itemTitle, String orderStatus,
													   String startTime, String endTime, PagerInfo pagerInfo);

	/**
	 * 查询订单详情
	 * @param merchantId 商户id
	 * @param orderId 订单id
	 * @return
	 */
	YdUserOrderResult getOrderDetail(Integer merchantId, String orderId) throws BusinessException;

	/**
	 * 分页查询已完成订单明细，不查询订单子订单
	 * @param merchantId
	 * @param orderId
	 * @param startTime
	 * @param endTime
	 * @param pagerInfo
	 * @return
	 */
	public Page<YdUserOrderResult> findOrderTransDetailListByPage(Integer merchantId, String orderId, String startTime,
																  String endTime, PagerInfo pagerInfo);

	/**
	 * 添加商户订单YdShopOrder
	 * @param ydUserOrderResult
	 * @Description:
	 */
	public void insertYdShopOrder(YdUserOrderResult ydUserOrderResult) throws BusinessException;

	/**
	 * 通过id修改商户订单YdShopOrder
	 * @param ydUserOrderResult
	 * @Description:
	 */
	public void updateYdUserOrder(YdUserOrderResult ydUserOrderResult) throws BusinessException;

	/**
	 * 校验订单信息
	 * @param orderId
	 * @Description:
	 */
	YdUserOrderResult checkUserOrder(Integer orderId) throws BusinessException;

    /**
     * 商户确认用户确定订单
     * @param merchantId
     * @param orderId
     */
    public void merchantConfirmUserOrder(Integer merchantId, Integer orderId) throws BusinessException;

	/**
	 * 用户提交订单
	 * @param userId	   用户id
	 * @param merchantId   商户id
	 * @param couponId	   优惠券id
	 * @param addressId	   收货地址id
	 * @param skuId		   商户商品skuId
	 * @param num		   商品购买数量
	 * @param isCheckIntegralReduce     是否选中积分抵扣
	 * @param isCheckOldMachineReduce	是否选中旧机抵扣
	 * @param type		   car | item
	 * @param receivingWay ZT(自提) | PS(配送)
	 * @param carIds	   购物车carIds
	 * @param giftList	   礼品idList id and num
	 * @return
	 * @throws BusinessException
	 */
	Map<String, Object> submitOrder(Integer userId, Integer merchantId, Integer couponId, Integer addressId, Integer skuId, Integer num,
									String isCheckIntegralReduce, String isCheckOldMachineReduce,
									String type, String receivingWay, String carIds, List<MerchantGiftReq> giftList) throws BusinessException;

	/**
	 * 根据订单状态查询订单数量
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderStatus 订单状态
	 * @return 订单数量
	 */
    Integer getUserOrderNumByOrderStatus(Integer userId, Integer merchantId, String orderStatus) throws BusinessException;

	/**
	 * 用户取消订单
	 * @param userId	用户id
	 * @param orderId	订单id
	 */
	void cancelOrder(Integer userId, Integer orderId) throws BusinessException;

	/**
	 * 根据订单id修改收货地址
	 * @param userId	用户id
	 * @param orderId	订单id
	 * @param addressId	收货地址id
	 * @return
	 */
	void updateReceiveAddress(Integer userId, Integer orderId, Integer addressId) throws BusinessException;

	/**
	 * 用户礼品订单确认收货
	 * @param userId	用户id
	 * @param expressOrderId  物流单号
	 */
    void userConfirmGiftOrder(Integer userId, String expressOrderId) throws BusinessException;

	/**
	 * 用户订单确认收货
	 * @param userId
	 * @param orderId
	 */
	void userConfirmOrder(Integer userId, Integer orderId) throws BusinessException;

	/**
	 * 更新用户订单的二维码
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @param imageUrl	  订单二维码url
	 */
	void updateYdUserOrderQrCode(Integer userId, Integer merchantId, Integer orderId, String imageUrl);

	/**
	 * 商户订单发货
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @param expressOrderId 快递单号
	 */
	void updateOrderExpress(Integer merchantId, Integer orderId, String expressOrderId) throws BusinessException;

	/**
	 * 修改订单支付成功状态
	 * @param orderId	订单id
	 * @param billNo	支付流水号
	 */
    void updateUserOrderPayStatus(String orderId, String billNo) throws BusinessException;

	/**
	 * 用户支付
	 * @param userId
	 * @param orderId
	 * @param domain
	 * @throws BusinessException
	 */
	Map<String,String> toPay(Integer userId, Integer orderId, String domain) throws BusinessException;

	/**
	 * 设置旧机抵扣金额
	 * @param merchantId
	 * @param orderId
	 * @param price		旧机抵扣金额
	 * @param mobileName	手机型号
	 */
	void setOldMachineReduce(Integer merchantId, Integer orderId, Double price, String mobileName) throws BusinessException;

	/**
	 * 设置其他优惠金额
	 * @param merchantId
	 * @param orderId
	 * @param price
	 */
    void setOtherPrice(Integer merchantId, Integer orderId, Double price) throws BusinessException;

	/**
	 * 商户在app上确认待处理订单
	 * @param merchantId
	 * @param orderId
	 * @throws BusinessException
	 */
	void confirmAppOrder(Integer merchantId, Integer orderId) throws BusinessException;

	/**
	 * 定时服务取消未付款订单
	 * @param merchantId, 不传是所有商户的
	 * @throws BusinessException
	 */
	void synCancelNoPayOrder(Integer merchantId) throws BusinessException;

}
