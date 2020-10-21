package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title:礼品订单明细表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:54:16
 * @Version:1.1.0
 */
public class YdGiftOrderDetailResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer giftOrderId;

    private String giftOrderDetailNo;
    
    private Integer supplierId;
    
    private Integer giftId;

    private Integer merchantGiftId;
    
    /**
     * 真实售价(如果为阶梯价的话显示阶梯价)
     */
    private Double salePrice;
    
    private Double salePriceTotal;
    
    private Double marketPrice;

    private Double purchasePrice;
    
    private Integer num;

    private String orderStatus;

    private String settlementStatus;

    private Date confirmGoodsTime;

    private Date deliveryTime;
    
    private String expressCompany;
    
    private String expressCompanyNumber;
    
    private String expressOrderId;

    // ------------------------------ 附加

    // 交易类型 线上online | 线下 offline(平台自己导入的叫线下订单，只有平台可以看)
    private String transType;

    private String title;

    private String subTitle;

    private String giftDesc;

    private String imageUrl;

    private String supplierName;

    private String merchantName;

    private String startTime;

    private String endTime;

    private List<YdGiftOrderDetailResult> childList;

    private String realname;

    private String mobile;

    private String province;

    private String city;

    private String district;

    private String address;

    private String receiveWay;

    /**
     * WAIT | SUCCESS
     */
    private String payStatus;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getGiftOrderId() {
        return giftOrderId;
    }
    
    public void setGiftOrderId(Integer giftOrderId) {
        this.giftOrderId = giftOrderId;
    }
    public Integer getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
    public Integer getGiftId() {
        return giftId;
    }
    
    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }
    public Double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
    public Double getSalePriceTotal() {
        return salePriceTotal;
    }
    
    public void setSalePriceTotal(Double salePriceTotal) {
        this.salePriceTotal = salePriceTotal;
    }
    public Double getMarketPrice() {
        return marketPrice;
    }
    
    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }
    public Integer getNum() {
        return num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getExpressCompany() {
        return expressCompany;
    }
    
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }
    public String getExpressCompanyNumber() {
        return expressCompanyNumber;
    }
    
    public void setExpressCompanyNumber(String expressCompanyNumber) {
        this.expressCompanyNumber = expressCompanyNumber;
    }
    public String getExpressOrderId() {
        return expressOrderId;
    }
    
    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getConfirmGoodsTime() {
        return confirmGoodsTime;
    }

    public void setConfirmGoodsTime(Date confirmGoodsTime) {
        this.confirmGoodsTime = confirmGoodsTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<YdGiftOrderDetailResult> getChildList() {
        return childList;
    }

    public void setChildList(List<YdGiftOrderDetailResult> childList) {
        this.childList = childList;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGiftOrderDetailNo() {
        return giftOrderDetailNo;
    }

    public void setGiftOrderDetailNo(String giftOrderDetailNo) {
        this.giftOrderDetailNo = giftOrderDetailNo;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Integer getMerchantGiftId() {
        return merchantGiftId;
    }

    public void setMerchantGiftId(Integer merchantGiftId) {
        this.merchantGiftId = merchantGiftId;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }


    public String getGiftDesc() {
        return giftDesc;
    }

    public void setGiftDesc(String giftDesc) {
        this.giftDesc = giftDesc;
    }


    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getReceiveWay() {
        return receiveWay;
    }

    public void setReceiveWay(String receiveWay) {
        this.receiveWay = receiveWay;
    }
}
