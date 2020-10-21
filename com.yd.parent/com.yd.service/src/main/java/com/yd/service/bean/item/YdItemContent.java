package com.yd.service.bean.item;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:商品图文信息
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:23:13
 * @Version:1.1.0
 */
public class YdItemContent extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 商品编号
     */
    private Integer itemId;
    
    /**
     * 商品详情,富文本内容
     */
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
