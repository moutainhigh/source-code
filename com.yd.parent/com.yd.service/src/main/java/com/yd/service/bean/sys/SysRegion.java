package com.yd.service.bean.sys;

import java.io.Serializable;

/**
 * @Title:地区表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-03 16:21:47
 * @Version:1.1.0
 */
public class SysRegion implements Serializable{
    
    private Integer id;
    
    /**
     * 行政区域父ID，例如区县的pid指向市，市的pid指向省，省的pid则是0
     */
    private Integer pid;
    
    /**
     * 行政区域名称
     */
    private String name;
    
    /**
     * 行政区域类型，如如1则是省， 如果是2则是市，如果是3则是区县
     */
    private Integer type;
    
    /**
     * 行政区域编码
     */
    private Integer code;

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
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
}
