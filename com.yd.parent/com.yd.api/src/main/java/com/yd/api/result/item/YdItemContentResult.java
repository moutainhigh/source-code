package com.yd.api.result.item;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商品图文信息
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:23:13
 * @Version:1.1.0
 */
public class YdItemContentResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer itemId;
    
    private String content;

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
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
