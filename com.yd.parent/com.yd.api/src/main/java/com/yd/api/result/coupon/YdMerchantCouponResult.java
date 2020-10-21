package com.yd.api.result.coupon;

import java.io.Serializable;

import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.core.base.bean.BaseModel;
import java.util.Date;
import java.util.List;

/**
 * @Title:商户优惠券
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-26 11:40:33
 * @Version:1.1.0
 */
public class YdMerchantCouponResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    /**
     * 优惠券标题
     */
    private String couponTitle;
    
    /**
     * 优惠券类型 MJ | ZJ
     */
    private String couponType;
    
    /**
     * 优惠券发行量 0代表无限量
     */
    private Integer couponAmount;
    
    /**
     * 每人领取数量限制
     */
    private Integer limitAmount;
    
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
     * 有效日期类型: 日期范围, 领取当天, 固定天数
     */
    private String validType;
    
    private Date validStartTime;
    
    private Date validEndTime;
    
    /**
     * 领取后有效天数
     */
    private Integer validDay;
    
    /**
     * 使用范围类型: 全场通用, 指定商品
     */
    private String useRangeType;
    
    /**
     * 指定可使用商品ids
     */
    private String canUseItemIds;
    
    /**
     * 是否上架 Y|N
     */
    private String isShelf;
    
    /**
     * 适用场景 : 直接领用， 抽奖活动专用
     */
    private String couponResource;
    
    private String remark;

    // --------------------------------- 附加

    private String startTime;

    private String endTime;

    // 用户领取数量
    private Integer receiveCount;

    // 用户使用数量
    private Integer useCount;

    // 用户未使用使用数量
    private Integer noUseCount;

    // 用户使用中数量
    private Integer inUseCount;


    private List<YdMerchantItemResult> itemList;

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
    public Integer getLimitAmount() {
        return limitAmount;
    }
    
    public void setLimitAmount(Integer limitAmount) {
        this.limitAmount = limitAmount;
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
    public String getValidType() {
        return validType;
    }
    
    public void setValidType(String validType) {
        this.validType = validType;
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

    public Integer getValidDay() {
        return validDay;
    }
    
    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
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
    public String getCouponResource() {
        return couponResource;
    }
    
    public void setCouponResource(String couponResource) {
        this.couponResource = couponResource;
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

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Integer getNoUseCount() {
        return noUseCount;
    }

    public void setNoUseCount(Integer noUseCount) {
        this.noUseCount = noUseCount;
    }

    public List<YdMerchantItemResult> getItemList() {
        return itemList;
    }

    public void setItemList(List<YdMerchantItemResult> itemList) {
        this.itemList = itemList;
    }

    public Integer getInUseCount() {
        return inUseCount;
    }

    public void setInUseCount(Integer inUseCount) {
        this.inUseCount = inUseCount;
    }
}
