package com.yg.core.base.bean;

import java.util.Date;

/**
 * @author wuyc
 * created 2019/10/16 16:53
 **/
public class BaseModel{
    private Date createTime;
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
