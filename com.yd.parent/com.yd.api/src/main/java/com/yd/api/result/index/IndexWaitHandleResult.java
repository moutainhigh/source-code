package com.yd.api.result.index;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * 首页待处理数据
 * @author wuyc
 * created 2019/10/23 13:40
 **/
public class IndexWaitHandleResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待处理订单数量 */
    private Integer orderCount;

    /** 待处理商品数量 */
    private Integer itemCount;

    /** 待处理礼品数量 */
    private Integer giftCount;

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(Integer giftCount) {
        this.giftCount = giftCount;
    }
}
