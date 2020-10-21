package com.yd.service.bean.user;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title:用户交易数据统计
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-07 17:05:25
 * @Version:1.1.0
 */
public class YdUserTransStatistic extends BaseModel implements Serializable{

    private Integer id;

    private Integer userId;

    private Integer merchantId;

    /**
     * 最近一次交易时间
     */
    private Date lastTransDate;

    /**
     * 购买总次数
     */
    private Integer totalBuyCount;

    /**
     * 购买商品总数量
     */
    private Integer totalBuyItemCount;

    /**
     * 总交易金额
     */
    private Double totalTransAmount;

    /**
     * 总实际付款金额
     */
    private Double totalPayAmount;

    /**
     * 用户名
     */
    private String nickname;

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
    public Date getLastTransDate() {
        return lastTransDate;
    }
    
    public void setLastTransDate(Date lastTransDate) {
        this.lastTransDate = lastTransDate;
    }
    public Integer getTotalBuyCount() {
        return totalBuyCount;
    }
    
    public void setTotalBuyCount(Integer totalBuyCount) {
        this.totalBuyCount = totalBuyCount;
    }
    public Integer getTotalBuyItemCount() {
        return totalBuyItemCount;
    }
    
    public void setTotalBuyItemCount(Integer totalBuyItemCount) {
        this.totalBuyItemCount = totalBuyItemCount;
    }
    public Double getTotalTransAmount() {
        return totalTransAmount;
    }
    
    public void setTotalTransAmount(Double totalTransAmount) {
        this.totalTransAmount = totalTransAmount;
    }
    public Double getTotalPayAmount() {
        return totalPayAmount;
    }
    
    public void setTotalPayAmount(Double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String username) {
        this.nickname = username;
    }
}
