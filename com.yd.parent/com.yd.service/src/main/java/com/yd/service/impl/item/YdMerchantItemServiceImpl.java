package com.yd.service.impl.item;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.yd.api.crawer.CrawerService;
import com.yd.api.result.item.*;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.*;
import com.yd.service.bean.cart.YdUserCart;
import com.yd.service.bean.decoration.YdMerchantCompareItem;
import com.yd.service.bean.decoration.YdMerchantHotItem;
import com.yd.service.bean.item.*;
import com.yd.service.bean.order.YdUserOrderDetail;
import com.yd.service.dao.cart.YdUserCartDao;
import com.yd.service.dao.decoration.YdMerchantCompareItemDao;
import com.yd.service.dao.decoration.YdMerchantHotItemDao;
import com.yd.service.dao.item.*;
import com.yd.service.dao.order.YdUserOrderDetailDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:30:19
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantItemServiceImpl implements YdMerchantItemService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantItemServiceImpl.class);

	@Resource
	private YdItemDao ydItemDao;

	@Resource
	private YdUserCartDao ydUserCartDao;

	@Resource
	private YdItemSkuDao ydItemSkuDao;

	@Resource
	private YdItemImageDao ydItemImageDao;

	@Resource
	private YdItemContentDao ydItemContentDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdItemSpecNameDao ydItemSpecNameDao;

	@Resource
	private YdItemSpecValueDao ydItemSpecValueDao;

	@Resource
	private YdMerchantItemSkuDao ydMerchantItemSkuDao;

	@Resource
	private YdMerchantHotItemDao ydMerchantHotItemDao;

	@Resource
	private YdUserOrderDetailDao ydUserOrderDetailDao;

	@Resource
	private YdMerchantItemImageDao ydMerchantItemImageDao;

	@Resource
	private YdMerchantCompareItemDao ydMerchantCompareItemDao;

	@Resource
	private YdMerchantItemContentDao ydMerchantItemContentDao;

	@Resource
	private CrawerService crawerService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantItemResult getYdShopMerchantItemById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantItemResult ydShopMerchantItemResult = null;
		YdMerchantItem ydShopMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(id);
		if (ydShopMerchantItem != null) {
			ydShopMerchantItemResult = new YdMerchantItemResult();
			BeanUtilExt.copyProperties(ydShopMerchantItemResult, ydShopMerchantItem);
		}
		return ydShopMerchantItemResult;
	}

	@Override
	public List<YdMerchantItemResult> getAll(YdMerchantItemResult ydShopMerchantItemResult) {
		YdMerchantItem ydShopMerchantItem = null;
		if (ydShopMerchantItemResult != null) {
			ydShopMerchantItem = new YdMerchantItem();
			BeanUtilExt.copyProperties(ydShopMerchantItem, ydShopMerchantItemResult);
		}
		List<YdMerchantItem> dataList = this.ydMerchantItemDao.getAll(ydShopMerchantItem);
		List<YdMerchantItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantItemResult.class);
		return resultList;
	}

	@Override
	public Page<YdMerchantItemResult> findMerchantItemListByPage(YdMerchantItemResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantItemResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantItem ydMerchantItem = new YdMerchantItem();
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());
		BeanUtilExt.copyProperties(ydMerchantItem, params);

		int amount = ydMerchantItemDao.getMerchantItemCount(ydMerchantItem);
		if (amount > 0) {
			List<YdMerchantItem> dataList = this.ydMerchantItemDao.findMerchantItemListByPage(
					ydMerchantItem, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantItemResult.class);
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	/**
	 * 获取商品详情
	 * @param merchantId	商户id
	 * @param id			商户商品id
	 * @param itemId		商品库商品id
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantItemResult getMerchantItemDetail(Integer merchantId, Integer id, Integer itemId) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertNonNull(ydItem, "err_item_not_exist", "商品不存在");

		YdMerchantItemResult ydMerchantItemResult = new YdMerchantItemResult();
		YdMerchantItem ydMerchantItem = new YdMerchantItem();

		// 商品id为空， 查询商品库商品信息以及规格, 不为空查询门店商品信息以及sku信息
		if (id == null) {
			List<YdMerchantItemSku> skuList = new ArrayList<>();
			List<YdItemSku> ydItemSkuList = ydItemSkuDao.findItemSkuListByItemId(ydItem.getId());

			ydMerchantItem.setItemId(ydItem.getId());
			ydMerchantItem.setTitle(ydItem.getTitle());
			ydMerchantItem.setCover(ydItem.getItemCover());
			ydMerchantItem.setSalePrice(ydItemSkuList.get(0).getSalePrice());
			ydMerchantItem.setIsEnable(ydItem.getIsEnable());
			BeanUtilExt.copyProperties(ydMerchantItemResult, ydMerchantItem);

			ydItemSkuList.forEach(ydItemSku -> {
				YdMerchantItemSku ydMerchantItemSku = new YdMerchantItemSku();
				ydMerchantItemSku.setSkuId(ydItemSku.getId());
				ydMerchantItemSku.setItemId(itemId);
				ydMerchantItemSku.setSalePrice(ydItemSku.getSalePrice());
				ydMerchantItemSku.setIsEnable(ydItemSku.getIsEnable());
				ydMerchantItemSku.setSpecNameValueJson(ydItemSku.getSpecNameValueJson());
				ydMerchantItemSku.setSpecValueIdPath(ydItemSku.getSpecValueIdPath());
				skuList.add(ydMerchantItemSku);
			});
			// 设置商品规格
			ydMerchantItemResult.setSkuList(DTOUtils.convertList(skuList, YdMerchantItemSkuResult.class));
		} else {
			// 设置商品基本信息
			ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(id);
			BeanUtilExt.copyProperties(ydMerchantItemResult, ydMerchantItem);
			// 设置商品规格
			List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(ydMerchantItemResult.getId(), null);
			ydMerchantItemResult.setSkuList(DTOUtils.convertList(skuList, YdMerchantItemSkuResult.class));

			// 查询商户商品图片
			List<YdMerchantItemImage> merchantImageList = ydMerchantItemImageDao.getYdMerchantItemImageByItemId(id);
			ydMerchantItemResult.setMerchantImageList(DTOUtils.convertList(merchantImageList, YdMerchantItemImageResult.class));

			// 查询商户商品图文详情
			YdMerchantItemContent merchantItemContent = ydMerchantItemContentDao.getYdMerchantItemContentByItemId(id);
			if (merchantItemContent != null) {
				ydMerchantItemResult.setMerchantContent(merchantItemContent.getContent());
			}
		}

		// 查询平台商品图片
		List<YdItemImage> imageList = ydItemImageDao.findImageListByItemId(ydMerchantItemResult.getItemId());
		ydMerchantItemResult.setImageList(DTOUtils.convertList(imageList, YdItemImageResult.class));

		// 查询平台商品图文详情
		YdItemContent ydItemContent = ydItemContentDao.getYdItemContentByItemId(ydMerchantItemResult.getItemId());
		if (ydItemContent != null) {
            ydMerchantItemResult.setContent(ydItemContent.getContent());
        }

		// 查询商品规格名
		List<YdItemSpecName> specNameList = ydItemSpecNameDao.findSpecNameListByItemId(ydMerchantItemResult.getItemId());
		ydMerchantItemResult.setSpecNameList(DTOUtils.convertList(specNameList, YdItemSpecNameResult.class));

		// 查询商品规格值
		List<YdItemSpecValue> specValueList = ydItemSpecValueDao.findSpecValueListByItemId(ydMerchantItemResult.getItemId());
		ydMerchantItemResult.setSpecValueList(DTOUtils.convertList(specValueList, YdItemSpecValueResult.class));
		return ydMerchantItemResult;
	}

    /**
     * 查询商城商品详情(与后台组装数据逻辑不一致)
     * @param merchantItemId
     * @return
     * @throws BusinessException
     */
    @Override
    public YdMerchantItemResult getFrontMerchantItemDetail(Integer merchantItemId) throws BusinessException {
		ValidateBusinessUtils.assertNonNull(merchantItemId, "err_no_item_id", "商品id不可以为空");
        YdMerchantItemResult ydMerchantItemResult = new YdMerchantItemResult();

        YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(merchantItemId);
        logger.info("====merchantItemId=" + merchantItemId);
		ValidateBusinessUtils.assertNonNull(ydMerchantItem, "err_no_item_id", "商品不存在");
        BeanUtilExt.copyProperties(ydMerchantItemResult, ydMerchantItem);

        // 设置商品规格
        List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(ydMerchantItemResult.getId(), "Y");
        List<YdMerchantItemSkuResult> skuResultList = DTOUtils.convertList(skuList, YdMerchantItemSkuResult.class);
        ydMerchantItemResult.setSkuList(skuResultList);

        // 查询设置平台商品图片, 商户商品图片
        List<YdItemImageResult> allImageList = new ArrayList<>();
		List<YdItemImage> imageList = ydItemImageDao.findImageListByItemId(ydMerchantItemResult.getItemId());
		List<YdMerchantItemImage> merchantImageList = ydMerchantItemImageDao.getYdMerchantItemImageByItemId(merchantItemId);
		if (CollectionUtils.isNotEmpty(merchantImageList)) {
			allImageList.addAll(DTOUtils.convertList(merchantImageList, YdItemImageResult.class));
		}
		if (CollectionUtils.isNotEmpty(imageList)) {
			allImageList.addAll(DTOUtils.convertList(imageList, YdItemImageResult.class));
		}
		ydMerchantItemResult.setImageList(allImageList);
		ydMerchantItemResult.setMerchantImageList(DTOUtils.convertList(merchantImageList, YdMerchantItemImageResult.class));

        // 查询设置平台商品图文详情, 商户商品图文详情
        YdItemContent ydItemContent = ydItemContentDao.getYdItemContentByItemId(ydMerchantItemResult.getItemId());
		YdMerchantItemContent merchantItemContent = ydMerchantItemContentDao.getYdMerchantItemContentByItemId(merchantItemId);

		StringBuilder stringBuilder = new StringBuilder();
		if (merchantItemContent != null) {
			stringBuilder.append(merchantItemContent.getContent());
			ydMerchantItemResult.setMerchantContent(merchantItemContent.getContent());
		}
		if (ydItemContent != null) {
			stringBuilder.append(ydItemContent.getContent());
		}
		ydMerchantItemResult.setContent(stringBuilder.toString());

        // 查询商品规格名
        List<YdItemSpecName> specNameList = ydItemSpecNameDao.findSpecNameListByItemId(ydMerchantItemResult.getItemId());
        List<YdItemSpecNameResult> itemSpecNameResultList = DTOUtils.convertList(specNameList, YdItemSpecNameResult.class);

        // 查询商品规格值
        List<YdItemSpecValue> specValueList = ydItemSpecValueDao.findSpecValueListByItemId(ydMerchantItemResult.getItemId());
        List<YdItemSpecValueResult> specValueResultList = DTOUtils.convertList(specValueList, YdItemSpecValueResult.class);

        // 将规格值放入规格对象之中
        itemSpecNameResultList.forEach(name -> name.setItemSpecValues(specValueResultList
                .stream()
                .filter(value -> value.getSpecId().equals(name.getId()))
                .collect(Collectors.toList())));

        // 设置售价最大值,最小值
		Double maxPrice = skuResultList.stream().max(Comparator.comparing(YdMerchantItemSkuResult::getSalePrice)).get().getSalePrice();
		Double minPrice = skuResultList.stream().min(Comparator.comparing(YdMerchantItemSkuResult::getSalePrice)).get().getSalePrice();
		ydMerchantItemResult.setSalePrice(skuList.get(0).getSalePrice());
		ydMerchantItemResult.setMaxPrice(maxPrice);
		ydMerchantItemResult.setMinPrice(minPrice);

        ydMerchantItemResult.setSpecNameList(itemSpecNameResultList);
        ydMerchantItemResult.setSpecValueList(specValueResultList);
        setSkuSpecIdJson(skuResultList, specValueResultList);
        return ydMerchantItemResult;
    }

    private void setSkuSpecIdJson(List<YdMerchantItemSkuResult> skuResultList, List<YdItemSpecValueResult> specValueResultList) {
        if (!CollectionUtils.isEmpty(specValueResultList)) {
            Map<Integer, Integer> specNameMap = new HashMap<>();
            specValueResultList.forEach((specValue) -> {
                specNameMap.put(specValue.getId(), specValue.getSpecId());
            });

            skuResultList.forEach((sku) -> {
                Map<Integer, Integer> nameValueMap = new HashMap<>();
                Stream.of(sku.getSpecValueIdPath().split(","))
                        .collect(Collectors.toList())
                        .forEach((valueId) -> {
                            nameValueMap.put(specNameMap.get(Integer.valueOf(valueId)), Integer.valueOf(valueId));
                        });
                sku.setSpecNameValueIdJson(new Gson().toJson(nameValueMap));
            });
        }
    }

    /**
	 * 新增修改商品
	 * @param itemInfo
	 * @throws BusinessException
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public void saveOrUpdate(YdMerchantItemResult itemInfo) throws BusinessException {
		if (itemInfo.getId() == null) {
			insertMerchantItem(itemInfo);
		} else {
			updateMerchantItem(itemInfo);
		}
	}

	public void insertMerchantItem(YdMerchantItemResult itemInfo) throws BusinessException {
		// 校验商品信息
		YdMerchantItem ydMerchantItem = checkMerchantItemParam(itemInfo);
		// 校验规格信息
		checkMerchantSkuParam(itemInfo);

		YdItem ydItem = ydItemDao.getYdItemById(itemInfo.getItemId());
		ValidateBusinessUtils.assertNonNull(ydItem, "err_no_item_id", "商品库对应itemId不存在");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(itemInfo.getMerchantId());
		itemInfo.setMerchantId(storeInfo.getId());

		Double minSalePrice = getItemMinPrice(itemInfo);

		// 新增商品库商品的时候先判断是否已经发布过此商品
		YdMerchantItem data = ydMerchantItemDao.getYdShopMerchantItemByItemId(itemInfo.getMerchantId(), itemInfo.getItemId());
		ValidateBusinessUtils.assertNull(data, "err_exist_item_id", "已经添加过此商品");

		// 保存商品
		ydMerchantItem = new YdMerchantItem();
		BeanUtilExt.copyProperties(ydMerchantItem, itemInfo);
		if (StringUtils.isNotEmpty(ydItem.getItemCover())) {
			ydMerchantItem.setCover(ydItem.getItemCover());
		}
		ydMerchantItem.setSalePrice(minSalePrice);
		ydMerchantItem.setCreateTime(new Date());
		ydMerchantItem.setBrandName(ydItem.getBrand());
		ydMerchantItem.setBrandId(ydItem.getBrandId());
		ydMerchantItemDao.insertYdShopMerchantItem(ydMerchantItem);

		Integer merchantItemId = ydMerchantItem.getId();

		// 保存规格, 查询商品规格值
		Map<Integer, YdItemSku> itemSkuMap = getItemSkuMapInfo(itemInfo.getItemId());
		itemInfo.getSkuList().forEach(merchantItemSku -> {
			YdMerchantItemSku ydMerchantItemSku = new YdMerchantItemSku();
			BeanUtilExt.copyProperties(ydMerchantItemSku, merchantItemSku);

			ydMerchantItemSku.setCreateTime(new Date());
			ydMerchantItemSku.setUpdateTime(new Date());
			ydMerchantItemSku.setMerchantItemId(merchantItemId);
			ydMerchantItemSku.setStock(merchantItemSku.getStock());
			ydMerchantItemSku.setFirstCategoryId(itemInfo.getFirstCategoryId());
			ydMerchantItemSku.setSecondCategoryId(itemInfo.getSecondCategoryId());

			// 设置商品sku属性组合
			YdItemSku ydItemSku = itemSkuMap.get(merchantItemSku.getSkuId());
			ydMerchantItemSku.setSpecValueIdPath(ydItemSku.getSpecValueIdPath());
			ydMerchantItemSku.setSpecNameValueJson(ydItemSku.getSpecNameValueJson());
			ydMerchantItemSkuDao.insertYdMerchantItemSku(ydMerchantItemSku);
		});

		initMerchantItemImageAndContent(merchantItemId, itemInfo.getMerchantContent(), itemInfo.getMerchantImageList());
	}

	public void updateMerchantItem(YdMerchantItemResult itemInfo) throws BusinessException {
		logger.info("======商户商品saveOrUpdate的itemInfo=" + JSON.toJSONString(itemInfo));
		// 校验商品信息
		YdMerchantItem ydMerchantItem = checkMerchantItemParam(itemInfo);
		// 校验规格信息
		checkMerchantSkuParam(itemInfo);

		YdItem ydItem = ydItemDao.getYdItemById(itemInfo.getItemId());
		ValidateBusinessUtils.assertNonNull(ydItem, "err_no_item_id", "商品库对应itemId不存在");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(itemInfo.getMerchantId());
		itemInfo.setMerchantId(storeInfo.getId());

		Double minSalePrice = getItemMinPrice(itemInfo);

		// 查询商品元数据, 修改商品
		if (StringUtils.isNotEmpty(ydItem.getItemCover())) {
			ydMerchantItem.setCover(ydItem.getItemCover());
		}
		ydMerchantItem.setUpdateTime(new Date());
		ydMerchantItem.setTitle(itemInfo.getTitle());
		ydMerchantItem.setIsEnable(itemInfo.getIsEnable());
		ydMerchantItem.setSalePrice(minSalePrice);
		ydMerchantItem.setFirstCategoryId(itemInfo.getFirstCategoryId());
		ydMerchantItem.setSecondCategoryId(itemInfo.getSecondCategoryId());
		ydMerchantItemDao.updateYdShopMerchantItem(ydMerchantItem);

		// 查询旧规格，修改规格
		Map<Integer, YdMerchantItemSku> merchantItemSkuMap = getMerchantItemSkuMapInfo(itemInfo.getId());
		itemInfo.getSkuList().forEach(merchantItemSku -> {
			YdMerchantItemSku oldMerchantItemSku = merchantItemSkuMap.get(merchantItemSku.getId());
			oldMerchantItemSku.setUpdateTime(new Date());
			oldMerchantItemSku.setSalePrice(merchantItemSku.getSalePrice());
			oldMerchantItemSku.setIsEnable(merchantItemSku.getIsEnable());
			oldMerchantItemSku.setStock(merchantItemSku.getStock());
			oldMerchantItemSku.setFirstCategoryId(itemInfo.getFirstCategoryId());
			oldMerchantItemSku.setSecondCategoryId(itemInfo.getSecondCategoryId());
			ydMerchantItemSkuDao.updateYdMerchantItemSku(oldMerchantItemSku);
		});
		initMerchantItemImageAndContent(itemInfo.getId(), itemInfo.getMerchantContent(), itemInfo.getMerchantImageList());
	}

	/**
	 * 删除商户商品
	 * @param merchantId		商户id
	 * @param merchantItemId	商户商品id
	 * @throws BusinessException
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteMerchantItem(Integer merchantId, Integer merchantItemId) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(merchantItemId == null || merchantItemId <= 0,
				"err_item_id", "非法的商品id");

		YdMerchantItem ydMerchantItem = new YdMerchantItem();
		ydMerchantItem.setId(merchantItemId);
		ydMerchantItem.setMerchantId(merchantId);
		List<YdMerchantItem> merchantItemList = ydMerchantItemDao.getAll(ydMerchantItem);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(merchantItemList),
				"err_item_not_exist", "商品不存在");

		ydMerchantItem = merchantItemList.get(0);
		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
				"err_item_enable", "上架中的商品无法删除");

		// 判断商品是否是热门商品
		YdMerchantHotItem ydMerchantHotItem = ydMerchantHotItemDao.getYdMerchantHotItemByItemIdAndMerchantId(merchantId, merchantItemId);
		ValidateBusinessUtils.assertFalse(ydMerchantHotItem != null,
				"err_item_enable", "商品在热门商品推荐，无法删除");

		// 删除商户商品
		ydMerchantItemDao.deleteMerchantItemById(merchantId, merchantItemId);
		// 删除商户商品sku
		ydMerchantItemSkuDao.deleteMerchantItemSkuByMerchantItemId(merchantItemId);
		// 删除商户商品图片
		ydMerchantItemImageDao.deleteYdMerchantItemImage(merchantItemId);
		//删除商户商品详情
		ydMerchantItemContentDao.deleteYdMerchantItemContent(merchantItemId);
	}

	/**
	 * 上下架商品
	 * @param merchantId
	 * @param merchantItemId
	 * @param type	up | down
	 * @throws BusinessException
	 */
	@Override
	public void upOrDownMerchantItem(Integer merchantId, Integer merchantItemId, String type) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");
		ValidateBusinessUtils.assertFalse(merchantItemId == null || merchantItemId <= 0,
				"err_item_id", "非法的商品id");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(type) || !(type.equalsIgnoreCase("up") ||
						type.equalsIgnoreCase("down")),"err_item_handel_status", "非法的商品上下架状态");

		// 判断商品是否是热门商品
		YdMerchantHotItem ydMerchantHotItem = ydMerchantHotItemDao.getYdMerchantHotItemByItemIdAndMerchantId(merchantId, merchantItemId);
		ValidateBusinessUtils.assertNull(ydMerchantHotItem,"err_item_enable", "商品在热门商品推荐，无法操作");

        // 判断商品是否是比价商品
        YdMerchantCompareItem ydMerchantCompareItem = new YdMerchantCompareItem();
        ydMerchantCompareItem.setMerchantId(merchantId);
        ydMerchantCompareItem.setMerchantItemId(merchantItemId);
        List<YdMerchantCompareItem> compareItemList = ydMerchantCompareItemDao.getAll(ydMerchantCompareItem);
        ValidateBusinessUtils.assertCollectionEmpty(compareItemList,"err_item_compare", "商品在比价商品列表，无法操作");

        YdMerchantItem ydMerchantItem = new YdMerchantItem();
		ydMerchantItem.setId(merchantItemId);
		ydMerchantItem.setMerchantId(merchantId);
		List<YdMerchantItem> merchantItemList = ydMerchantItemDao.getAll(ydMerchantItem);
		ValidateBusinessUtils.assertCollectionNotEmpty(merchantItemList, "err_item_not_exist", "商品不存在");

		YdMerchantItemSku ydMerchantItemSku = new YdMerchantItemSku();
		ydMerchantItemSku.setMerchantItemId(merchantItemId);
		List<YdMerchantItemSku> merchantSkuList = ydMerchantItemSkuDao.getAll(ydMerchantItemSku);
		ValidateBusinessUtils.assertCollectionNotEmpty(merchantSkuList, "err_sku_not_exist", "商品sku不存在");

		ydMerchantItem = merchantItemList.get(0);
		if (type.equalsIgnoreCase("up")) {
			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
					"err_item_handel_status", "商品已经上架，请勿重复操作");

			List<YdMerchantItemSku> skuList = merchantSkuList.stream()
					.filter(data -> data.getStock() > 0)
					.collect(Collectors.toList());

			ValidateBusinessUtils.assertCollectionNotEmpty(skuList, "err_no_item_id", "商品sku库存全部小于0，无法上架");
			Double minSalePrice = skuList.stream()
					.min(Comparator.comparing(YdMerchantItemSku :: getSalePrice)).get().getSalePrice();

			// 修改商品上架, 库存大于0的sku全部上架
			ydMerchantItem.setIsEnable("Y");
			ydMerchantItem.setSalePrice(minSalePrice);
			ydMerchantItemDao.updateYdShopMerchantItem(ydMerchantItem);
			skuList.forEach(merchantItemSku -> {
				merchantItemSku.setIsEnable("Y");
				ydMerchantItemSkuDao.updateYdMerchantItemSku(merchantItemSku);
			});
		} else if (type.equalsIgnoreCase("down")) {
			ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
					"err_item_handel_status", "商品已经下架，请勿重复操作");
			ydMerchantItem.setIsEnable("N");
			// 修改商品，商品sku全部下架
			ydMerchantItemDao.updateYdShopMerchantItem(ydMerchantItem);
			merchantSkuList.forEach(merchantSku -> {
				merchantSku.setIsEnable("N");
				ydMerchantItemSkuDao.updateYdMerchantItemSku(merchantSku);
			});
		}
	}

	/**
	 * 分页查询商城商品 (包含销量)
	 * @param brandId	商户id
	 * @param brandId	品牌id
	 * @param type		time | sales | price
	 * @param sort		asc(价格升序) | desc (价格降序)
	 * @param pagerInfo
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Page<YdMerchantItemResult> findFrontMerchantItemList(Integer merchantId, Integer brandId, String type,
																String sort, PagerInfo pagerInfo) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		if ("price".equalsIgnoreCase(type)) {
            ValidateBusinessUtils.assertFalse(!("asc".equalsIgnoreCase(sort) || "desc".equalsIgnoreCase(sort)),
                    "err_item_sort", "排序标识不正确");
        }

		Page<YdMerchantItemResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = ydMerchantItemDao.getFrontMerchantItemCount(merchantId, brandId);
		if (amount > 0) {
			List<YdMerchantItem> dataList = this.ydMerchantItemDao.findFrontMerchantItemList(merchantId, brandId, type, sort,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantItemResult> resultList = DTOUtils.convertList(dataList, YdMerchantItemResult.class);

			resultList.forEach(ydMerchantItemResult -> {
				List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(ydMerchantItemResult.getId(), "Y");
				YdMerchantItemSku ydMerchantItemSku = skuList.get(0);
//				YdMerchantItemSku ydMerchantItemSku = skuList.stream()
//						.min(Comparator.comparing(YdMerchantItemSku::getSalePrice)).get();
				ydMerchantItemResult.setBijiaList(crawerService.getBijia(ydMerchantItemSku.getId()));
				ydMerchantItemResult.setSalePrice(ydMerchantItemSku.getSalePrice());
			});
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public List<YdMerchantItemResult> findFrontCompareItemList(Integer merchantId) throws BusinessException {
		List<YdMerchantCompareItem> compareList = ydMerchantCompareItemDao.findListByMerchant(merchantId);
		if (CollectionUtils.isEmpty(compareList)) return null;
		List<Integer> itemIds = compareList.stream().map(YdMerchantCompareItem::getMerchantItemId).collect(Collectors.toList());
		List<YdMerchantItemResult> resultList = new ArrayList<>();
		itemIds.forEach(merchantItemId -> {
			YdMerchantItemResult ydMerchantItemResult = getFrontMerchantItemDetail(merchantItemId);
			if (ydMerchantItemResult != null) {
				// 获取比价图设置
				YdItem ydItem = ydItemDao.getYdItemById(ydMerchantItemResult.getItemId());
				ydMerchantItemResult.setHeadImageUrl(ydItem.getHeadImageUrl());
				ydMerchantItemResult.setIsHeadImage(ydItem.getIsHeadImage());

				// 设置比价信息
				YdMerchantItemSkuResult ydMerchantItemSkuResult = ydMerchantItemResult.getSkuList().get(0);
				ydMerchantItemResult.setBijiaList(crawerService.getBijia(ydMerchantItemSkuResult.getId()));

				// 价钱设置最小值
				ydMerchantItemResult.setSalePrice(ydMerchantItemSkuResult.getSalePrice());
				resultList.add(ydMerchantItemResult);
			}
		});
		return resultList;
	}

	/**
	 * 用户根据购物车或者商品查询商品规格列表
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param skuId		 商户商品规格id
	 * @param num		 商品数量
	 * @param carIds	 购物车ids
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<YdMerchantItemSkuResult> findMerchantItemListByCarIdList(Integer userId, Integer merchantId, Integer skuId, Integer num,
																	  String carIds,  String type) throws BusinessException {
		List<YdMerchantItemSkuResult> skuList = new ArrayList();
		if ("car".equalsIgnoreCase(type)) {
			List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
					.map(Integer::valueOf).collect(Collectors.toList());
			ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(carIdList),
					"err_empty_car_id_list", "购物车id不可以为空");
			List<YdUserCart> cartList = this.ydUserCartDao.findCartListByIdList(userId, merchantId, carIdList);
			cartList.forEach(data -> {
				// 查询商品的信息
				YdMerchantItemSku ydMerchantItemSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(data.getMerchantSkuId());
				ValidateBusinessUtils.assertFalse(ydMerchantItemSku == null,
						"err_not_exist_item", "商品sku不存在");
				ValidateBusinessUtils.assertFalse(ydMerchantItemSku.getIsEnable().equalsIgnoreCase("N"),
						"err_sku_enable", ydMerchantItemSku.getTitle() + "已经下架");

				YdMerchantItem ydMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantItemSku.getMerchantItemId());
				ValidateBusinessUtils.assertFalse(ydMerchantItem == null,
						"err_item_id", "商品不存在");
				ValidateBusinessUtils.assertFalse(ydMerchantItem.getIsEnable().equalsIgnoreCase("N"),
						"err_item_enable", ydMerchantItem.getTitle() + "已经下架");

				// 处理图片，规格图片为空，取商品图片
				if (StringUtils.isEmpty(ydMerchantItemSku.getSkuCover()) && StringUtils.isNotEmpty(ydMerchantItem.getCover())) {
					ydMerchantItemSku.setSkuCover(ydMerchantItem.getCover());
				}

				YdMerchantItemSkuResult ydMerchantItemSkuResult = new YdMerchantItemSkuResult();
				BeanUtilExt.copyProperties(ydMerchantItemSkuResult, ydMerchantItemSku);
				ydMerchantItemSkuResult.setNum(data.getNum());
				ydMerchantItemSkuResult.setTitle(ydMerchantItem.getTitle());
				skuList.add(ydMerchantItemSkuResult);
			});
		} else if ("item".equalsIgnoreCase(type)) {
			ValidateBusinessUtils.assertFalse(skuId == null || skuId <= 0,
					"err_sku_id", "skuId不可以为空");
			ValidateBusinessUtils.assertFalse(num == null || num <= 0,
					"err_num", "购买数量不可以为空");

			YdMerchantItemSku ydMerchantItemSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(skuId);
			ValidateBusinessUtils.assertFalse(ydMerchantItemSku == null,
					"err_not_exist_item", "商品不存在");
			ValidateBusinessUtils.assertFalse(ydMerchantItemSku.getIsEnable().equalsIgnoreCase("N"),
					"err_sku_enable", ydMerchantItemSku.getTitle() + "已经下架");

			YdMerchantItem ydMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantItemSku.getMerchantItemId());
			ValidateBusinessUtils.assertFalse(ydMerchantItem == null,
					"err_item_id", "商品不存在");
			ValidateBusinessUtils.assertFalse(ydMerchantItem.getIsEnable().equalsIgnoreCase("N"),
					"err_item_enable", ydMerchantItem.getTitle() + "已经下架");

			// 处理图片，规格图片为空，取商品图片
			if (StringUtils.isEmpty(ydMerchantItemSku.getSkuCover()) && StringUtils.isNotEmpty(ydMerchantItem.getCover())) {
				ydMerchantItemSku.setSkuCover(ydMerchantItem.getCover());
			}

			YdMerchantItemSkuResult ydMerchantItemSkuResult = new YdMerchantItemSkuResult();
			BeanUtilExt.copyProperties(ydMerchantItemSkuResult, ydMerchantItemSku);
			ydMerchantItemSkuResult.setNum(num);
			ydMerchantItemSkuResult.setTitle(ydMerchantItem.getTitle());
			skuList.add(ydMerchantItemSkuResult);
		}
		return skuList;
	}

	/**
	 * 提交订单减库存
	 * @param skuList
	 * @throws BusinessException
	 */
	@Override
	public void reduceItemSkuStock(List<YdMerchantItemSkuResult> skuList) throws BusinessException {
		ValidateBusinessUtils.assertCollectionNotEmpty(skuList, "err_skuList_empty", "扣除库存数据不可以为空");
		skuList.forEach(ydMerchantItemSkuResult -> {
			int result = ydMerchantItemSkuDao.reduceItemSkuStock(ydMerchantItemSkuResult.getId(), ydMerchantItemSkuResult.getNum());
			ValidateBusinessUtils.assertIdNotNull(result, "err_no_stock", ydMerchantItemSkuResult.getId() + "扣减库存失败，库存不足");
		});
	}

	/**
	 * 下单失败退还库存
	 * @param skuList
	 * @throws BusinessException
	 */
	@Override
	public void addItemSkuStock(List<YdMerchantItemSkuResult> skuList) throws BusinessException {
		ValidateBusinessUtils.assertCollectionNotEmpty(skuList, "err_skuList_empty", "退还库存数据不可以为空");
		skuList.forEach(ydMerchantItemSkuResult -> {
			ydMerchantItemSkuDao.addItemSkuStock(ydMerchantItemSkuResult.getId(), ydMerchantItemSkuResult.getNum());
		});
	}

	/**
	 * 统计平台商品， 平台商品规格，
	 * 商户商品， 商户商品规格销量
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void synItemSales() {
		// 查询所有已经完成的订单
		List<YdUserOrderDetail> allOrderList = ydUserOrderDetailDao.findCompleteOrderList(null, null);
		if (CollectionUtils.isNotEmpty(allOrderList)) {
			Map<Integer, Map<Integer, List<YdUserOrderDetail>>> allMap = allOrderList.stream().collect(
					Collectors.groupingBy(YdUserOrderDetail :: getMerchantItemId,
							Collectors.groupingBy(YdUserOrderDetail :: getMerchantSkuId)));
			setItemSales(allMap, "all");
		}

		// 查询本月已经完成的订单
		String startTime = DateUtils.getThisMonthFirstDayStr(new Date(), DateUtils.TODAY_START_DATETIME_FORMAT);
		String endTime = DateUtils.getNextMonthFirstDayStr(new Date(), DateUtils.TODAY_START_DATETIME_FORMAT);
		List<YdUserOrderDetail> thisMonthOrderList = ydUserOrderDetailDao.findCompleteOrderList(startTime, endTime);

		if (CollectionUtils.isNotEmpty(thisMonthOrderList)) {
			Map<Integer, Map<Integer, List<YdUserOrderDetail>>> thisMonthMap = thisMonthOrderList.stream().collect(
					Collectors.groupingBy(YdUserOrderDetail :: getMerchantItemId,
							Collectors.groupingBy(YdUserOrderDetail :: getMerchantSkuId)));
			setItemSales(thisMonthMap, "month");
		}
	}


	// -------------------------------------------------   私有方法     ---------------------------------------------

	private Double getItemMinPrice(YdMerchantItemResult itemInfo) {
		if (itemInfo.getIsEnable().equalsIgnoreCase("N")) return 0.0;

		List<YdMerchantItemSkuResult> skuList = itemInfo.getSkuList().stream()
				.filter(data -> data.getIsEnable().equals("Y"))
				.collect(Collectors.toList());
		ValidateBusinessUtils.assertCollectionNotEmpty(skuList, "err_no_item_id", "商品上架必须上架一个规格");
		Double minSalePrice = skuList.stream()
				.min(Comparator.comparing(YdMerchantItemSkuResult::getSalePrice)).get().getSalePrice();
		return minSalePrice;
	}

	private Map<Integer, YdMerchantItemSku> getMerchantItemSkuMapInfo(Integer merchantItemId) {
		Map<Integer, YdMerchantItemSku> merchantItemSkuMap = new HashMap<>();
		List<YdMerchantItemSku> merchantItemSkuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(merchantItemId, null);
		merchantItemSkuList.forEach(merchantItemSku -> {
			merchantItemSkuMap.put(merchantItemSku.getId(), merchantItemSku);
		});
		return merchantItemSkuMap;
	}

	private Map<Integer, YdItemSku> getItemSkuMapInfo(Integer itemId) {
		Map<Integer, YdItemSku> itemSkuMap = new HashMap<>();
		List<YdItemSku> itemSkuList = ydItemSkuDao.findItemSkuListByItemId(itemId);
		itemSkuList.forEach(itemSku -> {
			itemSkuMap.put(itemSku.getId(), itemSku);
		});
		return itemSkuMap;
	}

	private void checkMerchantSkuParam(YdMerchantItemResult itemInfo) {
		List<YdMerchantItemSkuResult> skuList = itemInfo.getSkuList();
		ValidateBusinessUtils.assertCollectionNotEmpty(skuList, "err_sku_is_null", "商品sku不可为空");

		skuList.forEach(merchantItemSku -> {
			ValidateBusinessUtils.assertNonNull(merchantItemSku.getItemId(), "err_no_item_id", "商品库对应itemId不可以为空");

			ValidateBusinessUtils.assertNonNull(merchantItemSku.getSkuId(), "err_no_item_sku_id", "商品库对应skuId不可以为空");

			ValidateBusinessUtils.assertNonNull(merchantItemSku.getSalePrice(), "err_sku_sale_price", "商品sku售价不可以为空");

			ValidateBusinessUtils.assertFalse(merchantItemSku.getSalePrice() <= 0, "err_sku_sale_price", "商品sku售价必须大于0");

			ValidateBusinessUtils.assertStringNotBlank(merchantItemSku.getIsEnable(), "err_sku_enable", "sku上架状态不可以为空");

			ValidateBusinessUtils.assertNonNull(merchantItemSku.getStock(), "err_no_stock", "sku库存不可以为空");

			if ("Y".equalsIgnoreCase(merchantItemSku.getIsEnable())) {
				ValidateBusinessUtils.assertFalse(merchantItemSku.getStock() < 0, "err_no_stock", "上架的sku库存必须大于等于0");
			}

		});
	}

	private YdMerchantItem checkMerchantItemParam(YdMerchantItemResult itemInfo) {
		YdMerchantItem ydMerchantItem = null;
		ValidateBusinessUtils.assertStringNotBlank(itemInfo.getIsEnable(), "err_item_enable", "商品上架状态不可以为空");
		if (itemInfo.getId() != null) {
			ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(itemInfo.getId());
			ValidateBusinessUtils.assertNonNull(ydMerchantItem,"err_item_not_exist", "商品不存在");
			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantItem.getIsEnable()),
					"err_item_enable", "上架中的商品不可以编辑");
		}

		ValidateBusinessUtils.assertFalse(itemInfo.getMerchantId() == null ||
				itemInfo.getMerchantId() <= 0, "err_merchant_id", "非法的商户id");

		ValidateBusinessUtils.assertStringNotBlank(itemInfo.getTitle(), "err_item_title_is_empty", "商品标题不可以为空");

		ValidateBusinessUtils.assertFalse(itemInfo.getFirstCategoryId() == null ||
				itemInfo.getFirstCategoryId() <= 0, "err_first_category_id", "一级分类不可以为空");

		ValidateBusinessUtils.assertFalse(itemInfo.getSecondCategoryId() == null ||
				itemInfo.getSecondCategoryId() <= 0, "err_second_category_id", "二级分类不可以为空");

		ValidateBusinessUtils.assertNonNull(itemInfo.getItemId(), "err_no_item_id", "商品库对应itemId不可以为空");
		return ydMerchantItem;
	}

	/**
	 * 设置商户商品销量
	 * @param orderMap
	 * @param type
	 */
	private void setItemSales(Map<Integer, Map<Integer, List<YdUserOrderDetail>>> orderMap, String type) {
		List<YdMerchantItem> merchantItemList = new ArrayList<>();
		List<YdMerchantItemSku> merchantSkuList = new ArrayList<>();
		for(Map.Entry<Integer, Map<Integer, List<YdUserOrderDetail>>> entry : orderMap.entrySet()){
			Integer merchantItemId = entry.getKey();
			Map<Integer, List<YdUserOrderDetail>> merchantSkuMap = entry.getValue();

			int itemSalesTotal = 0;
			for(Map.Entry<Integer, List<YdUserOrderDetail>> skuEntry : merchantSkuMap.entrySet()){
				Integer merchantSkuId = skuEntry.getKey();
				List<YdUserOrderDetail> orderList = skuEntry.getValue();
				int skuSalesTotal = orderList.stream().mapToInt(YdUserOrderDetail::getNum).sum();

				// 设置sku销量
				YdMerchantItemSku ydMerchantItemSku = new YdMerchantItemSku();
				ydMerchantItemSku.setId(merchantSkuId);
				if ("all".equalsIgnoreCase(type)) {
					ydMerchantItemSku.setTotalSalesNum(skuSalesTotal);
				} else {
					ydMerchantItemSku.setMonthSalesNum(skuSalesTotal);
				}
				merchantSkuList.add(ydMerchantItemSku);
				itemSalesTotal += skuSalesTotal;
			}
			// 设置商品销量
			YdMerchantItem ydMerchantItem = new YdMerchantItem();
			ydMerchantItem.setId(merchantItemId);
			if ("all".equalsIgnoreCase(type)) {
				ydMerchantItem.setTotalSalesNum(itemSalesTotal);
			} else {
				ydMerchantItem.setMonthSalesNum(itemSalesTotal);
			}
			merchantItemList.add(ydMerchantItem);
		}

		logger.info("type = " + type + ", merchantItemList = " + JSON.toJSONString(merchantItemList));
		logger.info("type = " + type + ", merchantSkuList = " + JSON.toJSONString(merchantSkuList));

		merchantItemList.forEach(ydMerchantItem -> {
			ydMerchantItemDao.updateYdShopMerchantItem(ydMerchantItem);
		});

		merchantSkuList.forEach(ydMerchantItemSku -> {
			ydMerchantItemSkuDao.updateYdMerchantItemSku(ydMerchantItemSku);
		});
	}

	/**
	 * 处理商户的图片以及详情
	 * @param merchantItemId
	 * @param content
	 * @param imageList
	 */
	private void initMerchantItemImageAndContent(Integer merchantItemId, String content, List<YdMerchantItemImageResult> imageList) {
		// 处理商户自己的content
		ydMerchantItemContentDao.deleteYdMerchantItemContent(merchantItemId);
		if (StringUtils.isNotEmpty(content)) {
			YdMerchantItemContent merchantItemContent = new YdMerchantItemContent();
			merchantItemContent.setCreateTime(new Date());
			merchantItemContent.setUpdateTime(new Date());
			merchantItemContent.setItemId(merchantItemId);
			merchantItemContent.setContent(content);
			ydMerchantItemContentDao.insertYdMerchantItemContent(merchantItemContent);
		}

		// 处理商户自己的image
		ydMerchantItemImageDao.deleteYdMerchantItemImage(merchantItemId);
		if (CollectionUtils.isNotEmpty(imageList)) {
			for (int i = 0; i < imageList.size(); i++) {
				YdMerchantItemImage merchantItemImage = new YdMerchantItemImage();
				merchantItemImage.setCreateTime(new Date());
				merchantItemImage.setUpdateTime(new Date());
				merchantItemImage.setSort(i);
				merchantItemImage.setItemId(merchantItemId);
				merchantItemImage.setImageUrl(imageList.get(i).getImageUrl());
				ydMerchantItemImageDao.insertYdMerchantItemImage(merchantItemImage);
			}
		}
	}

}

