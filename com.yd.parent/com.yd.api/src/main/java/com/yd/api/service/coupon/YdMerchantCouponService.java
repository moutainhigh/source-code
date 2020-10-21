package com.yd.api.service.coupon;

import java.util.List;
import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户优惠券Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-26 11:40:33
 * @Version:1.1.0
 */
public interface YdMerchantCouponService {

	/**
	 * 通过id得到商户优惠券YdMerchantCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantCouponResult getYdMerchantCouponById(Integer id);

	/**
	 * 分页查询商户优惠券YdMerchantCoupon
	 * @param ydMerchantCouponResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantCouponResult> findYdMerchantCouponListByPage(YdMerchantCouponResult ydMerchantCouponResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户优惠券YdMerchantCoupon
	 * @param ydMerchantCouponResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCouponResult> getAll(YdMerchantCouponResult ydMerchantCouponResult);

	/**
	 * 添加商户优惠券YdMerchantCoupon
	 * @param ydMerchantCouponResult
	 * @Description:
	 */
	public void insertYdMerchantCoupon(YdMerchantCouponResult ydMerchantCouponResult) throws BusinessException;

	/**
	 * 通过id修改商户优惠券YdMerchantCoupon throws BusinessException;
	 * @param ydMerchantCouponResult
	 * @Description:
	 */
	public void updateYdMerchantCoupon(YdMerchantCouponResult ydMerchantCouponResult) throws BusinessException;

	/**
	 * 商户优惠券增加发行量
	 * @param merchantId	商户id
	 * @param couponId		优惠券id
	 * @param num			增加的优惠券数量
	 */
    void addCouponAmount(Integer merchantId, Integer couponId, Integer num) throws BusinessException;

	/**
	 * 查询商户可用优惠券列表
	 * @param itemId		商品id
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @return
	 */
    List<YdMerchantCouponResult> findFrontMerchantCouponList(Integer itemId, Integer userId, Integer merchantId) throws BusinessException;

	/**
	 * 用户领取优惠券
	 * @param couponId		用户id
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @return
	 */
	void receiveCoupon(Integer couponId, Integer userId, Integer merchantId) throws BusinessException;

	/**
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
