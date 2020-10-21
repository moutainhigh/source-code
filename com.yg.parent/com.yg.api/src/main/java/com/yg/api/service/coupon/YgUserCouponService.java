package com.yg.api.service.coupon;

import com.yg.api.result.coupon.YgMerchantCouponResult;
import com.yg.api.result.coupon.YgUserCouponResult;
import com.yg.api.result.coupon.YgUserCouponResult;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;

import java.util.List;

/**
 * @Title:用户优惠券Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
public interface YgUserCouponService {

	/**
	 * 通过id得到用户优惠券YdUserCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgUserCouponResult getYdUserCouponById(Integer id);

	/**
	 * 分页查询用户优惠券YdUserCoupon
	 * @param ydUserCouponResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YgUserCouponResult> findYdUserCouponListByPage(YgUserCouponResult ydUserCouponResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有用户优惠券YdUserCoupon
	 * @param ydUserCouponResult
	 * @return 
	 * @Description:
	 */
	public List<YgUserCouponResult> getAll(YgUserCouponResult ydUserCouponResult);

	/**
	 * 保存用户优惠券领取记录
	 * @param userId
	 * @throws BusinessException
	 */
	public void insertYgUserCoupon(Integer userId, YgMerchantCouponResult ygMerchantCouponResult) throws BusinessException;

	/**
	 * 通过id修改用户优惠券YdUserCoupon throws BusinessException;
	 * @param ydUserCouponResult
	 * @Description:
	 */
	public void updateYdUserCoupon(YgUserCouponResult ydUserCouponResult) throws BusinessException;

}
