package com.yd.api.result.rotary;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

import java.util.Date;

/**
 * @Title:转盘抽奖记录
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:47:52
 * @Version:1.1.0
 */
public class YdRotaryDrawRecordResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 
     */
    private Integer merchantId;
    
    /**
     * 
     */
    private Integer userId;


    /**
     * 活动id
     */
    private Integer activityId;
    
    /**
     * 抽奖活动奖品id
     */
    private Integer prizeId;
    
    /**
     * 奖品标题
     */
    private String prizeName;
    
    /**
     * 奖励类型 YHQ
     */
    private String type;
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
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getPrizeId() {
        return prizeId;
    }
    
    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }
    public String getPrizeName() {
        return prizeName;
    }
    
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
