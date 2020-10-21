package com.yd.api.result.item;

import com.alibaba.fastjson.JSONObject;
import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * @Title:商品sku表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:26:15
 * @Version:1.1.0
 */
public class YdItemSkuResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer itemId;
    
    /**
     * 
     */
    private Integer skuId;
    
    /**
     * 一级分类id
     */
    private Integer firstCategoryId;
    
    /**
     * 二级分类id
     */
    private Integer secondCategoryId;
    
    /** 商品标题 */
    private String title;
    
    /**
     * 副标题(子标题)
     */
    private String subTitle;
    
    /**
     * 封面图
     */
    private String skuCover;
    
    /**
     * 规格id字符串(1,2,3,4,5)
     */
    private String specValueIdPath;
    
    /**
     * 规格名称和值的json串({"尺码":"38","颜色":"黑色"})
     */
    private String specNameValueJson;
    
    /**
     * 商品售价(用于下单结算的价格)
     */
    private Double salePrice;
    
    /**
     * 市场价
     */
    private Double marketPrice;
    
    /**
     * 库存数量
     */
    private Integer stock;
    
    /**
     * 是否启用该商品(Y|N)
     */
    private String isEnable;
    
    /**
     * 排序字段
     */
    private Integer sort;

    // -------------------------------------- get set -----------------------------------------------
    
    public String getSpecNameValues() {
    	if(StringUtils.isEmpty(this.specNameValueJson)) {
    		return "";
    	}
    	String str="";
    	JSONObject json=JSONObject.parseObject(this.specNameValueJson);
    	for(Entry<String,Object> entry:json.entrySet()) {
    		str+=entry.getValue()+" ";
    	}
    	
    	return str;
    }
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
    public String getSubTitle() {
        return subTitle;
    }
    
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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
    public Double getMarketPrice() {
        return marketPrice;
    }
    
    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
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
}
