package com.yd.service.bean.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:门店信息审核记录表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-05 18:31:13
 * @Version:1.1.0
 */
public class YdMerchantInfoAudit extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /**
     * 审核状态 WAIT(待审核), SUCCESS(审核通过), REFUSE(审核拒绝)
     */
    private String auditStatus;

    private Integer merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 商户手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String contact;

    /**
     * 身份证号码
     */
    private String card;

    private Double longitude;

    private Double latitude;

    private String province;

    private String city;

    private String district;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String address;

    private String businessDay;

    private String businessStartTime;

    /**
     * 营业结束时间
     */
    private String businessEndTime;

    /**
     * 商户图片地址
     */
    private String merchantUrl;

    /**
     * 上传的同意协议地址
     */
    private String protocolUrl;

    /**
     * 营业执照地址，多张用逗号隔开
     */
    private String licenseUrls;

    private String publicQrCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
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
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(String businessDay) {
        this.businessDay = businessDay;
    }
    public String getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(String businessStartTime) {
        this.businessStartTime = businessStartTime;
    }
    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }
    public String getMerchantUrl() {
        return merchantUrl;
    }

    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }
    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }
    public String getLicenseUrls() {
        return licenseUrls;
    }

    public void setLicenseUrls(String licenseUrls) {
        this.licenseUrls = licenseUrls;
    }

    public String getPublicQrCode() {
        return publicQrCode;
    }

    public void setPublicQrCode(String publicQrCode) {
        this.publicQrCode = publicQrCode;
    }
}
