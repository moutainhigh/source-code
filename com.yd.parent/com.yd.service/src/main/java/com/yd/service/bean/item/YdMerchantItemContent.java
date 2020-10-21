package com.yd.service.bean.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户商品详情
 * @Description:
 * @Author:Wuyc
 * @Since:2020-04-20 17:24:09
 * @Version:1.1.0
 */
public class YdMerchantItemContent extends BaseModel implements Serializable{
    
    private Integer id;
    
    /** 商户商品编号 */
    private Integer itemId;
    
    /**  商品详情,富文本内容 */
    private String content;

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
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
