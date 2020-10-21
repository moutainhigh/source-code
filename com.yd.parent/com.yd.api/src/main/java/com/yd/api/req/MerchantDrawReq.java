package com.yd.api.req;

import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdMerchantDrawPrizeResult;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/12/4 12:43
 **/
public class MerchantDrawReq implements Serializable {

    private static final long serialVersionUID = 1L;

    YdMerchantDrawActivityResult drawActivity;

    List<YdMerchantDrawPrizeResult> prizeList;

    public YdMerchantDrawActivityResult getDrawActivity() {
        return drawActivity;
    }

    public void setDrawActivity(YdMerchantDrawActivityResult drawActivity) {
        this.drawActivity = drawActivity;
    }

    public List<YdMerchantDrawPrizeResult> getPrizeList() {
        return prizeList;
    }

    public void setPrizeList(List<YdMerchantDrawPrizeResult> prizeList) {
        this.prizeList = prizeList;
    }
}
