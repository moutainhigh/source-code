package com.yd.service.bean.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户礼品账户流水
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:27:31
 * @Version:1.1.0
 */
public class YdMerchantGiftTrans extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 商户id
     */
    private Integer merchantId;
    
    /**
     * 外部订单号
     */
    private String outOrderId;
    
    /**
     * 交易流水来源(充钱|扣钱...)
     */
    private String transSource;
    
    /**
     * 交易金额(单位元) 出是负数
     */
    private Double transAmount;
    
    /**
     * 交易类型(IN|OUT)
     */
    private String transType;
    
    /**
     * 交易流水描述信息
     */
    private String transDesc;
    
    /**
     * 外部订单状态
     */
    private String outOrderStatus;
    
    /**
     * 交易金额(前)
     */
    private Double transBeforeAmount;
    
    /**
     * 交易金额(后)
     */
    private Double transAfterAmount;

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
    public String getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getTransSource() {
        return transSource;
    }
    
    public void setTransSource(String transSource) {
        this.transSource = transSource;
    }
    public Double getTransAmount() {
        return transAmount;
    }
    
    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }
    public String getTransType() {
        return transType;
    }
    
    public void setTransType(String transType) {
        this.transType = transType;
    }
    public String getTransDesc() {
        return transDesc;
    }
    
    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }
    public String getOutOrderStatus() {
        return outOrderStatus;
    }
    
    public void setOutOrderStatus(String outOrderStatus) {
        this.outOrderStatus = outOrderStatus;
    }
    public Double getTransBeforeAmount() {
        return transBeforeAmount;
    }
    
    public void setTransBeforeAmount(Double transBeforeAmount) {
        this.transBeforeAmount = transBeforeAmount;
    }
    public Double getTransAfterAmount() {
        return transAfterAmount;
    }
    
    public void setTransAfterAmount(Double transAfterAmount) {
        this.transAfterAmount = transAfterAmount;
    }

}
