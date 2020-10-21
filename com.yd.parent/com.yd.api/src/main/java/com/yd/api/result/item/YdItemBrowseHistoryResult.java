package com.yd.api.result.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:门店商品浏览记录
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:22:13
 * @Version:1.1.0
 */
public class YdItemBrowseHistoryResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer merchantId;
    
    private Integer userId;
    
    private Integer itemId;
    
    private Integer skuId;
    
    private Integer merchantItemId;
    
    private Integer merchantSkuId;
    
    /** 访问者ip */
    private String visitorIp;


    // -------------------------------------- get set -----------------------------------------------

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
    public String getVisitorIp() {
        return visitorIp;
    }
    
    public void setVisitorIp(String visitorIp) {
        this.visitorIp = visitorIp;
    }
}
