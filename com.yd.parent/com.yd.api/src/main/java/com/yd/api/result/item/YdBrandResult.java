package com.yd.api.result.item;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:商品品牌
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-19 14:09:38
 * @Version:1.1.0
 */
public class YdBrandResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /** 品牌标识 */
    private String brandAlias;
    
    /** 品牌名称 */
    private String brandName;
    
    /** 分类的图标 */
    private String icon;

    // ------------------------------------------------- 附加属性 ------------------------------------

    private String merchantName;

    // ------------------------------------------------- get,set ------------------------------------

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getBrandAlias() {
        return brandAlias;
    }
    
    public void setBrandAlias(String brandAlias) {
        this.brandAlias = brandAlias;
    }
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
