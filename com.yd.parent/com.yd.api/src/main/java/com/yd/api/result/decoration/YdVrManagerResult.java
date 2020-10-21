package com.yd.api.result.decoration;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:店铺vr设置
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:36:46
 * @Version:1.1.0
 */
public class YdVrManagerResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    private Integer merchantId;
    
    /** 跳转链接 */
    private String jumpUrl;


    // ------------------------------------------------- 附加属性 ------------------------------------

    private String merchantName;

    // ------------------------------------------------- get,set ------------------------------------

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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
