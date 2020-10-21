package com.yd.api.result.rotary;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:用户
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-18 11:46:36
 * @Version:1.1.0
 */
public class YdRotaryDrawUserResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;


    /**
     *
     */
    private Integer userId;

    /**
     *
     */
    private Integer merchantId;

    private Integer activityId;

    /**
     * 外部订单id (YYMMDD + REURCE + ID)
     */
    private String outOrderId;

    /**
     * 抽奖次数类型 All(一共多少机会) || DAY(每日有几次机会)
     */
    private String drawCountType;

    /**
     * 剩余抽奖次数
     */
    private Integer userDrawCount;

    /**
     * 有效开始时间
     */
    private Date validTime;

    /**
     * 过期时间
     */
    private Date expireTime;
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
    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getDrawCountType() {
        return drawCountType;
    }

    public void setDrawCountType(String drawCountType) {
        this.drawCountType = drawCountType;
    }
    public Integer getUserDrawCount() {
        return userDrawCount;
    }

    public void setUserDrawCount(Integer userDrawCount) {
        this.userDrawCount = userDrawCount;
    }
    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
