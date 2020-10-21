package com.yd.api.req;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/2 18:07
 **/
public class SubmitOrderReq implements Serializable {

    private static final long serialVersionUID = 1L;

    List<MerchantGiftReq> giftList;

    private Integer couponId;

    private String carIds;

    private Integer skuId;

    private Integer num;

    /** car | item */
    private String type;

    /** 收货方式  ZT(自提) | PS(配送) */
    private String receivingWay;

    private Integer addressId;

    // 是否勾选积分抵扣
    private String isCheckIntegralReduce;

    // 是否勾选旧机抵扣
    private String isCheckOldMachineReduce;

    private Integer merchantId;

    public List<MerchantGiftReq> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<MerchantGiftReq> giftList) {
        this.giftList = giftList;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
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

    public String getReceivingWay() {
        return receivingWay;
    }

    public void setReceivingWay(String receivingWay) {
        this.receivingWay = receivingWay;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getIsCheckIntegralReduce() {
        return isCheckIntegralReduce;
    }

    public void setIsCheckIntegralReduce(String isCheckIntegralReduce) {
        this.isCheckIntegralReduce = isCheckIntegralReduce;
    }

    public String getIsCheckOldMachineReduce() {
        return isCheckOldMachineReduce;
    }

    public void setIsCheckOldMachineReduce(String isCheckOldMachineReduce) {
        this.isCheckOldMachineReduce = isCheckOldMachineReduce;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
