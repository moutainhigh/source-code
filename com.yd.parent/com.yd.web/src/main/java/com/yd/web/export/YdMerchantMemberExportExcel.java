package com.yd.web.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.yd.core.utils.JodaTime;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺礼品导出excel
 * @author wuyc
 * created 2020/03/06 12:26
 **/
@ExcelTarget("ydMerchantMemberExportExcel")
public class YdMerchantMemberExportExcel implements Serializable {

    // @ExcelTarget("excelExportMilkDetailOrder")
    private static final long serialVersionUID = 1L;

    @Excel(name = "开通时间", format = JodaTime.YYYY_MM_DD_HH_MM_SS)
    private Date startTime;

    @Excel(name = "会员类型")
    private String memberType;

    @Excel(name = "开通类型")
    private String openType;

    @Excel(name = "交易金额")
    private Double payPrice;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
}
