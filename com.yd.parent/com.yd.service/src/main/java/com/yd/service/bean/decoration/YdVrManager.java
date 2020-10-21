package com.yd.service.bean.decoration;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:店铺vr设置
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:36:46
 * @Version:1.1.0
 */
public class YdVrManager extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private Integer merchantId;
    
    /** 跳转链接 */
    private String jumpUrl;

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
    public String getJumpUrl() {
        return jumpUrl;
    }
    
    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
