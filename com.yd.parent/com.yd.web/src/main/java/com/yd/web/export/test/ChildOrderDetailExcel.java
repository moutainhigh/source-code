package com.yd.web.export.test;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;

/**
 * @author wuyc
 * created 2020/6/17 20:07
 **/
@ExcelTarget("childOrderDetailExcel")
public class ChildOrderDetailExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "商品id")
    private Integer itemId;

    @Excel(name = "商品名称")
    private String title;

    @Excel(name = "商品规格")
    private String itemSpec;

    @Excel(name = "一级分类")
    private String firstCategoryName;

    @Excel(name = "二级分类")
    private String secondCategoryName;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemSpec() {
        return itemSpec;
    }

    public void setItemSpec(String itemSpec) {
        this.itemSpec = itemSpec;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }

    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName;
    }
}
