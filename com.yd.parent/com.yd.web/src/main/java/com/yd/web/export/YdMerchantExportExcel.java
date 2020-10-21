package com.yd.web.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import java.io.Serializable;
import java.util.List;

/**
 * 店铺礼品导出excel
 * @author wuyc
 * created 2020/03/06 12:26
 **/
@ExcelTarget("ydMerchantExportExcel")
public class YdMerchantExportExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "商户名称")
    private String merchantName;

    @Excel(name = "手机号")
    private String mobile;

    @Excel(name = "渠道编码")
    private Integer channel;

    @ExcelCollection(name = "会员开通记录")
    private List<YdMerchantMemberExportExcel> recordList;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public List<YdMerchantMemberExportExcel> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<YdMerchantMemberExportExcel> recordList) {
        this.recordList = recordList;
    }
}
