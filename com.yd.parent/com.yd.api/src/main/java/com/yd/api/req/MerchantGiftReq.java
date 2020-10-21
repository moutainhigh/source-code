package com.yd.api.req;

import java.io.Serializable;

/**
 * @author wuyc
 * created 2019/12/2 18:00
 **/
public class MerchantGiftReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
