package com.yd.api.result.draw;

import java.io.Serializable;
import com.yd.core.base.bean.BaseModel;

/**
 * @Title:用户抽奖记录
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:35:07
 * @Version:1.1.0
 */
public class YdUserDrawRecordResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    private Integer userId;
    
    private Integer activityId;
    
    /**
     * 奖品id
     */
    private Integer prizeId;
    
    /**
     * 奖励类型 YHQ(优惠券) WZH(未中奖)
     */
    private String prizeType;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户手机号
     */
    private String mobile;
    
    /**
     * 用户中奖领取后的优惠券id
     */
    private Integer userCouponId;

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
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    public Integer getPrizeId() {
        return prizeId;
    }
    
    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }
    public String getPrizeType() {
        return prizeType;
    }
    
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getUserCouponId() {
        return userCouponId;
    }
    
    public void setUserCouponId(Integer userCouponId) {
        this.userCouponId = userCouponId;
    }
}
