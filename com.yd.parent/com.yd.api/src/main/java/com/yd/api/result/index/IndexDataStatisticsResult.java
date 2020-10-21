package com.yd.api.result.index;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * 首页数据统计返回数据
 * @author wuyc
 * created 2019/10/23 13:40
 **/
public class IndexDataStatisticsResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 店铺收入(线下+ 线上)*/
    private Double merchantIncome;

    /** 线上收入 */
    private Double toAccountIncome;

    /**
     * 订单完成数
     */
    private Integer completeOrderCount;

    private Integer userCompleteOrderCount;

    /**
     * 访客数
     */
    private Integer visitCount;


    public Double getMerchantIncome() {
        return merchantIncome;
    }

    public void setMerchantIncome(Double merchantIncome) {
        this.merchantIncome = merchantIncome;
    }

    public Double getToAccountIncome() {
        return toAccountIncome;
    }

    public void setToAccountIncome(Double toAccountIncome) {
        this.toAccountIncome = toAccountIncome;
    }

    public Integer getCompleteOrderCount() {
        return completeOrderCount;
    }

    public void setCompleteOrderCount(Integer completeOrderCount) {
        this.completeOrderCount = completeOrderCount;
    }

    public Integer getUserCompleteOrderCount() {
        return userCompleteOrderCount;
    }

    public void setUserCompleteOrderCount(Integer userCompleteOrderCount) {
        this.userCompleteOrderCount = userCompleteOrderCount;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }
}
