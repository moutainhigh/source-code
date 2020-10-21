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
@ExcelTarget("COrder1")
public class COrder1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "外部订单号")
    private String outOrderId;

    @Excel(name = "订单号")
    private Integer orderId;

    @Excel(name = "订单状态")
    private String orderStatus;

    @Excel(name = "用户id")
    private Integer userId;

    @Excel(name = "用户手机号")
    private String mobile;

    @Excel(name = "用户收货地址")
    private String address;

    @Excel(name = "订单总金额")
    private Double orderTotalPrice;

    @Excel(name = "商品总金额")
    private Double itemTotalPrice;

    @Excel(name = "商品成本总金额")
    private Double itemCbTotalPrice;

    @Excel(name = "运费金额")
    private Double freightPrice;

    @Excel(name = "总成本")
    private Double totalCb;

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(Double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public Double getItemCbTotalPrice() {
        return itemCbTotalPrice;
    }

    public void setItemCbTotalPrice(Double itemCbTotalPrice) {
        this.itemCbTotalPrice = itemCbTotalPrice;
    }

    public Double getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(Double freightPrice) {
        this.freightPrice = freightPrice;
    }

    public Double getTotalCb() {
        return totalCb;
    }

    public void setTotalCb(Double totalCb) {
        this.totalCb = totalCb;
    }

}
