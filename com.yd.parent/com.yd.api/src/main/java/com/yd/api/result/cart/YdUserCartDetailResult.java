package com.yd.api.result.cart;

import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.item.YdItemSpecNameResult;
import com.yd.api.result.item.YdItemSpecValueResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 用户购物车详情列表
 * @author wuyc
 * created 2019/11/13 9:15
 **/
public class YdUserCartDetailResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer merchantSkuId;

    private String itemCover;

    private Double salePrice;

    private String title;

    private Integer num;

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    private String specValueIdPath;

    private String specNameValueJson;

    private String specNameValueIdJson;

    private List<YdMerchantItemSkuResult> skuList;

    private List<YdItemSpecNameResult> specNameList;

    private List<YdItemSpecValueResult> specValueList;

    // 商品可领取的商户优惠券
    private List<YdMerchantCouponResult> merchantCouponList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantSkuId() {
        return merchantSkuId;
    }

    public void setMerchantSkuId(Integer merchantSkuId) {
        this.merchantSkuId = merchantSkuId;
    }

    public String getItemCover() {
        return itemCover;
    }

    public void setItemCover(String itemCover) {
        this.itemCover = itemCover;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public List<YdMerchantItemSkuResult> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<YdMerchantItemSkuResult> skuList) {
        this.skuList = skuList;
    }

    public String getSpecNameValueIdJson() {
        return specNameValueIdJson;
    }

    public void setSpecNameValueIdJson(String specNameValueIdJson) {
        this.specNameValueIdJson = specNameValueIdJson;
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

    public List<YdMerchantCouponResult> getMerchantCouponList() {
        return merchantCouponList;
    }

    public void setMerchantCouponList(List<YdMerchantCouponResult> merchantCouponList) {
        this.merchantCouponList = merchantCouponList;
    }
}
