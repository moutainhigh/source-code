package com.yd.service.bean.item;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商品规格属性名
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:28:25
 * @Version:1.1.0
 */
public class YdItemSpecName extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 商品编号
     */
    private Integer itemId;
    
    /**
     * 规格名称
     */
    private String specName;
    
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
    public Integer getItemId() {
        return itemId;
    }
    
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    public String getSpecName() {
        return specName;
    }
    
    public void setSpecName(String specName) {
        this.specName = specName;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
