package com.yd.web.request;

import com.yd.api.result.item.YdMerchantItemSkuResult;

import java.io.Serializable;
import java.util.List;

/**
 * 商品请求
 * @author wuyc
 * created 2019/10/22 15:05
 **/
public class MerchantItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 商品标题
     */
    private String title;

    private Integer merchantId;

    private Integer itemId;

    /**
     * 售价
     */
    private Integer salePrice;

    /**
     * 一级分类id
     */
    private Integer firstCategoryId;

    /**
     * 二级分类id
     */
    private Integer secondCategoryId;

    /**
     * 是否可用 Y | N
     */
    private String isEnable;

    private String content;

    /**
     * 图片集合
     */
    private List<String> imageList;


    List<YdMerchantItemSkuResult> itemSkuList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
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

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<YdMerchantItemSkuResult> getItemSkuList() {
        return itemSkuList;
    }

    public void setItemSkuList(List<YdMerchantItemSkuResult> itemSkuList) {
        this.itemSkuList = itemSkuList;
    }
}
