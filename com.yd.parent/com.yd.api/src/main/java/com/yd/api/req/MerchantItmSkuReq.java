package com.yd.api.req;

import java.io.Serializable;

/**
 * 商户规格请求
 * @author wuyc
 * created 2019/11/11 13:46
 **/
public class MerchantItmSkuReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer merchantItemSkuId;

    private Integer num;

    public Integer getMerchantItemSkuId() {
        return merchantItemSkuId;
    }

    public void setMerchantItemSkuId(Integer merchantItemSkuId) {
        this.merchantItemSkuId = merchantItemSkuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
