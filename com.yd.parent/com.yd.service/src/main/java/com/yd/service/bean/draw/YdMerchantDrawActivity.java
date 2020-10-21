package com.yd.service.bean.draw;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title:商户抽奖活动
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:33:33
 * @Version:1.1.0
 */
public class YdMerchantDrawActivity extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String uuid;

    private Integer merchantId;
    
    /**
     * DZP. 大转盘活动 JJG.九宫格活动, CHB 猜红包
     */
    private String activityType;

    /** 活动banner图 */
    private String bannerUrl;
    
    /**
     * 活动开始时间
     */
    private Date startTime;
    
    /**
     * 活动结束时间
     */
    private Date endTime;
    
    /**
     * 可抽奖用户身份 ALL 所有用户, NEW (新用户，未下单过的用户)
     */
    private String drawUserType;
    
    /**
     * 抽奖次数类型,对应 draw_count字段 ALL(整个活动一共抽奖) DAY(每日抽奖次数)
     */
    private String drawCountType;
    
    /**
     * 活动总抽奖次数
     */
    private Integer drawCount;
    
    /**
     * 活动规则
     */
    private String rules;
    
    /**
     * 是否上下架 Y上架 N下架
     */
    private String isEnable;
    
    /**
     * 是否删除 Y 删除  N 未删除
     */
    private String isFlag;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String getDrawUserType() {
        return drawUserType;
    }
    
    public void setDrawUserType(String drawUserType) {
        this.drawUserType = drawUserType;
    }
    public String getDrawCountType() {
        return drawCountType;
    }
    
    public void setDrawCountType(String drawCountType) {
        this.drawCountType = drawCountType;
    }
    public Integer getDrawCount() {
        return drawCount;
    }
    
    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }
    public String getRules() {
        return rules;
    }
    
    public void setRules(String rules) {
        this.rules = rules;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public String getIsFlag() {
        return isFlag;
    }
    
    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}
