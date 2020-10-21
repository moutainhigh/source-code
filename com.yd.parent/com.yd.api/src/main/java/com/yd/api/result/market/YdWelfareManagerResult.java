package com.yd.api.result.market;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:福利管理
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:20:37
 * @Version:1.1.0
 */
public class YdWelfareManagerResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 商户ids,逗号隔开 */
    private String merchantIds;
    
    /** 标题 */
    private String title;
    
    /** 图片地址 */
    private String imageUrl;
    
    /** 跳转链接 */
    private String jumpUrl;

    private String isEnable;
    
    /** 排序字段 */
    private Integer sort;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMerchantIds() {
        return merchantIds;
    }
    
    public void setMerchantIds(String merchantIds) {
        this.merchantIds = merchantIds;
    }
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
