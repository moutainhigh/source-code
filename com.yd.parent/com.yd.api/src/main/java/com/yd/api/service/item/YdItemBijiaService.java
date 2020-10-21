package com.yd.api.service.item;

import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.crawer.result.YdBijiaResult;
import com.yd.api.result.item.YdItemBrowseHistoryResult;
import com.yd.core.utils.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * @Title:商品浏览记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:29:34
 * @Version:1.1.0
 */
public interface YdItemBijiaService {

    List<CrawerSiteSkuResult> getBijiaBySkuId(Integer skuId) throws BusinessException;

	void updateItemCompareConfig(Integer skuId, String siteName, String link) throws BusinessException;

	void synItemNewPrice();
}
