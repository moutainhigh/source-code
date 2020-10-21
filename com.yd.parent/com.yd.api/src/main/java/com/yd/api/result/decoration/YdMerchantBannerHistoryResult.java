package com.yd.api.result.decoration;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户历史banner图
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:34:30
 * @Version:1.1.0
 */
public class YdMerchantBannerHistoryResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 商户id
     */
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
