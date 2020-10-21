package com.yd.service.bean.decoration;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商户历史banner图
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:34:30
 * @Version:1.1.0
 */
public class YdMerchantBannerHistory extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer merchantId;
    
    /**
     * 图片地址
     */
    private String pictureUrl;
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
    public String getPictureUrl() {
        return pictureUrl;
    }
    
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
