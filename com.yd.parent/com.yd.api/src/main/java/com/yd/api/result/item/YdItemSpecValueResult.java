package com.yd.api.result.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商品规格属性值
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:28:50
 * @Version:1.1.0
 */
public class YdItemSpecValueResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 商品编号
     */
    private Integer itemId;
    
    /**
     * 规格名称id
     */
    private Integer specId;
    
    /**
     * 规格的值
     */
    private String specValue;
    
    /**
     * 排序字段
     */
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
    public Integer getSpecId() {
        return specId;
    }
    
    public void setSpecId(Integer specId) {
        this.specId = specId;
    }
    public String getSpecValue() {
        return specValue;
    }
    
    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
