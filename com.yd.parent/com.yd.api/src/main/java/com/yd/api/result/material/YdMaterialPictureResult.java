package com.yd.api.result.material;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:素材库
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-28 16:03:27
 * @Version:1.1.0
 */
public class YdMaterialPictureResult extends BaseModel implements Serializable{
    
    /**
     * 
     */
    private Integer id;
    

    /**
     * 图片地址
     */
    private String pictureUrl;
    
    /**
     * 是否可用 Y|N
     */
    private String isEnable;
    
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
    public String getPictureUrl() {
        return pictureUrl;
    }
    
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
