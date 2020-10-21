package com.yg.api.result.coupon;

import java.io.Serializable;
import com.yg.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商户优惠券
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-26 14:17:08
 * @Version:1.1.0
 */
public class YgMerchantCouponResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private Integer merchantId;
    
    /** 优惠券标题
     */
    private String couponTitle;
    
    /** 优惠券类型 MJ | ZJ
     */
    private String couponType;
    
    /** 优惠券发行量 0代表无限量
     */
    private Integer couponAmount;
    
    /** 优惠券面值，必须是整数
     */
    private Integer couponPrice;
    
    /** 优惠券描述
     */
    private String couponDesc;
    
    /** 使用条件: 0代表无门槛使用, 其余数字代表到指定数字才可以使用
     */
    private Double useConditionPrice;
    
    /** 
     */
    private Date validStartTime;
    
    private Date validEndTime;
    
    /** 使用范围类型: ALL(全场通用), (ITEM)指定商品
     */
    private String useRangeType;
    
    /** 指定可使用商品ids
     */
    private String canUseItemIds;
    
    /** 是否上架 Y|N
     */
    private String isShelf;
    
    private String remark;

    private String startTime;

    private String endTime;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
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
    public Integer getCouponAmount() {
        return couponAmount;
    }
    
    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
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
    public String getIsShelf() {
        return isShelf;
    }
    
    public void setIsShelf(String isShelf) {
        this.isShelf = isShelf;
    }
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
