package com.yd.api.service.gift;

import java.util.List;

import com.yd.api.result.gift.YdGiftCategoryResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:平台礼品分类Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:41:45
 * @Version:1.1.0
 */
public interface YdGiftCategoryService {

	/**
	 * 通过id得到平台礼品分类YdGiftCategory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftCategoryResult getYdGiftCategoryById(Integer id) throws BusinessException;

	/**
	 * 分页查询礼品分类
	 * @param categoryName
	 * @param pagerInfo
	 * @return
	 * @throws BusinessException
	 */
	Page<YdGiftCategoryResult> findCategoryListByPage(String categoryName, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有平台礼品分类YdGiftCategory
	 * @param ydGiftCategoryResult
	 * @return 
	 * @Description:
	 */
	public List<YdGiftCategoryResult> getAll(YdGiftCategoryResult ydGiftCategoryResult) throws BusinessException;

	/**
	 * 添加平台礼品分类YdGiftCategory
	 * @param ydGiftCategoryResult
	 * @Description:
	 */
	public void insertYdGiftCategory(YdGiftCategoryResult ydGiftCategoryResult) throws BusinessException;
	

	/**
	 * 通过id修改平台礼品分类YdGiftCategory
	 * @param ydGiftCategoryResult
	 * @Description:
	 */
	public void updateYdGiftCategory(YdGiftCategoryResult ydGiftCategoryResult) throws BusinessException;

	/**
	 * 通过删除平台礼品分类YdGiftCategory
	 * @param ydGiftCategoryResult
	 * @Description:
	 */
	public void deleteYdGiftCategory(YdGiftCategoryResult ydGiftCategoryResult) throws BusinessException;

}
