package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户订单详情
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:37:13
 * @Version:1.1.0
 */
public class YdUserOrderDetailResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private String orderDetailNo;
    
    /**
     * 用户id编号
     */
    private Integer userId;

    private Integer merchantId;
    
    /**
     * 订单id
     */
    private Integer orderId;
    
    /** 商品id */
    private Integer itemId;
    
    /**
     * 商品sku编号
     */
    private Integer skuId;
    
    /**
     * 商品名称
     */
    private String merchantItemTitle;
    
    /**
     * 商品封面图
     */
    private String cover;
    
    private Integer merchantItemId;
    
    private Integer merchantSkuId;
    
    /**
     * 商品sku售价
     */
    private Double merchantItemPrice;
    
    /**
     * 购买的商品数量
     */
    private Integer num;
    
    /**
     * 商品sku的specValueIdpath
     */
    private String specValueIdPath;
    
    /**
     * 商品sku的规格名称值json串
     */
    private String specNameValueJson;
    
    /**
     * 商品sku的规格值,"/"拼接(黑色/XXl)
     */
    private String specValueStr;

    private String startTime;

    private String endTime;

    private String orderStatus;

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
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public Integer getSkuId() {
        return skuId;
    }
    
    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }
    public String getMerchantItemTitle() {
        return merchantItemTitle;
    }
    
    public void setMerchantItemTitle(String merchantItemTitle) {
        this.merchantItemTitle = merchantItemTitle;
    }
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    public Integer getMerchantItemId() {
        return merchantItemId;
    }
    
    public void setMerchantItemId(Integer merchantItemId) {
        this.merchantItemId = merchantItemId;
    }
    public Integer getMerchantSkuId() {
        return merchantSkuId;
    }
    
    public void setMerchantSkuId(Integer merchantSkuId) {
        this.merchantSkuId = merchantSkuId;
    }
    public Double getMerchantItemPrice() {
        return merchantItemPrice;
    }
    
    public void setMerchantItemPrice(Double merchantItemPrice) {
        this.merchantItemPrice = merchantItemPrice;
    }
    public Integer getNum() {
        return num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getSpecValueIdPath() {
        return specValueIdPath;
    }
    
    public void setSpecValueIdPath(String specValueIdPath) {
        this.specValueIdPath = specValueIdPath;
    }
    public String getSpecNameValueJson() {
        return specNameValueJson;
    }
    
    public void setSpecNameValueJson(String specNameValueJson) {
        this.specNameValueJson = specNameValueJson;
    }
    public String getSpecValueStr() {
        return specValueStr;
    }
    
    public void setSpecValueStr(String specValueStr) {
        this.specValueStr = specValueStr;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderDetailNo() {
        return orderDetailNo;
    }

    public void setOrderDetailNo(String orderDetailNo) {
        this.orderDetailNo = orderDetailNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
