package com.yd.web.request;

import com.yd.api.req.MerchantItmSkuReq;

import java.io.Serializable;
import java.util.List;

/**
 * 商品规格id数量
 * @author wuyc
 * created 2019/11/11 13:42
 **/
public class MerchantItemSkuRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<MerchantItmSkuReq> skuList;

    private Integer merchantId;

    public List<MerchantItmSkuReq> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<MerchantItmSkuReq> skuList) {
        this.skuList = skuList;
    }


    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
