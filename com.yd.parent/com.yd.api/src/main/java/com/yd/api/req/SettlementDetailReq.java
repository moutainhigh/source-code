package com.yd.api.req;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/2 18:00
 **/
public class SettlementDetailReq implements Serializable {

    private static final long serialVersionUID = 1L;

    List<MerchantGiftReq> giftList;

    private String carIds;

    private Integer skuId;

    private Integer num;

    /** car | item */
    private String type;

    private Integer merchantId;

    public List<MerchantGiftReq> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<MerchantGiftReq> giftList) {
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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
