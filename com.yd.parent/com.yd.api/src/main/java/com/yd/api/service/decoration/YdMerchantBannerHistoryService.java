package com.yd.api.service.decoration;

import com.yd.api.result.decoration.YdMerchantBannerHistoryResult;

import java.util.List;

/**
 * @Title:商户历史banner图Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:34:30
 * @Version:1.1.0
 */
public interface YdMerchantBannerHistoryService {

	/**
	 * 通过id得到商户历史banner图YdShopMerchantBannerHistory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBannerHistoryResult getYdShopMerchantBannerHistoryById(Integer id);

	/**
	 * 得到所有商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistoryResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBannerHistoryResult> getAll(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult);


	/**
	 * 添加商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistoryResult
	 * @Description:
	 */
	public void insertYdShopMerchantBannerHistory(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult);
	

	/**
	 * 通过id修改商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistoryResult
	 * @Description:
	 */
	public void updateYdShopMerchantBannerHistory(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult);
	
	
}
