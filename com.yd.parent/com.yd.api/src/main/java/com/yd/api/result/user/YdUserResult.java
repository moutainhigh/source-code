package com.yd.api.result.user;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

import java.util.Date;

/**
 * @Title:用户
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:54:35YdUserResult
 * @Version:1.1.0YdShopUserResult
 */
public class YdUserResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String mobile;

    private String nickname;

    private String image;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
