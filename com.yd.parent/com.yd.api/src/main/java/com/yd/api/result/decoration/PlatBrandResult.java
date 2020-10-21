package com.yd.api.result.decoration;

import java.io.Serializable;

/**
 * 平台品牌列表
 * @author wuyc
 * created 2019/10/31 16:34
 **/
public class PlatBrandResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String brandAlias;

    private String brandName;

    public String getBrandAlias() {
        return brandAlias;
    }

    public void setBrandAlias(String brandAlias) {
        this.brandAlias = brandAlias;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
