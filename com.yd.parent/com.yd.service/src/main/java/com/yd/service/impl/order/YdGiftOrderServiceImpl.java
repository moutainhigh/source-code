package com.yd.service.impl.order;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.api.result.user.YdUserAddressResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.core.enums.YdOrderNoTypeEnum;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import com.yd.service.bean.order.YdGiftOrderDetail;
import com.yd.service.bean.order.YdUserOrder;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import com.yd.service.dao.order.YdGiftOrderDetailDao;
import com.yd.service.dao.order.YdUserOrderDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.order.YdGiftOrderDao;
import com.yd.service.bean.order.YdGiftOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:礼品订单主表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:53:33
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdGiftOrderServiceImpl implements YdGiftOrderService {

	private static final Logger logger = LoggerFactory.getLogger(YdGiftOrderServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdUserOrderDao ydUserOrderDao;

	@Resource
	private YdGiftOrderDao ydGiftOrderDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdGiftOrderDetailDao ydGiftOrderDetailDao;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdGiftOrderResult getYdGiftOrderById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdGiftOrderResult ydGiftOrderResult = null;
		YdGiftOrder ydGiftOrder = this.ydGiftOrderDao.getYdGiftOrderById(id);
		if (ydGiftOrder != null) {
			ydGiftOrderResult = new YdGiftOrderResult();
			BeanUtilExt.copyProperties(ydGiftOrderResult, ydGiftOrder);
		}
		return ydGiftOrderResult;
	}

	@Override
	public Page<YdGiftOrderResult> findYdGiftOrderListByPage(YdGiftOrderResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdGiftOrderResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		BeanUtilExt.copyProperties(ydGiftOrder, params);
		
		int amount = this.ydGiftOrderDao.getYdGiftOrderCount(ydGiftOrder);
		if (amount > 0) {
			List<YdGiftOrder> dataList = this.ydGiftOrderDao.findYdGiftOrderListByPage(
					ydGiftOrder, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdGiftOrderResult> resultList = DTOUtils.convertList(dataList, YdGiftOrderResult.class);

			resultList.forEach(ydGiftOrderResult -> {
				List<YdGiftOrderDetailResult> giftOrderDetailGroupByExpress = getGiftOrderDetailGroupByExpress(ydGiftOrderResult.getId());
				ydGiftOrderResult.setDetailList(giftOrderDetailGroupByExpress);
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public Page<YdGiftOrderResult> findSupplerGiftOrderListByPage(YdGiftOrderResult params, PagerInfo pagerInfo) {
		Page<YdGiftOrderResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		BeanUtilExt.copyProperties(ydGiftOrder, params);
		int amount = this.ydGiftOrderDao.getSupplerGiftOrderCount(params);

		if (amount > 0) {
			List<YdGiftOrder> dataList = this.ydGiftOrderDao.findSupplerGiftOrderListByPage(ydGiftOrder,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdGiftOrderResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdGiftOrderResult> getAll(YdGiftOrderResult ydGiftOrderResult) {
		YdGiftOrder ydGiftOrder = null;
		if (ydGiftOrderResult != null) {
			ydGiftOrder = new YdGiftOrder();
			BeanUtilExt.copyProperties(ydGiftOrder, ydGiftOrderResult);
		}
		List<YdGiftOrder> dataList = this.ydGiftOrderDao.getAll(ydGiftOrder);
		return DTOUtils.convertList(dataList, YdGiftOrderResult.class);
	}

	/**
	 * 商户下单创建礼品订单
	 * @param merchantId		商户id
	 * @param totalGiftCount	礼品总数量
	 * @param totalDetailCount	子订单数量
	 * @param totalSalePrice	总售价
	 * @param totalMarketPrice	总市场价
	 * @return
	 */
	@Override
	public YdGiftOrderResult addMerchantGiftOrder(Integer merchantId, Integer totalGiftCount, Integer totalDetailCount, Double totalSalePrice, Double totalMarketPrice) {

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");

		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		Date nowDate = new Date();
		ydGiftOrder.setCreateTime(nowDate);
		ydGiftOrder.setUpdateTime(nowDate);
		ydGiftOrder.setType("in");
		ydGiftOrder.setTransType("online");
		ydGiftOrder.setMerchantId(merchantId);
		ydGiftOrder.setPayStatus("SUCCESS");
		ydGiftOrder.setOrderStatus("SUCCESS");
		ydGiftOrder.setPayTime(nowDate);
		ydGiftOrder.setTotalGiftCount(totalGiftCount);
		ydGiftOrder.setTotalDetailCount(totalDetailCount);
		ydGiftOrder.setTotalSalePrice(totalSalePrice);
		ydGiftOrder.setTotalMarketPrice(totalMarketPrice);

		// 设置商户收货地址
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ydGiftOrder.setRealname(ydMerchant.getContact());
		ydGiftOrder.setMobile(ydMerchant.getMobile());
		ydGiftOrder.setProvinceId(ydMerchant.getProvinceId());
		ydGiftOrder.setProvince(ydMerchant.getProvince());
		ydGiftOrder.setCityId(ydMerchant.getCityId());
		ydGiftOrder.setCity(ydMerchant.getCity());
		ydGiftOrder.setDistrictId(ydMerchant.getDistrictId());
		ydGiftOrder.setDistrict(ydMerchant.getDistrict());
		ydGiftOrder.setAddress(ydMerchant.getAddress());
		ydGiftOrder.setGiftOrderNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.GIFT_IN));
		this.ydGiftOrderDao.insertYdGiftOrder(ydGiftOrder);

		YdGiftOrderResult giftOrderResult = new YdGiftOrderResult();
		BeanUtilExt.copyProperties(giftOrderResult, ydGiftOrder);
		return giftOrderResult;
	}

	/**
	 * 查询礼品订单详情
	 * @param giftOrderId 礼品订单id
	 * @return List<YdGiftOrderDetailResult>
	 */
	@Override
	public YdGiftOrderResult getGiftOrderDetailList(Integer merchantId, Integer giftOrderId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(giftOrderId == null || giftOrderId <= 0,
				"err_gift_order_id", "非法的礼品订单id");
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setId(giftOrderId);
		ydGiftOrder.setMerchantId(storeInfo.getId());
		List<YdGiftOrder> allList = this.ydGiftOrderDao.getAll(ydGiftOrder);

		if (CollectionUtils.isEmpty(allList)) return null;
		ydGiftOrder = allList.get(0);
		YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
		BeanUtilExt.copyProperties(ydGiftOrderResult, ydGiftOrder);

		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(giftOrderId);
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);

		detailResultList.forEach(ydGiftOrderDetailResult -> {
			// 如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getGiftId() == null) {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType().equalsIgnoreCase("platform")) {
					YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
					ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
					ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
				} else {
					ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
					ydGiftOrderDetailResult.setGiftDesc(ydMerchantGift.getGiftDesc());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
				}
			} else {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			}
		});
		ydGiftOrderResult.setDetailList(detailResultList);
		return ydGiftOrderResult;
	}

	/**
	 * 根据订单号查询礼品订单详情
	 * @param userId	用户id
	 * @param orderId	商品订单id
	 * @return
	 */
	@Override
	public YdGiftOrderResult getGiftOrderDetailByItemOrderId(Integer userId, Integer orderId) throws BusinessException{
		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
				"err_user_id", "非法的用户id");

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_order_id", "非法的订单id");

		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setUserId(userId);
		ydGiftOrder.setOutOrderId(orderId + "");
		List<YdGiftOrder> giftOrderList = ydGiftOrderDao.getAll(ydGiftOrder);
		YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
		if (CollectionUtils.isEmpty(giftOrderList)) {
			return ydGiftOrderResult;
		}
		BeanUtilExt.copyProperties(ydGiftOrderResult, giftOrderList.get(0));
		// 查询礼品订单详情
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(ydGiftOrderResult.getId());
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList= DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);
		detailResultList.forEach(ydGiftOrderDetailResult -> {
			// 如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getGiftId() == null) {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType().equalsIgnoreCase("platform")) {
					YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
					ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
					ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
				} else {
					ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
					ydGiftOrderDetailResult.setGiftDesc(ydMerchantGift.getGiftDesc());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
				}
			} else {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			}
		});
		detailResultList.stream().collect(Collectors.groupingBy(YdGiftOrderDetailResult :: getExpressCompanyNumber));
		ydGiftOrderResult.setDetailList(detailResultList);
		return ydGiftOrderResult;
	}

	/**
	 * 根据商户id， 订单id，根据物流单号分组查询用户礼品订单详情
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @return
	 */
	@Override
	public List<YdGiftOrderDetailResult> getMerchantGiftOrderDetailGroupByExpress(Integer merchantId, Integer orderId) throws BusinessException{
		// 校验获取门店信息
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_order_id", "非法的订单id");
		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(orderId  + "");
		if (ydGiftOrder == null) {
			return new ArrayList<>();
		}
		return getGiftOrderDetailGroupByExpress(ydGiftOrder.getId());
	}

	/**
	 * 根据商户id， 订单id查询礼品订单详情，不分组
	 * @param merchantId  商户id
	 * @param orderId	  订单id
	 * @return
	 */
	@Override
	public List<YdGiftOrderDetailResult> getAppGiftOrderDetail(Integer merchantId, Integer orderId) throws BusinessException {
		// 校验获取门店信息
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_order_id", "非法的订单id");
		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(orderId  + "");
		if (ydGiftOrder == null) {
			return new ArrayList<>();
		}

		YdUserOrder ydUserOrder = ydUserOrderDao.getYdUserOrderById(orderId);

		// 查询礼品订单详情
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(ydGiftOrder.getId());
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);

		detailResultList.forEach(ydGiftOrderDetailResult -> {
			// 如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getGiftId() == null) {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType().equalsIgnoreCase("platform")) {
					YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
					ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
					ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
				} else {
					ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
					ydGiftOrderDetailResult.setGiftDesc(ydMerchantGift.getGiftDesc());
				}
			} else {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			}
			ydGiftOrderDetailResult.setReceiveWay(ydUserOrder.getReceiveWay());
		});

		List<YdGiftOrderDetailResult> result = new ArrayList<>();
		Map<String, List<YdGiftOrderDetailResult>> map = detailResultList.stream()
				.collect(Collectors.groupingBy(YdGiftOrderDetailResult :: getOrderStatus));

		// 按照顺序设置，代发货，待收货，已完成
		if (CollectionUtils.isNotEmpty(map.get(YdUserOrderStatusEnum.WAIT_DELIVER.getCode()))) {
			result.addAll(map.get(YdUserOrderStatusEnum.WAIT_DELIVER.getCode()));
		}
		if (CollectionUtils.isNotEmpty(map.get(YdUserOrderStatusEnum.WAIT_GOODS.getCode()))) {
			result.addAll(map.get(YdUserOrderStatusEnum.WAIT_GOODS.getCode()));
		}
		if (CollectionUtils.isNotEmpty(map.get(YdUserOrderStatusEnum.SUCCESS.getCode()))) {
			result.addAll(map.get(YdUserOrderStatusEnum.SUCCESS.getCode()));
		}
		return result;
	}

	/**
	 * 根据用户id， 订单id，根据物流单号分组查询用户礼品订单详情
	 * @param userId	商户id
	 * @param orderId	订单id
	 * @return
	 */
	@Override
	public List<YdGiftOrderDetailResult> getFrontGiftOrderDetailAndGroupByExpress(Integer userId, Integer orderId) throws BusinessException{
		ValidateBusinessUtils.assertFalse(userId == null || userId <= 0,
				"err_user_id", "非法的用户id");

		ValidateBusinessUtils.assertFalse(orderId == null || orderId <= 0,
				"err_order_id", "非法的订单id");

		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setUserId(userId);
		ydGiftOrder.setOutOrderId(orderId + "");
		List<YdGiftOrder> giftOrderList = ydGiftOrderDao.getAll(ydGiftOrder);
		if (CollectionUtils.isEmpty(giftOrderList)) {
			return new ArrayList<>();
		}
		return getGiftOrderDetailGroupByExpress(giftOrderList.get(0).getId());
	}

	/**
	 * 根据礼品订单id查询礼品订单详情，并且根据物流信息分组分组
	 * @param giftOrderId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdGiftOrderResult getAppGiftOrderGroupByExpress(Integer giftOrderId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(giftOrderId == null || giftOrderId <= 0,
				"err_gift_order_id", "非法的礼品订单id");

		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderById(giftOrderId);
		ValidateBusinessUtils.assertFalse(ydGiftOrder == null,
				"err_gift_order_id", "礼品订单不存在");

		YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
		BeanUtilExt.copyProperties(ydGiftOrderResult, ydGiftOrder);
		ydGiftOrderResult.setDetailList(getGiftOrderDetailGroupByExpress(ydGiftOrderResult.getId()));
		return ydGiftOrderResult;
	}

	/**
	 * 商城用户下单，创建礼品订单
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @param outOrderId	订单id
	 * @param receivingWay	ZT(自提) | PS(配送)
	 * @param ydMerchantResult		商户信息
	 * @param ydUserAddressResult	用户地址信息
	 * @param giftResultList		下单的礼品数据
	 * @return
	 */
	@Override
	public YdGiftOrderResult createUserGiftOrder(Integer userId, Integer merchantId, Integer outOrderId, String receivingWay, YdMerchantResult ydMerchantResult,
												 YdUserAddressResult ydUserAddressResult, List<YdMerchantGiftResult> giftResultList) {
		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setCreateTime(new Date());
		// 对商户而言, in(入库,购买礼品商城的) | out(出库,商城下单的礼品)
		ydGiftOrder.setType("out");

		ydGiftOrder.setUserId(userId);
		ydGiftOrder.setMerchantId(merchantId);
		ydGiftOrder.setOutOrderId(outOrderId + "");

		ydGiftOrder.setOrderStatus("WAIT");
		ydGiftOrder.setPayStatus("WAIT");
		ydGiftOrder.setPayTime(null);
		ydGiftOrder.setBillNo(null);
		ydGiftOrder.setTransType("online");

		if (CollectionUtils.isNotEmpty(giftResultList)) {
			ydGiftOrder.setTotalDetailCount(giftResultList.size());
			ydGiftOrder.setTotalGiftCount(giftResultList.stream().mapToInt(YdMerchantGiftResult :: getNum ).sum());
			ydGiftOrder.setTotalSalePrice(giftResultList.stream().mapToDouble(x -> x.getNum() * x.getSalePrice()).sum());
			ydGiftOrder.setTotalMarketPrice(giftResultList.stream().mapToDouble(x -> x.getNum() * x.getMarketPrice()).sum());
		}

		// 自提收货人，地址填写商户的, 配送填写用户的
		if ("ZT".equalsIgnoreCase(receivingWay)) {
			ydGiftOrder.setRealname(ydMerchantResult.getContact());
			ydGiftOrder.setMobile(ydMerchantResult.getMobile());

			ydGiftOrder.setProvince(ydMerchantResult.getProvince());
			ydGiftOrder.setProvinceId(ydMerchantResult.getProvinceId());

			ydGiftOrder.setDistrictId(ydMerchantResult.getDistrictId());
			ydGiftOrder.setDistrict(ydMerchantResult.getDistrict());

			ydGiftOrder.setCityId(ydMerchantResult.getCityId());
			ydGiftOrder.setCity(ydMerchantResult.getCity());

			ydGiftOrder.setAddress(ydMerchantResult.getAddress());
		} else if ("PS".equalsIgnoreCase(receivingWay)) {
			ydGiftOrder.setRealname(ydUserAddressResult.getRealname());
			ydGiftOrder.setMobile(ydUserAddressResult.getMobile());

			ydGiftOrder.setProvince(ydUserAddressResult.getProvince());
			ydGiftOrder.setProvinceId(ydUserAddressResult.getProvinceId());

			ydGiftOrder.setDistrictId(ydUserAddressResult.getDistrictId());
			ydGiftOrder.setDistrict(ydUserAddressResult.getDistrict());

			ydGiftOrder.setCityId(ydUserAddressResult.getCityId());
			ydGiftOrder.setCity(ydUserAddressResult.getCity());

			ydGiftOrder.setAddress(ydUserAddressResult.getAddress());
		}
		ydGiftOrder.setGiftOrderNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.GIFT_OUT));
		this.ydGiftOrderDao.insertYdGiftOrder(ydGiftOrder);

		YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
		BeanUtilExt.copyProperties(ydGiftOrderResult, ydGiftOrder);
		return ydGiftOrderResult;
	}

	/**
	 * 根据订单id查询礼品订单以及礼品订单详情
	 * @param orderId 订单id
	 * @return
	 */
	@Override
	public YdGiftOrderResult getGiftOrderDetail(Integer orderId) throws BusinessException {
		if (orderId == null || orderId <= 0) return null;

		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setOutOrderId(orderId + "");
		List<YdGiftOrder> giftOrderList = ydGiftOrderDao.getAll(ydGiftOrder);
		if (CollectionUtils.isEmpty(giftOrderList)) {
			return null;
		}
		YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
		BeanUtilExt.copyProperties(ydGiftOrderResult, giftOrderList.get(0));

		// 查询礼品订单详情
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(ydGiftOrderResult.getId());
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);

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
				if (ydMerchantGift == null) {
					logger.info("====ydGiftOrderDetailResult=" + JSON.toJSONString(ydGiftOrderDetailResult));
					continue;
				}
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
		ydGiftOrderResult.setPayPrice(payPrice);
		ydGiftOrderResult.setDetailList(detailResultList);
		return ydGiftOrderResult;
	}

	/**
	 * 根据订单id校验礼品订单是否需要充值，总计金额，需要支付金额
	 * @param merchantId
	 * @param orderId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Map<String, Object> checkGiftOrderIsCharge(Integer merchantId, Integer orderId) throws BusinessException {
		// 校验获取门店信息
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("isCharge", "N");
		resultMap.put("totalPrice", 0.00);
		resultMap.put("payPrice", 0.00);

		YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderByOutOrderId(orderId  + "");
		if (ydGiftOrder == null) {
			return resultMap;
		}

		// 查询礼品订单详情
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(ydGiftOrder.getId());
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);
		if (CollectionUtils.isEmpty(detailResultList)) return resultMap;

		Double totalPrice = detailResultList.stream().mapToDouble(YdGiftOrderDetailResult :: getSalePriceTotal).sum();
		// 查询付款金额（付给供应商的钱）
		Double payPrice = detailResultList.stream()
				.filter(data -> data.getSupplierId() != null && data.getSupplierId() > 0)
				.mapToDouble(YdGiftOrderDetailResult :: getSalePriceTotal).sum();

		// 查询未支付给供应商的钱
		Double noPayPrice = detailResultList.stream()
				.filter(data -> data.getOrderStatus().equalsIgnoreCase("WAIT"))
				.mapToDouble(YdGiftOrderDetailResult :: getSalePriceTotal).sum();

		// 查询商户余额
		YdMerchantGiftAccount giftAccount = ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(storeInfo.getId());

		if (giftAccount != null && giftAccount.getBalance() < noPayPrice) {
			resultMap.put("isCharge", "Y");
		}
		resultMap.put("totalPrice", totalPrice);
		resultMap.put("payPrice", payPrice);
		return resultMap;
	}

	/**
	 * 查询订单详情，根据物流单号分组
	 * @param giftOrderId
	 * @return
	 */
	private List<YdGiftOrderDetailResult> getGiftOrderDetailGroupByExpress(Integer giftOrderId) {
		// 查询礼品订单详情
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setGiftOrderId(giftOrderId);
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		List<YdGiftOrderDetailResult> detailResultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);

		detailResultList.forEach(ydGiftOrderDetailResult -> {
			// 查询礼品信息,  如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getGiftId() == null) {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType() != null) {
					if ("platform".equalsIgnoreCase(ydMerchantGift.getGiftType())) {
						YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
						ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
						ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
						ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
						ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					} else {
						ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getGiftDesc());
						ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
						ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
						ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
					}
				}
			} else {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			}
		});
		List<YdGiftOrderDetailResult> result = new ArrayList<>();

		List<YdGiftOrderDetailResult> waitList = detailResultList.stream()
				.filter(ydGiftOrderDetailResult -> ydGiftOrderDetailResult.getExpressOrderId() == null)
				.collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(waitList)) {
			YdGiftOrderDetailResult waitData = new YdGiftOrderDetailResult();
			waitData.setOrderStatus("WAIT_DELIVER");
			waitData.setChildList(waitList);
			result.add(waitData);
		}

		List<YdGiftOrderDetailResult> deliverList = detailResultList.stream()
				.filter(ydGiftOrderDetailResult -> ydGiftOrderDetailResult.getExpressOrderId() != null)
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(deliverList)) {
			Map<String, List<YdGiftOrderDetailResult>> map = deliverList.stream()
					.collect(Collectors.groupingBy(YdGiftOrderDetailResult :: getExpressOrderId));
			for(Map.Entry<String, List<YdGiftOrderDetailResult>> entry : map.entrySet()){
				YdGiftOrderDetailResult ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
				List<YdGiftOrderDetailResult> value = entry.getValue();
				ydGiftOrderDetailResult.setGiftOrderId(value.get(0).getGiftOrderId());
				ydGiftOrderDetailResult.setOrderStatus(value.get(0).getOrderStatus());
				ydGiftOrderDetailResult.setExpressCompany(value.get(0).getExpressCompany());
				ydGiftOrderDetailResult.setExpressCompanyNumber(value.get(0).getExpressCompanyNumber());
				ydGiftOrderDetailResult.setExpressOrderId(value.get(0).getExpressOrderId());
				ydGiftOrderDetailResult.setChildList(value);
				result.add(ydGiftOrderDetailResult);
			}
		}
		return result;
	}

}

