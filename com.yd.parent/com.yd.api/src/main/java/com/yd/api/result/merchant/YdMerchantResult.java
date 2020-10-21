package com.yd.api.result.merchant;

import com.yd.api.result.member.YdMerchantMemberOpenRecordResult;
import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Title:优度后台商户
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:06
 * @Version:1.1.0
 */
public class YdMerchantResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer pid;

    /** 会员类型 */
    private String memberType;

    /** 会员等级 */
    private Integer memberLevel;

    private Date memberValidTime;

    private String merchantName;

    private String mobile;

    private String password;

    private String contact;

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

    private String memberValidTimeStr;

    /** 商户图片地址 */
    private String merchantUrl;

    /** 上传的同意协议地址 */
    private String protocolUrl;

    /** 营业执照地址，多张用逗号隔开 */
    private String licenseUrls;

    private String wxOpenId;

    /** 商户公众号二维码 */
    private String publicQrCode;

    /** 商户商城二维码 */
    private String shopQrCode;

    private String isOpenPay;

    private String payPassword;
    
    private String hasSetPayPassword;

    /** Y|N 是否开启在线比价  */
    private String isComparePrice;

    /** Y|N 是否开启旧机抵扣 */
    private String isOldMachineReduce;

    private String roleIds;
    private String roleNames;
    private String groupCode;

    private Integer inviteId;

    private String isFlag;

    /** 店铺H5页面的地址 */
    private String shopUrl;

    // ------------------------------------------------- 附加属性 ------------------------------------

    /** 会员状态 valid代表未过期，invalid代表已过期  */
    private String memberStatus;

    private String isOpenVr;

    private String vrJumpUrl;

    private String bindWxDetail;

    private List<YdMerchantMemberOpenRecordResult> memberRecodeList;

    // ------------------------------------------------- get,set ------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public Date getMemberValidTime() {
        return memberValidTime;
    }

    public void setMemberValidTime(Date memberValidTime) {
        this.memberValidTime = memberValidTime;
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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public String getIsComparePrice() {
        return isComparePrice;
    }

    public void setIsComparePrice(String isComparePrice) {
        this.isComparePrice = isComparePrice;
    }

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

    public String getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(String businessDay) {
        this.businessDay = businessDay;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getIsOpenPay() {
        return isOpenPay;
    }

    public void setIsOpenPay(String isOpenPay) {
        this.isOpenPay = isOpenPay;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getHasSetPayPassword() {
		return hasSetPayPassword;
	}

	public void setHasSetPayPassword(String hasSetPayPassword) {
		this.hasSetPayPassword = hasSetPayPassword;
	}

	public String getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public String getMemberValidTimeStr() {
        return memberValidTimeStr;
    }

    public void setMemberValidTimeStr(String memberValidTimeStr) {
        this.memberValidTimeStr = memberValidTimeStr;
    }

    public String getIsOldMachineReduce() {
        return isOldMachineReduce;
    }

    public void setIsOldMachineReduce(String isOldMachineReduce) {
        this.isOldMachineReduce = isOldMachineReduce;
    }

	public String getBindWxDetail() {
		return bindWxDetail;
	}

	public void setBindWxDetail(String bindWxDetail) {
		this.bindWxDetail = bindWxDetail;
	}

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public List<YdMerchantMemberOpenRecordResult> getMemberRecodeList() {
        return memberRecodeList;
    }

    public void setMemberRecodeList(List<YdMerchantMemberOpenRecordResult> memberRecodeList) {
        this.memberRecodeList = memberRecodeList;
    }

    public String getPublicQrCode() {
        return publicQrCode;
    }

    public void setPublicQrCode(String publicQrCode) {
        this.publicQrCode = publicQrCode;
    }

    public String getShopQrCode() {
        return shopQrCode;
    }

    public void setShopQrCode(String shopQrCode) {
        this.shopQrCode = shopQrCode;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }


    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getIsOpenVr() {
        return isOpenVr;
    }

    public void setIsOpenVr(String isOpenVr) {
        this.isOpenVr = isOpenVr;
    }

    public String getVrJumpUrl() {
        return vrJumpUrl;
    }

    public void setVrJumpUrl(String vrJumpUrl) {
        this.vrJumpUrl = vrJumpUrl;
    }
}