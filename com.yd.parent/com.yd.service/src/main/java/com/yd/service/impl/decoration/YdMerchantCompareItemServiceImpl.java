package com.yd.service.impl.decoration;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.decoration.YdMerchantCompareItemResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdMerchantCompareItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.dao.item.YdMerchantItemDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.decoration.YdMerchantCompareItemDao;
import com.yd.service.bean.decoration.YdMerchantCompareItem;

/**
 * @Title:商户配置比价商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:29:13
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantCompareItemServiceImpl implements YdMerchantCompareItemService {

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdMerchantCompareItemDao ydMerchantCompareItemDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantCompareItemResult getYdMerchantCompareItemById(Integer id) throws BusinessException {
		if (id == null || id <= 0) return null;
		YdMerchantCompareItemResult ydMerchantCompareItemResult = null;
		YdMerchantCompareItem ydMerchantCompareItem = this.ydMerchantCompareItemDao.getYdMerchantCompareItemById(id);
		if (ydMerchantCompareItem != null) {
			ydMerchantCompareItemResult = new YdMerchantCompareItemResult();
			BeanUtilExt.copyProperties(ydMerchantCompareItemResult, ydMerchantCompareItem);
		}	
		return ydMerchantCompareItemResult;
	}

	@Override
	public void deleteYdMerchantCompareItem(Integer merchantItemId) throws BusinessException {
		this.ydMerchantCompareItemDao.deleteByMerchantItemId(merchantItemId);
	}

	@Override
	public Page<YdMerchantCompareItemResult> findYdMerchantCompareItemListByPage(YdMerchantCompareItemResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantCompareItemResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
		BeanUtilExt.copyProperties(ydMerchantCompareItem, params);
		
		int amount = this.ydMerchantCompareItemDao.getYdMerchantCompareItemCount(ydMerchantCompareItem);
		if (amount > 0) {
			List<YdMerchantCompareItem> dataList = this.ydMerchantCompareItemDao.findYdMerchantCompareItemListByPage(
				ydMerchantCompareItem, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantCompareItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantCompareItemResult.class);
			resultList.forEach(merchantCompareItemResult -> {
				YdMerchantItem merchantItem = ydMerchantItemDao.getYdShopMerchantItemById(merchantCompareItemResult.getMerchantItemId());
				merchantCompareItemResult.setTitle(merchantItem.getTitle());
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantCompareItemResult> getAll(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantCompareItemResult.getMerchantId());
		ydMerchantCompareItemResult.setMerchantId(storeInfo.getId());

		YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
		BeanUtilExt.copyProperties(ydMerchantCompareItem, ydMerchantCompareItemResult);
		List<YdMerchantCompareItem> dataList = this.ydMerchantCompareItemDao.getAll(ydMerchantCompareItem);
		List<YdMerchantCompareItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantCompareItemResult.class);
		resultList.forEach(merchantCompareItemResult -> {
			YdMerchantItem merchantItem = ydMerchantItemDao.getYdShopMerchantItemById(merchantCompareItemResult.getMerchantItemId());
			merchantCompareItemResult.setTitle(merchantItem.getTitle());
		});
		return resultList;
	}

	@Override
	public void insertYdMerchantCompareItem(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantCompareItemResult.getMerchantId());
		ydMerchantCompareItemResult.setMerchantId(storeInfo.getId());

		YdMerchantCompareItem params = new YdMerchantCompareItem();
		params.setMerchantId(storeInfo.getId());
		List<YdMerchantCompareItem> dataList = this.ydMerchantCompareItemDao.getAll(params);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(dataList) && dataList.size() > 4,
				"err_compare_item_count", "最多添加五个比价商品");

		ydMerchantCompareItemResult.setCreateTime(new Date());
		ydMerchantCompareItemResult.setUpdateTime(new Date());
		YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
		BeanUtilExt.copyProperties(ydMerchantCompareItem, ydMerchantCompareItemResult);
		this.ydMerchantCompareItemDao.insertYdMerchantCompareItem(ydMerchantCompareItem);
	}
	
	@Override
	public void updateYdMerchantCompareItem(YdMerchantCompareItemResult ydMerchantCompareItemResult) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantCompareItemResult.getMerchantId());
		ydMerchantCompareItemResult.setMerchantId(storeInfo.getId());

		ydMerchantCompareItemResult.setUpdateTime(new Date());
		YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
		BeanUtilExt.copyProperties(ydMerchantCompareItem, ydMerchantCompareItemResult);
		this.ydMerchantCompareItemDao.updateYdMerchantCompareItem(ydMerchantCompareItem);
	}

	@Override
	public void listSort(Integer merchantId, List<YdMerchantCompareItemResult> dataList) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertCollectionNotEmpty(dataList, "err_list_empty", "数据不可以为空");
		ValidateBusinessUtils.assertFalse(dataList.size() > 5, "err_compare_item_count", "最多添加五个比价商品");
		dataList.forEach(data -> {
			ValidateBusinessUtils.assertNonNull(data.getMerchantItemId(), "err_data_empty", "商品id不可以为空");
		});

		Integer sort = 0;
		this.ydMerchantCompareItemDao.deleteYdMerchantCompareItem(merchantId);
		for (YdMerchantCompareItemResult merchantCompareItemResult : dataList) {
			YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
			BeanUtilExt.copyProperties(ydMerchantCompareItem, merchantCompareItemResult);
			ydMerchantCompareItem.setMerchantId(merchantId);
			ydMerchantCompareItem.setSort(sort++);
			this.ydMerchantCompareItemDao.insertYdMerchantCompareItem(ydMerchantCompareItem);
		}
	}

	@Override
	public void batchInsertYdMerchantCompareItem(Integer currMerchantId, String merchantItemIds) throws BusinessException{
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(currMerchantId);
		Integer merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertStringNotBlank(merchantItemIds, "err_merchantItemIds_empty", "数据不可以为空");
		List<String> itemList = StringUtil.stringToList(merchantItemIds, ",");
		ValidateBusinessUtils.assertFalse(itemList.size() > 5, "err_compare_item_count", "最多添加五个比价商品");
		this.ydMerchantCompareItemDao.deleteYdMerchantCompareItem(merchantId);

		Integer sort = 0;
		for (String merchantItemId : itemList) {
			YdMerchantItem merchantItem = ydMerchantItemDao.getYdShopMerchantItemById(Integer.valueOf(merchantItemId));
			ValidateBusinessUtils.assertNonNull(merchantItemIds, "err_merchantItem_empty", merchantItem + "找不到商品");
			ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(merchantItem.getIsEnable()),
					"err_merchantItem_isEnable_empty", merchantItem + "找不未上架，不可以选择");
			YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
			ydMerchantCompareItem.setCreateTime(new Date());
			ydMerchantCompareItem.setSort(sort++);
			ydMerchantCompareItem.setMerchantId(merchantId);
			ydMerchantCompareItem.setMerchantItemId(Integer.valueOf(merchantItemId));
			this.ydMerchantCompareItemDao.insertYdMerchantCompareItem(ydMerchantCompareItem);
		}
	}

}

