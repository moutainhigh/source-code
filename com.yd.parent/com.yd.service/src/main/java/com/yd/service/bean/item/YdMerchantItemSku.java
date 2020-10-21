package com.yd.service.bean.item;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商户商品sku
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:32:12
 * @Version:1.1.0
 */
public class YdMerchantItemSku extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /** 平台商品id */
    private Integer itemId;
    
    /** 平台商品skuid */
    private Integer skuId;
    
    private Integer merchantItemId;
    
    /** 一级分类id */
    private Integer firstCategoryId;
    
    /** 二级分类id */
    private Integer secondCategoryId;
    
    /** 商品标题 */
    private String title;
    
    /** 封面图 */
    private String skuCover;
    
    /** 规格id字符串(1,2,3,4,5) */
    private String specValueIdPath;
    
    /** 规格名称和值的json串({"尺码":"38","颜色":"黑色"}) */
    private String specNameValueJson;
    
    /** 商品售价(用于下单结算的价格) */
    private Double salePrice;
    
    /**
     * 库存数量
     */
    private Integer stock;
    
    /** 是否启用该商品(Y|N) */
    private String isEnable;
    
    /** 排序字段 */
    private Integer sort;

    /** 总销量 */
    private Integer totalSalesNum;

    /** 月销量 */
    private Integer monthSalesNum;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSkuCover() {
        return skuCover;
    }
    
    public void setSkuCover(String skuCover) {
        this.skuCover = skuCover;
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
    public Double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
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

    public Integer getTotalSalesNum() {
        return totalSalesNum;
    }

    public void setTotalSalesNum(Integer totalSalesNum) {
        this.totalSalesNum = totalSalesNum;
    }

    public Integer getMonthSalesNum() {
        return monthSalesNum;
    }

    public void setMonthSalesNum(Integer monthSalesNum) {
        this.monthSalesNum = monthSalesNum;
    }
}
