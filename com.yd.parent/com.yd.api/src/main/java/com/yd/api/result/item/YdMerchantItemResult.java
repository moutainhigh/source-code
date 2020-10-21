package com.yd.api.result.item;

import com.yd.api.crawer.result.BijiaColumn;
import com.yd.core.base.bean.BaseModel;

import javax.xml.soap.Text;
import java.io.Serializable;
import java.util.List;

/**
 * @Title:商户商品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:30:19
 * @Version:1.1.0
 */
public class YdMerchantItemResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer merchantId;
    
    private Integer itemId;
    
    /**  一级分类id */
    private Integer firstCategoryId;
    
    /**  二级分类id */
    private Integer secondCategoryId;
    
    private String title;
    
    /** 商品封面图片 */
    private String cover;
    
    /** 商品售价(用于下单结算的价格),仅仅做冗余 */
    private Double salePrice;
    
    /** 是否启用该商品(Y|N) */
    private String isEnable;
    
    /**
     * 商品规格数量(颜色,尺寸)
     */
    private Integer specNum;

    /** 总销量 */
    private Integer totalSalesNum;

    /**  月销量 */
    private Integer monthSalesNum;

    private Integer brandId;

    private String brandName;

    /** 排序字段 */
    private Integer sort;


    // -------------------------------------  附加字段 -----------------------------------------

    private List<YdMerchantItemSkuResult> skuList;

    private List<YdItemImageResult> imageList;

    private List<YdItemSpecNameResult> specNameList;

    private List<YdItemSpecValueResult> specValueList;

    private String content;

    /** 商户商品详情 */
    private String merchantContent;

    /** 商户商品图片 */
    private List<YdMerchantItemImageResult> merchantImageList;

    private String firstCategoryName;

    private String secondCategoryName;

    private Double minPrice;

    private Double maxPrice;

    private List<BijiaColumn> bijiaList;

    private String isHeadImage;

    private String headImageUrl;

    // -------------------------------------- get set -----------------------------------------------

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
    public String getCover() {
        return cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    public Double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public Integer getSpecNum() {
        return specNum;
    }
    
    public void setSpecNum(Integer specNum) {
        this.specNum = specNum;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public List<YdMerchantItemSkuResult> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<YdMerchantItemSkuResult> skuList) {
        this.skuList = skuList;
    }

    public List<YdItemImageResult> getImageList() {
        return imageList;
    }

    public void setImageList(List<YdItemImageResult> imageList) {
        this.imageList = imageList;
    }

    public List<YdItemSpecNameResult> getSpecNameList() {
        return specNameList;
    }

    public void setSpecNameList(List<YdItemSpecNameResult> specNameList) {
        this.specNameList = specNameList;
    }

    public List<YdItemSpecValueResult> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<YdItemSpecValueResult> specValueList) {
        this.specValueList = specValueList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }

    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName;
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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMerchantContent() {
        return merchantContent;
    }

    public void setMerchantContent(String merchantContent) {
        this.merchantContent = merchantContent;
    }

    public List<YdMerchantItemImageResult> getMerchantImageList() {
        return merchantImageList;
    }

    public void setMerchantImageList(List<YdMerchantItemImageResult> merchantImageList) {
        this.merchantImageList = merchantImageList;
    }

    public List<BijiaColumn> getBijiaList() {
        return bijiaList;
    }

    public void setBijiaList(List<BijiaColumn> bijiaList) {
        this.bijiaList = bijiaList;
    }

    public String getIsHeadImage() {
        return isHeadImage;
    }

    public void setIsHeadImage(String isHeadImage) {
        this.isHeadImage = isHeadImage;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }
}
