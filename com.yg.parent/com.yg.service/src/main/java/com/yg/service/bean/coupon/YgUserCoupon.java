package com.yg.service.bean.coupon;

import com.yg.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title:用户优惠券
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
public class YgUserCoupon extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer userId;
    
    private Integer merchantId;

    private Integer couponId;

    /**
     * 优惠券标题
     */
    private String couponTitle;

    /**
     * 优惠券类型 MJ | ZJ
     */
    private String couponType;

    /**
     * 优惠券面值，必须是整数
     */
    private Integer couponPrice;

    /**
     * 优惠券描述
     */
    private String couponDesc;

    /**
     * 使用条件: 0代表无门槛使用, 其余数字代表到指定数字才可以使用
     */
    private Double useConditionPrice;

    /**
     * 使用范围类型: 全场通用, 指定商品
     */
    private String useRangeType;

    /**
     * 指定可使用商品ids
     */
    private String canUseItemIds;

    private Date validStartTime;
    
    private Date validEndTime;

    private String nickname;

    private String mobile;
    
    /**
     * Y|N|IN
     */
    private String useStatus;
    
    private Date useTime;
    
    /**
     * 订单编号
     */
    private String outOrderId;

    //--------------------------- 附加 ------------------------------

    private String couponStatus;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public Integer getCouponId() {
        return couponId;
    }
    
    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }
    public Date getValidStartTime() {
        return validStartTime;
    }
    
    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }
    public Date getValidEndTime() {
        return validEndTime;
    }
    
    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }
    public Date getUseTime() {
        return useTime;
    }
    
    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }
    public String getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public Integer getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Integer couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public Double getUseConditionPrice() {
        return useConditionPrice;
    }

    public void setUseConditionPrice(Double useConditionPrice) {
        this.useConditionPrice = useConditionPrice;
    }

    public String getUseRangeType() {
        return useRangeType;
    }

    public void setUseRangeType(String useRangeType) {
        this.useRangeType = useRangeType;
    }

    public String getCanUseItemIds() {
        return canUseItemIds;
    }

    public void setCanUseItemIds(String canUseItemIds) {
        this.canUseItemIds = canUseItemIds;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }
}
