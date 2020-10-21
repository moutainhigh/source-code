package com.yd.api.result.message;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:商户消息通知
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-17 17:33:49
 * @Version:1.1.0
 */
public class YdMerchantMessageResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = -1L;
    
    private Integer id;
    
    private Integer merchantId;
    
    private Integer pid;
    
    /** 消息类型 platform_gift_up(平台礼品上架)  user_submit_order(用户下单) */
    private String messageType;
    
    /** 根据message_type 判断，可能是orderId 或者是giftId  */
    private Integer outOrderId;
    
    /** 消息内容 */
    private String contents;
    
    /** 是否已读  Y | N, 默认未读 */
    private String isRead;

    // ---------------------------------       附加     --------------------------------

    private String startTime;

    private String endTime;

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
    public Integer getPid() {
        return pid;
    }
    
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public Integer getOutOrderId() {
        return outOrderId;
    }
    
    public void setOutOrderId(Integer outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getContents() {
        return contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getIsRead() {
        return isRead;
    }
    
    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
