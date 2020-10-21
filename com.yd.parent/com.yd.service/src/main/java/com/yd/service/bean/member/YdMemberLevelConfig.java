package com.yd.service.bean.member;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:优度会员配置
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:33:56
 * @Version:1.1.0
 */
public class YdMemberLevelConfig extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer memberLevel;
    
    /** 会员类型: 升级版，普通版
     */
    private String memberType;

    /** 角色类型 */
    private Integer roleId;
    
    /** 有效时长(月为单位)
     */
    private Integer validLength;
    
    /** 会员价
     */
    private Double memberPrice;
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    public Double getMemberPrice() {
        return memberPrice;
    }
    
    public void setMemberPrice(Double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
