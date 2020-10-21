package com.yg.service.bean.item;

import com.yg.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户积分商品
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-27 13:34:01
 * @Version:1.1.0
 */
public class YgMerchantIntegralItem extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private Integer merchantId;
    
    private String title;
    
    private String subTitle;
    
    private String cover;
    
    /**
     * 兑换需要积分
     */
    private Integer score;
    
    private Double marketPrice;
    
    private Integer couponId;
    
    /**
     * 是否上架  Y上架，N下架
     */
    private String isEnable;
    
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
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSubTitle() {
        return subTitle;
    }
    
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    public Double getMarketPrice() {
        return marketPrice;
    }
    
    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }
    public Integer getCouponId() {
        return couponId;
    }
    
    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
