package com.yd.service.bean.rotary;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:优度转盘抽奖奖品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:36:56
 * @Version:1.1.0
 */
public class YdRotaryDrawPrize extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 大转盘抽奖活动id
     */
    private Integer activityId;
    
    /**
     * 奖励类型 YHQ:优惠券
     */
    private String type;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 奖励物品标题
     */
    private String name;
    
    /**
     * 中奖几率
     */
    private Integer winRate;

    /**
     * 优惠券金额
     */
    private Double couponPrice;
    
    /**
     * 排序
     */
    private Integer sort;
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
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Integer getWinRate() {
        return winRate;
    }
    
    public void setWinRate(Integer winRate) {
        this.winRate = winRate;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }
}
