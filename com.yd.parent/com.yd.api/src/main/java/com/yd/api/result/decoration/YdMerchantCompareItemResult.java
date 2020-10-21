package com.yd.api.result.decoration;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户配置比价商品
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:29:13
 * @Version:1.1.0
 */
public class YdMerchantCompareItemResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer merchantId;
    
    private Integer merchantItemId;

    private Integer sort;

    // ------------------------------------------------- 附加属性 ------------------------------------

    private String merchantName;

    /** 商品標題 */
    private String title;

    // ------------------------------------------------- get,set ------------------------------------

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
    public Integer getMerchantItemId() {
        return merchantItemId;
    }
    
    public void setMerchantItemId(Integer merchantItemId) {
        this.merchantItemId = merchantItemId;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
