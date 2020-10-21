package com.yd.service.dao.gift;

import java.util.List;
import com.yd.service.bean.gift.YdGiftCategory;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:平台礼品分类Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:41:45
 * @Version:1.1.0
 */
public interface YdGiftCategoryDao {

	/**
	 * 通过id得到平台礼品分类YdGiftCategory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftCategory getYdGiftCategoryById(Integer id);

	/**
	 * 通过categoryName得到平台礼品分类YdGiftCategory
	 * @param categoryName
	 * @return
	 * @Description:
	 */
	public YdGiftCategory getYdGiftCategoryByName(String categoryName);

	/**
	 * 得到所有平台礼品分类YdGiftCategory
	 * @param ydGiftCategory
	 * @return 
	 * @Description:
	 */
	public List<YdGiftCategory> getAll(YdGiftCategory ydGiftCategory);

	/**
	 * 查询分类数量
	 * @param params
	 * @return
	 */
	int getCategoryCount(@Param("params") YdGiftCategory params);

	/**
	 * 分页查询礼品分类数据
	 * @param params
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<YdGiftCategory> findCategoryListByPage(@Param("params") YdGiftCategory params,
												@Param("pageStart") Integer pageStart,
												@Param("pageSize") Integer pageSize);

	/**
	 * 添加平台礼品分类YdGiftCategory
	 * @param ydGiftCategory
	 * @Description:
	 */
	public void insertYdGiftCategory(YdGiftCategory ydGiftCategory);
	
	/**
	 * 通过id修改平台礼品分类YdGiftCategory
	 * @param ydGiftCategory
	 * @Description:
	 */
	public void updateYdGiftCategory(YdGiftCategory ydGiftCategory);

	public void deleteYdGiftCategory(Integer id);

}
