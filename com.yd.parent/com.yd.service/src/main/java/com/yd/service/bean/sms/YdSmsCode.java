package com.yd.service.bean.sms;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;
import java.util.Date;

/**
 * @Title:短信验证码
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
public class YdSmsCode extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    private Integer id;

    /**
     * 接收短信的手机号
     */
    private String mobile;
    
    /**
     * 验证码信息(短信内容)
     */
    private String smsCode;
    
    /**
     * 短信来源(注册,登录...)
     */
    private String source;
    
    /**
     * 发送短信的操作平台
     */
    private String platform;
    
    /**
     * 有效时间
     */
    private Date validTime;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getSmsCode() {
        return smsCode;
    }
    
    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }
}
