package com.yg.service.impl.coupon;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yg.api.result.coupon.YgMerchantCouponResult;
import com.yg.api.result.merchant.YgMerchantResult;
import com.yg.api.service.coupon.YgMerchantCouponService;
import com.yg.api.service.coupon.YgUserCouponService;
import com.yg.core.utils.BeanUtilExt;
import com.yg.core.utils.*;
import com.yg.api.service.coupon.YgMerchantCouponService;
import com.yg.service.bean.coupon.YgUserCoupon;
import com.yg.service.dao.coupon.YgUserCouponDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yg.service.dao.coupon.YgMerchantCouponDao;
import com.yg.service.bean.coupon.YgMerchantCoupon;

/**
 * @Title:商户优惠券Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-26 14:17:08
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YgMerchantCouponServiceImpl implements YgMerchantCouponService {

	@Resource
	private YgUserCouponDao ygUserCouponDao;

	@Resource
	private YgMerchantCouponDao ygMerchantCouponDao;

	@Resource
	private YgUserCouponService ygUserCouponService;

	@Override
	public YgMerchantCouponResult getYgMerchantCouponById(Integer id) {
		if (id == null || id <= 0) return null;
		YgMerchantCouponResult ygMerchantCouponResult = null;
		YgMerchantCoupon ygMerchantCoupon = this.ygMerchantCouponDao.getYgMerchantCouponById(id);
		if (ygMerchantCoupon != null) {
			ygMerchantCouponResult = new YgMerchantCouponResult();
			BeanUtilExt.copyProperties(ygMerchantCouponResult, ygMerchantCoupon);
		}	
		return ygMerchantCouponResult;
	}

	@Override
	public Page<YgMerchantCouponResult> findYgMerchantCouponListByPage(YgMerchantCouponResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YgMerchantCouponResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YgMerchantCoupon ygMerchantCoupon = new YgMerchantCoupon();
		BeanUtilExt.copyProperties(ygMerchantCoupon, params);
		
		int amount = this.ygMerchantCouponDao.getYgMerchantCouponCount(ygMerchantCoupon);
		if (amount > 0) {
			List<YgMerchantCoupon> dataList = this.ygMerchantCouponDao.findYgMerchantCouponListByPage(
				ygMerchantCoupon, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YgMerchantCouponResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YgMerchantCouponResult> getAll(YgMerchantCouponResult ygMerchantCouponResult) {
		YgMerchantCoupon ygMerchantCoupon = null;
		if (ygMerchantCouponResult != null) {
			ygMerchantCoupon = new YgMerchantCoupon();
			BeanUtilExt.copyProperties(ygMerchantCoupon, ygMerchantCouponResult);
		}
		List<YgMerchantCoupon> dataList = this.ygMerchantCouponDao.getAll(ygMerchantCoupon);
		return DTOUtils.convertList(dataList, YgMerchantCouponResult.class);
	}

	@Override
	public void insertYgMerchantCoupon(YgMerchantCouponResult ygMerchantCouponResult) {
		if (null != ygMerchantCouponResult) {
			ygMerchantCouponResult.setCreateTime(new Date());
			ygMerchantCouponResult.setUpdateTime(new Date());
			YgMerchantCoupon ygMerchantCoupon = new YgMerchantCoupon();
			BeanUtilExt.copyProperties(ygMerchantCoupon, ygMerchantCouponResult);
			this.ygMerchantCouponDao.insertYgMerchantCoupon(ygMerchantCoupon);
		}
	}
	
	@Override
	public void updateYgMerchantCoupon(YgMerchantCouponResult ygMerchantCouponResult) {
		// checkCouponParams();

		if (null != ygMerchantCouponResult) {
			ygMerchantCouponResult.setUpdateTime(new Date());
			YgMerchantCoupon ygMerchantCoupon = new YgMerchantCoupon();
			BeanUtilExt.copyProperties(ygMerchantCoupon, ygMerchantCouponResult);
			this.ygMerchantCouponDao.updateYgMerchantCoupon(ygMerchantCoupon);
		}
	}

	@Override
	public void receiveCoupon(Integer couponId, Integer userId, Integer merchantId) throws BusinessException {
		// 判断优惠券是否存在
		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		YgMerchantCoupon merchantCoupon = ygMerchantCouponDao.getYgMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(merchantCoupon.getIsShelf()),
				"err_is_shelf", "优惠券未上架");

		long startTime = merchantCoupon.getValidStartTime().getTime();
		long endTime = merchantCoupon.getValidEndTime().getTime();
		long nowTime = new Date().getTime();
		ValidateBusinessUtils.assertFalse(startTime > nowTime || endTime < nowTime,
				"err_date_range", "优惠券不可领取");

		// 校验发行量
		YgUserCoupon ygUserCoupon = new YgUserCoupon();
		ygUserCoupon.setCouponId(couponId);
		ygUserCouponDao.getYdUserCouponCount(ygUserCoupon);
		int sendCount = ygUserCouponDao.getYdUserCouponCount(ygUserCoupon);
		if (merchantCoupon.getCouponAmount() != 0) {
			ValidateBusinessUtils.assertFalse(merchantCoupon.getCouponAmount() <= sendCount,
					"err_more_limit", "非常抱歉，优惠券已经被领完，您不可以领取");
		}

		// 保存领取记录
		YgMerchantCouponResult ydMerchantCouponResult = new YgMerchantCouponResult();
		BeanUtilExt.copyProperties(ydMerchantCouponResult, merchantCoupon);
		this.ygUserCouponService.insertYgUserCoupon(userId, ydMerchantCouponResult);
	}

	@Override
	public void deleteMerchantCoupon(Integer merchantId, Integer couponId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		YgMerchantCoupon merchantCoupon = ygMerchantCouponDao.getYgMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		ygMerchantCouponDao.deleteMerchantCoupon(couponId);
	}

	@Override
	public void upOrDownMerchantCoupon(Integer merchantId, Integer couponId, String isShelf) throws BusinessException {
		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(isShelf),
				"err_is_enable_empty", "上下架状态不可以为空");

		YgMerchantCoupon merchantCoupon = ygMerchantCouponDao.getYgMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		merchantCoupon.setIsShelf(isShelf);
		ygMerchantCouponDao.updateYgMerchantCoupon(merchantCoupon);
	}

	//-------------------------------------   private method   ------------------------------------------
	private void checkCouponParams(YgMerchantCouponResult params) throws BusinessException {

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCouponTitle()),
				"err_empty_coupon_title", "优惠券标题不可以为空");

		ValidateBusinessUtils.assertFalse(params.getCouponAmount() == null || params.getCouponAmount() < 0,
				"err_empty_coupon_amount", "发行量不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCouponType()),
				"err_empty_coupon_type", "优惠券类型不可以为空");

		ValidateBusinessUtils.assertFalse(params.getCouponPrice() == null || params.getCouponPrice() <= 0,
				"err_empty_coupon_price", "优惠券面值不可以为空");

		ValidateBusinessUtils.assertFalse(params.getUseConditionPrice() == null,
				"err_empty_coupon_use_condition", "优惠券使用条件不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getUseRangeType()),
				"err_empty_range_type", "优惠券适用商品不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getIsShelf()),
				"err_empty_is_shelf", "是否上架不可以为空");


		params.setValidStartTime(DateUtils.getDateFromStr(params.getStartTime()));
		params.setValidEndTime(DateUtils.getDateFromStr(params.getEndTime()));

		String useRangeType = params.getUseRangeType();
		if ("ALL".equalsIgnoreCase(useRangeType)) {
			params.setCanUseItemIds(null);
		} else if ("ITEM".equalsIgnoreCase(useRangeType)) {
			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCanUseItemIds()),
					"err_empty_use_items", "请选择可使用的商品");
		}
	}

}

