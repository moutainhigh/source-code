package com.yd.api.result.login;

import java.io.Serializable;

/**
 * @author wuyc
 * created 2019/10/16 10:19
 **/
public class AdminLoginResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 会话标识
     */
    private String sessionId;

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
