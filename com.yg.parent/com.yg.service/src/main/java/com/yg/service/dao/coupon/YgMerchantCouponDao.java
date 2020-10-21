package com.yg.service.dao.coupon;

import com.yg.service.bean.coupon.YgMerchantCoupon;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Title:商户优惠券Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-26 11:40:33
 * @Version:1.1.0
 */
public interface YgMerchantCouponDao {

	/**
	 * 通过id得到商户优惠券
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchantCoupon getYgMerchantCouponById(Integer id);
	
	/**
	 * 获取数量
	 * @param ygMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public int getYgMerchantCouponCount(YgMerchantCoupon ygMerchantCoupon);
	
	/**
	 * 分页获取数据
	 * @param ygMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantCoupon> findYgMerchantCouponListByPage(@Param("params") YgMerchantCoupon ygMerchantCoupon,
                                                                 @Param("pageStart") Integer pageStart,
                                                                 @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有商户优惠券
	 * @param ygMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public List<YgMerchantCoupon> getAll(YgMerchantCoupon ygMerchantCoupon);

	/**
	 * 添加商户优惠券
	 * @param ygMerchantCoupon
	 * @Description:
	 */
	public void insertYgMerchantCoupon(YgMerchantCoupon ygMerchantCoupon);
	
	/**
	 * 通过id修改商户优惠券
	 * @param ygMerchantCoupon
	 * @Description:
	 */
	public void updateYgMerchantCoupon(YgMerchantCoupon ygMerchantCoupon);


    void deleteMerchantCoupon(Integer couponId);
}
