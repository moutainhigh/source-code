package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdItemSpecValue;


/**
 * @Title:平台商品规格值Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:33:26
 * @Version:1.1.0
 */
public interface YdItemSpecValueDao {

	/**
	 * 通过id得到平台商品规格值YdItemSpecValue
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdItemSpecValue getYdItemSpecValueById(Integer id);

	/**
	 * 通过itemId得到平台商品规格名YdItemSpecValue
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public List<YdItemSpecValue> findSpecValueListByItemId(Integer itemId);

	/**
	 * 得到所有平台商品规格值YdItemSpecValue
	 * @param ydItemSpecValue
	 * @return 
	 * @Description:
	 */
	public List<YdItemSpecValue> getAll(YdItemSpecValue ydItemSpecValue);


	/**
	 * 添加平台商品规格值YdItemSpecValue
	 * @param ydItemSpecValue
	 * @Description:
	 */
	public void insertYdItemSpecValue(YdItemSpecValue ydItemSpecValue);
	

	/**
	 * 通过id修改平台商品规格值YdItemSpecValue
	 * @param ydItemSpecValue
	 * @Description:
	 */
	public void updateYdItemSpecValue(YdItemSpecValue ydItemSpecValue);

	public List<Integer> getYdItemSpecValueIdBySpecId(Integer specId);

	public List<YdItemSpecValue> getYdItemSpecValueByItemId(Integer itemId);
	
}
