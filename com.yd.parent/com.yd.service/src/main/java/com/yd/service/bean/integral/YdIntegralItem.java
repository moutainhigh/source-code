package com.yd.service.bean.integral;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:积分商品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:16:34
 * @Version:1.1.0
 */
public class YdIntegralItem extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    /** 商品名称 */
    private String title;
    
    /** 售价 */
    private Double salePrice;
    
    /** 结算价 */
    private Double settlementPrice;
    
    /** 兑换所需积分 */
    private Integer integralCount;
    
    /** 渠道编码 */
    private String channelCode;
    
    /** 二维码跳转链接 */
    private String qrCodeUrl;
    
    /** 图片地址 */
    private String imageUrl;
    
    /** Y(上架)|N(下架) */
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
    public Double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
    public Double getSettlementPrice() {
        return settlementPrice;
    }
    
    public void setSettlementPrice(Double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }
    public Integer getIntegralCount() {
        return integralCount;
    }
    
    public void setIntegralCount(Integer integralCount) {
        this.integralCount = integralCount;
    }
    public String getChannelCode() {
        return channelCode;
    }
    
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }
    
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
