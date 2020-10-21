package com.yd.service.bean.rotary;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:转盘抽奖分享记录
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:53:47
 * @Version:1.1.0
 */
public class YdRotaryDrawShareRecord extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
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
     * 分享增加的抽奖次数
     */
    private Integer addDrawCount;
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
    public Integer getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    public Integer getAddDrawCount() {
        return addDrawCount;
    }
    
    public void setAddDrawCount(Integer addDrawCount) {
        this.addDrawCount = addDrawCount;
    }
}
