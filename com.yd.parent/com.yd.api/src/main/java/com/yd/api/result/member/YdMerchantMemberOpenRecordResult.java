package com.yd.api.result.member;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title:优度商户会员开通记录
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:58:04
 * @Version:1.1.0
 */
public class YdMerchantMemberOpenRecordResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    /** 用户id
     */
    private Integer merchantId;

    private Integer roleId;
    
    /** 会员等级
     */
    private Integer memberLevel;
    
    /** 会员类型 (升级版，普通版)
     */
    private String memberType;
    
    /** 有效时长(月为单位)
     */
    private Integer validLength;

    /** 会员有效开始时间 */
    private Date startTime;

    /** 会员有效结束时间 */
    private Date endTime;
    
    /** 支付金额
     */
    private Double payPrice;
    
    /** 开通类型: SJ(会员升级), XF(会员续费),ZC(会员开通,注册)
     */
    private String openType;
    
    /** 开通方式 (自动开通, 人工开通)
     */
    private String openMethod;
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
    public Integer getMemberLevel() {
        return memberLevel;
    }
    
    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
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
    public Double getPayPrice() {
        return payPrice;
    }
    
    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }
    public String getOpenType() {
        return openType;
    }
    
    public void setOpenType(String openType) {
        this.openType = openType;
    }
    public String getOpenMethod() {
        return openMethod;
    }
    
    public void setOpenMethod(String openMethod) {
        this.openMethod = openMethod;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
