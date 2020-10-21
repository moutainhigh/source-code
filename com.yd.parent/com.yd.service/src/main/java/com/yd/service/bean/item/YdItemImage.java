package com.yd.service.bean.item;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商品图片表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:23:59
 * @Version:1.1.0
 */
public class YdItemImage extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    

    private Integer itemId;
    
    private String imageUrl;
    
    private Integer sort;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
