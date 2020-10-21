package com.yd.web.request;

import com.yd.api.result.market.YdWelfareManagerResult;

import java.io.Serializable;
import java.util.List;

/**
 * 福利排序
 * @author wuyc
 * created 2020/05/9 21:02
 **/
public class WelfareManagerRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<YdWelfareManagerResult> dataList;

    public List<YdWelfareManagerResult> getDataList() {
        return dataList;
    }

    public void setDataList(List<YdWelfareManagerResult> dataList) {
        this.dataList = dataList;
    }
}
