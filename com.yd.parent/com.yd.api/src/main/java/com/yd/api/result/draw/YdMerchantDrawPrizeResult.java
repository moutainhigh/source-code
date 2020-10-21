package com.yd.api.result.draw;
import com.yd.core.base.bean.BaseModel;
import java.io.Serializable;

/**
 * @Title:商户抽奖活动奖品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:34:22
 * @Version:1.1.0
 */
public class YdMerchantDrawPrizeResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 抽奖活动id
     */
    private Integer activityId;
    
    /**
     * 奖励类型 YHQ:优惠券 WZJ: 未中奖
     */
    private String prizeType;
    
    /**
     * 优惠id
     */
    private Integer couponId;
    
    /**
     * 中奖几率
     */
    private Double winRate;
    
    /**
     * 排序
     */
    private Integer sort;

    private String couponTitle;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    public String getPrizeType() {
        return prizeType;
    }
    
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
    public Integer getCouponId() {
        return couponId;
    }
    
    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }
    public Double getWinRate() {
        return winRate;
    }
    
    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }
}
