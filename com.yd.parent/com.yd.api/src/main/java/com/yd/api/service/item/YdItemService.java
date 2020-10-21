package com.yd.api.service.item;

import java.util.List;

import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:平台商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:28:35
 * @Version:1.1.0
 */
public interface YdItemService {
	void sysItemImage();


	/**
	 * 通过id得到平台商品YdItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemResult getYdItemById(Integer id);

	Page<YdItemResult> findItemListByPage(YdItemResult params, PagerInfo pagerInfo);

	/**
	 * 得到所有平台商品YdItem
	 * @param ydItemResult
	 * @return 
	 * @Description:
	 */
	public List<YdItemResult> getAll(YdItemResult ydItemResult);

	/**
	 * 添加平台商品YdItem
	 * @param ydItemResult
	 * @Description:
	 */
	public void insertYdItem(YdItemResult ydItemResult);

	/**
	 * 通过id修改平台商品YdItem
	 * @param ydItemResult
	 * @Description:
	 */
	public void updateYdItem(YdItemResult ydItemResult) throws BusinessException;

	public void initItemFromCrawer(CrawerItemResult item, List<CrawerSpecNameWithSpecVal> specNameWithSpecValList);

	public void updatePubTime(Integer id, String pubTime);

	public void addOrUpdateContent(int itemId,String content);
	/**
	 * 平台上下架商品
	 * @param itemId
	 * @param type	up | down
	 * @throws BusinessException
	 */
    void upOrDownItem(Integer itemId, String type) throws BusinessException;

	/**
	 *  平台删除商品
	 * @param itemId 商户商品id
	 * @throws BusinessException
	 */
	void deleteItem(Integer itemId) throws BusinessException;

	/**
	 * 查询商品库商品详情
	 * @param itemId 商品库商品id
	 * @return
	 * @throws BusinessException
	 */
    YdItemResult getItemDetail(Integer itemId) throws BusinessException;

	/**
	 * 比价页头部编辑
	 * @param itemId
	 * @param imageUrl
	 */
	void updateIsHeadImage(Integer itemId, String imageUrl) throws BusinessException;
}
