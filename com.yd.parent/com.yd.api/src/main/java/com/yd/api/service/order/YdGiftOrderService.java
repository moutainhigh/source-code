package com.yd.api.service.order;

import java.util.List;
import java.util.Map;

import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.api.result.user.YdUserAddressResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:礼品订单主表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:53:33
 * @Version:1.1.0
 */
public interface YdGiftOrderService {

	/**
	 * 通过id得到礼品订单主表YdGiftOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftOrderResult getYdGiftOrderById(Integer id);

	/**
	 * 分页查询礼品订单主表YdGiftOrder
	 * @param ydGiftOrderResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdGiftOrderResult> findYdGiftOrderListByPage(YdGiftOrderResult ydGiftOrderResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 分页查询供应商礼品订单
	 * @param ydGiftOrderResult
	 * @param pagerInfo
	 * @return
	 * @Description:
	 */
	Page<YdGiftOrderResult> findSupplerGiftOrderListByPage(YdGiftOrderResult ydGiftOrderResult, PagerInfo pagerInfo);

	/**
	 * 得到所有礼品订单主表YdGiftOrder
	 * @param ydGiftOrderResult
	 * @return 
	 * @Description:
	 */
	public List<YdGiftOrderResult> getAll(YdGiftOrderResult ydGiftOrderResult);

	/**
	 * 商户下单创建礼品订单
	 * @param merchantId		商户id
	 * @param totalGiftCount	礼品总数量
	 * @param totalDetailCount	子订单数量
	 * @param totalSalePrice	总售价
	 * @param totalMarketPrice	总市场价
	 * @return
	 */
	YdGiftOrderResult addMerchantGiftOrder(Integer merchantId, Integer totalGiftCount, Integer totalDetailCount,Double totalSalePrice, Double totalMarketPrice);

	/**
	 * 查询礼品订单详情
	 * @param giftOrderId 礼品订单id
	 * @return List<YdGiftOrderDetailResult>
	 */
	YdGiftOrderResult getGiftOrderDetailList(Integer merchantId, Integer giftOrderId) throws BusinessException;

	/**
	 * 查询礼品订单详情
	 * @param userId	用户id
	 * @param orderId	商品订单id
	 * @return
	 */
	YdGiftOrderResult getGiftOrderDetailByItemOrderId(Integer userId, Integer orderId) throws BusinessException;

	/**
	 * 根据商户id， 订单id查询礼品订单详情，根据物流单号分组
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @return
	 */
	List<YdGiftOrderDetailResult> getMerchantGiftOrderDetailGroupByExpress(Integer merchantId, Integer orderId) throws BusinessException;

	/**
	 * 根据商户id， 订单id查询礼品订单详情，不分组
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @return
	 */
	List<YdGiftOrderDetailResult> getAppGiftOrderDetail(Integer merchantId, Integer orderId) throws BusinessException;


	/**
	 * 根据用户id， 订单id礼品订单详情，根据物流单号分组查询用户
	 * @param userId	用户id
	 * @param orderId	订单id
	 * @return
	 */
	List<YdGiftOrderDetailResult> getFrontGiftOrderDetailAndGroupByExpress(Integer userId, Integer orderId) throws BusinessException;

	/**
	 * 根据礼品订单id查询礼品订单详情，并且根据物流信息分组分组
	 * @param giftOrderId
	 * @return
	 * @throws BusinessException
	 */
	YdGiftOrderResult getAppGiftOrderGroupByExpress(Integer giftOrderId) throws BusinessException;

	/**
	 * 商城用户下单，创建礼品订单
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @param outOrderId	订单id
	 * @param receivingWay	ZT(自提) | PS(配送)
	 * @param ydMerchantResult		商户信息
	 * @param ydUserAddressResult	用户地址信息
	 * @param giftResultList		下单的礼品数据
	 */
	YdGiftOrderResult createUserGiftOrder(Integer userId, Integer merchantId, Integer outOrderId, String receivingWay, YdMerchantResult ydMerchantResult,
										  YdUserAddressResult ydUserAddressResult, List<YdMerchantGiftResult> giftResultList);

	/**
	 * 根据订单id查询礼品订单以及礼品订单详情
	 * @param orderId 订单id
	 * @return
	 */
	YdGiftOrderResult getGiftOrderDetail(Integer orderId) throws BusinessException;

	/**
	 * 根据订单id校验礼品订单是否需要充值，总计金额，需要支付金额
	 * @param merchantId
	 * @param orderId
	 * @return
	 * @throws BusinessException
	 */
	Map<String, Object> checkGiftOrderIsCharge(Integer merchantId, Integer orderId) throws BusinessException;
}
