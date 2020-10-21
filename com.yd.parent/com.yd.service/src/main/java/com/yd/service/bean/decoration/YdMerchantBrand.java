package com.yd.service.bean.decoration;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:门店品牌管理
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:08:16
 * @Version:1.1.0
 */
public class YdMerchantBrand extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer merchantId;

    /**  品牌标识 */
    private String brandAlias;

    /** 品牌名称 */
    private String brandName;
    
    /** 分类的图标 */
    private String icon;
    
    private Integer brandId;

    /** 排序字段 */
    private Integer sort;

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
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getBrandAlias() {
        return brandAlias;
    }

    public void setBrandAlias(String brandAlias) {
        this.brandAlias = brandAlias;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
}
