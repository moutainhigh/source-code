package com.yd.service.impl.gift;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.gift.YdGiftCategoryResult;
import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.service.gift.YdGiftCategoryService;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.dao.gift.YdGiftDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.gift.YdGiftCategoryDao;
import com.yd.service.bean.gift.YdGiftCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:平台礼品分类Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:41:45
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdGiftCategoryServiceImpl implements YdGiftCategoryService {

	private static final Logger logger = LoggerFactory.getLogger(YdGiftCategoryServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdGiftCategoryDao ydGiftCategoryDao;

	@Override
	public YdGiftCategoryResult getYdGiftCategoryById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdGiftCategoryResult ydGiftCategoryResult = null;
		YdGiftCategory ydGiftCategory = this.ydGiftCategoryDao.getYdGiftCategoryById(id);
		if (ydGiftCategory != null) {
			ydGiftCategoryResult = new YdGiftCategoryResult();
			BeanUtilExt.copyProperties(ydGiftCategoryResult, ydGiftCategory);
		}
		
		return ydGiftCategoryResult;
	}

	/**
	 * 分页查询礼品分类
	 * @param categoryName
	 * @param pagerInfo
	 * @return
	 */
	@Override
	public Page<YdGiftCategoryResult> findCategoryListByPage(String categoryName, PagerInfo pagerInfo) throws BusinessException {
		Page<YdGiftCategoryResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdGiftCategory ydGiftCategory = new YdGiftCategory();
		ydGiftCategory.setCategoryName(categoryName);
		int amount = this.ydGiftCategoryDao.getCategoryCount(ydGiftCategory);
		if (amount > 0) {
			List<YdGiftCategory> dataList =  this.ydGiftCategoryDao.findCategoryListByPage(ydGiftCategory,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdGiftCategoryResult> resultList = DTOUtils.convertList(dataList, YdGiftCategoryResult.class);

			// 设置礼品数量
			resultList.forEach(ydGiftCategoryResult -> {
				ydGiftCategoryResult.setNumber(ydGiftDao.getGiftCountByCategoryId(ydGiftCategoryResult.getId()));
			});
			pageData.setData(resultList);
		}
		return pageData;
	}

	/**
	 * 查询所有礼品分类以及分类下的礼品
	 * @param ydGiftCategoryResult
	 * @return
	 */
	@Override
	public List<YdGiftCategoryResult> getAll(YdGiftCategoryResult ydGiftCategoryResult) {
		YdGiftCategory ydGiftCategory = null;
		if (ydGiftCategoryResult != null) {
			ydGiftCategory = new YdGiftCategory();
			BeanUtilExt.copyProperties(ydGiftCategory, ydGiftCategoryResult);
		}
		List<YdGiftCategory> dataList = this.ydGiftCategoryDao.getAll(ydGiftCategory);
		if (CollectionUtils.isEmpty(dataList)) return null;

		List<YdGiftCategoryResult> resultList = DTOUtils.convertList(dataList, YdGiftCategoryResult.class);
		resultList.forEach(giftCategory -> {
			YdGift ydGift = new YdGift();
			ydGift.setCategoryId(giftCategory.getId());
			ydGift.setIsEnable("Y");
			ydGift.setIsFlag("N");
			List<YdGift> giftList = ydGiftDao.getAll(ydGift);
			giftCategory.setGiftList(DTOUtils.convertList(giftList, YdGiftResult.class));
		});
		return resultList;
	}

	@Override
	public void insertYdGiftCategory(YdGiftCategoryResult params) throws BusinessException{
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCategoryName()),
				"err_empty_gift_category_name", "礼品分类名称不可以为空");

		YdGiftCategory giftCategory = this.ydGiftCategoryDao.getYdGiftCategoryByName(params.getCategoryName());

		ValidateBusinessUtils.assertFalse(giftCategory != null,
				"err_gift_category_name", "礼品分类名称重复");
		params.setCreateTime(new Date());
		YdGiftCategory ydGiftCategory = new YdGiftCategory();
		BeanUtilExt.copyProperties(ydGiftCategory, params);
		this.ydGiftCategoryDao.insertYdGiftCategory(ydGiftCategory);
	}

	@Override
	public void updateYdGiftCategory(YdGiftCategoryResult params) throws BusinessException{

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCategoryName()),
				"err_empty_gift_category_name", "礼品分类名称不可以为空");

		ValidateBusinessUtils.assertFalse(params.getId() == null || params.getId() <= 0,
				"err_gift_category_id", "错误的礼品分类id");

		YdGiftCategory giftCategory = this.ydGiftCategoryDao.getYdGiftCategoryByName(params.getCategoryName());
		ValidateBusinessUtils.assertFalse(giftCategory != null && giftCategory.getId().equals(params.getId()),
				"err_gift_category_name", "礼品分类名称重复");

		params.setUpdateTime(new Date());
		YdGiftCategory ydGiftCategory = new YdGiftCategory();
		BeanUtilExt.copyProperties(ydGiftCategory, params);
		this.ydGiftCategoryDao.updateYdGiftCategory(ydGiftCategory);
	}

	/**
	 * 删除礼品分类
	 * @param params
	 */
	@Override
	public void deleteYdGiftCategory(YdGiftCategoryResult params) throws BusinessException{
		ValidateBusinessUtils.assertFalse(params.getId() == null || params.getId() <= 0,
				"err_gift_category_id", "错误的礼品分类id");

		// 查询分类数量
		YdGiftCategory ydGiftCategory = new YdGiftCategory();
		ydGiftCategory.setId(params.getId());
		List<YdGiftCategory> dataList = this.ydGiftCategoryDao.findCategoryListByPage(ydGiftCategory, 0, 1);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(dataList),
				"err_not_exist_gift_category", "礼品分类不存在");

		int number = ydGiftDao.getGiftCountByCategoryId(dataList.get(0).getId());
		ValidateBusinessUtils.assertFalse(number > 0,
				"err_gift_category_is_use", "分类被礼品使用中");

		this.ydGiftCategoryDao.deleteYdGiftCategory(params.getId());
	}

}

