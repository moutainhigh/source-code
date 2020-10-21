package com.yd.api.result.merchant;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户渠道
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-30 13:02:38
 * @Version:1.1.0
 */
public class YdMerchantChannelResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 邀请商户的渠道号
     */
    private Integer id;
    

    /**
     * 用户绑定手机号
     */
    private Integer merchantId;
    
    /**
     * 邀请链接
     */
    private String inviteUrl;
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
    public String getInviteUrl() {
        return inviteUrl;
    }
    
    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}
