package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemBrowseHistory;


/**
 * @Title:商品浏览记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:29:34
 * @Version:1.1.0
 */
public interface YdItemBrowseHistoryDao {

	/**
	 * 通过id得到商品浏览记录YdItemBrowseHistory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemBrowseHistory getYdItemBrowseHistoryById(Integer id);

	/**
	 * 得到所有商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistory
	 * @return 
	 * @Description:
	 */
	public List<YdItemBrowseHistory> getAll(YdItemBrowseHistory ydItemBrowseHistory);


	/**
	 * 添加商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistory
	 * @Description:
	 */
	public void insertYdItemBrowseHistory(YdItemBrowseHistory ydItemBrowseHistory);
	

	/**
	 * 通过id修改商品浏览记录YdItemBrowseHistory
	 * @param ydItemBrowseHistory
	 * @Description:
	 */
	public void updateYdItemBrowseHistory(YdItemBrowseHistory ydItemBrowseHistory);
	
}
