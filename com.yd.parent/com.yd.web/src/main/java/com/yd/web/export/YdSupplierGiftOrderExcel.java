package com.yd.web.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.yd.core.utils.JodaTime;
//import org.jeecgframework.poi.excel.annotation.Excel;
//import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商礼品订单导出excel
 * @author wuyc
 * created 2020/03/06 12:26
 **/
@ExcelTarget("供应商礼品订单导出")
public class YdSupplierGiftOrderExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "订单号")
    private Integer id;

    @Excel(name = "下单时间", format = JodaTime.YYYY_MM_DD_HH_MM_SS)
    private Date createTime;

    @Excel(name = "礼品名称")
    private String title;

    @Excel(name = "副标题")
    private String subTitle;

    @Excel(name = "礼品图片地址")
    private String imageUrl;

    @Excel(name = "收货人")
    private String realname;

    @Excel(name = "收货人手机号")
    private String mobile;

    @Excel(name = "收货人地址")
    private String address;

    @Excel(name = "订单状态")
    private String orderStatus;

    @Excel(name = "供应商")
    private String supplierName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
