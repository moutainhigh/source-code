package com.yd.service.dao.decoration;

import java.util.List;
import com.yd.service.bean.decoration.YdMerchantBanner;

/**
 * @Title:商户banner图Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:33:42
 * @Version:1.1.0
 */
public interface YdMerchantBannerDao {

	/**
	 * 通过id得到商户banner图YdShopMerchantBanner
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBanner getYdShopMerchantBannerById(Integer id);

	/**
	 * 得到所有商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBanner
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBanner> getAll(YdMerchantBanner ydShopMerchantBanner);


	/**
	 * 添加商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBanner
	 * @Description:
	 */
	public void insertYdShopMerchantBanner(YdMerchantBanner ydShopMerchantBanner);
	

	/**
	 * 通过id修改商户banner图YdShopMerchantBanner
	 * @param ydShopMerchantBanner
	 * @Description:
	 */
	public void updateYdShopMerchantBanner(YdMerchantBanner ydShopMerchantBanner);

	/**
	 * 删除商户banner
	 * @param merchantId
	 */
    void deleteYdShopMerchantBannerByMerchantId(Integer merchantId);
}
