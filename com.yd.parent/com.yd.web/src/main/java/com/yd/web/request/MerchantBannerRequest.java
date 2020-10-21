package com.yd.web.request;

import com.yd.api.result.decoration.YdMerchantBannerResult;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuyc
 * created 2019/10/22 15:05
 **/
public class MerchantBannerRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    List<YdMerchantBannerResult> bannerList;

    public List<YdMerchantBannerResult> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<YdMerchantBannerResult> bannerList) {
        this.bannerList = bannerList;
    }
}
