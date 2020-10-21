package com.yd.service.bean.user;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:用户商户绑定关系表
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-29 10:11:01
 * @Version:1.1.0
 */
public class YdUserMerchant extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer userId;
    
    private Integer merchantId;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
