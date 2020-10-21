package com.yd.service.bean.member;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:优度商户会员注册，续费，升级支付记录表
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:59:25
 * @Version:1.1.0
 */
public class YdMerchantMemberPayRecord extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;

    /** 申请类型 :ZC(注册) , XF(续费)，SJ(升级)
     */
    private String applyType;
    
    /** 商户手机号
     */
    private String mobile;
    
    /** 商户id， 注册不需要，续费需要
     */
    private Integer merchantId;
    
    /** 商户申请会员填写的密码(续费不需要)
     */
    private String password;
    
    /** 升级版，普通版
     */
    private String memberType;
    
    /** 有效时长(月为单位)
     */
    private Integer validLength;
    
    /** 会员价
     */
    private Double memberPrice;

    /** 会员等级 */
    private Integer memberLevel;

    private Integer roleId;

    /** 邀请人id */
    private Integer inviteId;
    
    /** 是否支付 N | Y
     */
    private String isPay;
    
    /** 流水号
     */
    private String billNo;
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getApplyType() {
        return applyType;
    }
    
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMemberType() {
        return memberType;
    }
    
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
    public Integer getValidLength() {
        return validLength;
    }
    
    public void setValidLength(Integer validLength) {
        this.validLength = validLength;
    }
    public Double getMemberPrice() {
        return memberPrice;
    }
    
    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }
    public String getIsPay() {
        return isPay;
    }
    
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
