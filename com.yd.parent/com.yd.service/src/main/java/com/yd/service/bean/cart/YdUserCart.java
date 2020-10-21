package com.yd.service.bean.cart;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:用户购物车
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-08 10:08:32
 * @Version:1.1.0
 */
public class YdUserCart extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer userId;
    
    private Integer merchantId;
    
    private Integer merchantSkuId;
    
    private Integer num;


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
    public Integer getMerchantSkuId() {
        return merchantSkuId;
    }
    
    public void setMerchantSkuId(Integer merchantSkuId) {
        this.merchantSkuId = merchantSkuId;
    }
    public Integer getNum() {
        return num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
}
