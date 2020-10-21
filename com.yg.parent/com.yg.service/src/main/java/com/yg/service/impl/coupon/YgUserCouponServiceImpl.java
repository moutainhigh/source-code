package com.yg.service.impl.coupon;

import com.alibaba.fastjson.JSON;
import com.yg.api.result.coupon.YgMerchantCouponResult;
import com.yg.api.result.coupon.YgUserCouponResult;
import com.yg.api.service.coupon.YgUserCouponService;
import com.yg.api.service.merchant.YgMerchantService;
import com.yg.core.utils.*;
import com.yg.service.bean.coupon.YgUserCoupon;
import com.yg.service.dao.coupon.YgUserCouponDao;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.util.*;

/**
 * @Title:用户优惠券Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YgUserCouponServiceImpl implements YgUserCouponService {

	private static final Logger logger = LoggerFactory.getLogger(YgUserCouponServiceImpl.class);

	@Resource
	private YgUserCouponDao ygUserCouponDao;

	@Resource
	private YgMerchantService ygMerchantService;

	@Override
	public YgUserCouponResult getYdUserCouponById(Integer id) {
		if (id == null || id <= 0) return null;
		YgUserCouponResult ygUserCouponResult = null;
		YgUserCoupon ydUserCoupon = this.ygUserCouponDao.getYdUserCouponById(id);
		if (ydUserCoupon != null) {
			ygUserCouponResult = new YgUserCouponResult();
			BeanUtilExt.copyProperties(ygUserCouponResult, ydUserCoupon);
		}
		return ygUserCouponResult;
	}

	@Override
	public Page<YgUserCouponResult> findYdUserCouponListByPage(YgUserCouponResult params, PagerInfo pagerInfo) throws BusinessException {

		Page<YgUserCouponResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YgUserCoupon ydUserCoupon = new YgUserCoupon();
		BeanUtilExt.copyProperties(ydUserCoupon, params);
		
		int amount = this.ygUserCouponDao.getYdUserCouponCount(ydUserCoupon);
		if (amount > 0) {
			List<YgUserCoupon> dataList = this.ygUserCouponDao.findYdUserCouponListByPage(
					ydUserCoupon, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YgUserCouponResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YgUserCouponResult> getAll(YgUserCouponResult ygUserCouponResult) {
		YgUserCoupon ydUserCoupon = null;
		if (ygUserCouponResult != null) {
			ydUserCoupon = new YgUserCoupon();
			BeanUtilExt.copyProperties(ydUserCoupon, ygUserCouponResult);
		}
		List<YgUserCoupon> dataList = this.ygUserCouponDao.getAll(ydUserCoupon);
		return DTOUtils.convertList(dataList, YgUserCouponResult.class);
	}

	/**
	 * 保存用户优惠券领取记录
	 * @param userId
	 * @param merchantCoupon
	 * @throws BusinessException
	 */
	@Override
	public void insertYgUserCoupon(Integer userId, YgMerchantCouponResult merchantCoupon) throws BusinessException {
		Date nowDate = new Date();
		YgUserCoupon ygUserCoupon = new YgUserCoupon();
		ygUserCoupon.setCreateTime(nowDate);
		ygUserCoupon.setUserId(userId);
		ygUserCoupon.setMerchantId(merchantCoupon.getMerchantId());


		// 设置优惠券基本信息
		ygUserCoupon.setCouponId(merchantCoupon.getId());
		ygUserCoupon.setCouponTitle(merchantCoupon.getCouponTitle());
		ygUserCoupon.setCouponType(merchantCoupon.getCouponType());
		ygUserCoupon.setCouponPrice(merchantCoupon.getCouponPrice());
		ygUserCoupon.setUseConditionPrice(merchantCoupon.getUseConditionPrice());
		ygUserCoupon.setCouponDesc(merchantCoupon.getCouponDesc());
		ygUserCoupon.setUseRangeType(merchantCoupon.getUseRangeType());
		ygUserCoupon.setCanUseItemIds(merchantCoupon.getCanUseItemIds());

		// 设置优惠券有效时间
		ygUserCoupon.setValidStartTime(merchantCoupon.getValidStartTime());
		ygUserCoupon.setValidEndTime(merchantCoupon.getValidEndTime());

		// 设置优惠券使用状态
		ygUserCoupon.setUseStatus("N");
		ygUserCoupon.setUseTime(null);
		ygUserCoupon.setOutOrderId(null);
		logger.info("====保存优惠券信息的入参=" + JSON.toJSONString(ygUserCoupon));
		this.ygUserCouponDao.insertYdUserCoupon(ygUserCoupon);
	}

	@Override
	public void updateYdUserCoupon(YgUserCouponResult ygUserCouponResult) {
		ygUserCouponResult.setUpdateTime(new Date());
		YgUserCoupon ydUserCoupon = new YgUserCoupon();
		BeanUtilExt.copyProperties(ydUserCoupon, ygUserCouponResult);
		this.ygUserCouponDao.updateYdUserCoupon(ydUserCoupon);
	}

}

