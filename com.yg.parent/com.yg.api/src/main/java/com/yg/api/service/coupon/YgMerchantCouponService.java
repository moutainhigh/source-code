package com.yg.api.service.coupon;

import java.util.List;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;
import com.yg.api.result.coupon.YgMerchantCouponResult;


/**
 * @Title:商户优惠券Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-26 14:17:08
 * @Version:1.1.0
 */
public interface YgMerchantCouponService {

	/**
	 * 通过id得到商户优惠券YgMerchantCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchantCouponResult getYgMerchantCouponById(Integer id);

	/**
	 * 分页查询商户优惠券YgMerchantCoupon
	 * @param ygMerchantCouponResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YgMerchantCouponResult> findYgMerchantCouponListByPage(YgMerchantCouponResult ygMerchantCouponResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户优惠券YgMerchantCoupon
	 * @param ygMerchantCouponResult
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantCouponResult> getAll(YgMerchantCouponResult ygMerchantCouponResult);

	/**
	 * 添加商户优惠券YgMerchantCoupon
	 * @param ygMerchantCouponResult
	 * @Description:
	 */
	public void insertYgMerchantCoupon(YgMerchantCouponResult ygMerchantCouponResult) throws BusinessException;

	/**
	 * 通过id修改商户优惠券YgMerchantCoupon throws BusinessException;
	 * @param ygMerchantCouponResult
	 * @Description:
	 */
	public void updateYgMerchantCoupon(YgMerchantCouponResult ygMerchantCouponResult)throws BusinessException;

	/**
	 * 用户领取优惠券
	 * @param couponId		用户id
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @return
	 */
	void receiveCoupon(Integer couponId, Integer userId, Integer merchantId) throws BusinessException;

	/**
	 * 删除优惠券
	 * @param merchantId 商户id
	 * @param couponId	 优惠券id
	 * @throws BusinessException
	 */
	void deleteMerchantCoupon(Integer merchantId, Integer couponId) throws BusinessException;

	/**
	 * 商户上下架优惠券
	 * @param merchantId 商户id
	 * @param couponId	 优惠券id
	 * @param isShelf	 Y: 上架, N: 下架
	 * @throws BusinessException
	 */
	void upOrDownMerchantCoupon(Integer merchantId, Integer couponId, String isShelf) throws BusinessException;
	
}
