package com.yd.service.impl.order;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.pay.util.WexinPublicPayUtils;
import com.yd.api.req.MerchantGiftReq;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantGiftTransResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.api.result.order.YdUserOrderDetailResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserAddressResult;
import com.yd.api.service.cart.YdUserCartService;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.coupon.YdUserCouponService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.api.service.user.YdUserAddressService;
import com.yd.api.service.user.YdUserService;
import com.yd.api.wx.util.ExpressUtil;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.enums.YdOrderNoTypeEnum;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.utils.*;
import com.yd.service.bean.coupon.YdUserCoupon;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.order.*;
import com.yd.service.bean.user.YdUserAuth;
import com.yd.service.dao.coupon.YdUserCouponDao;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import com.yd.service.dao.order.*;
import com.yd.service.dao.user.YdUserAuthDao;
import com.yd.service.dao.user.YdUserDao;
import com.yd.service.impl.login.YdAdminLoginServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户订单Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:35:32
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserOrderServiceImpl implements YdUserOrderService {

    private static final Logger logger = LoggerFactory.getLogger(YdAdminLoginServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdUserDao ydUserDao;

	@Resource
	private YdUserAuthDao ydUserAuthDao;

	@Resource
	private YdGiftOrderDao ydGiftOrderDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdUserOrderDao ydUserOrderDao;

	@Resource
	private YdUserCouponDao ydUserCouponDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdMerchantOrderConfigDao ydMerchantOrderConfigDao;

	@Resource
	private WeixinService weixinService;

	@Resource
	private YdGiftOrderService ydGiftOrderService;

	@Resource
	private YdUserCouponService ydUserCouponService;

	@Resource
	private YdUserAddressService ydUserAddressService;

	@Resource
	private YdGiftOrderDetailDao ydGiftOrderDetailDao;

	@Resource
	private YdUserOrderDetailDao ydUserOrderDetailDao;

	@Resource
	private YdUserService ydUserService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdUserCartService ydUserCartService;

	@Resource
	private YdMerchantItemService ydMerchantItemService;

	@Resource
	private YdGiftOrderDetailService ydGiftOrderDetailService;

	@Resource
	private YdMerchantGiftService ydMerchantGiftService;

	@Resource
	private YdMerchantGiftAccountService ydMerchantGiftAccountService;

	@Resource
	private YdMerchantGiftTransService ydMerchantGiftTransService;

	@Override
	public YdUserOrderResult getYdShopOrderById(Integer id) {
		if (id == null || id <= 0) return null;
		YdUserOrderResult ydShopOrderResult = null;
		YdUserOrder ydShopOrder = this.ydUserOrderDao.getYdUserOrderById(id);
		if (ydShopOrder != null) {
			ydShopOrderResult = new YdUserOrderResult();
			BeanUtilExt.copyProperties(ydShopOrderResult, ydShopOrder);
		}
		return ydShopOrderResult;
	}

	/**
	 * 根据用户获取订单详情
	 * @param userId  	用户id
	 * @param orderId	订单id
	 * @return
	 */
	@Override
	public YdUserOrderResult getFrontOrderDetail(Integer userId, Integer orderId) {
		ydUserService.checkUserInfo(userId);

		YdUserOrderResult ydUserOrderResult = this.checkUserOrder(orderId);

		// 设置订单详情
		List<YdUserOrderDetail> orderDetailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(orderId);
		ydUserOrderResult.setOrderDetails(DTOUtils.convertList(orderDetailList, YdUserOrderDetailResult.class));
		return ydUserOrderResult;
	}

	@Override
	public List<YdUserOrderResult> getAll(YdUserOrderResult ydUserOrderResult) {
		YdUserOrder ydUserOrder = new YdUserOrder();
		if (ydUserOrderResult != null) {
			BeanUtilExt.copyProperties(ydUserOrder, ydUserOrderResult);
		}
		List<YdUserOrder> dataList = this.ydUserOrderDao.getAll(ydUserOrder);
		return DTOUtils.convertList(dataList, YdUserOrderResult.class);
	}

	@Override
	public Page<YdUserOrderResult> findFrontOrderListByPage(YdUserOrderResult ydUserOrderResult, PagerInfo pagerInfo) {
		Page<YdUserOrderResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());

		YdUserOrder ydUserOrder = new YdUserOrder();
		BeanUtilExt.copyProperties(ydUserOrder, ydUserOrderResult);
		Integer merchantId = ydUserOrder.getMerchantId();

		int amount = ydUserOrderDao.getUserOrderNumByOrderStatus(ydUserOrder.getUserId(), merchantId, ydUserOrder.getOrderStatus());
		if (amount > 0) {
			// 查询订单列表
			List<YdUserOrder> dataList = ydUserOrderDao.findFrontOrderListByPage(ydUserOrder.getUserId(), merchantId,
					ydUserOrder.getOrderStatus(), pagerInfo.getStart(), pagerInfo.getPageSize());

			List<YdUserOrderResult> resultList = DTOUtils.convertList(dataList, YdUserOrderResult.class);

			resultList.forEach(orderData -> {
				// 设置商品订单详情
				List<YdUserOrderDetail> orderDetailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(orderData.getId());
				orderData.setOrderDetails(DTOUtils.convertList(orderDetailList, YdUserOrderDetailResult.class));

				// 设置礼品订单
				orderData.setGiftOrderResult(ydGiftOrderService.getGiftOrderDetail(orderData.getId()));

				// 设置订单过期时间
				YdMerchantOrderConfig ydMerchantOrderConfig = ydMerchantOrderConfigDao.getYdShopOrderConfigByMerchantId(merchantId);
				if (ydMerchantOrderConfig == null) {
					ydMerchantOrderConfig = new YdMerchantOrderConfig();
					ydMerchantOrderConfig.setOrderAutoCancelTime(2);
				}
				orderData.setExpireTime(DateUtils.addHour(orderData.getCreateTime(), ydMerchantOrderConfig.getOrderAutoCancelTime()));
			});
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public Page<YdUserOrderResult> findOrderListByPage(Integer merchantId, String orderId, String itemTitle, String orderStatus,
													   String startTime, String endTime, PagerInfo pagerInfo) {
		Page<YdUserOrderResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		if (merchantId != null) {
			YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
			merchantId = storeInfo.getId();
		}
		int amount = ydUserOrderDao.findOrderListCount(merchantId, orderId, itemTitle, orderStatus, startTime, endTime, null, null);
		if (amount > 0) {
			// 查询订单列表
			List<YdUserOrder> dataList = ydUserOrderDao.findOrderListByPage(merchantId, orderId, itemTitle, orderStatus,
					startTime, endTime, null, null, pagerInfo.getStart(), pagerInfo.getPageSize());

			List<YdUserOrderResult> resultList = DTOUtils.convertList(dataList, YdUserOrderResult.class);

			resultList.forEach(orderData -> {
				// 设置子订单
				List<YdUserOrderDetail> orderDetailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(orderData.getId());
				orderData.setOrderDetails(DTOUtils.convertList(orderDetailList, YdUserOrderDetailResult.class));

				// 设置订单下商品总数量
				orderData.setOrderItemCount(orderDetailList.stream().mapToInt(YdUserOrderDetail :: getNum).sum());

				// 设置礼品订单
				orderData.setGiftOrderResult(ydGiftOrderService.getGiftOrderDetail(orderData.getId()));

				YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(orderData.getMerchantId());
				if (ydMerchant != null) {
					orderData.setMerchantName(ydMerchant.getMerchantName());
				}
			});
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	/**
	 * 查询订单详情
	 * @param merchantId 商户id
	 * @param orderId 订单id
	 * @return
	 */
	@Override
	public YdUserOrderResult getOrderDetail(Integer merchantId, String orderId) throws BusinessException {
		YdUserOrder ydUserOrder = new YdUserOrder();
		ydUserOrder.setId(Integer.valueOf(orderId));
		if (merchantId != null) {
			YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
			ydUserOrder.setMerchantId(storeInfo.getId());
		}

		List<YdUserOrder> allList = ydUserOrderDao.getAll(ydUserOrder);
		if (CollectionUtils.isEmpty(allList)) return null;
		ydUserOrder = allList.get(0);

		YdUserOrderResult ydUserOrderResult = new YdUserOrderResult();
		BeanUtilExt.copyProperties(ydUserOrderResult, ydUserOrder);

		// 设置订单详情
		List<YdUserOrderDetail> orderDetailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(ydUserOrder.getId());
		ydUserOrderResult.setOrderDetails(DTOUtils.convertList(orderDetailList, YdUserOrderDetailResult.class));

		// 设置礼品订单详情
		ydUserOrderResult.setGiftOrderResult(ydGiftOrderService.getGiftOrderDetail(Integer.valueOf(orderId)));
		return ydUserOrderResult;
	}

	/**
	 * 分页查询已完成订单明细，不查询订单子订单
	 * @param merchantId
	 * @param orderId
	 * @param startTime
	 * @param endTime
	 * @param pagerInfo
	 * @return
	 */
	@Override
	public Page<YdUserOrderResult> findOrderTransDetailListByPage(Integer merchantId, String orderId, String startTime,
																  String endTime, PagerInfo pagerInfo) {
		Page<YdUserOrderResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = ydUserOrderDao.findOrderTransDetailListCount(merchantId, orderId, startTime, endTime);
		if (amount > 0) {
			// 查询订单列表
			List<YdUserOrder> dataList = ydUserOrderDao.findOrderTransDetailListByPage(merchantId, orderId, startTime, endTime,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdUserOrderResult> resultList = DTOUtils.convertList(dataList, YdUserOrderResult.class);
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public void insertYdShopOrder(YdUserOrderResult ydShopOrderResult) {
        ydShopOrderResult.setCreateTime(new Date());
        ydShopOrderResult.setUpdateTime(new Date());
        YdUserOrder ydShopOrder = new YdUserOrder();
        BeanUtilExt.copyProperties(ydShopOrder, ydShopOrderResult);
        this.ydUserOrderDao.insertYdShopOrder(ydShopOrder);
	}
	
	@Override
	public void updateYdUserOrder(YdUserOrderResult ydShopOrderResult) {
        ydShopOrderResult.setUpdateTime(new Date());
        YdUserOrder ydShopOrder = new YdUserOrder();
        BeanUtilExt.copyProperties(ydShopOrder, ydShopOrderResult);
        this.ydUserOrderDao.updateYdUserOrder(ydShopOrder);
	}

	@Override
	public YdUserOrderResult checkUserOrder(Integer orderId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(orderId, "err_order_id_empty", "非法的订单id");

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertNonNull(ydUserOrder, "err_not_exist_order", "用户订单不存在");

		YdUserOrderResult ydUserOrderResult = new YdUserOrderResult();
		BeanUtilExt.copyProperties(ydUserOrderResult, ydUserOrder);
		return ydUserOrderResult;
	}

	/**
     * 商家确认订单
     * @param merchantId
     * @param orderId
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
    public void merchantConfirmUserOrder(Integer merchantId, Integer orderId) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdUserOrderResult ydUserOrderResult = this.checkUserOrder(orderId);
		YdUserOrder ydUserOrder = new YdUserOrder();
		BeanUtilExt.copyProperties(ydUserOrder, ydUserOrderResult);
		ValidateBusinessUtils.assertFalse(!YdUserOrderStatusEnum.WAIT_HANDLE.getCode().equalsIgnoreCase(ydUserOrder.getOrderStatus()),
				"err_order_status", "用户订单状态不正确");

		// 线下自提的订单直接修改为订单成功
		ydUserOrder.setPayStatus("Y");
		ydUserOrder.setPayTime(new Date());
		ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.SUCCESS.getCode());
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);

		// 判断是否使用优惠券， 如果使用优惠券， 处理优惠券为已经使用
		YdUserCoupon ydUserCoupon = ydUserCouponDao.getYdUserCouponByOutOrderId(orderId + "");
		if (ydUserCoupon != null) {
			ydUserCoupon.setUseTime(new Date());
			ydUserCoupon.setUseStatus("Y");
			ydUserCouponDao.updateYdUserCoupon(ydUserCoupon);
		}

		// 查询修改礼品订单状态
		updateZtOrderGiftStatusAndToPay(ydUserOrder);
	}

	/**
	 * 用户提交订单
	 * @param userId	   用户id
	 * @param merchantId   商户id
	 * @param couponId	   优惠券id
	 * @param addressId    收货地址id
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
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> submitOrder(Integer userId, Integer merchantId, Integer couponId, Integer addressId, Integer skuId, Integer num,
										   String isCheckIntegralReduce, String isCheckOldMachineReduce, String type, String receivingWay,
										   String carIds, List<MerchantGiftReq> giftList) throws BusinessException {
		// 校验用户信息
		ydUserService.checkUserInfo(userId);

		// 校验商户信息
		YdMerchantResult ydMerchantResult = ydMerchantService.checkMerchantInfo(merchantId);

		// 校验收货方式, 收货地址
		ValidateBusinessUtils.assertStringNotBlank(receivingWay, "err_empty_receive_way", "收货方式不可以为空");
		YdUserAddressResult ydUserAddressResult = ydUserAddressService.checkAddressId(addressId);

		// 查询商户规格列表
		List<YdMerchantItemSkuResult> skuList = ydMerchantItemService.findMerchantItemListByCarIdList(userId, merchantId, skuId, num, carIds, type);

		// 计算总金额，商品总数量
		Integer itemCount = skuList.stream().mapToInt(YdMerchantItemSkuResult :: getNum).sum();
		Double skuTotalPrice = getSkuListSumTotalPrice(skuList).doubleValue();

		// 校验优惠券是否可用以及金额
		Integer couponPrice = 0;
		if (couponId != null && couponId > 0) {
			YdUserCouponResult ydUserCouponResult = ydUserCouponService.checkCouponIsCanUse(userId, merchantId, couponId, skuTotalPrice, skuList);
			ValidateBusinessUtils.assertNonNull(ydUserCouponResult, "err_coupon_not_can_use", "优惠券不可以使用");
			couponPrice = ydUserCouponResult.getCouponPrice();
		}

		// 查询校验礼品
		List<YdMerchantGiftResult> giftResultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(giftList)) {
			List<Integer> giftIdList = giftList.stream().map(MerchantGiftReq::getId).collect(Collectors.toList());
			List<YdMerchantGift> ydGiftList = ydMerchantGiftDao.findYdMerchantGiftByIdList(merchantId, giftIdList);
			giftResultList = DTOUtils.convertList(ydGiftList, YdMerchantGiftResult.class);
			giftResultList.forEach(ydMerchantGiftResult -> {
				giftList.forEach(gift -> {
					if (ydMerchantGiftResult.getId().equals(gift.getId())) {
						ydMerchantGiftResult.setNum(gift.getNum());
					}
				});
			});
			// 礼品实选金额
			double orderGiftPrice = giftResultList.stream().mapToDouble(data -> data.getNum() * data.getMarketPrice()).sum();

			// 礼品可选金额
			Double giftPrice = ydMerchantGiftService.getOrderGiftPrice(merchantId, carIds, skuId, num, type);
			logger.info("===礼品可选金额giftPrice===" + giftPrice + " ;===礼品实选金额orderGiftPrice===" + orderGiftPrice);
			ValidateBusinessUtils.assertFalse(orderGiftPrice > giftPrice ,"err_gift_order", "礼品选择超额，请重新选择");
		}

		// 创建商品订单，商品订单详情，礼品订单，礼品订单详情, 卧槽，有点复杂，好难受
		YdUserOrder ydUserOrder = createOrder(userId, merchantId, couponId, itemCount, skuTotalPrice, couponPrice,
				isCheckIntegralReduce, isCheckOldMachineReduce, receivingWay, ydUserAddressResult, ydMerchantResult);
		createOrderDetail(userId, merchantId, ydUserOrder.getId(), skuList);

		// 商品减库存
		ydMerchantItemService.reduceItemSkuStock(skuList);

		if (CollectionUtils.isNotEmpty(giftList)) {
			// 创建礼品订单
			YdGiftOrderResult ydGiftOrderResult = ydGiftOrderService.createUserGiftOrder(userId, merchantId, ydUserOrder.getId(), receivingWay, ydMerchantResult, ydUserAddressResult, giftResultList);
			// 创建礼品详情
			Integer giftOrderId = ydGiftOrderResult.getId();
			giftResultList.forEach(ydMerchantGiftResult -> {
			    Double purchasePrice = 0.00;
			    if ("platform".equalsIgnoreCase(ydMerchantGiftResult.getGiftType())) {
                    YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGiftResult.getGiftId());
                    if (ydGift != null && ydGift.getPurchasePrice() != null) {
                        purchasePrice = ydGift.getPurchasePrice();
                    }
                }
				ydGiftOrderDetailService.createMerchantGiftOrderDetail(giftOrderId, ydMerchantGiftResult.getSupplierId(),
						ydMerchantGiftResult.getId(), ydMerchantGiftResult.getGiftId(), ydMerchantGiftResult.getNum(),
						ydMerchantGiftResult.getMarketPrice(), ydMerchantGiftResult.getSalePrice(), purchasePrice);
			});
		}

		// 删除购物车ids
		if ("car".equalsIgnoreCase(type)) {
			List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
					.map(Integer::valueOf).collect(Collectors.toList());
			ydUserCartService.deleteCart(userId, merchantId, carIdList);
		}

		// 优惠券变为使用中,增加订单id
		if (couponId != null) {
			ydUserCouponDao.updateCouponStatusInUse(userId, merchantId, couponId, ydUserOrder.getId() + "");
		}
		Map<String, Object> result = new HashMap<>();
		result.put("id", ydUserOrder.getId());
		return result;
	}

	/**
	 * 根据订单状态查询订单数量
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderStatus 订单状态
	 * @return 订单数量
	 */
	@Override
	public Integer getUserOrderNumByOrderStatus(Integer userId, Integer merchantId, String orderStatus) throws BusinessException {
		// 校验用户信息
//		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
//				"err_user_id", "非法的用户id");
//
//		// 校验商户信息
//		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
//				"err_user_id", "非法的商户id");

		return ydUserOrderDao.getUserOrderNumByOrderStatus(userId, merchantId, orderStatus);
	}

    /**
     * 用户取消订单
     * @param userId	用户id
     * @param orderId	订单id
     * @throws BusinessException
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Integer userId, Integer orderId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(userId, "err_user_id", "非法的用户id");

    	YdUserOrderResult ydUserOrderResult = checkUserOrder(orderId);
    	ValidateBusinessUtils.assertFalse(!(YdUserOrderStatusEnum.WAIT_PAY.getCode().equalsIgnoreCase(ydUserOrderResult.getOrderStatus())
				|| YdUserOrderStatusEnum.WAIT_HANDLE.getCode().equalsIgnoreCase(ydUserOrderResult.getOrderStatus())),
				"err_order_status", "错误的订单状态");

		YdUserOrder ydUserOrder = new YdUserOrder();
		BeanUtilExt.copyProperties(ydUserOrder, ydUserOrderResult);

        // 修改订单状态, 退还库存， 退还优惠券,
        ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.CANCEL.getCode());
        ydUserOrder.setUpdateTime(new Date());
        this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);

		List<YdUserOrderDetail> detailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(ydUserOrder.getId());
		List<YdMerchantItemSkuResult> skuList = new ArrayList<>();
		detailList.forEach(ydUserOrderDetail -> {
			YdMerchantItemSkuResult ydMerchantItemSkuResult = new YdMerchantItemSkuResult();
			ydMerchantItemSkuResult.setId(ydUserOrderDetail.getMerchantSkuId());
			ydMerchantItemSkuResult.setNum(ydUserOrderDetail.getNum());
			skuList.add(ydMerchantItemSkuResult);
		});
		ydMerchantItemService.addItemSkuStock(skuList);

        if (ydUserOrder.getUserCouponId() != null) {
			ydUserCouponDao.refundCoupon(userId, ydUserOrder.getUserCouponId());
		}
    }

	/**
	 * 根据订单id修改收货地址
	 * @param userId	用户id
	 * @param orderId	订单id
	 * @param addressId	收货地址id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateReceiveAddress(Integer userId, Integer orderId, Integer addressId) throws BusinessException {
		// 校验用户信息
		ydUserService.checkUserInfo(userId);

		// 校验收货地址
		YdUserAddressResult ydUserAddressResult = ydUserAddressService.checkAddressId(addressId);

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertNonNull(ydUserOrder, "err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getUserId().intValue() != userId,
				"err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse("ZT".equalsIgnoreCase(ydUserOrder.getReceiveWay()),
				"err_receive_way", "自提订单不可以修改收货地址");

		// 修改商品收货地址
		ydUserOrder.setRealname(ydUserAddressResult.getRealname());
		ydUserOrder.setMobile(ydUserAddressResult.getMobile());
		ydUserOrder.setDistrict(ydUserAddressResult.getDistrict());
		ydUserOrder.setDistrictId(ydUserAddressResult.getDistrictId());
		ydUserOrder.setCityId(ydUserAddressResult.getCityId());
		ydUserOrder.setCity(ydUserAddressResult.getCity());
		ydUserOrder.setProvince(ydUserAddressResult.getProvince());
		ydUserOrder.setProvinceId(ydUserAddressResult.getProvinceId());
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);

		// 判断是否存在礼品订单，存在的话修改礼品收货地址
		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(ydUserOrder.getId() + "");
		if (ydGiftOrder != null) {
			ydUserOrder.setRealname(ydUserAddressResult.getRealname());
			ydUserOrder.setMobile(ydUserAddressResult.getMobile());
			ydUserOrder.setDistrict(ydUserAddressResult.getDistrict());
			ydUserOrder.setDistrictId(ydUserAddressResult.getDistrictId());
			ydUserOrder.setCityId(ydUserAddressResult.getCityId());
			ydUserOrder.setCity(ydUserAddressResult.getCity());
			ydUserOrder.setProvince(ydUserAddressResult.getProvince());
			ydUserOrder.setProvinceId(ydUserAddressResult.getProvinceId());
			this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);
		}
	}

	/**
	 * 用户礼品订单确认收货
	 * @param userId	用户id
	 * @param expressOrderId  物流单号
	 */
	@Override
	public void userConfirmGiftOrder(Integer userId, String expressOrderId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(userId, "err_user_id", "非法的用户id");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(expressOrderId),
				"err_express_order_id", "非法的物流单号");

		// String companyName = ExpressUtil.getExpressCompany(expressOrderId);
		String companyName = "请根据物流单号自己去网上查询";

		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setExpressCompany(companyName);
		ydGiftOrderDetail.setExpressOrderId(expressOrderId);
		List<YdGiftOrderDetail> giftList = ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(giftList),
				"err_not_exist_gift_order_detail", "礼品订单详情不存在");

		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderById(giftList.get(0).getGiftOrderId());
		ValidateBusinessUtils.assertNonNull(ydGiftOrder, "err_not_exist_gift_order", "礼品订单不存在");

		ValidateBusinessUtils.assertFalse(ydGiftOrder.getUserId() == null || ydGiftOrder.getUserId() != userId,
				"err_not_exist_gift_order", "非法的礼品订单，请联系管理员处理");

		// 更新礼品订单详情信息
		giftList.forEach(giftOrderDetail -> {
			giftOrderDetail.setOrderStatus("SUCCESS");
			ydGiftOrderDetailDao.updateYdGiftOrderDetail(giftOrderDetail);
		});

		// 更新礼品订单信息
		ydGiftOrder.setOrderStatus("SUCCESS");
		ydGiftOrderDao.updateYdGiftOrder(ydGiftOrder);
	}

	/**
	 * 用户订单确认收货
	 * @param userId	用户id
	 * @param orderId 	用户订单id
	 */
	@Override
	public void userConfirmOrder(Integer userId, Integer orderId) {
		ValidateBusinessUtils.assertIdNotNull(userId, "err_user_id", "非法的用户id");

		ValidateBusinessUtils.assertIdNotNull(orderId, "err_order_id", "非法的订单id");

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertNonNull(ydUserOrder,"err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getUserId() == null || ydUserOrder.getUserId() != userId,
				"err_not_exist_user_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse(!"PS".equalsIgnoreCase(ydUserOrder.getReceiveWay()),
				"err_receive_way", "错误的订单配送信息");

		ValidateBusinessUtils.assertFalse(!"WAIT_GOODS".equalsIgnoreCase(ydUserOrder.getOrderStatus()),
				"err_order_status", "订单状态不正确");


		ValidateBusinessUtils.assertFalse("WAIT".equalsIgnoreCase(ydUserOrder.getPayStatus()),
				"err_order_pay_status", "订单尚未支付");

		ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.SUCCESS.getCode());
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);

		// 判断是否使用优惠券， 如果使用优惠券， 处理优惠券为已经使用
		YdUserCoupon ydUserCoupon = ydUserCouponDao.getYdUserCouponByOutOrderId(orderId + "");
		if (ydUserCoupon != null) {
			ydUserCoupon.setUseTime(new Date());
			ydUserCoupon.setUseStatus("Y");
			ydUserCouponDao.updateYdUserCoupon(ydUserCoupon);
		}
	}

	/**
	 * 更新用户订单的二维码
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @param imageUrl	  订单二维码url
	 */
	@Override
	public void updateYdUserOrderQrCode(Integer userId, Integer merchantId, Integer orderId, String imageUrl) {
		YdUserOrder ydUserOrder = new YdUserOrder();
		ydUserOrder.setId(orderId);
		ydUserOrder.setUserId(userId);
		ydUserOrder.setMerchantId(merchantId);
		ydUserOrder.setQrCodeUrl(imageUrl);
		ydUserOrderDao.updateYdUserOrder(ydUserOrder);
	}

	/**
	 * 商户订单发货
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @param expressOrderId 快递单号
	 */
	@Override
	public void updateOrderExpress(Integer merchantId, Integer orderId, String expressOrderId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(orderId, "err_order_id", "非法的订单id");

		ValidateBusinessUtils.assertStringNotBlank(expressOrderId, "err_empty_express_order_id", "物流单号不可以为空");

		// String companyName = ExpressUtil.getExpressCompany(expressOrderId);
		String companyName = "请根据物流单号自己去网上查询";

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertNonNull(ydUserOrder, "err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse(!"PS".equalsIgnoreCase(ydUserOrder.getReceiveWay()),
				"err_receive_way", "错误的订单配送信息");

		ValidateBusinessUtils.assertFalse(!"WAIT_DELIVER".equalsIgnoreCase(ydUserOrder.getOrderStatus()),
				"err_order_status", "订单状态不正确");

		ValidateBusinessUtils.assertFalse("WAIT".equalsIgnoreCase(ydUserOrder.getPayStatus()),
				"err_order_pay_status", "订单尚未支付");

		// 判断订单是否有购买供应商的礼品订单，有的话，去扣钱购买
		updatePsOrderGiftStatusAndToPay(ydUserOrder, expressOrderId, companyName);

		ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.WAIT_GOODS.getCode());
		ydUserOrder.setExpressOrderId(expressOrderId);
		ydUserOrder.setExpressCompany(companyName);
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);
	}

	@Override
	public void updateUserOrderPayStatus(String outOrderId, String billNo) throws BusinessException {
		ValidateBusinessUtils.assertStringNotBlank(billNo, "err_empty_bill_no", "流水号不可以为空");
		ValidateBusinessUtils.assertStringNotBlank(outOrderId, "err_empty_bill_no", "对外订单号不可以为空");

		ValidateBusinessUtils.assertFalse(!outOrderId.startsWith(SystemPrefixConstants.YD_ORDER_PREFIX),
				"err_notify_order_id", "错误格式的订单号" + outOrderId);

		String[] split = outOrderId.split("-");
		logger.info("====快抢订单支付天猫回调outOrderId=" + outOrderId + "截取后的值=" + JSON.toJSONString(split));
		// String[] split = outOrderId.split(SystemPrefixConstants.YD_ORDER_PREFIX);
		Integer id = Integer.valueOf(split[1]);

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(id);
		ValidateBusinessUtils.assertNonNull(ydUserOrder, "err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse("ZT".equalsIgnoreCase(ydUserOrder.getReceiveWay()),
				"err_order_receive_way", "错误的配送方式");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydUserOrder.getPayStatus())
						|| StringUtils.isNotEmpty(ydUserOrder.getBillNo()),
				"err_order_pay_status", "订单已经支付");

		ydUserOrder.setPayStatus("Y");
		ydUserOrder.setPayTime(new Date());
		ydUserOrder.setBillNo(billNo);
		ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.WAIT_DELIVER.getCode());
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);
	}

	@Override
	public Map<String,String> toPay(Integer userId, Integer orderId, String domain) throws BusinessException {
		ydUserService.checkUserInfo(userId);

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_empty_order_id", "订单id不可以为空");

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertFalse(ydUserOrder == null,
				"err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse(!YdUserOrderStatusEnum.WAIT_PAY.getCode().equalsIgnoreCase(ydUserOrder.getOrderStatus()),
				"err_order_receive_way", "错误的订单状态");

		ValidateBusinessUtils.assertFalse("ZT".equalsIgnoreCase(ydUserOrder.getReceiveWay()),
				"err_order_receive_way", "错误的配送方式");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydUserOrder.getPayStatus())
						&& StringUtils.isNotEmpty(ydUserOrder.getBillNo()),
				"err_order_pay_status", "订单已经支付");

		WbWeixinAccountResult weixinConfig = weixinService.getByWeixinAccountByType("91kuaiqiang");

		YdUserAuth ydUserAuth = ydUserAuthDao.getYdUserAuthByUserId(userId);

		Integer totalFee = MathUtils.multiply(ydUserOrder.getPayPrice(), 100, 0).intValue();
		String outOrderId = SystemPrefixConstants.YD_ORDER_PREFIX + ydUserOrder.getId() + "-" + ydUserOrder.getPayEntryCount();

		String notifyUrl = domain + "/yd/callback/weixin/orderPay/notify";
		String returnUrl = domain + "/store/front/" + ydUserOrder.getMerchantId() + "/index/my";
		// 重试次数+1
		ydUserOrderDao.updatePayEntryCount(ydUserOrder.getId());
		Map<String,String> returnMap = WexinPublicPayUtils.wechatPay(totalFee, weixinConfig.getAppId(),
				weixinConfig.getMchId(), weixinConfig.getSignKey(), outOrderId, "优度支付",
				"127.0.0.1", ydUserAuth.getOpenId(), returnUrl, notifyUrl);
		return returnMap;
	}

	/**
	 * 设置旧机抵扣金额
	 * @param merchantId
	 * @param orderId
	 * @param price		旧机抵扣金额
	 * @param mobileName	手机型号
	 */
	@Override
	public void setOldMachineReduce(Integer merchantId, Integer orderId, Double price, String mobileName) throws BusinessException {
		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_empty_order_id", "订单id不可以为空");

		ValidateBusinessUtils.assertFalse(price == null || price <= 0,
				"err_order_oldMachine_price", "请输入正确的旧机抵扣金额");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobileName),
				"err_mobile_name_isnull", "请输入手机型号");

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertFalse(ydUserOrder == null,
				"err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse(!ydUserOrder.getOrderStatus().equalsIgnoreCase(YdUserOrderStatusEnum.WAIT_HANDLE.getCode()),
				"err_order_status_isnull", "订单状态不正确");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydUserOrder.getIsCheckOldMachineReduce()) ||
						ydUserOrder.getIsCheckOldMachineReduce().equalsIgnoreCase("N"),
				"err_old_machine_isnull", "旧机抵扣未开启");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getOldMachineReducePrice() != null &&
						ydUserOrder.getOldMachineReducePrice() > 0,
				"err_old_machine_isnull", "已经设置过旧机抵扣金额");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		// 修改旧机抵扣金额
		ydUserOrderDao.updateOldMachinePrice(merchantId, orderId, price, mobileName);
	}

	/**
	 * 设置其它优惠金额
	 * @param merchantId
	 * @param orderId
	 * @param price
	 * @throws BusinessException
	 */
	@Override
	public void setOtherPrice(Integer merchantId, Integer orderId, Double price) throws BusinessException {
		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_empty_order_id", "订单id不可以为空");

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(price == null || price <= 0,
				"err_order_other_price", "请输入正确的其它优惠金额");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertFalse(ydUserOrder == null,
				"err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse(!ydUserOrder.getMerchantId().equals(storeInfo.getId()),
				"err_not_exist_order", "非法的订单id,请联系管理员处理");

		ValidateBusinessUtils.assertFalse(!(ydUserOrder.getOrderStatus().equalsIgnoreCase(YdUserOrderStatusEnum.WAIT_HANDLE.getCode())
						|| ydUserOrder.getOrderStatus().equalsIgnoreCase(YdUserOrderStatusEnum.WAIT_PAY.getCode())),
				"err_order_status", "错误的订单状态");


		// 查看新设置的优惠金额是否比订单金额大
		if (ydUserOrder.getManualReducePrice() != null && ydUserOrder.getManualReducePrice() > 0) {
			// 没手动优惠前的金额
			double noManualReducePrice = MathUtils.add(ydUserOrder.getPayPrice(), ydUserOrder.getManualReducePrice(), 2);
			double payPrice = MathUtils.subtract(noManualReducePrice, price, 2);
			ValidateBusinessUtils.assertFalse(payPrice <= 0,
					"err_order_status", "优惠后的金额必须大于0，请重新输入");
		} else {
			double payPrice = MathUtils.subtract(ydUserOrder.getPayPrice(), price, 2);
			ValidateBusinessUtils.assertFalse(payPrice <= 0,
					"err_order_status", "优惠后的金额必须大于0，请重新输入");
		}

		// 修改其它金额
		int count = ydUserOrderDao.updateOrderOtherPrice(merchantId, orderId, price);
		ValidateBusinessUtils.assertFalse(count <= 0,
				"err_order_status", "设置失败，请联系管理员进行处理");
	}

	/**
	 * 商户在app上确认待处理订单
	 * @param merchantId
	 * @param orderId
	 * @throws BusinessException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void confirmAppOrder(Integer merchantId, Integer orderId) throws BusinessException {

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_empty_order_id", "订单id不可以为空");

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		YdUserOrder ydUserOrder = this.ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertFalse(ydUserOrder == null,
				"err_not_exist_order", "用户订单不存在");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		ValidateBusinessUtils.assertFalse(!ydUserOrder.getMerchantId().equals(storeInfo.getId()),
				"err_not_exist_order", "非法的订单id,请联系管理员处理");

		ValidateBusinessUtils.assertFalse(!ydUserOrder.getOrderStatus().equalsIgnoreCase(YdUserOrderStatusEnum.WAIT_HANDLE.getCode()),
				"err_status_order", "错误的订单状态");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getPayStatus().equalsIgnoreCase("Y"),
				"err_status_order", "订单已经支付，订单状态错误");

		// 线下自提的订单直接修改为订单成功
		ydUserOrder.setPayStatus("Y");
		ydUserOrder.setPayTime(new Date());
		ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.SUCCESS.getCode());
		this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);

		// 判断是否使用优惠券， 如果使用优惠券， 处理优惠券为已经使用
		YdUserCoupon ydUserCoupon = ydUserCouponDao.getYdUserCouponByOutOrderId(orderId + "");
		if (ydUserCoupon != null) {
			ydUserCoupon.setUseTime(new Date());
			ydUserCoupon.setUseStatus("Y");
			ydUserCouponDao.updateYdUserCoupon(ydUserCoupon);
		}

		// 付款供应商订单, 修改自提礼品状态
		updateZtOrderGiftStatusAndToPay(ydUserOrder);
	}

	/**
	 * 定时服务取消未付款订单
	 * @param merchantId, 不传是所有商户的
	 * @throws BusinessException
	 */
	@Override
	public void synCancelNoPayOrder(Integer merchantId) throws BusinessException{
		logger.info("========进入定时服务取消未付款订单======");
		try {
			YdUserOrder ydUserOrder = new YdUserOrder();
			ydUserOrder.setReceiveWay("PS");
			ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.WAIT_PAY.getCode());
			if (merchantId != null) {
				ydUserOrder.setMerchantId(merchantId);
			}
			List<YdUserOrder> userOrderList = ydUserOrderDao.getAll(ydUserOrder);
			if (CollectionUtils.isEmpty(userOrderList)) return;

			Map<Integer, YdMerchantOrderConfig> timeConfigMap = new HashMap<>();
			Map<Integer, List<YdUserOrder>> merchantOrderMap = userOrderList.stream().collect(Collectors.groupingBy(YdUserOrder::getMerchantId));
			for(Map.Entry<Integer, List<YdUserOrder>> entry : merchantOrderMap.entrySet()){
				YdMerchantOrderConfig ydMerchantOrderConfig = ydMerchantOrderConfigDao.getYdShopOrderConfigByMerchantId(entry.getKey());
				if (ydMerchantOrderConfig == null) {
					ydMerchantOrderConfig = new YdMerchantOrderConfig();
					ydMerchantOrderConfig.setOrderAutoCancelTime(2);
					ydMerchantOrderConfig.setMerchantId(entry.getKey());
				}
				timeConfigMap.put(entry.getKey(), ydMerchantOrderConfig);
			}

			// 筛选应该取消的订单
			Date nowDate = new Date();
			List<YdUserOrder> cancelList = new ArrayList<>();
			userOrderList.forEach(order -> {
				YdMerchantOrderConfig ydMerchantOrderConfig = timeConfigMap.get(order.getMerchantId());
				Date endTime = DateUtils.addHour(order.getCreateTime(), ydMerchantOrderConfig.getOrderAutoCancelTime());
				if (endTime.getTime() < nowDate.getTime()) {
					cancelList.add(order);
				}
			});

			logger.info("====应该取消的订单list=" + JSON.toJSONString(cancelList));
			cancelList.forEach(data -> {
				// 取消订单，退还商品库存, 退还优惠券
				ydUserOrderDao.cancelNoPayOrder(data.getId());
				List<YdUserOrderDetail> detailList = ydUserOrderDetailDao.getYdShopOrderDetailByOrderId(data.getId());
				List<YdMerchantItemSkuResult> skuList = new ArrayList<>();
				detailList.forEach(ydUserOrderDetail -> {
					YdMerchantItemSkuResult ydMerchantItemSkuResult = new YdMerchantItemSkuResult();
					ydMerchantItemSkuResult.setNum(ydUserOrderDetail.getNum());
					ydMerchantItemSkuResult.setId(ydUserOrderDetail.getMerchantSkuId());
					skuList.add(ydMerchantItemSkuResult);
				});
				ydMerchantItemService.addItemSkuStock(skuList);
				if (data.getUserCouponId() != null) {
					ydUserCouponDao.refundCoupon(data.getUserId(), data.getUserCouponId());
				}
			});
			logger.info("========离开定时服务取消未付款订单======");
		} catch (Exception e) {
			logger.info("========定时服务取消未付款订单出错======");
		}
	}

	// ------------------------------------ private -------------------------------------

	/**
	 * 创建商品订单
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @param userCouponId	用户优惠券id
	 * @param itemCount		商品总数量
	 * @param orderPrice	订单总金额
	 * @param couponPrice	优惠金额
	 * @param isCheckIntegralReduce     是否选中积分抵扣
	 * @param isCheckOldMachineReduce	是否选中旧机抵扣
	 * @param receivingWay	收货方式
	 * @param ydUserAddress	用户收货地址
	 * @param ydMerchant	商户信息
	 */
	private YdUserOrder createOrder(Integer userId, Integer merchantId, Integer userCouponId, Integer itemCount, Double orderPrice, Integer couponPrice, String isCheckIntegralReduce,
									String isCheckOldMachineReduce, String receivingWay, YdUserAddressResult ydUserAddress, YdMerchantResult ydMerchant) {
		YdUserOrder ydUserOrder = new YdUserOrder();
		ydUserOrder.setCreateTime(new Date());
		ydUserOrder.setUserId(userId);
		ydUserOrder.setMerchantId(merchantId);
		ydUserOrder.setItemCount(itemCount);
		// ydUserOrder.setRemark("这个是订单备注");

		ydUserOrder.setUserCouponId(userCouponId);
		ydUserOrder.setReceiveWay(receivingWay);

        if (StringUtils.isEmpty(isCheckIntegralReduce)) {
            ydUserOrder.setIsCheckIntegralReduce("N");
        } else {
            ydUserOrder.setIsCheckIntegralReduce(isCheckIntegralReduce);
        }
        if (StringUtils.isEmpty(isCheckOldMachineReduce)) {
            ydUserOrder.setIsCheckOldMachineReduce("N");
        } else {
			ydUserOrder.setIsCheckOldMachineReduce(isCheckOldMachineReduce);
		}

		// 设置收货信息
		if ("ZT".equalsIgnoreCase(receivingWay)){
            ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.WAIT_HANDLE.getCode());

			ydUserOrder.setMobile(ydUserAddress.getMobile());
			ydUserOrder.setRealname(ydUserAddress.getRealname());

			ydUserOrder.setProvince(ydMerchant.getProvince());
			ydUserOrder.setProvinceId(ydMerchant.getProvinceId());

			ydUserOrder.setDistrictId(ydMerchant.getDistrictId());
			ydUserOrder.setDistrict(ydMerchant.getDistrict());

			ydUserOrder.setCityId(ydMerchant.getCityId());
			ydUserOrder.setCity(ydMerchant.getCity());

			ydUserOrder.setAddress(ydMerchant.getAddress());
		} else if ("PS".equalsIgnoreCase(receivingWay)){
            ydUserOrder.setOrderStatus(YdUserOrderStatusEnum.WAIT_PAY.getCode());

			ydUserOrder.setIsCheckIntegralReduce("N");
			ydUserOrder.setIsCheckOldMachineReduce("N");

			ydUserOrder.setMobile(ydUserAddress.getMobile());
			ydUserOrder.setRealname(ydUserAddress.getRealname());

			ydUserOrder.setProvince(ydUserAddress.getProvince());
			ydUserOrder.setProvinceId(ydUserAddress.getProvinceId());

			ydUserOrder.setCityId(ydUserAddress.getCityId());
			ydUserOrder.setCity(ydUserAddress.getCity());

			ydUserOrder.setDistrictId(ydUserAddress.getDistrictId());
			ydUserOrder.setDistrict(ydUserAddress.getDistrict());

			ydUserOrder.setAddress(ydUserAddress.getAddress());
		}

		// 实付金额  (下单时实付金额 = 订单金额 - 优惠券金额)
		Double payPrice = new BigDecimal(orderPrice + "")
				.subtract(new BigDecimal(couponPrice + ""))
				.setScale(2, BigDecimal.ROUND_UP).doubleValue();

		ValidateBusinessUtils.assertFalse(payPrice <= 0,
				"err_pay_price", "实付金额必须大于0");

		ydUserOrder.setPayPrice(payPrice);
		// 订单金额
		ydUserOrder.setOrderPrice(orderPrice);
		// 优惠券金额
		ydUserOrder.setCouponPrice(Double.valueOf(couponPrice));
		// 积分抵扣金额
		ydUserOrder.setIntegralReducePrice(0.00);
		// 旧机抵扣金额
		ydUserOrder.setOldMachineReducePrice(0.00);
		// 手动优惠金额
		ydUserOrder.setManualReducePrice(0.00);

		// 设置支付状态
		ydUserOrder.setPayStatus("WAIT");
		ydUserOrder.setPayTime(null);
		ydUserOrder.setBillNo(null);

		// 设置发货状态
		ydUserOrder.setShipStatus("WAIT");
		ydUserOrder.setShipTime(null);

		// 设置确认收货状态
		ydUserOrder.setConfirmGoodsStatus("WAIT");
		ydUserOrder.setConfirmGoodsTime(null);

		// 设置物流信息
		ydUserOrder.setExpressCompany(null);
		ydUserOrder.setExpressOrderId(null);
		ydUserOrder.setExpressCompanyNumber(null);

		ydUserOrder.setOrderNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.ITEM_ORDER));
		ydUserOrder.setPayEntryCount(0);
		this.ydUserOrderDao.insertYdShopOrder(ydUserOrder);
		return ydUserOrder;
	}

	/**
	 * 创建商品订单详情
	 * @param userId	  用户id
	 * @param merchantId  商户id
	 * @param orderId	  orderId
	 * @param skuList	  商品skuList
	 */
	private void createOrderDetail(Integer userId, Integer merchantId, Integer orderId, List<YdMerchantItemSkuResult> skuList) {
		skuList.forEach(ydMerchantItemSkuResult -> {
			YdUserOrderDetail ydUserOrderDetail = new YdUserOrderDetail();
			ydUserOrderDetail.setCreateTime(new Date());
			ydUserOrderDetail.setUserId(userId);
			ydUserOrderDetail.setMerchantId(merchantId);
			ydUserOrderDetail.setOrderId(orderId);
			ydUserOrderDetail.setItemId(ydMerchantItemSkuResult.getItemId());
			ydUserOrderDetail.setSkuId(ydMerchantItemSkuResult.getSkuId());

			ydUserOrderDetail.setMerchantItemTitle(ydMerchantItemSkuResult.getTitle());
			ydUserOrderDetail.setCover(ydMerchantItemSkuResult.getSkuCover());
			ydUserOrderDetail.setMerchantItemId(ydMerchantItemSkuResult.getMerchantItemId());
			ydUserOrderDetail.setMerchantSkuId(ydMerchantItemSkuResult.getId());
			ydUserOrderDetail.setMerchantItemPrice(ydMerchantItemSkuResult.getSalePrice());
			ydUserOrderDetail.setNum(ydMerchantItemSkuResult.getNum());
			ydUserOrderDetail.setSpecNameValueJson(ydMerchantItemSkuResult.getSpecNameValueJson());
			ydUserOrderDetail.setSpecValueIdPath(ydMerchantItemSkuResult.getSpecValueIdPath());
			ydUserOrderDetail.setSpecValueStr(ydMerchantItemSkuResult.getSpecNameValueIdJson());
			ydUserOrderDetail.setOrderDetailNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.ITEM_ORDER));
			ydUserOrderDetailDao.insertYdShopOrderDetail(ydUserOrderDetail);
		});
	}

	/**
	 * 查询商品总金额
	 * @param skuList
	 * @return
	 */
	private BigDecimal getSkuListSumTotalPrice(List<YdMerchantItemSkuResult> skuList) {
		BigDecimal totalMonty = new BigDecimal(0.00);
		for (YdMerchantItemSkuResult ydMerchantItemSkuResult : skuList) {
			totalMonty = new BigDecimal(ydMerchantItemSkuResult.getSalePrice() + "")
					.multiply(new BigDecimal(ydMerchantItemSkuResult.getNum()))
					.add(totalMonty)
					.setScale(2, BigDecimal.ROUND_UP);
		}
		return totalMonty;
	}

	/**
	 * 商户订单成功后去供应商下单礼品
	 * @param ydUserOrder
	 */
	private void updateZtOrderGiftStatusAndToPay(YdUserOrder ydUserOrder) throws BusinessException {
		// 商户下单去购买供应商礼品, 扣除商户礼品账户余额, 修改礼品订单状态
		Integer merchantId = ydUserOrder.getMerchantId();
		YdMerchantGiftAccountResult giftAccount = ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(merchantId);

		// 查询商户礼品订单, 礼品子订单
		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(ydUserOrder.getId() + "");
		if (ydGiftOrder == null) return;

		if (ydGiftOrder.getPayStatus().equalsIgnoreCase("SUCCESS")) return;

		List<YdGiftOrderDetail> giftOrderDetailList = ydGiftOrderDetailDao.getYdGiftOrderDetailByGiftOrderId(ydGiftOrder.getId());
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(giftOrderDetailList, YdGiftOrderDetailResult.class);

		//

		// 计算需要支付给供应商的礼品金额;
		Double payPrice = getToPaySupplierGiftPrice(detailResultList);

		if (payPrice > 0) {
			logger.info("====礼品账户余额===" + JSON.toJSONString(giftAccount) + " ,订单礼品所需金额===" + JSON.toJSONString(payPrice));
			// 扣除礼品账户余额,修改礼品订单状态
			int result = this.ydMerchantGiftAccountDao.reduceGiftAccountBalance(merchantId, payPrice);
			if (result > 0) {
				// 记录礼品账户余额流水
				Double giftAfterBalance = MathUtils.subtract(giftAccount.getBalance(), payPrice, 2);
				YdMerchantGiftTransResult ydMerchantGiftTransResult = ydMerchantGiftTransService.addMerchantGiftTrans(merchantId, ydUserOrder.getOrderNo(),
						YdMerchantTransSourceEnum.PURCHASE_GIFT.getCode(),"out", YdMerchantTransSourceEnum.PURCHASE_GIFT.getDescription(),
						"SUCCESS", payPrice, giftAccount.getBalance(), giftAfterBalance);

				// 修改礼品订单状态
				ydGiftOrder.setPayStatus("SUCCESS");
				ydGiftOrder.setPayTime(new Date());
				ydGiftOrder.setBillNo(ydMerchantGiftTransResult.getId() + "");
				ydGiftOrderDao.updateYdGiftOrder(ydGiftOrder);

				// 修改商户自有的礼品已完成
				for (YdGiftOrderDetail ydGiftOrderDetail : giftOrderDetailList) {
					if (ydGiftOrderDetail.getSupplierId() == null || ydGiftOrderDetail.getSupplierId() <= 0) {
						ydGiftOrderDetail.setOrderStatus("SUCCESS");
						ydGiftOrderDetail.setExpressCompany("上门自提");
						ydGiftOrderDetail.setExpressOrderId("上门自提");
						this.ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
					}
				}
			} else {
				logger.info("====确认自提订单商户礼品账户余额扣费失败");
				ValidateBusinessUtils.assertFalse(true,"err_no_money", "商户礼品账户余额扣费失败");
			}
		} else {
			// 修改商户自有的礼品已完成
			for (YdGiftOrderDetail ydGiftOrderDetail : giftOrderDetailList) {
				if (ydGiftOrderDetail.getSupplierId() == null || ydGiftOrderDetail.getSupplierId() <= 0) {
					ydGiftOrderDetail.setOrderStatus("SUCCESS");
					ydGiftOrderDetail.setExpressCompany("上门自提");
					ydGiftOrderDetail.setExpressOrderId("上门自提");
					this.ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
				}
			}
		}
	}

	/**
	 * 修改配送的礼品订单
	 * @param ydUserOrder
	 * @param expressOrderId
	 * @param companyName
	 * @throws BusinessException
	 */
	private void updatePsOrderGiftStatusAndToPay(YdUserOrder ydUserOrder, String expressOrderId, String companyName) throws BusinessException {
		// 查询商户礼品订单, 礼品子订单
		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(ydUserOrder.getId() + "");
		if (ydGiftOrder == null) return;

		if (ydGiftOrder.getPayStatus().equalsIgnoreCase("SUCCESS")) return;

		List<YdGiftOrderDetail> giftOrderDetailList = ydGiftOrderDetailDao.getYdGiftOrderDetailByGiftOrderId(ydGiftOrder.getId());
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(giftOrderDetailList, YdGiftOrderDetailResult.class);

		// 先给商户自己的礼品发货,别问我为啥，产品让改的，乱七八糟的卧槽了
		giftOrderDetailList.forEach(data -> {
			if (data.getSupplierId() == null || data.getSupplierId() <= 0) {
				if (StringUtils.isEmpty(data.getExpressOrderId())) {
					//  /** WAIT_DELIVER | WAIT_GOODS | SUCCESS  */
					data.setOrderStatus("WAIT_GOODS");
					data.setExpressCompany(companyName);
					data.setExpressOrderId(expressOrderId);
					ydGiftOrderDetailDao.updateYdGiftOrderDetail(data);
				}
			}
		});


		// 计算需要支付给供应商的礼品金额;
		Double payPrice = getToPaySupplierGiftPrice(detailResultList);
		if (payPrice > 0) {
			Integer merchantId = ydUserOrder.getMerchantId();
			YdMerchantGiftAccountResult giftAccount = ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(merchantId);
			logger.info("====礼品账户余额===" + JSON.toJSONString(giftAccount) + " ,订单礼品所需金额===" + JSON.toJSONString(payPrice));
			// 扣除礼品账户余额,修改礼品订单状态
			int result = this.ydMerchantGiftAccountDao.reduceGiftAccountBalance(merchantId, payPrice);
			if (result > 0) {
				// 记录礼品账户余额流水
				Double giftAfterBalance = MathUtils.subtract(giftAccount.getBalance(), payPrice, 2);
				YdMerchantGiftTransResult ydMerchantGiftTransResult = ydMerchantGiftTransService.addMerchantGiftTrans(merchantId, ydUserOrder.getOrderNo(),
						YdMerchantTransSourceEnum.PURCHASE_GIFT.getCode(),"out", YdMerchantTransSourceEnum.PURCHASE_GIFT.getDescription(),
						"SUCCESS", payPrice, giftAccount.getBalance(), giftAfterBalance);

				// 修改礼品订单状态
				ydGiftOrder.setOrderStatus("WAIT");
				ydGiftOrder.setPayStatus("SUCCESS");
				ydGiftOrder.setPayTime(new Date());
				ydGiftOrder.setBillNo(ydMerchantGiftTransResult.getId() + "");
				ydGiftOrderDao.updateYdGiftOrder(ydGiftOrder);
			} else {
				logger.info("====商户礼品账户余额扣费失败,账户余额不足");
				ValidateBusinessUtils.assertFalse(true,"err_no_money", "线上下单扣除商户礼品账户余额失败");
			}
		}
	}

	/**
	 * // 计算需要支付给供应商的礼品金额;
	 * @param detailResultList
	 * @return
	 */
	private Double getToPaySupplierGiftPrice(List<YdGiftOrderDetailResult> detailResultList) {
		// 计算需要支付的金额;
		// 设置礼品信息,计算需要支付供应商的金额
		Double payPrice = 0.00;
		for (YdGiftOrderDetailResult ydGiftOrderDetailResult : detailResultList) {
			// 如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getMerchantGiftId() == null) {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			} else {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType().equalsIgnoreCase("platform")) {
					YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
					ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
					ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());

					// 判断是否有阶梯价, 根据阶梯价计算金额
					if (ydGift.getLadderNumber() == null || ydGift.getLadderPrice() == null) {
						payPrice = payPrice + ydGiftOrderDetailResult.getNum() * ydGift.getSalePrice();
					} else {
						Double salePrice = ydGiftOrderDetailResult.getNum() < ydGift.getLadderNumber() ? ydGift.getSalePrice() : ydGift.getLadderPrice();
						payPrice = payPrice + ydGiftOrderDetailResult.getNum() * salePrice;
					}

				} else {
					ydGiftOrderDetailResult.setGiftDesc(ydMerchantGift.getGiftDesc());
					ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
				}
			}
		}
		return payPrice;
	}

	public static void main(String[] args) {
		String outOrderId = "ydOrder-880-0";
		String[] split = outOrderId.split("-");
		System.out.println(split[1]);
	}


}

