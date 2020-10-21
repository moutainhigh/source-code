package com.yd.web.export.test;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2020/6/17 20:07
 **/
@ExcelTarget("订单导出")
public class POrder1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "主订单号", needMerge = true)
    private Integer pOrderId;

    @ExcelCollection(name = "子订单信息")
    private List<COrder1> childList;

    public Integer getpOrderId() {
        return pOrderId;
    }

    public void setpOrderId(Integer pOrderId) {
        this.pOrderId = pOrderId;
    }

    public List<COrder1> getChildList() {
        return childList;
    }

    public void setChildList(List<COrder1> childList) {
        this.childList = childList;
    }
}
