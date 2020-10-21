package com.yd.api.result.market;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:门店礼品占比
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:09
 * @Version:1.1.0
 */
public class YdMerchantGiftRateResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    private Integer rate;

    // ------------------------------------- 附加字段

    private List<YdMerchantGiftRateResult> childList;

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    private Integer merchantItemId;

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
    public Integer getRate() {
        return rate;
    }
    
    public void setRate(Integer rate) {
        this.rate = rate;
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

    public Integer getMerchantItemId() {
        return merchantItemId;
    }

    public void setMerchantItemId(Integer merchantItemId) {
        this.merchantItemId = merchantItemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
