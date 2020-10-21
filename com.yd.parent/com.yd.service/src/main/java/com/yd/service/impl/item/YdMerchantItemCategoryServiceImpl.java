package com.yd.service.impl.item;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.item.YdMerchantItemCategoryResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.item.YdMerchantItemCategoryService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.dao.item.YdMerchantItemDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.item.YdMerchantItemCategoryDao;
import com.yd.service.bean.item.YdMerchantItemCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户商品分类Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:31:16
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantItemCategoryServiceImpl implements YdMerchantItemCategoryService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantItemCategoryServiceImpl.class);


	@Resource
	private YdMerchantItemCategoryDao ydShopMerchantItemCategoryDao;

	@Resource
	private YdMerchantItemDao ydShopMerchantItemDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantItemCategoryResult getYdShopMerchantItemCategoryById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult = new YdMerchantItemCategoryResult();
		YdMerchantItemCategory ydShopMerchantItemCategory = this.ydShopMerchantItemCategoryDao.getYdShopMerchantItemCategoryById(id);
		if (ydShopMerchantItemCategory != null) {
			BeanUtilExt.copyProperties(ydShopMerchantItemCategoryResult, ydShopMerchantItemCategory);
		}
		return ydShopMerchantItemCategoryResult;
	}

	@Override
	public List<YdMerchantItemCategoryResult> getAll(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult) {
		YdMerchantItemCategory ydShopMerchantItemCategory = null;
		if (ydShopMerchantItemCategoryResult != null) {
			ydShopMerchantItemCategory = new YdMerchantItemCategory();
			BeanUtilExt.copyProperties(ydShopMerchantItemCategoryResult, ydShopMerchantItemCategory);
		}
		List<YdMerchantItemCategory> dataList = this.ydShopMerchantItemCategoryDao.getAll(ydShopMerchantItemCategory);
		return DTOUtils.convertList(dataList, YdMerchantItemCategoryResult.class);
	}

	@Override
	public Page<YdMerchantItemCategoryResult> findCategoryListByPage(YdMerchantItemCategoryResult params, PagerInfo pagerInfo) {
		Page<YdMerchantItemCategoryResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		YdMerchantItemCategory ydShopMerchantItemCategory = new YdMerchantItemCategory();
		BeanUtilExt.copyProperties(ydShopMerchantItemCategory, params);

		ydShopMerchantItemCategory.setPid(0);
		int amount = this.ydShopMerchantItemCategoryDao.getCategoryListByCount(ydShopMerchantItemCategory);
		if (amount > 0) {
			// 查询一级分类
			List<YdMerchantItemCategory> dataList = this.ydShopMerchantItemCategoryDao.findCategoryListByPage(
					ydShopMerchantItemCategory, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantItemCategoryResult> resultList = DTOUtils.convertList(dataList, YdMerchantItemCategoryResult.class);

			// 查询二级分类
			resultList.forEach(firCategory -> {
				ydShopMerchantItemCategory.setPid(firCategory.getId());
				List<YdMerchantItemCategory> subList = this.ydShopMerchantItemCategoryDao.findCategoryListByPage(
						ydShopMerchantItemCategory, pagerInfo.getStart(), pagerInfo.getPageSize());
				firCategory.setChildList(DTOUtils.convertList(subList, YdMerchantItemCategoryResult.class));
			});

			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public void insertYdShopMerchantItemCategory(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydShopMerchantItemCategoryResult.getMerchantId());
		ydShopMerchantItemCategoryResult.setMerchantId(storeInfo.getId());

		checkSaveOrUpdateParams(ydShopMerchantItemCategoryResult);
		int maxSort = ydShopMerchantItemCategoryDao.findMerchantCategoryMaxSort(ydShopMerchantItemCategoryResult.getPid(),
				ydShopMerchantItemCategoryResult.getMerchantId());
		ydShopMerchantItemCategoryResult.setCreateTime(new Date());
		ydShopMerchantItemCategoryResult.setSort(maxSort + 1);
		YdMerchantItemCategory ydShopMerchantItemCategory = new YdMerchantItemCategory();
		BeanUtilExt.copyProperties(ydShopMerchantItemCategory, ydShopMerchantItemCategoryResult);
		this.ydShopMerchantItemCategoryDao.insertYdShopMerchantItemCategory(ydShopMerchantItemCategory);
	}

	@Override
	public void updateYdShopMerchantItemCategory(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult) {
		checkSaveOrUpdateParams(ydShopMerchantItemCategoryResult);

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydShopMerchantItemCategoryResult.getMerchantId());
		ydShopMerchantItemCategoryResult.setMerchantId(storeInfo.getId());

		ydShopMerchantItemCategoryResult.setUpdateTime(new Date());
		YdMerchantItemCategory ydShopMerchantItemCategory = new YdMerchantItemCategory();
		BeanUtilExt.copyProperties(ydShopMerchantItemCategory, ydShopMerchantItemCategoryResult);
		this.ydShopMerchantItemCategoryDao.updateYdShopMerchantItemCategory(ydShopMerchantItemCategory);
	}

	@Override
	public void deleteYdShopMerchantItemCategory(YdMerchantItemCategoryResult params) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		ValidateBusinessUtils.assertFalse(params.getId() == null || params.getId() <= 0,
				"err_category_id", "非法的分类id");
		ValidateBusinessUtils.assertFalse(params.getPid() == null,
				"err_no_category_pid", "分类等级不可以为空");

		// 判断一级分类下是否有子分类, 分类下面是否拥有商品
		int itemCount = 0;
		YdMerchantItem ydMerchantItem = new YdMerchantItem();
		if (params.getPid() == 0) {
			List<YdMerchantItemCategory> dataList = this.ydShopMerchantItemCategoryDao.getYdShopMerchantItemCategoryByPid(params.getId());
			ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(dataList), "err_has_sub_category",
					"一级分类下面拥有子分类,不可以删除");
			ydMerchantItem.setFirstCategoryId(params.getId());
			ydMerchantItem.setSecondCategoryId(0);
			itemCount = ydShopMerchantItemDao.getMerchantItemCount(ydMerchantItem);
		} else {
			ydMerchantItem.setFirstCategoryId(params.getPid());
			ydMerchantItem.setSecondCategoryId(params.getId());
			itemCount = ydShopMerchantItemDao.getMerchantItemCount(ydMerchantItem);
		}
		ValidateBusinessUtils.assertFalse(itemCount > 0, "err_has_item",
				"分类下面有对应的商品,不可以删除");

		this.ydShopMerchantItemCategoryDao.deleteYdShopMerchantItemCategoryById(params.getId());
	}

	@Override
	public void SortYdShopMerchantItemCategory(Integer id, Integer pid, Integer merchantId, String type) {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_category_id", "非法的分类id");

		ValidateBusinessUtils.assertFalse(pid == null || pid < 0,
				"err__pid", "非法的分类pid");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(type),
				"err_sort_type", "排序类型不可以为空");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantItemCategory resourceData = null;
		YdMerchantItemCategory ydShopMerchantItemCategory = ydShopMerchantItemCategoryDao.getYdShopMerchantItemCategoryById(id);
		// 查询升序或者排序的最大sort
		if ("up".equals(type)) {
			YdMerchantItemCategory descData = ydShopMerchantItemCategoryDao.getMerchantCategoryDesc(
					pid, ydShopMerchantItemCategory.getSort(), merchantId);
			ValidateBusinessUtils.assertFalse(descData == null,
					"err_sort_up", "已经是第一个，无法上移");
			resourceData = descData;
		} else if ("down".equals(type)) {
			YdMerchantItemCategory ascData = ydShopMerchantItemCategoryDao.getMerchantCategoryAsc(
					pid, ydShopMerchantItemCategory.getSort(), merchantId);
			ValidateBusinessUtils.assertFalse(ascData == null,
					"err_sort_down", "已经是最后一个，无法下移");
			resourceData = ascData;
		}

		int distSort = ydShopMerchantItemCategory.getSort();
		ydShopMerchantItemCategory.setSort(resourceData.getSort());
		resourceData.setSort(distSort);
		ydShopMerchantItemCategoryDao.updateYdShopMerchantItemCategory(ydShopMerchantItemCategory);
		ydShopMerchantItemCategoryDao.updateYdShopMerchantItemCategory(resourceData);
	}

	private void checkSaveOrUpdateParams(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydShopMerchantItemCategoryResult.getCategoryName()),
				"err_no_category_name", "分类名称不可以为空");

		ValidateBusinessUtils.assertFalse(ydShopMerchantItemCategoryResult.getPid() == null,
				"err_no_category_pid", "分类等级不可以为空");

		ValidateBusinessUtils.assertFalse(ydShopMerchantItemCategoryResult.getMerchantId() == null,
				"err_no_merchant_id", "商户id不可以为空");

		if (ydShopMerchantItemCategoryResult.getPid() == 0) {
			YdMerchantItemCategory ydShopMerchantItemCategory = ydShopMerchantItemCategoryDao.getYdShopMerchantItemCategoryBydName(
					ydShopMerchantItemCategoryResult.getId(), ydShopMerchantItemCategoryResult.getMerchantId(), ydShopMerchantItemCategoryResult.getCategoryName());
			ValidateBusinessUtils.assertFalse(ydShopMerchantItemCategory != null,
					"err_repeat_category_name", "一级分类名称重复");
		}
	}

}

