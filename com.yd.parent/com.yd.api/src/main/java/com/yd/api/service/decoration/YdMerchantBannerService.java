package com.yd.api.service.decoration;

import com.yd.api.result.decoration.YdMerchantBannerResult;
import com.yd.core.utils.BusinessException;

import java.util.List;

/**
 * @Title:商户banner图Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:33:42
 * @Version:1.1.0
 */
public interface YdMerchantBannerService {

	/**
	 * 通过id得到商户banner图YdShopMerchantBanner
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBannerResult getYdShopMerchantBannerById(Integer id);

	/**
	 * 得到所有商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBannerResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBannerResult> getAll(YdMerchantBannerResult ydShopMerchantBannerResult);


	/**
	 * 添加商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBannerResult
	 * @Description:
	 */
	public void insertYdShopMerchantBanner(YdMerchantBannerResult ydShopMerchantBannerResult);
	
	/**
	 * 通过id修改商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBannerResult
	 * @Description:
	 */
	public void updateYdShopMerchantBanner(YdMerchantBannerResult ydShopMerchantBannerResult);


	public void saveOrUpdate(Integer merchantId, List<YdMerchantBannerResult> bannerList) throws BusinessException;
}
