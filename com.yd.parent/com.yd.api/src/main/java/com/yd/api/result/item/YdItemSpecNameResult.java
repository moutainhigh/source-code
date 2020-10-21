package com.yd.api.result.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @Title:商品规格属性名
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:28:25
 * @Version:1.1.0
 */
public class YdItemSpecNameResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 商品编号
     */
    private Integer itemId;
    
    /** 规格名称 */
    private String specName;
    
    private Integer sort;


    // -------------------------------------  附加字段 -----------------------------------------

    private List<YdItemSpecValueResult> itemSpecValues;

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

    public List<YdItemSpecValueResult> getItemSpecValues() {
        return itemSpecValues;
    }

    public void setItemSpecValues(List<YdItemSpecValueResult> itemSpecValues) {
        this.itemSpecValues = itemSpecValues;
    }
}
