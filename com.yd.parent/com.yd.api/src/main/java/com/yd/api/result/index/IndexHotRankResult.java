package com.yd.api.result.index;

import com.yd.core.base.bean.BaseModel;

import java.io.Serializable;

/**
 * 首页热销数据
 * @author wuyc
 * created 2019/10/23 13:40
 **/
public class IndexHotRankResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品销量
     */
    private Integer saleCount;

    /**
     * 商品浏览销量
     */
    private Integer browseCount;

    /**
     * 待处理礼品数量
     */
    private Double salePrice;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
}
