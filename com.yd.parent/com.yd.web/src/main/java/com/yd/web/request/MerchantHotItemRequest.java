package com.yd.web.request;

import com.yd.api.result.decoration.YdMerchantHotItemResult;

import java.io.Serializable;
import java.util.List;

/**
 * 门店首页热门商品排序
 * @author wuyc
 * created 2019/10/31 19:00
 **/
public class MerchantHotItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<YdMerchantHotItemResult> hotItemList;

    public List<YdMerchantHotItemResult> getHotItemList() {
        return hotItemList;
    }

    public void setHotItemList(List<YdMerchantHotItemResult> hotItemList) {
        this.hotItemList = hotItemList;
    }
}
