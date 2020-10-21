package com.yd.service.dao.decoration;

import java.util.List;
import java.util.Set;

import com.yd.service.bean.decoration.YdMerchantBannerHistory;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户历史banner图Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:34:30
 * @Version:1.1.0
 */
public interface YdMerchantBannerHistoryDao {

	/**
	 * 通过id得到商户历史banner图YdShopMerchantBannerHistory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBannerHistory getYdShopMerchantBannerHistoryById(Integer id);

	/**
	 * 得到所有商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistory
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBannerHistory> getAll(YdMerchantBannerHistory ydShopMerchantBannerHistory);


	/**
	 * 添加商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistory
	 * @Description:
	 */
	public void insertYdShopMerchantBannerHistory(YdMerchantBannerHistory ydShopMerchantBannerHistory);
	

	/**
	 * 通过id修改商户历史banner图YdShopMerchantBannerHistory
	 * @param ydShopMerchantBannerHistory
	 * @Description:
	 */
	public void updateYdShopMerchantBannerHistory(YdMerchantBannerHistory ydShopMerchantBannerHistory);

	/**
	 * 查询商户banner图上传历史记录
	 * @param merchantId
	 * @param picUrlList
	 * @return
	 */
    List<YdMerchantBannerHistory> findMerchantBannerPictureHistoryList(@Param("merchantId") Integer merchantId,
                                                                       @Param("list") Set<String> picUrlList);
}
