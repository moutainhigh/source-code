package com.yd.service.bean.decoration;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:门店首页热门商品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:09:00
 * @Version:1.1.0
 */
public class YdMerchantHotItem extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer merchantId;
    
    /**
     * 一级分类id
     */
    private Integer firstCategoryId;
    
    /**
     * 二级分类id
     */
    private Integer secondCategoryId;
    
    /**
     * 商户商品id
     */
    private Integer merchantItemId;
    
    /**
     * 是否启用该商品(Y|N)
     */
    private String isEnable;
    
    /**
     * 排序字段
     */
    private Integer sort;

    //********************附加

    private String title;

    private Double salePrice;

    private String itemCover;

    private Integer saleNum;

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
    public Integer getMerchantItemId() {
        return merchantItemId;
    }
    
    public void setMerchantItemId(Integer merchantItemId) {
        this.merchantItemId = merchantItemId;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getItemCover() {
        return itemCover;
    }

    public void setItemCover(String itemCover) {
        this.itemCover = itemCover;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }
}
