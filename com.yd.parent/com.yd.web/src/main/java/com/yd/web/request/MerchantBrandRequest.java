package com.yd.web.request;

import com.yd.api.result.decoration.YdMerchantBrandResult;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌排序请求
 * @author wuyc
 * created 2019/10/31 17:57
 **/
public class MerchantBrandRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<YdMerchantBrandResult> brandList;

    public List<YdMerchantBrandResult> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<YdMerchantBrandResult> brandList) {
        this.brandList = brandList;
    }
}
