package com.yd.api.result.gift;

import java.io.Serializable;
import java.util.List;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:平台礼品分类
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:41:45
 * @Version:1.1.0
 */
public class YdGiftCategoryResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    /** 分类名称 */
    private String categoryName;
    
    /** 分类图标 */
    private String icon;

    //------------------------ 附加字段

    /**
     * 礼品分类对应的商品数量
     */
    private Integer number;

    /**
     * 分类下对应的礼品
     */
    private List<YdGiftResult> giftList;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<YdGiftResult> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<YdGiftResult> giftList) {
        this.giftList = giftList;
    }
}
