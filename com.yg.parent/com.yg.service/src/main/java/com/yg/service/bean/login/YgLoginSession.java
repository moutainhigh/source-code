package com.yg.service.bean.login;

import com.yg.core.base.bean.BaseModel;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title:登录session
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 09:49:07
 * @Version:1.1.0
 */
public class YgLoginSession extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 登录时间
     */
    private Date loginTime;
    
    /**
     * 登陆者id编号
     */
    private Integer loginUserId;
    
    /**
     * 会话标识
     */
    private String sessionId;
    
    /**
     * 登录用户来源(C端，B端)
     */
    private String userSource;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getLoginTime() {
        return loginTime;
    }
    
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
    public Integer getLoginUserId() {
        return loginUserId;
    }
    
    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getUserSource() {
        return userSource;
    }
    
    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }
}
