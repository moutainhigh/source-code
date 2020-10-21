package com.yd.api.result.gift;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商户礼品购物车
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-01 14:29:29
 * @Version:1.1.0
 */
public class YdMerchantGiftCartResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;

    /**
     * 下单的商户id
     */
    private Integer merchantId;
    
    /**
     * 供应商id(商户id)
     */
    private Integer supplierId;
    
    /**
     * 礼品id
     */
    private Integer giftId;
    
    /**
     * 礼品数量
     */
    private Integer num;
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    public Integer getNum() {
        return num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
}
