package com.yd.api.result.user;

import java.io.Serializable;

import com.yd.core.base.bean.BaseModel;

/**
 * @Title:用户授权表
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 10:59:46
 * @Version:1.1.0
 */
public class YdUserAuthResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    
    /** 1:微信  */
    private Integer type;
    
    private Integer userId;
    
    /** 唯一编号 微信的openId */
    private String openId;
    
    private String unionId;
    
    private String nickname;
    
    private String headImage;
    
    /**
     * 性别 0:未知 1:男 2:女 
     */
    private Integer sex;


    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getOpenId() {
        return openId;
    }
    
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getUnionId() {
        return unionId;
    }
    
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getHeadImage() {
        return headImage;
    }
    
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
    public Integer getSex() {
        return sex;
    }
    
    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
