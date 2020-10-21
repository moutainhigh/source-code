package com.yd.api.service.coupon;

import java.util.List;

import com.yd.api.result.cart.YdUserCartResult;
import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户优惠券Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
public interface YdUserCouponService {

	/**
	 * 通过id得到用户优惠券YdUserCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserCouponResult getYdUserCouponById(Integer id);

	/**
	 * 分页查询用户优惠券YdUserCoupon
	 * @param ydUserCouponResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdUserCouponResult> findYdUserCouponListByPage(YdUserCouponResult ydUserCouponResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有用户优惠券YdUserCoupon
	 * @param ydUserCouponResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserCouponResult> getAll(YdUserCouponResult ydUserCouponResult);

	/**
	 * 保存用户优惠券领取记录
	 * @param userId
	 * @throws BusinessException
	 */
	public void insertYdUserCoupon(Integer userId, YdMerchantCouponResult ydMerchantCouponResult) throws BusinessException;

	/**
	 * 通过id修改用户优惠券YdUserCoupon throws BusinessException;
	 * @param ydUserCouponResult
	 * @Description:
	 */
	public void updateYdUserCoupon(YdUserCouponResult ydUserCouponResult) throws BusinessException;

	/**
	 * 根据商品查询用户可用优惠券列表,
	 * 按照优惠金额自大到小排序，同等优惠金额过期快过期的在上面
	 * @param userId	  	用户id
	 * @param merchantId	商户id
	 * @param itemIdList	商品idList
	 * @param skuList	用户购买的商品skuId ， num， 售价 这几个字段有用
	 * @return List<YdUserCouponResult> 可用优惠券集合
	 */
	public List<YdUserCouponResult> findUserCanUseCouponListByItemIds(Integer userId, Integer merchantId, List<Integer> itemIdList, List<YdMerchantItemSkuResult> skuList);

	/**
	 * 校验优惠券是否可用
	 * @param userId	  	用户id
	 * @param couponId	  	优惠券id
	 * @param merchantId	商户id
	 * @param skuTotalPrice	商品总金额
	 * @param skuList	商户商品skuList
	 * @return YdUserCouponResult 优惠券信息
	 */
	public YdUserCouponResult checkCouponIsCanUse(Integer userId, Integer merchantId, Integer couponId, Double skuTotalPrice, List<YdMerchantItemSkuResult> skuList) throws BusinessException;

}
