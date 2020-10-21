package com.yd.service.bean.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:供货商礼品订单
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 17:26:44
 * @Version:1.1.0
 */
public class YdSupplerGiftOrder extends BaseModel implements Serializable{
    
    private Integer id;
    
    /**
     * 外部编号
     */
    private String outOrderId;
    
    /**
     * 用户下单 | 商户采购 | 
     */
    private String type;
    
    /**
     * 是否付款 Y|N
     */
    private String payStatus;
    
    /**
     * WAIT(未发货)|SUCCESS(已发货)
     */
    private String orderStatus;
    
    /**
     * 用户id
     */
    private Integer userId;
    
    /**
     * 供应商id
     */
    private Integer supplierId;
    
    /**
     * 礼品id
     */
    private Integer giftId;
    
    /**
     * 收货人
     */
    private String realname;
    
    /**
     * 收货人电话
     */
    private String mobile;
    
    private Integer provinceId;
    
    private String province;
    
    private Integer cityId;
    
    private String city;
    
    private Integer districtId;
    
    private String district;
    
    private String address;
    
    /**
     * 快递公司
     */
    private String expressCompany;
    
    /**
     * 快递公司编号
     */
    private String expressCompanyNumber;
    
    /**
     * 快递单号
     */
    private String expressOrderId;

    public Integer getId() {
        return id;
    }
    
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
    public String getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
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
}
