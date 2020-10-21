package com.yd.api.result.order;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商户充值订单
 * @Description:
 * @Author:Wuyc
 * @Since:2020-01-13 15:56:23
 * @Version:1.1.0
 */
public class YdMerchantRechargeOrderResult extends BaseModel implements Serializable{
    
    private Integer id;
    
    /**
     * 用户id
     */
    private Integer merchantId;
    
    /**
     * 下单的商户id
     */
    private Integer pid;
    
    /**
     * Y | N
     */
    private String isPay;
    
    /**
     * 充值金额
     */
    private Double payPrice;
    
    private String outOrderId;
    
    private String billNo;

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
    public Integer getPid() {
        return pid;
    }
    
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getIsPay() {
        return isPay;
    }
    
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    public Double getPayPrice() {
        return payPrice;
    }
    
    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
    public String getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
