package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemContent;


/**
 * @Title:平台商品图文信息Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:30:33
 * @Version:1.1.0
 */
public interface YdItemContentDao {

	/**
	 * 通过id得到平台商品图文信息YdItemContent
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemContent getYdItemContentById(Integer id);

	/**
	 * 通过id得到平台商品图文信息YdItemContent
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public YdItemContent getYdItemContentByItemId(Integer itemId);

	/**
	 * 得到所有平台商品图文信息YdItemContent
	 * @param ydItemContent
	 * @return 
	 * @Description:
	 */
	public List<YdItemContent> getAll(YdItemContent ydItemContent);


	/**
	 * 添加平台商品图文信息YdItemContent
	 * @param ydItemContent
	 * @Description:
	 */
	public void insertYdItemContent(YdItemContent ydItemContent);
	

	/**
	 * 通过id修改平台商品图文信息YdItemContent
	 * @param ydItemContent
	 * @Description:
	 */
	public void updateYdItemContent(YdItemContent ydItemContent);
	
}
