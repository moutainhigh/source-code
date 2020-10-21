package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户自动取消订单时间配置
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:36:37
 * @Version:1.1.0
 */
public class YdUserOrderConfigResult extends BaseModel implements Serializable{

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
     * 订单自动取消时间(单位小时)
     */
    private Integer orderAutoCancelTime;
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
    public Integer getOrderAutoCancelTime() {
        return orderAutoCancelTime;
    }
    
    public void setOrderAutoCancelTime(Integer orderAutoCancelTime) {
        this.orderAutoCancelTime = orderAutoCancelTime;
    }
}
