package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title:礼品订单主表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:53:33
 * @Version:1.1.0
 */
public class YdGiftOrderResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 订单编号
     */
    private String outOrderId;

    private String giftOrderNo;

    /**
     * in(入库,购买礼品商城的) | out(出库,商城下单的礼品)
     */
    private String type;

    /** 交易类型 线上online | 线下 offline(平台自己导入的叫线下订单，只有平台可以看) */
    private String transType;

    private Integer userId;
    
    private Integer merchantId;
    
    /** WAIT | SUCCESS  */
    private String orderStatus;
    
    private String payStatus;
    
    private Date payTime;
    
    private String billNo;


    // 订单礼品总数量
    private Integer totalGiftCount;

    // 子订单总数量
    private Integer totalDetailCount;
    
    private Double totalSalePrice;
    
    private Double totalMarketPrice;
    
    private String realname;
    
    private String mobile;
    
    private Integer provinceId;
    
    private String province;
    
    private Integer cityId;
    
    private String city;
    
    private Integer districtId;
    
    private String district;
    
    private String address;

    public Integer getId() {
        return id;
    }


    //-----------------------------------附加

    private Integer supplierId;

    private String supplierName;

    private String merchantName;

    private String startTime;

    private String endTime;

    /** 商户确认自提订单，计算购买供应商礼品订单的总金额 */
    private Double payPrice;

    private List<YdGiftOrderDetailResult> detailList;


    public void setId(Integer id) {
        this.id = id;
    }
    public String getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    public Date getPayTime() {
        return payTime;
    }
    
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Double getTotalSalePrice() {
        return totalSalePrice;
    }
    
    public void setTotalSalePrice(Double totalSalePrice) {
        this.totalSalePrice = totalSalePrice;
    }
    public Double getTotalMarketPrice() {
        return totalMarketPrice;
    }
    
    public void setTotalMarketPrice(Double totalMarketPrice) {
        this.totalMarketPrice = totalMarketPrice;
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
    public Integer getProvinceId() {
        return provinceId;
    }
    
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    public Integer getCityId() {
        return cityId;
    }
    
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public Integer getDistrictId() {
        return districtId;
    }
    
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
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

    public Integer getTotalGiftCount() {
        return totalGiftCount;
    }

    public void setTotalGiftCount(Integer totalGiftCount) {
        this.totalGiftCount = totalGiftCount;
    }

    public Integer getTotalDetailCount() {
        return totalDetailCount;
    }

    public void setTotalDetailCount(Integer totalDetailCount) {
        this.totalDetailCount = totalDetailCount;
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

    public List<YdGiftOrderDetailResult> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<YdGiftOrderDetailResult> detailList) {
        this.detailList = detailList;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public String getGiftOrderNo() {
        return giftOrderNo;
    }

    public void setGiftOrderNo(String giftOrderNo) {
        this.giftOrderNo = giftOrderNo;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
}
