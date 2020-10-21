package com.yd.service.dao.coupon;

import java.util.List;
import com.yd.service.bean.coupon.YdMerchantCoupon;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户优惠券Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-26 11:40:33
 * @Version:1.1.0
 */
public interface YdMerchantCouponDao {

	/**
	 * 通过id得到商户优惠券YdMerchantCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantCoupon getYdMerchantCouponById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantCouponCount(YdMerchantCoupon ydMerchantCoupon);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCoupon> findYdMerchantCouponListByPage(@Param("params") YdMerchantCoupon ydMerchantCoupon,
                                                                 @Param("pageStart") Integer pageStart,
                                                                 @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有商户优惠券YdMerchantCoupon
	 * @param ydMerchantCoupon
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantCoupon> getAll(YdMerchantCoupon ydMerchantCoupon);

	/**
	 * 添加商户优惠券YdMerchantCoupon
	 * @param ydMerchantCoupon
	 * @Description:
	 */
	public void insertYdMerchantCoupon(YdMerchantCoupon ydMerchantCoupon);
	
	/**
	 * 通过id修改商户优惠券YdMerchantCoupon
	 * @param ydMerchantCoupon
	 * @Description:
	 */
	public void updateYdMerchantCoupon(YdMerchantCoupon ydMerchantCoupon);

	/**
	 * 商户优惠券增加发行量
	 * @param couponId	优惠券id
	 * @param num		增加的数量
	 */
    void addCouponAmount(@Param("couponId") Integer couponId, @Param("num") Integer num);

	/**
	 * 查询商户可用优惠券列表
	 * @param merchantId 商户id
	 * @return
	 */
	List<YdMerchantCoupon> findFrontMerchantCouponList(@Param("merchantId") Integer merchantId,
													   @Param("itemId") Integer itemId);

    void deleteMerchantCoupon(Integer couponId);
}
