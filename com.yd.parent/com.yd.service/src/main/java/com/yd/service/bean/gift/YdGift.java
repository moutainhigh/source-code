package com.yd.service.bean.gift;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:平台礼品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 17:17:52
 * @Version:1.1.0
 */
public class YdGift extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 供应商id(商户id)
     */
    private Integer supplierId;

    /**
     * 礼品分类id
     */
    private Integer categoryId;

    /**
     * 礼品名称
     */
    private String title;

    /**
     * 副标题(子标题)
     */
    private String subTitle;

    /**
     * 商品售价(用于下单结算的价格)
     */
    private Double salePrice;

    /**
     * 市场价(划价线)
     */
    private Double marketPrice;

    /**
     * 采购价
     */
    private Double purchasePrice;

    /**
     * 阶梯数
     */
    private Integer ladderNumber;

    /**
     * 阶梯售价，与ladder_price对应
     */
    private Double ladderPrice;

    private String imageUrl;

    /**
     * 礼品描述
     */
    private String giftDesc;

    /**
     * 是否上架(Y|N)
     */
    private String isEnable;

    /**
     * 是否删除(Y|N)
     */
    private String isFlag;


    private String supplierName;

    private Double minPrice;

    private Double maxPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
    public Integer getLadderNumber() {
        return ladderNumber;
    }

    public void setLadderNumber(Integer ladderNumber) {
        this.ladderNumber = ladderNumber;
    }
    public Double getLadderPrice() {
        return ladderPrice;
    }

    public void setLadderPrice(Double ladderPrice) {
        this.ladderPrice = ladderPrice;
    }
    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public String getGiftDesc() {
        return giftDesc;
    }

    public void setGiftDesc(String giftDesc) {
        this.giftDesc = giftDesc;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
}
