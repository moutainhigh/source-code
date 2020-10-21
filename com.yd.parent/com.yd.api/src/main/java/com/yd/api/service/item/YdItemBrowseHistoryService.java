package com.yd.api.service.item;

import java.util.List;

import com.yd.api.result.item.YdItemBrowseHistoryResult;

/**
 * @Title:商品浏览记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:29:34
 * @Version:1.1.0
 */
public interface YdItemBrowseHistoryService {

	/**
	 * 通过id得到商品浏览记录YdItemBrowseHistory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemBrowseHistoryResult getYdItemBrowseHistoryById(Integer id);

	/**
	 * 得到所有商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistoryResult
	 * @return 
	 * @Description:
	 */
	public List<YdItemBrowseHistoryResult> getAll(YdItemBrowseHistoryResult ydItemBrowseHistoryResult);


	/**
	 * 添加商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistoryResult
	 * @Description:
	 */
	public void insertYdItemBrowseHistory(YdItemBrowseHistoryResult ydItemBrowseHistoryResult);
	

	/**
	 * 通过id修改商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistoryResult
	 * @Description:
	 */
	public void updateYdItemBrowseHistory(YdItemBrowseHistoryResult ydItemBrowseHistoryResult);
	
	
}
