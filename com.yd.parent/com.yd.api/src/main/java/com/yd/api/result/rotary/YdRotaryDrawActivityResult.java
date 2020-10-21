package com.yd.api.result.rotary;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

import java.util.Date;

/**
 * @Title:优度转盘抽奖活动
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:37:40
 * @Version:1.1.0
 */
public class YdRotaryDrawActivityResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 是否启用 Y启用 N禁用
     */
    private String isEnable;

    /**
     * 1. 大转盘活动 2.九宫格活动
     */
    private String type;

    /**
     * 活动名称
     */
    private String title;

    /**
     * 活动规则
     */
    private String activityRule;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 抽奖次数类型,对应 draw_total_count字段 ALL(整个活动一共抽奖类型) DAY(每日抽奖次数)
     */
    private String drawCountType;

    /**
     * 活动总抽奖次数
     */
    private Integer drawTotalCount;

    /**
     * 分享一次增加的抽奖次数
     */
    private Integer shareAddDrawCount;

    private String startDateTime;

    private String endDateTime;

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

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
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

    public String getDrawCountType() {
        return drawCountType;
    }

    public void setDrawCountType(String drawCountType) {
        this.drawCountType = drawCountType;
    }

    public Integer getDrawTotalCount() {
        return drawTotalCount;
    }

    public void setDrawTotalCount(Integer drawTotalCount) {
        this.drawTotalCount = drawTotalCount;
    }

    public Integer getShareAddDrawCount() {
        return shareAddDrawCount;
    }

    public void setShareAddDrawCount(Integer shareAddDrawCount) {
        this.shareAddDrawCount = shareAddDrawCount;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
