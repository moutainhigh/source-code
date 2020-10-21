package com.yd.api.result.integral;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:积分订单
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:43:35
 * @Version:1.1.0
 */
public class YdIntegralOrderResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    /**
     * HBW(汉堡王), ZY(中域)
     */
    private String type;
    
    private Integer merchantId;
    private Integer pid;
    
    private String mobile;
    
    private Integer itemId;
    
    private Integer score;
    
    /** 订单面额 */
    private Double orderPrice;
    
    /** 结算金额 */
    private Double settlementPrice;
    
    private String orderStatus;

    private String outOrderId;

    private String billNo;

    private String startTime;

    private String endTime;

    private String merchantName;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    public Double getOrderPrice() {
        return orderPrice;
    }
    
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }
    public Double getSettlementPrice() {
        return settlementPrice;
    }
    
    public void setSettlementPrice(Double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
