package com.yd.api.crawer;

import java.util.List;
import java.util.Map;

import com.yd.api.crawer.result.BijiaColumn;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.crawer.result.CrawerTmallItemResult;

public interface CrawerService {

	void addBrand(CrawerBrandResult brand);

	List<CrawerBrandResult> getBrandList();

	void addItem(CrawerItemResult item);

	List<CrawerItemResult> getItemList();

	void updateExecuteStatus(Integer id);

	CrawerItemResult getDetail(Integer id);

	List<CrawerSiteBrandResult> getAllCrawerSiteBrand();

	List<CrawerTmallItemResult> getUndoTmallItem();

	void addSku(CrawerSiteSkuResult result);

	void updateTmallItemStatus(Integer id);

	void updateTmallItemTitle(Integer id, String title);

	List<CrawerSiteSkuResult> getUndoSkuList();
	
	List<CrawerSiteSkuResult> getCoverNullList();

	int updateSkuByLink(CrawerSiteSkuResult ss);

	List<CrawerSiteSkuResult> getAllSkuList();

	Map<String, List<CrawerSiteSkuResult>> search(Integer skuId);
	
	List<CrawerSiteSkuResult> search(String siteName,String brand,String title);

	List<BijiaColumn> getBijia(Integer merchantSkuId);

	void createSearchIndex();

	void chooseItem(Integer skuId, String currSite,Integer itemId);

	/**
	 * 商品库商品绑定平台商品
	 */
	void bindOtherPlatformItem();
}
