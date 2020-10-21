package com.yg.api.result.merchant;

import com.yg.core.base.bean.BaseModel;
import java.io.Serializable;

/**
 * @Title:商户信息
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-25 16:44:35
 * @Version:1.1.0
 */
public class YgMerchantResult extends BaseModel implements Serializable{
    
    private Integer id;
    
    private String merchantName;
    private String mobile;
    
    private String contact;
    
    private String password;
    
    private Integer commissionRate;
    
    private Integer roleId;

    private String groupCode;
    
    private String isEnable;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getCommissionRate() {
        return commissionRate;
    }
    
    public void setCommissionRate(Integer commissionRate) {
        this.commissionRate = commissionRate;
    }
    public Integer getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public String getIsEnable() {
        return isEnable;
    }
    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
