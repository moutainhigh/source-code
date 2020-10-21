package com.yd.service.bean.item;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商户商品分类
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:31:16
 * @Version:1.1.0
 */
public class YdMerchantItemCategory extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 上级分类id， 一级分类的pid是0
     */
    private Integer pid;
    
    /**
     * 
     */
    private Integer merchantId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 分类的图标
     */
    private String icon;
    
    /**
     * 跳转链接
     */
    private String jumpUrl;
    
    /**
     * 是否可用(Y|N)
     */
    private String isEnable;
    
    /**
     * 是否显示(Y|N)
     */
    private String isShow;
    
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
    public Integer getPid() {
        return pid;
    }
    
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
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
    public String getJumpUrl() {
        return jumpUrl;
    }
    
    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public String getIsShow() {
        return isShow;
    }
    
    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
