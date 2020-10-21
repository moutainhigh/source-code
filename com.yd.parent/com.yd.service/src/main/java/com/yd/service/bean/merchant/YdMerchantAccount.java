package com.yd.service.bean.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:优度商户账户
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:54
 * @Version:1.1.0
 */
public class YdMerchantAccount extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
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
