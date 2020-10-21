package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemImage;


/**
 * @Title:平台商品图片Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:31:14
 * @Version:1.1.0
 */
public interface YdItemImageDao {

	/**
	 * 通过id得到平台商品图片YdItemImage
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemImage getYdItemImageById(Integer id);


	public List<YdItemImage> findImageListByItemId(Integer itemId);

	/**
	 * 得到所有平台商品图片YdItemImage
	 * @param ydItemImage
	 * @return 
	 * @Description:
	 */
	public List<YdItemImage> getAll(YdItemImage ydItemImage);


	/**
	 * 添加平台商品图片YdItemImage
	 * @param ydItemImage
	 * @Description:
	 */
	public void insertYdItemImage(YdItemImage ydItemImage);
	

	/**
	 * 通过id修改平台商品图片YdItemImage
	 * @param ydItemImage
	 * @Description:
	 */
	public void updateYdItemImage(YdItemImage ydItemImage);

	/**
	 * 删除图片
	 * @param itemId
	 */
    void deleteYdItemImage(Integer itemId);
}
