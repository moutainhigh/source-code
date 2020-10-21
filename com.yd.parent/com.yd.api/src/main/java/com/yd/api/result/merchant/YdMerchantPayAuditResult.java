package com.yd.api.result.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户支付申请管理
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-25 11:17:40
 * @Version:1.1.0
 */
public class YdMerchantPayAuditResult extends BaseModel implements Serializable{
    
    private Integer id;

    private Integer merchantId;

    private String merchantName;

    /**
     * 审核状态 WAIT | REFUSE | SUCCESS
     */
    private String auditStatus;

    private String contact;

    private String roleIds;

    private String mobile;

    private String roleNames;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
}
