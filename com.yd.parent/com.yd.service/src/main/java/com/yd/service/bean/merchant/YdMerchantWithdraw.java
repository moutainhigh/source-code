package com.yd.service.bean.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title:商户提现记录
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 16:39:54
 * @Version:1.1.0
 */
public class YdMerchantWithdraw extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 提现的门店id */
    private Integer storeId;
    
    /** 提现的商户id */
    private Integer merchantId;
    
    /** 组别(门店提现merchant，经销商提现supplier) */
    private String groupCode;
    
    /** 提现金额 */
    private Double withdrawAmount;

    /** 提现手续费 */
    private Double rateAmount;
    
    /** 提现微信号 */
    private String withdrawOpenId;
    
    /** 真实姓名 */
    private String realname;
    
    /** 身份证号 */
    private String idCard;
    
    /** 审核状态(WAIT|FAIL|SUCCESS),现在全部SUCCESS，后期开通审核后在做)  */
    private String status;
    
    /** 打款状态 */
    private String moneyStatus;
    
    /** 打款时间 */
    private Date moneyTime;
    
    /** 流水号 */
    private String billNo;
    
    /** 打款失败提示 */
    private String msg;
    
    /** 备注 */
    private String remark;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getStoreId() {
        return storeId;
    }
    
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public String getGroupCode() {
        return groupCode;
    }
    
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
    public Double getWithdrawAmount() {
        return withdrawAmount;
    }
    
    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }
    public String getWithdrawOpenId() {
        return withdrawOpenId;
    }
    
    public void setWithdrawOpenId(String withdrawOpenId) {
        this.withdrawOpenId = withdrawOpenId;
    }
    public String getRealname() {
        return realname;
    }
    
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getIdCard() {
        return idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMoneyStatus() {
        return moneyStatus;
    }
    
    public void setMoneyStatus(String moneyStatus) {
        this.moneyStatus = moneyStatus;
    }
    public Date getMoneyTime() {
        return moneyTime;
    }
    
    public void setMoneyTime(Date moneyTime) {
        this.moneyTime = moneyTime;
    }
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Double getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(Double rateAmount) {
        this.rateAmount = rateAmount;
    }
}
