package com.yd.api.result.item;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商户商品详情
 * @Description:
 * @Author:Wuyc
 * @Since:2020-04-20 17:24:48
 * @Version:1.1.0
 */
public class YdMerchantItemImageResult extends BaseModel implements Serializable{
    
    private Integer id;
    
    /** 商户商品编号 */
    private Integer itemId;
    
    /** 图片地址 */
    private String imageUrl;
    
    /**  排序字段 */
    private Integer sort;

    // -------------------------------------- get set -----------------------------------------------

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
