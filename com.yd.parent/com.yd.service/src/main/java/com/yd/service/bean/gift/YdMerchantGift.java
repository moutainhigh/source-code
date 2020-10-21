package com.yd.service.bean.gift;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户礼品库
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 09:33:43
 * @Version:1.1.0
 */
public class YdMerchantGift extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    /**
     * 礼品类型 merchant(商户) | platform(平台)
     */
    private String giftType;
    
    private Integer merchantId;
    
    private Integer supplierId;
    
    /**
     * 平台礼品id
     */
    private Integer giftId;
    
    /**
     * 礼品分类id
     */
    private Integer categoryId;
    
    private String title;
    
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
     * 礼品图片
     */
    private String imageUrl;
    
    /**
     * 礼品描述
     */
    private String giftDesc;
    
    /**
     * 是否上架(Y|N)
     */
    private String isEnable;


    //----------------------------------------------附加字段
    private Double minPrice;

    private Double maxPrice;

    private String isFlag;

    /** 礼品状态，后台列表查询使用 (in_shelves | no_shelves | invalid) */
    private String giftStatus;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getGiftType() {
        return giftType;
    }
    
    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public Integer getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
    public Integer getGiftId() {
        return giftId;
    }
    
    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
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
    
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getGiftDesc() {
        return giftDesc;
    }
    
    public void setGiftDesc(String giftDesc) {
        this.giftDesc = giftDesc;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
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

    public String getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }


    public String getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(String giftStatus) {
        this.giftStatus = giftStatus;
    }
}
