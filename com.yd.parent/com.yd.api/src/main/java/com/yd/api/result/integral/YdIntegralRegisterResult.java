package com.yd.api.result.integral;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:积分登记
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:42:46
 * @Version:1.1.0
 */
public class YdIntegralRegisterResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    /**
     * HBW(汉堡王), ZY(中域)
     */
    private String type;
    
    private Integer merchantId;
    
    private Integer pid;
    
    private String mobile;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public Integer getMerchantId() {
        return merchantId;
    }
    
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
    public Integer getPid() {
        return pid;
    }
    
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
