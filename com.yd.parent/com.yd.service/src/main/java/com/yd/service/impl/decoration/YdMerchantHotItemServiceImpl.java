package com.yd.service.impl.decoration;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.crawer.CrawerService;
import com.yd.api.result.decoration.YdMerchantHotItemResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdMerchantHotItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.decoration.YdMerchantBanner;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.dao.item.YdMerchantItemDao;
import com.yd.service.dao.item.YdMerchantItemSkuDao;
import com.yd.service.dao.order.YdUserOrderDetailDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.decoration.YdMerchantHotItemDao;
import com.yd.service.bean.decoration.YdMerchantHotItem;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:门店首页热门商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:09:00
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantHotItemServiceImpl implements YdMerchantHotItemService {

	@Resource
	private YdMerchantHotItemDao ydMerchantHotItemDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdMerchantItemSkuDao ydMerchantItemSkuDao;

	@Resource
	private CrawerService crawerService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantHotItemResult getYdMerchantHotItemById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantHotItemResult ydMerchantHotItemResult = null;
		YdMerchantHotItem ydMerchantHotItem = this.ydMerchantHotItemDao.getYdMerchantHotItemById(id);
		if (ydMerchantHotItem != null) {
			ydMerchantHotItemResult = new YdMerchantHotItemResult();
			BeanUtilExt.copyProperties(ydMerchantHotItemResult, ydMerchantHotItem);
		}
		return ydMerchantHotItemResult;
	}

	@Override
	public Page<YdMerchantHotItemResult> findYdMerchantHotItemListByPage(YdMerchantHotItemResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantHotItemResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantHotItem ydMerchantHotItem = new YdMerchantHotItem();
		BeanUtilExt.copyProperties(ydMerchantHotItem, params);
		
		int amount = this.ydMerchantHotItemDao.getYdMerchantHotItemCount(ydMerchantHotItem);
		if (amount > 0) {
			List<YdMerchantHotItem> dataList = this.ydMerchantHotItemDao.findYdMerchantHotItemListByPage(ydMerchantHotItem, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantHotItemResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	/**
	 * 查询所有的门店热门商品
	 * @param ydMerchantHotItemResult
	 * @return
	 */
	@Override
	public List<YdMerchantHotItemResult> getAll(YdMerchantHotItemResult ydMerchantHotItemResult) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantHotItemResult.getMerchantId());

		YdMerchantHotItem ydMerchantHotItem = new YdMerchantHotItem();
		ydMerchantHotItem.setMerchantId(storeInfo.getId());

		List<YdMerchantHotItem> dataList = this.ydMerchantHotItemDao.getAll(ydMerchantHotItem);
		return DTOUtils.convertList(dataList, YdMerchantHotItemResult.class);
	}

	/**
	 * 门店热门商品排序
	 * @param hotItemList
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void sortHotItem(List<YdMerchantHotItemResult> hotItemList) {
		hotItemList.forEach(merchantBrand -> {
			ValidateBusinessUtils.assertFalse(merchantBrand.getId() == null,
					"err_empty_id", "id不可以为空");
			ValidateBusinessUtils.assertFalse(merchantBrand.getSort() == null,
					"err_empty_sort", "排序号不可以为空");
			YdMerchantHotItem ydMerchantHotItem = new YdMerchantHotItem();
			ydMerchantHotItem.setId(merchantBrand.getId());
			ydMerchantHotItem.setSort(merchantBrand.getSort());
			this.ydMerchantHotItemDao.updateYdMerchantHotItem(ydMerchantHotItem);
		});
	}

	/**
	 * 新增门店首页热门商品
	 * @param ydMerchantHotItemResult
	 */
	@Override
	public void insertYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult) {
		List<YdMerchantHotItem> hotItemList = this.ydMerchantHotItemDao.getYdMerchantHotItemByMerchantId(ydMerchantHotItemResult.getMerchantId());

		YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantHotItemResult.getMerchantItemId());
		ValidateBusinessUtils.assertFalse(ydMerchantItem == null,
				"err_not_item", "商品不存在");
		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
				"err_not_enable", "下架中的商品不可以使用");

		ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(hotItemList) &&
				hotItemList.size() >= 8, "err_max_hot_item", "商品已达上限");

		YdMerchantHotItem hotItem = this.ydMerchantHotItemDao.getYdMerchantHotItemByItemIdAndMerchantId(
				ydMerchantHotItemResult.getMerchantId(), ydMerchantHotItemResult.getMerchantItemId());
		ValidateBusinessUtils.assertFalse(hotItem != null,
				"err_exist_hot_item", "热门商品已经存在");

		hotItem = new YdMerchantHotItem();
		ydMerchantHotItemResult.setCreateTime(new Date());
		ydMerchantHotItemResult.setUpdateTime(new Date());
		ydMerchantHotItemResult.setSort(999);
		BeanUtilExt.copyProperties(hotItem, ydMerchantHotItemResult);
		this.ydMerchantHotItemDao.insertYdMerchantHotItem(hotItem);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void insertYdMerchantHotItemList(Integer merchantId, List<YdMerchantHotItemResult> hotItemResultList) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		Integer storeId = storeInfo.getId();

		List<YdMerchantHotItem> hotItemList = this.ydMerchantHotItemDao.getYdMerchantHotItemByMerchantId(storeId);
		ValidateBusinessUtils.assertFalse(hotItemList.size() + hotItemResultList.size() > 8,
				"err_max_hot_item", "商品已达上限");
		hotItemResultList.forEach(ydMerchantHotItemResult -> {
			YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantHotItemResult.getMerchantItemId());
			ValidateBusinessUtils.assertFalse(ydMerchantItem == null,
					"err_not_item", "商品不存在");

			ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
					"err_not_enable", "下架中的商品不可以使用");

			YdMerchantHotItem hotItem = this.ydMerchantHotItemDao.getYdMerchantHotItemByItemIdAndMerchantId(
					storeId, ydMerchantHotItemResult.getMerchantItemId());
			ValidateBusinessUtils.assertFalse(hotItem != null,
					"err_exist_hot_item", "热门商品已经存在");

			hotItem = new YdMerchantHotItem();
			hotItem.setSort(999);
			hotItem.setCreateTime(new Date());
			hotItem.setUpdateTime(new Date());
			hotItem.setMerchantId(storeId);
			hotItem.setMerchantItemId(ydMerchantItem.getId());
			hotItem.setFirstCategoryId(ydMerchantItem.getFirstCategoryId());
			hotItem.setSecondCategoryId(ydMerchantItem.getSecondCategoryId());
			this.ydMerchantHotItemDao.insertYdMerchantHotItem(hotItem);
		});

	}

	/**
	 * 修改门店首页热门商品
	 * @param ydMerchantHotItemResult
	 */
	@Override
	public void updateYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult) {
		Integer merchantId = ydMerchantHotItemResult.getMerchantId();
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		ydMerchantHotItemResult.setMerchantId(storeInfo.getId());

		ValidateBusinessUtils.assertFalse(ydMerchantHotItemResult.getId() == null,
				"err_empty_id", "热门商品id不可以为空");

		YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantHotItemResult.getMerchantItemId());
		ValidateBusinessUtils.assertFalse(ydMerchantItem == null,
				"err_not_item", "商品不存在");
		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
				"err_not_enable", "下架中的商品不可以使用");

		YdMerchantHotItem hotItem = this.ydMerchantHotItemDao.getYdMerchantHotItemByItemIdAndMerchantId(
				ydMerchantHotItemResult.getMerchantId(), ydMerchantHotItemResult.getMerchantItemId());
		ValidateBusinessUtils.assertFalse(hotItem != null && hotItem.getId() == ydMerchantHotItemResult.getId(),
				"err_exist_hot_item", "商品已经存在");

		hotItem = this.ydMerchantHotItemDao.getYdMerchantHotItemById(ydMerchantHotItemResult.getId());
		ValidateBusinessUtils.assertFalse(hotItem == null,
				"err_empty_hot_item", "热门商品不存在");

		List<YdMerchantHotItem> hotItemList = this.ydMerchantHotItemDao.getYdMerchantHotItemByMerchantId(
				ydMerchantHotItemResult.getMerchantId());
		ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(hotItemList) &&
				hotItemList.size() >= 8, "err_max_hot_item", "商品已达上限");

		YdMerchantHotItem ydMerchantHotItem = new YdMerchantHotItem();
		BeanUtilExt.copyProperties(ydMerchantHotItem, ydMerchantHotItemResult);
		ydMerchantHotItem.setUpdateTime(new Date());
		ydMerchantHotItem.setSort(hotItem.getSort());
		this.ydMerchantHotItemDao.updateYdMerchantHotItem(ydMerchantHotItem);
	}

	/**
	 * 删除门店热门商品
	 * @param ydMerchantHotItemResult
	 * @throws BusinessException
	 */
	@Override
	public void deleteYdMerchantHotItem(YdMerchantHotItemResult ydMerchantHotItemResult) throws BusinessException {
		ValidateBusinessUtils.assertFalse(ydMerchantHotItemResult.getId() == null,
				"err_empty_id", "热门商品id不可以为空");

		this.ydMerchantHotItemDao.deleteYdMerchantHotItem(ydMerchantHotItemResult.getId());
	}

	/**
	 * 查询商城热门商品
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<YdMerchantHotItemResult> findFrontHotItemList(Integer merchantId) {
		ValidateBusinessUtils.assertIdNotNull(merchantId, "err_merchant_id", "非法的商户id");

		YdMerchantHotItem ydMerchantHotItem = new YdMerchantHotItem();
		ydMerchantHotItem.setMerchantId(merchantId);
		List<YdMerchantHotItem> dataList = this.ydMerchantHotItemDao.getAll(ydMerchantHotItem);

		List<YdMerchantHotItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantHotItemResult.class);
		// 查询设置设置销量
		resultList.forEach(hotItem -> {
			YdMerchantItem ydMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(hotItem.getMerchantItemId());
			if (ydMerchantItem == null) {
				hotItem.setSaleNum(0);
			} else {
				hotItem.setSaleNum(ydMerchantItem.getMonthSalesNum());
				// 设置比价信息, 取第一个规格
				List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(ydMerchantItem.getId(), "Y");
//				YdMerchantItemSku ydMerchantItemSku = skuList.stream()
//						.min(Comparator.comparing(YdMerchantItemSku::getSalePrice)).get();
				YdMerchantItemSku ydMerchantItemSku = skuList.get(0);
				hotItem.setBijiaList(crawerService.getBijia(ydMerchantItemSku.getId()));

				// 价钱设置最小值
				ydMerchantItem.setSalePrice(ydMerchantItemSku.getSalePrice());
			}
		});
		return resultList;
	}

}

