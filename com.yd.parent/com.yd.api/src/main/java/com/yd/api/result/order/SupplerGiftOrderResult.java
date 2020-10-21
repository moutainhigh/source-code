package com.yd.api.result.order;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * 供应商订单列表返回值
 * @author wuyc
 * created 2019/11/30 15:46
 **/
public class SupplerGiftOrderResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer giftOrderId;

    private Integer supplierId;

    private Integer merchantId;

    private String supplerName;

    private String merchantName;

    private Integer giftId;

    private Integer num;

    private String orderStatus;

    private String expressCompany;

    private String expressCompanyNumber;

    private String expressOrderId;

    private String realname;

    private String mobile;

    private Integer provinceId;

    private String province;

    private Integer cityId;

    private String city;

    private Integer districtId;

    private String district;

    private String address;

}
