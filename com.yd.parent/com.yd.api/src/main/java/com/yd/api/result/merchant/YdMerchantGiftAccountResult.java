package com.yd.api.result.merchant;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商户礼品账户
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:22:27
 * @Version:1.1.0
 */
public class YdMerchantGiftAccountResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 企业商户id
     */
    private Integer merchantId;
    
    /**
     * 账户余额(单位元)
     */
    private Double balance;
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
    public Double getBalance() {
        return balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
