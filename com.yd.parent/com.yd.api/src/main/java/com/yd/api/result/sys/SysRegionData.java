package com.yd.api.result.sys;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/3 16:45
 **/
public class SysRegionData implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 行政区域名称
     */
    private String name;

    private List<SysRegionData> childList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SysRegionData> getChildList() {
        return childList;
    }

    public void setChildList(List<SysRegionData> childList) {
        this.childList = childList;
    }
}
