package com.yd.api.result.user;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:用户收货地址
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-28 10:17:22
 * @Version:1.1.0
 */
public class YdUserAddressResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    private Integer userId;
    
    /**
     * 姓名
     */
    private String realname;
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 性别(male,female)
     */
    private String sex;
    
    /**
     * 是否是默认地址(Y|N)
     */
    private String isDefault;
    
    private Integer provinceId;
    
    private Integer cityId;
    
    private Integer districtId;
    
    private String province;
    
    private String city;
    
    private String district;
    
    private String address;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
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
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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
}
