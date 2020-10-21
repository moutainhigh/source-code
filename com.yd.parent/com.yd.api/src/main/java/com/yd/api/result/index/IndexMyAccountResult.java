package com.yd.api.result.index;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * 首页我的账户数据
 * @author wuyc
 * created 2019/10/23 13:40
 **/
public class IndexMyAccountResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商户余额 */
    private Double balance;

    /** 交易额 */
    private Double transPrice;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTransPrice() {
        return transPrice;
    }

    public void setTransPrice(Double transPrice) {
        this.transPrice = transPrice;
    }
}
