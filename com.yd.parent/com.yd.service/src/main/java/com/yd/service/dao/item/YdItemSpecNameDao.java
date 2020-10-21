package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemSpecName;


/**
 * @Title:平台商品规格名Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:32:38
 * @Version:1.1.0
 */
public interface YdItemSpecNameDao {

	/**
	 * 通过id得到平台商品规格名YdItemSpecName
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemSpecName getYdItemSpecNameById(Integer id);

	/**
	 * 通过itemId得到平台商品规格名YdItemSpecName
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public List<YdItemSpecName> findSpecNameListByItemId(Integer itemId);

	/**
	 * 得到所有平台商品规格名YdItemSpecName
	 * @param ydItemSpecName
	 * @return 
	 * @Description:
	 */
	public List<YdItemSpecName> getAll(YdItemSpecName ydItemSpecName);


	/**
	 * 添加平台商品规格名YdItemSpecName
	 * @param ydItemSpecName
	 * @Description:
	 */
	public void insertYdItemSpecName(YdItemSpecName ydItemSpecName);
	

	/**
	 * 通过id修改平台商品规格名YdItemSpecName
	 * @param ydItemSpecName
	 * @Description:
	 */
	public void updateYdItemSpecName(YdItemSpecName ydItemSpecName);

	public List<YdItemSpecName> getYdItemSpecNameByItemId(Integer itemId);
	
}
