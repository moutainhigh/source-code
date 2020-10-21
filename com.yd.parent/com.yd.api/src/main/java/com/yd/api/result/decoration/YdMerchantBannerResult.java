package com.yd.api.result.decoration;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户banner图
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:33:42
 * @Version:1.1.0
 */
public class YdMerchantBannerResult extends BaseModel implements Serializable{

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
     * 
     */
    private String position;
    
    /**
     * 图片地址
     */
    private String pictureUrl;
    
    /**
     * 跳转地址类型 商品|分类|链接
     */
    private String jumpType;
    
    /**
     * 跳转链接
     */
    private String jumpUrl;
    
    /**
     * 排序字段
     */
    private Integer sort;
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
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }
    
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getJumpType() {
        return jumpType;
    }
    
    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }
    public String getJumpUrl() {
        return jumpUrl;
    }
    
    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
