package com.yd.web.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;

/**
 * 店铺礼品导出excel
 * @author wuyc
 * created 2020/03/06 12:26
 **/
@ExcelTarget("店铺礼品导出")
public class YdMerchantGiftExcel implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Excel(name = "开始时间", format = JodaTime.YYYY_MM_DD_HH_MM_SS)
//    private Date createTime;

    @Excel(name = "礼品名称")
    private String title;

    @Excel(name = "副标题")
    private String subTitle;

    @Excel(name = "采购单价")
    private Double salePrice;

    @Excel(name = "划线价")
    private Double marketPrice;

    @Excel(name = "礼品图片地址")
    private String imageUrl;

    @Excel(name = "礼品状态")
    private String giftStatus;

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

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(String giftStatus) {
        this.giftStatus = giftStatus;
    }
}
