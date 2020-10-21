package com.yd.web.request;

import com.yd.api.result.decoration.YdMerchantCompareItemResult;
import com.yd.api.result.market.YdWelfareManagerResult;

import java.io.Serializable;
import java.util.List;

/**
 * 商户比价商品排序
 * @author wuyc
 * created 2020/05/9 21:02
 **/
public class MerchantCompareItemRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<YdMerchantCompareItemResult> dataList;

    public List<YdMerchantCompareItemResult> getDataList() {
        return dataList;
    }

    public void setDataList(List<YdMerchantCompareItemResult> dataList) {
        this.dataList = dataList;
    }
}
