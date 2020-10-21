package com.yd.service.dao.coupon;

import java.util.List;
import com.yd.service.bean.coupon.YdUserCoupon;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户优惠券Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
public interface YdUserCouponDao {

	/**
	 * 通过id得到用户优惠券YdUserCoupon
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserCoupon getYdUserCouponById(Integer id);

	/**
	 * 通过outOrderId得到用户优惠券YdUserCoupon
	 * @param outOrderId
	 * @return
	 * @Description:
	 */
	public YdUserCoupon getYdUserCouponByOutOrderId(@Param("outOrderId") String outOrderId);
	
	/**
	 * 获取数量
	 * @param ydUserCoupon
	 * @return 
	 * @Description:
	 */
	public int getYdUserCouponCount(YdUserCoupon ydUserCoupon);

	/**
	 * 分页获取数据
	 * @param ydUserCoupon
	 * @return 
	 * @Description:
	 */
	public List<YdUserCoupon> findYdUserCouponListByPage(@Param("params") YdUserCoupon ydUserCoupon,
                                                         @Param("pageStart") Integer pageStart,
                                                         @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有用户优惠券YdUserCoupon
	 * @param ydUserCoupon
	 * @return 
	 * @Description:
	 */
	public List<YdUserCoupon> getAll(YdUserCoupon ydUserCoupon);

	/**
	 * 添加用户优惠券YdUserCoupon
	 * @param ydUserCoupon
	 * @Description:
	 */
	public void insertYdUserCoupon(YdUserCoupon ydUserCoupon);

	/**
	 * 通过id修改用户优惠券YdUserCoupon
	 * @param ydUserCoupon
	 * @Description:
	 */
	public void updateYdUserCoupon(YdUserCoupon ydUserCoupon);

	/**
	 * 取消订单退还用户优惠券
	 * @param userId		用户id
	 * @param userCouponId	用户优惠券id
	 */
    void refundCoupon(Integer userId, Integer userCouponId);

	/**
	 * 修改优惠券为使用中
	 * @param userId
	 * @param merchantId
	 * @param couponId
	 */
	void updateCouponStatusInUse(@Param("userId") Integer userId, @Param("merchantId") Integer merchantId,
								 @Param("couponId") Integer couponId, @Param("outOrderId") String outOrderId);
}
