package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItem;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:平台商品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:28:35
 * @Version:1.1.0
 */
public interface YdItemDao {

	/**
	 * 通过id得到平台商品YdItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItem getYdItemById(Integer id);

	int getItemCount(YdItem ydItem);

	List<YdItem> findItemListByPage(@Param("params") YdItem ydItem,
									@Param("pageStart") Integer pageStart,
									@Param("pageSize") Integer pageSize);

	/**
	 * 得到所有平台商品YdItem
	 * @param ydItem
	 * @return 
	 * @Description:
	 */
	public List<YdItem> getAll(YdItem ydItem);

	List<YdItem> getAOtherItem(YdItem ydItem);


	/**
	 * 添加平台商品YdItem
	 * @param ydItem
	 * @Description:
	 */
	public void insertYdItem(YdItem ydItem);
	

	/**
	 * 通过id修改平台商品YdItem
	 * @param ydItem
	 * @Description:
	 */
	public void updateYdItem(YdItem ydItem);

	public void updatePubTime(@Param("id")Integer id,@Param("pubTime") String pubTime);

	/**
	 * 删除平台商品
	 * @param itemId
	 */
	void deleteItemById(Integer itemId);

    List<YdItem> findItemListGroupByTitle();

}
