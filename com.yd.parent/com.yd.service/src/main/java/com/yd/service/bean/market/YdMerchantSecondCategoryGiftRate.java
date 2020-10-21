package com.yd.service.bean.market;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:门店二级分类礼品占比
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:52:52
 * @Version:1.1.0
 */
public class YdMerchantSecondCategoryGiftRate extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    private Integer firstCategoryId;
    
    private Integer secondCategoryId;
    
    private Integer rate;

    //----------------附加
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
