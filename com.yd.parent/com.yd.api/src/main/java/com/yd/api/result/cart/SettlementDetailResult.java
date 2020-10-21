package com.yd.api.result.cart;

import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/2 14:05
 **/
public class SettlementDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<YdMerchantItemSkuResult> skuList;

    private List<YdUserCouponResult> couponList;

    private List<YdMerchantGiftResult> giftList;

    private String carIds;

    private Integer skuId;

    private Integer num;

    /** car | item */
    private String type;

    public List<YdMerchantItemSkuResult> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<YdMerchantItemSkuResult> skuList) {
        this.skuList = skuList;
    }

    public List<YdUserCouponResult> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<YdUserCouponResult> couponList) {
        this.couponList = couponList;
    }

    public List<YdMerchantGiftResult> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<YdMerchantGiftResult> giftList) {
        this.giftList = giftList;
    }

    public String getCarIds() {
        return carIds;
    }

    public void setCarIds(String carIds) {
        this.carIds = carIds;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
