package com.yd.service.impl.coupon;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.cart.YdUserCartResult;
import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.coupon.YdUserCouponService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.cart.YdUserCart;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.bean.user.YdUser;
import com.yd.service.dao.user.YdUserDao;
import com.yd.service.impl.merchant.YdMerchantServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.coupon.YdUserCouponDao;
import com.yd.service.bean.coupon.YdUserCoupon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:用户优惠券Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserCouponServiceImpl implements YdUserCouponService {

	private static final Logger logger = LoggerFactory.getLogger(YdUserCouponServiceImpl.class);

	@Resource
	private YdUserDao ydUserDao;

	@Resource
	private YdUserCouponDao ydUserCouponDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdUserCouponResult getYdUserCouponById(Integer id) {
		if (id == null || id <= 0) return null;
		YdUserCouponResult ydUserCouponResult = null;
		YdUserCoupon ydUserCoupon = this.ydUserCouponDao.getYdUserCouponById(id);
		if (ydUserCoupon != null) {
			ydUserCouponResult = new YdUserCouponResult();
			BeanUtilExt.copyProperties(ydUserCouponResult, ydUserCoupon);
		}
		return ydUserCouponResult;
	}

	@Override
	public Page<YdUserCouponResult> findYdUserCouponListByPage(YdUserCouponResult params, PagerInfo pagerInfo) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		Page<YdUserCouponResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		BeanUtilExt.copyProperties(ydUserCoupon, params);
		
		int amount = this.ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
		if (amount > 0) {
			List<YdUserCoupon> dataList = this.ydUserCouponDao.findYdUserCouponListByPage(
					ydUserCoupon, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdUserCouponResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdUserCouponResult> getAll(YdUserCouponResult ydUserCouponResult) {
		YdUserCoupon ydUserCoupon = null;
		if (ydUserCouponResult != null) {
			ydUserCoupon = new YdUserCoupon();
			BeanUtilExt.copyProperties(ydUserCoupon, ydUserCouponResult);
		}
		List<YdUserCoupon> dataList = this.ydUserCouponDao.getAll(ydUserCoupon);
		return DTOUtils.convertList(dataList, YdUserCouponResult.class);
	}

	/**
	 * 保存用户优惠券领取记录
	 * @param userId
	 * @param merchantCoupon
	 * @throws BusinessException
	 */
	@Override
	public void insertYdUserCoupon(Integer userId, YdMerchantCouponResult merchantCoupon) throws BusinessException {
		Date nowDate = new Date();
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		ydUserCoupon.setCreateTime(nowDate);
		ydUserCoupon.setUserId(userId);
		ydUserCoupon.setMerchantId(merchantCoupon.getMerchantId());

		YdUser ydUser = ydUserDao.getYdUserById(userId);
		ValidateBusinessUtils.assertFalse(ydUser == null,
				"err_user_not_exit", "用户不存在");
		if (ydUser.getNickname() != null) {
			ydUserCoupon.setNickname(ydUser.getNickname());
		}

		if (ydUser.getMobile() != null) {
			ydUserCoupon.setMobile(ydUser.getMobile());
		}

		// 设置优惠券基本信息
		ydUserCoupon.setCouponId(merchantCoupon.getId());
		ydUserCoupon.setCouponTitle(merchantCoupon.getCouponTitle());
		ydUserCoupon.setCouponType(merchantCoupon.getCouponType());
		ydUserCoupon.setCouponPrice(merchantCoupon.getCouponPrice());
		ydUserCoupon.setUseConditionPrice(merchantCoupon.getUseConditionPrice());
		ydUserCoupon.setCouponDesc(merchantCoupon.getCouponDesc());
		ydUserCoupon.setUseRangeType(merchantCoupon.getUseRangeType());
		ydUserCoupon.setCanUseItemIds(merchantCoupon.getCanUseItemIds());

		// 设置优惠券有效时间
		String validType = merchantCoupon.getValidType();
		logger.info("====validType=" + validType);
		if ("DATE_RANGE".equalsIgnoreCase(validType)) {
			logger.info("====DATE_RANGE");
			ydUserCoupon.setValidStartTime(merchantCoupon.getValidStartTime());
			ydUserCoupon.setValidEndTime(merchantCoupon.getValidEndTime());
		} else if ("TODAY".equalsIgnoreCase(validType)) {
			// 今日有效，结束时间设置为第二天凌晨 00:00:00
			logger.info("====TODAY");
			ydUserCoupon.setValidStartTime(nowDate);
			ydUserCoupon.setValidEndTime(DateUtils.getDayStartDate(DateUtils.addDays(nowDate, 1)));
		} else if ("FIX_DAY".equalsIgnoreCase(validType)) {
			// 领取后几日有效
			logger.info("====validType");
			ydUserCoupon.setValidStartTime(new Date());
			ydUserCoupon.setValidEndTime(DateUtils.addDays(nowDate, merchantCoupon.getValidDay()));
		}

		// 设置优惠券使用状态
		ydUserCoupon.setUseStatus("N");
		ydUserCoupon.setUseTime(null);
		ydUserCoupon.setOutOrderId(null);
		logger.info("====保存优惠券信息的入参=" + JSON.toJSONString(ydUserCoupon));
		this.ydUserCouponDao.insertYdUserCoupon(ydUserCoupon);
	}

	@Override
	public void updateYdUserCoupon(YdUserCouponResult ydUserCouponResult) {
		ydUserCouponResult.setUpdateTime(new Date());
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		BeanUtilExt.copyProperties(ydUserCoupon, ydUserCouponResult);
		this.ydUserCouponDao.updateYdUserCoupon(ydUserCoupon);
	}

	/**
	 * 根据商品查询用户可用优惠券列表,
	 * 按照优惠金额自大到小排序，同等优惠金额过期快过期的在上面
	 * @param userId	  	用户id
	 * @param merchantId	商户id
	 * @param itemIdList	商品idList
	 * @param skuList	用户购买的商品skuId ， num, 售价 这几个字段有用
	 * @return List<YdUserCouponResult> 可用优惠券集合
	 */
	@Override
	public List<YdUserCouponResult> findUserCanUseCouponListByItemIds(Integer userId, Integer merchantId, List<Integer> itemIdList,
																	  List<YdMerchantItemSkuResult> skuList) {
		// 设置查询条件
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		ydUserCoupon.setUserId(userId);
		ydUserCoupon.setMerchantId(merchantId);
		ydUserCoupon.setCouponStatus("CAN_USE");

		Map<Integer, YdUserCouponResult> resultMap = new HashMap<>();
		itemIdList.forEach(itemId -> {
			// 查询单个商品可用优惠券， 存在map中，过滤掉重复
			ydUserCoupon.setCanUseItemIds(String.valueOf(itemId));
			List<YdUserCoupon> couponList = this.ydUserCouponDao.findYdUserCouponListByPage(ydUserCoupon, 0, Integer.MAX_VALUE);
			if (CollectionUtils.isNotEmpty(couponList)) {
				couponList.forEach(userCoupon -> {
					YdUserCouponResult couponResult = new YdUserCouponResult();
					BeanUtilExt.copyProperties(couponResult, userCoupon);
					resultMap.put(userCoupon.getId(), couponResult);
				});
			}
		});

		// 从map中获取优惠券，返回结果
		List<YdUserCouponResult> couponList = new ArrayList<>();
		for(Map.Entry<Integer, YdUserCouponResult> entry : resultMap.entrySet()){
			YdUserCouponResult couponResult = entry.getValue();
			couponList.add(couponResult);
		}

		// 根据具体规格，数量，计算优惠券是否可用，不可用的去掉
		List<YdUserCouponResult> canUseCouponList = new ArrayList<>();
		for (YdUserCouponResult couponResult : couponList) {
			String couponType = couponResult.getCouponType();
			String useRangeType = couponResult.getUseRangeType();
			Double totalPrice = 0.00;
			if ("ZJ".equalsIgnoreCase(couponType)) {
				canUseCouponList.add(couponResult);
				continue;
			} else if ("MJ".equalsIgnoreCase(couponType)){
				// 计算优惠券可用商品总金额
				if ("ITEM".equalsIgnoreCase(useRangeType)) {
					String canUseItemIds = couponResult.getCanUseItemIds();
					for (YdMerchantItemSkuResult ydMerchantItemSkuResult : skuList) {
						if (canUseItemIds.contains(ydMerchantItemSkuResult.getMerchantItemId() + "")) {
							totalPrice += ydMerchantItemSkuResult.getNum() * ydMerchantItemSkuResult.getSalePrice();
						}
					}
				} else if ("ALL".equalsIgnoreCase(useRangeType)) {
					for (YdMerchantItemSkuResult ydMerchantItemSkuResult : skuList) {
						totalPrice += ydMerchantItemSkuResult.getNum() * ydMerchantItemSkuResult.getSalePrice();
					}
				}

				// 根据优惠券使用规格判断是否可以使用
				Double useConditionPrice = couponResult.getUseConditionPrice();
				if (useConditionPrice <= totalPrice) {
					canUseCouponList.add(couponResult);
				}
			}
		}

		// 按照优惠金额自大到小排序，同等优惠金额过期快过期的在上面
		List<YdUserCouponResult> resultList = canUseCouponList.stream().sorted(
				Comparator.comparing(YdUserCouponResult :: getCouponPrice).reversed()
						.thenComparing(YdUserCouponResult :: getValidEndTime))
				.collect(Collectors.toList());
		return resultList;
	}

	/**
	 * 校验优惠券是否可用
	 * @param userId	  	用户id
	 * @param merchantId	商户id
	 * @param couponId	  	优惠券id
	 * @param skuTotalPrice	商品总金额
	 * @param skuList		商品skuList
	 * @return
	 */
	@Override
	public YdUserCouponResult checkCouponIsCanUse(Integer userId, Integer merchantId, Integer couponId, Double skuTotalPrice,
												  List<YdMerchantItemSkuResult> skuList) throws BusinessException {
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		ydUserCoupon.setId(couponId);
		ydUserCoupon.setUserId(userId);
		ydUserCoupon.setMerchantId(merchantId);
		List<YdUserCoupon> couponList = this.ydUserCouponDao.getAll(ydUserCoupon);
		ValidateBusinessUtils.assertNonNull(couponList,"err_coupon_not_exit", "优惠券不存在");

		ydUserCoupon = couponList.get(0);
		YdUserCouponResult couponResult = new YdUserCouponResult();
		BeanUtilExt.copyProperties(couponResult, ydUserCoupon);

		String couponType = couponResult.getCouponType();
		// 判断优惠券类型
		if ("ZJ".equalsIgnoreCase(couponType)) {
			return couponResult;
		} else if ("MJ".equalsIgnoreCase(couponType)){
			String useRangeType = couponResult.getUseRangeType();
			Double useConditionPrice = couponResult.getUseConditionPrice();

			// 判断是全商品可用还是指定商品可用, 计算可使用总金额
			if ("ALL".equalsIgnoreCase(useRangeType)) {
				if (useConditionPrice > skuTotalPrice) {
					return null;
				} else {
					return couponResult;
				}
			} else if ("ITEM".equalsIgnoreCase(useRangeType)) {

				String canUseItemIds = couponResult.getCanUseItemIds();
				if (useConditionPrice > getCouponCanUsePrice(canUseItemIds, skuList).doubleValue() ) {
					return null;
				} else {
					return couponResult;
				}
			}
		}
		return couponResult;
	}

	/**
	 * 根据商品id查询优惠券满足条件的总金额
	 * @param canUseItemIds
	 * @param skuList
	 * @return
	 */
	private BigDecimal getCouponCanUsePrice(String canUseItemIds, List<YdMerchantItemSkuResult> skuList) {
		BigDecimal totalMonty = new BigDecimal(0.00);
		for (YdMerchantItemSkuResult ydMerchantItemSkuResult : skuList) {
			if (canUseItemIds.contains(ydMerchantItemSkuResult.getMerchantItemId() + "")) {
				totalMonty = new BigDecimal(ydMerchantItemSkuResult.getSalePrice() + "")
						.multiply(new BigDecimal(ydMerchantItemSkuResult.getNum()))
						.add(totalMonty)
						.setScale(2, BigDecimal.ROUND_UP);
			}
		}
		return totalMonty;
	}

}

