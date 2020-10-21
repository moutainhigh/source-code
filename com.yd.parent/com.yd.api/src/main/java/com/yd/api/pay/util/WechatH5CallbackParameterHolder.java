package com.yd.api.pay.util;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Xulg
 * Created in 2019-07-25 15:51
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class WechatH5CallbackParameterHolder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 去前缀的商户订单号
     */
    private String orderId;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 订单金额(分)
     */
    private Integer totalFee;

    /**
     * 用户在商户appid下的唯一标识
     */
    private String openId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 业务结果(SUCCESS|FAIL)
     */
    private String tradeStatus;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
