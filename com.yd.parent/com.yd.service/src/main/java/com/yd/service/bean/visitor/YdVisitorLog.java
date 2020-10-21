package com.yd.service.bean.visitor;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:接口访问记录
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-09 19:17:07
 * @Version:1.1.0
 */
public class YdVisitorLog extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    private Integer userId;
    
    private Integer itemId;
    
    private Integer skuId;
    
    private Integer merchantItemId;
    
    private Integer merchantSkuId;

    private String startTime;

    private String endTime;


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
}
