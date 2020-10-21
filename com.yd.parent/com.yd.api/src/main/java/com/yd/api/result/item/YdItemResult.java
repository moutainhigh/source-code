package com.yd.api.result.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:平台商品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:20:56
 * @Version:1.1.0
 */
public class YdItemResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Integer firstCategoryId;
    private Integer secondCategoryId;
    private String title;
    private String subTitle;
    private String itemCover;
    private Double price;
    private Double marketPrice;
    private String isEnable;
    private Integer specNum;
    private Integer sort;
    private String brand;
    private Integer brandId;
    private Integer crawerId;
    private String pubTime;

    private String isHeadImage;

    private String headImageUrl;

    // -------------------------------------  附加字段 -----------------------------------------

    private List<YdItemSkuResult> skuList;

    private List<YdItemImageResult> imageList;

    private List<YdItemSpecNameResult> specNameList;

    private List<YdItemSpecValueResult> specValueList;

    private String content;


    // -------------------------------------- get set -----------------------------------------------

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    public String getItemCover() {
        return itemCover;
    }
    
    public void setItemCover(String itemCover) {
        this.itemCover = itemCover;
    }
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getMarketPrice() {
        return marketPrice;
    }
    
    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
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

	public Integer getCrawerId() {
		return crawerId;
	}

	public void setCrawerId(Integer crawerId) {
		this.crawerId = crawerId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }


    public List<YdItemSkuResult> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<YdItemSkuResult> skuList) {
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
