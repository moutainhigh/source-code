package com.yd.api.result.market;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;


/**
 * @Title:门店商品礼品占比
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:59
 * @Version:1.1.0
 */
public class YdMerchantItemGiftRateResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer merchantId;
    
    private Integer firstCategoryId;
    
    private Integer secondCategoryId;

    private Integer merchantItemId;
    
    private Integer rate;

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
    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }
    
    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }
    public Integer getSecondCategoryId() {
        return secondCategoryId;
    }
    
    public void setSecondCategoryId(Integer secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }
    public Integer getRate() {
        return rate;
    }
    
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getMerchantItemId() {
        return merchantItemId;
    }

    public void setMerchantItemId(Integer merchantItemId) {
        this.merchantItemId = merchantItemId;
    }
}
