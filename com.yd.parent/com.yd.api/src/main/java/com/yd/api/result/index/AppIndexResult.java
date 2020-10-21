package com.yd.api.result.index;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @author wuyc
 * created 2019/12/24 14:51
 **/
public class AppIndexResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double balance;

    private Double giftBalance;

    private Integer noReadMessage;

    private Integer orderNum;

    private Double onlinePrice;

    private Double offlinePrice;

    private Double integralPrice;

    private Double oldMobilePrice;

    private Double orderPrice;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getGiftBalance() {
        return giftBalance;
    }

    public void setGiftBalance(Double giftBalance) {
        this.giftBalance = giftBalance;
    }

    public Integer getNoReadMessage() {
        return noReadMessage;
    }

    public void setNoReadMessage(Integer noReadMessage) {
        this.noReadMessage = noReadMessage;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(Double onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public Double getOfflinePrice() {
        return offlinePrice;
    }

    public void setOfflinePrice(Double offlinePrice) {
        this.offlinePrice = offlinePrice;
    }

    public Double getIntegralPrice() {
        return integralPrice;
    }

    public void setIntegralPrice(Double integralPrice) {
        this.integralPrice = integralPrice;
    }

    public Double getOldMobilePrice() {
        return oldMobilePrice;
    }

    public void setOldMobilePrice(Double oldMobilePrice) {
        this.oldMobilePrice = oldMobilePrice;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }
}
