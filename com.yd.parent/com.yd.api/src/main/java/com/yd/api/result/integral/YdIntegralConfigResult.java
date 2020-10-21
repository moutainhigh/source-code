package com.yd.api.result.integral;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * @Title:积分配置
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:17:05
 * @Version:1.1.0
 */
public class YdIntegralConfigResult extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    /**
     * 结算比率 百分单号
     */
    private Integer settlementRate;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSettlementRate() {
        return settlementRate;
    }
    
    public void setSettlementRate(Integer settlementRate) {
        this.settlementRate = settlementRate;
    }
}
