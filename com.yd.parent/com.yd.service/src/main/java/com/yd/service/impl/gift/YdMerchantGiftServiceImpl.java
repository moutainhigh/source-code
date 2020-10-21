package com.yd.service.impl.gift;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.req.MerchantItmSkuReq;
import com.yd.api.result.cart.YdUserCartResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.cart.YdUserCartService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGiftCart;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.bean.market.YdMerchantFirstCategoryGiftRate;
import com.yd.service.bean.market.YdMerchantGiftRate;
import com.yd.service.bean.market.YdMerchantItemGiftRate;
import com.yd.service.bean.market.YdMerchantSecondCategoryGiftRate;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftCartDao;
import com.yd.service.dao.item.YdMerchantItemSkuDao;
import com.yd.service.dao.market.YdMerchantFirstCategoryGiftRateDao;
import com.yd.service.dao.market.YdMerchantGiftRateDao;
import com.yd.service.dao.market.YdMerchantItemGiftRateDao;
import com.yd.service.dao.market.YdMerchantSecondCategoryGiftRateDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.bean.gift.YdMerchantGift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户礼品库Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 09:33:43
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantGiftServiceImpl implements YdMerchantGiftService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantGiftServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdUserCartService ydUserCartService;

	@Resource
	private YdMerchantItemSkuDao ydMerchantItemSkuDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdMerchantGiftCartDao ydMerchantGiftCartDao;

	@Resource
	private YdMerchantGiftRateDao ydMerchantGiftRateDao;

	@Resource
	private YdMerchantItemGiftRateDao ydMerchantItemGiftRateDao;

	@Resource
	private YdMerchantFirstCategoryGiftRateDao ydMerchantFirstCategoryGiftRateDao;

	@Resource
	private YdMerchantSecondCategoryGiftRateDao ydMerchantSecondCategoryGiftRateDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantGiftResult getYdMerchantGiftById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantGiftResult ydMerchantGiftResult = null;
		YdMerchantGift ydMerchantGift = this.ydMerchantGiftDao.getYdMerchantGiftById(id);
		if (ydMerchantGift != null) {
			ydMerchantGiftResult = new YdMerchantGiftResult();
			BeanUtilExt.copyProperties(ydMerchantGiftResult, ydMerchantGift);
			if ("platform".equalsIgnoreCase(ydMerchantGiftResult.getGiftType())) {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGiftResult.getGiftId());
				ydMerchantGiftResult.setTitle(ydGift.getTitle());
				ydMerchantGiftResult.setSubTitle(ydGift.getSubTitle());
				ydMerchantGiftResult.setMarketPrice(ydGift.getMarketPrice());
				ydMerchantGiftResult.setSalePrice(ydGift.getSalePrice());
				ydMerchantGiftResult.setImageUrl(ydGift.getImageUrl());
				ydMerchantGiftResult.setGiftDesc(ydGift.getGiftDesc());
			}
		}
		return ydMerchantGiftResult;
	}

	@Override
	public YdMerchantGiftResult getYdMerchantGiftDetail(Integer id) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_gift_id", "礼品id不可以为空");

		YdMerchantGift ydMerchantGift = this.ydMerchantGiftDao.getYdMerchantGiftById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantGift == null,
				"err_exist_merchant_gift", "礼品不存在");

		if ("platform".equalsIgnoreCase(ydMerchantGift.getGiftType())) {
			YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
			ydMerchantGift.setTitle(ydGift.getTitle());
			ydMerchantGift.setSubTitle(ydGift.getSubTitle());
			ydMerchantGift.setSalePrice(ydGift.getSalePrice());
			ydMerchantGift.setMarketPrice(ydGift.getMarketPrice());
			ydMerchantGift.setImageUrl(ydGift.getImageUrl());
			ydMerchantGift.setGiftDesc(ydGift.getGiftDesc());
		}

		YdMerchantGiftResult ydMerchantGiftResult = new YdMerchantGiftResult();
		BeanUtilExt.copyProperties(ydMerchantGiftResult, ydMerchantGift);
		return ydMerchantGiftResult;
	}

	@Override
	public List<YdMerchantGiftResult> findYdMerchantGiftByIdList(Integer merchantId, List<Integer> idList) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(idList),
				"err_empty_gift_id_list", "礼品id不可以为空");
		List<YdMerchantGift> dataList = this.ydMerchantGiftDao.findYdMerchantGiftByIdList(merchantId, idList);
		List<YdMerchantGiftResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftResult.class);
		resultList.forEach(ydMerchantGiftResult -> {
			if ("platform".equalsIgnoreCase(ydMerchantGiftResult.getGiftType())) {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGiftResult.getGiftId());
				ydMerchantGiftResult.setTitle(ydGift.getTitle());
				ydMerchantGiftResult.setSubTitle(ydGift.getSubTitle());
				ydMerchantGiftResult.setMarketPrice(ydGift.getMarketPrice());
				ydMerchantGiftResult.setSalePrice(ydGift.getSalePrice());
				ydMerchantGiftResult.setImageUrl(ydGift.getImageUrl());
				ydMerchantGiftResult.setGiftDesc(ydGift.getGiftDesc());
			}
		});
		return resultList;
	}

	@Override
	public Page<YdMerchantGiftResult> findYdMerchantGiftListByPage(YdMerchantGiftResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantGiftResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());
		YdMerchantGift ydMerchantGift = new YdMerchantGift();
		BeanUtilExt.copyProperties(ydMerchantGift, params);
		
		int amount = this.ydMerchantGiftDao.getYdMerchantGiftCount(ydMerchantGift);
		if (amount > 0) {
			List<YdMerchantGift> dataList = this.ydMerchantGiftDao.findYdMerchantGiftListByPage(
					ydMerchantGift, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantGiftResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantGiftResult> getAll(YdMerchantGiftResult ydMerchantGiftResult) {
		YdMerchantGift ydMerchantGift = null;
		if (ydMerchantGiftResult != null) {
			ydMerchantGift = new YdMerchantGift();
			BeanUtilExt.copyProperties(ydMerchantGift, ydMerchantGiftResult);
		}
		List<YdMerchantGift> dataList = this.ydMerchantGiftDao.getAll(ydMerchantGift);
		List<YdMerchantGiftResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftResult.class);
		return resultList;
	}

	/**
	 * 新增商户商品
	 * @param ydMerchantGiftResult
	 */
	@Override
	public void insertYdMerchantGift(YdMerchantGiftResult ydMerchantGiftResult) {
		checkMerchantGiftParams(ydMerchantGiftResult);
		ydMerchantGiftResult.setCreateTime(new Date());
		ydMerchantGiftResult.setUpdateTime(new Date());
		YdMerchantGift ydMerchantGift = new YdMerchantGift();
		BeanUtilExt.copyProperties(ydMerchantGift, ydMerchantGiftResult);
		this.ydMerchantGiftDao.insertYdMerchantGift(ydMerchantGift);
	}

	/**
	 * 修改商户商品
	 * @param ydMerchantGiftResult
	 */
	@Override
	public void updateYdMerchantGift(YdMerchantGiftResult ydMerchantGiftResult) {
		checkMerchantGiftParams(ydMerchantGiftResult);
		ydMerchantGiftResult.setUpdateTime(new Date());
		YdMerchantGift ydMerchantGift = new YdMerchantGift();
		BeanUtilExt.copyProperties(ydMerchantGift, ydMerchantGiftResult);
		this.ydMerchantGiftDao.updateYdMerchantGift(ydMerchantGift);
	}

	/**
	 * 上下架商户礼品
	 * @param merchantId 	商户id
	 * @param id			商户礼品id
	 * @param isEnable		Y | N
	 */
	@Override
	public void upOrDownYdMerchantGift(Integer merchantId, Integer id, String isEnable) throws BusinessException{
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantGift merchantGift = this.ydMerchantGiftDao.getYdMerchantGiftById(id);
		ValidateBusinessUtils.assertFalse(merchantGift == null,
				"err_exist_merchant_gift", "礼品不存在");
		ValidateBusinessUtils.assertFalse(merchantGift.getIsEnable().equalsIgnoreCase(isEnable),
				"err_exist_merchant_gift", "请勿重复操作");
		merchantGift.setIsEnable(isEnable);
		this.ydMerchantGiftDao.updateYdMerchantGift(merchantGift);
	}

	/**
	 * 删除商户礼品
	 * @param id	商户礼品id
	 */
	@Override
	public void deleteYdMerchantGift(Integer id) throws BusinessException{
		YdMerchantGift merchantGift = this.ydMerchantGiftDao.getYdMerchantGiftById(id);
		ValidateBusinessUtils.assertFalse(merchantGift == null,
				"err_exist_merchant_gift", "礼品不存在");
		this.ydMerchantGiftDao.deleteYdMerchantGift(id);
	}

	/**
	 * 导入商户礼品库
	 * @param currMerchantId 商户id
	 * @param carIdList	 购物车idList
	 */
	@Override
	public void exportMerchantGift(Integer currMerchantId, List<Integer> carIdList) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(currMerchantId);
		Integer merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(carIdList),
				"err_car_id_list", "购物车id不可以为空");
		List<YdMerchantGiftCart> giftCarList = ydMerchantGiftCartDao.getYdMerchantGiftCartByIdList(carIdList);

		// 根据礼品购物车查询平台礼品,
		List<Integer> giftIdList = giftCarList.stream().map(YdMerchantGiftCart::getGiftId).collect(Collectors.toList());
		List<YdGift> giftList = ydGiftDao.getYdGiftByIdList(giftIdList);
		logger.info("====导入礼品查询的礼品数据=" + JSON.toJSONString(giftList));
		if (CollectionUtils.isEmpty(giftList)) return;

		giftList.forEach(ydGift -> {
			// 查询是否导入过礼品库
			YdMerchantGift ydMerchantGift = new YdMerchantGift();
			ydMerchantGift.setMerchantId(merchantId);
			ydMerchantGift.setGiftId(ydGift.getId());
			ydMerchantGift.setGiftType("platform");
			List<YdMerchantGift> allList = ydMerchantGiftDao.getAll(ydMerchantGift);
			if (CollectionUtils.isEmpty(allList)) {
				// 为空是第一次导入,不为空说明导入过，不处理
				ydMerchantGift = new YdMerchantGift();
				ydMerchantGift.setCreateTime(new Date());

				ydMerchantGift.setGiftType("platform");
				ydMerchantGift.setMerchantId(merchantId);
				ydMerchantGift.setSupplierId(ydGift.getSupplierId());
				ydMerchantGift.setCategoryId(null);
				ydMerchantGift.setGiftId(ydGift.getId());

				ydMerchantGift.setTitle(ydGift.getTitle());
				ydMerchantGift.setSubTitle(ydGift.getSubTitle());
				ydMerchantGift.setSalePrice(ydGift.getSalePrice());
				ydMerchantGift.setMarketPrice(ydGift.getMarketPrice());
				ydMerchantGift.setImageUrl(ydGift.getImageUrl());
				ydMerchantGift.setGiftDesc(ydGift.getGiftDesc());
				ydMerchantGift.setIsEnable("N");
				ydMerchantGiftDao.insertYdMerchantGift(ydMerchantGift);
			}
		});
	}

	/**
	 * 根据商品查询礼品列表
	 * @param merchantId	商户id
	 * @param skuList	商户商品skuId + num
	 * @return
	 */
	@Override
	public List<YdMerchantGiftResult> findGiftListBySkuList(Integer merchantId, List<MerchantItmSkuReq> skuList) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(skuList),
				"err_empty_sku_list", "参数不可以为空");

		skuList.forEach(merchantItmSkuReq -> {
			ValidateBusinessUtils.assertFalse(merchantItmSkuReq.getMerchantItemSkuId() == null,
					"err_empty_sku_id", "参数不可以为空");
			ValidateBusinessUtils.assertFalse(merchantItmSkuReq.getNum() == null || merchantItmSkuReq.getNum() <= 0,
					"err_empty_sku_num", "参数不可以为空");
		});

		// 计算所有商品礼品占比总金额
		double giftSumPrice = getGiftSumPrice(merchantId, skuList);

		// 根据礼品占比总金额查询可选的礼品
		YdMerchantGift ydMerchantGift = new YdMerchantGift();
		ydMerchantGift.setMerchantId(merchantId);
		ydMerchantGift.setMaxPrice(giftSumPrice);
		ydMerchantGift.setIsEnable("Y");
		ydMerchantGift.setIsFlag("N");
		ydMerchantGift.setGiftStatus("in_shelves");
		List<YdMerchantGift> giftList = ydMerchantGiftDao.findYdMerchantGiftListByPage(ydMerchantGift, 0, Integer.MAX_VALUE);
        List<YdMerchantGiftResult> resultList = DTOUtils.convertList(giftList, YdMerchantGiftResult.class);

		// 设置免费礼品最多领取数量
        int skuTotal = skuList.stream().mapToInt(MerchantItmSkuReq::getNum).sum();
        resultList.forEach(ydMerchantGiftResult -> {
            if (ydMerchantGiftResult.getSalePrice() == 0) {
                ydMerchantGiftResult.setLimitCount(skuTotal);
            } else {
                ydMerchantGiftResult.setLimitCount(0);
            }
        });
        return resultList;
	}

	/**
	 * 计算所有商品礼品占比总金额
	 * @param merchantId
	 * @param skuList
	 * @return
	 */
	@Override
	public double getGiftSumPrice(Integer merchantId, List<MerchantItmSkuReq> skuList) {
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(skuList),
				"err_empty_sku_list", "参数不可以为空");

		skuList.forEach(merchantItmSkuReq -> {
			ValidateBusinessUtils.assertFalse(merchantItmSkuReq.getMerchantItemSkuId() == null,
					"err_empty_sku_id", "参数不可以为空");

			ValidateBusinessUtils.assertFalse(merchantItmSkuReq.getNum() == null || merchantItmSkuReq.getNum() <= 0,
					"err_empty_sku_num", "参数不可以为空");
		});

		double giftSumPrice = 0.00;
		// 查询所有的sku详情, 存入map中
		Map<Integer, YdMerchantItemSku> skuMap = new HashMap<>();
		List<Integer> skuIdList = skuList.stream().map(MerchantItmSkuReq::getMerchantItemSkuId).collect(Collectors.toList());
		List<YdMerchantItemSku> merchantItemSkuList = ydMerchantItemSkuDao.findYdMerchantItemSkuListByIds(skuIdList);
		merchantItemSkuList.forEach(ydMerchantItemSku -> {
			skuMap.put(ydMerchantItemSku.getId(), ydMerchantItemSku);
		});

		// 查询门店礼品占比
		YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

		// 查询所有一级分类礼品占比, 存入map中
		Set<Integer> firstCategoryIdSet = merchantItemSkuList.stream().map(YdMerchantItemSku::getFirstCategoryId).collect(Collectors.toSet());
		List<YdMerchantFirstCategoryGiftRate> firstCategoryGiftRateList = ydMerchantFirstCategoryGiftRateDao.findListByFirstCategoryIdList(merchantId, firstCategoryIdSet);
		Map<Integer, YdMerchantFirstCategoryGiftRate> firstCategoryGiftRateMap = new HashMap<>();
		firstCategoryGiftRateList.forEach(ydMerchantFirstCategoryGiftRate -> {
			firstCategoryGiftRateMap.put(ydMerchantFirstCategoryGiftRate.getFirstCategoryId(), ydMerchantFirstCategoryGiftRate);
		});

		// 查询所有二级分类礼品占比, 存入map中
		Set<Integer> secondCategoryIdSet = merchantItemSkuList.stream().map(YdMerchantItemSku::getSecondCategoryId).collect(Collectors.toSet());
		List<YdMerchantSecondCategoryGiftRate> secondCategoryGiftRateList = ydMerchantSecondCategoryGiftRateDao.findListBySecondCategoryIdList(merchantId, secondCategoryIdSet);
		Map<Integer, YdMerchantSecondCategoryGiftRate> secondCategoryGiftRateMap = new HashMap<>();
		secondCategoryGiftRateList.forEach(ydMerchantSecondCategoryGiftRate -> {
			secondCategoryGiftRateMap.put(ydMerchantSecondCategoryGiftRate.getSecondCategoryId(), ydMerchantSecondCategoryGiftRate);
		});

		// 查询所有商品的礼品占比
		Set<Integer> merchantItemIdSet = merchantItemSkuList.stream().map(YdMerchantItemSku::getMerchantItemId).collect(Collectors.toSet());
		List<YdMerchantItemGiftRate> itemGiftRateList = ydMerchantItemGiftRateDao.findListByMerchantIdList(merchantId, merchantItemIdSet);
		Map<Integer, YdMerchantItemGiftRate> itemGiftRateMap = new HashMap<>();
		itemGiftRateList.forEach(ydMerchantItemGiftRate -> {
			itemGiftRateMap.put(ydMerchantItemGiftRate.getMerchantItemId(), ydMerchantItemGiftRate);
		});

		// 计算礼品占比总金额 sku售价 * 数量 * 礼品占比
		for (MerchantItmSkuReq merchantItmSkuReq : skuList) {
			YdMerchantItemSku ydMerchantItemSku = skuMap.get(merchantItmSkuReq.getMerchantItemSkuId());

			// 查看商品占比
			YdMerchantItemGiftRate itemGiftRate = itemGiftRateMap.get(ydMerchantItemSku.getMerchantItemId());
			if (itemGiftRate != null) {
				giftSumPrice += new BigDecimal(ydMerchantItemSku.getSalePrice() + "")
						.multiply(new BigDecimal(merchantItmSkuReq.getNum() + ""))
						.multiply(new BigDecimal(itemGiftRate.getRate() + ""))
						.divide(new BigDecimal(100 + ""))
						.setScale(2, BigDecimal.ROUND_UP).doubleValue();
				continue;
			}

			// 二级分类占比
			YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = secondCategoryGiftRateMap.get(ydMerchantItemSku.getSecondCategoryId());
			if (ydMerchantSecondCategoryGiftRate != null) {
				giftSumPrice += new BigDecimal(ydMerchantItemSku.getSalePrice() + "")
						.multiply(new BigDecimal(merchantItmSkuReq.getNum() + ""))
						.multiply(new BigDecimal(ydMerchantSecondCategoryGiftRate.getRate() + ""))
						.divide(new BigDecimal(100 + ""))
						.setScale(2, BigDecimal.ROUND_UP).doubleValue();
				continue;
			}

			// 一级分类占比
			YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = firstCategoryGiftRateMap.get(ydMerchantItemSku.getFirstCategoryId());
			if (ydMerchantFirstCategoryGiftRate != null) {
				giftSumPrice += new BigDecimal(ydMerchantItemSku.getSalePrice() + "")
						.multiply(new BigDecimal(merchantItmSkuReq.getNum() + ""))
						.multiply(new BigDecimal(ydMerchantFirstCategoryGiftRate.getRate() + ""))
						.divide(new BigDecimal(100 + ""))
						.setScale(2, BigDecimal.ROUND_UP).doubleValue();
				continue;
			}

			// 门店礼品占比
			if (ydMerchantGiftRate != null) {
				giftSumPrice += new BigDecimal(ydMerchantItemSku.getSalePrice() + "")
						.multiply(new BigDecimal(merchantItmSkuReq.getNum() + ""))
						.multiply(new BigDecimal(ydMerchantGiftRate.getRate() + ""))
						.divide(new BigDecimal(100 + ""))
						.setScale(2, BigDecimal.ROUND_UP).doubleValue();
				continue;
			}
		}
		return giftSumPrice;
	}

	/**
	 * 获取下单时订单商品礼品占比总金额
	 * @param merchantId 商户id
	 * @param carIds	 购物车ids
	 * @param skuId		 规格id
	 * @param num		 购买数量
	 * @param num		 car 代表购物车
	 * @return 礼品占比总金额
	 * @throws BusinessException
	 */
	@Override
	public double getOrderGiftPrice(Integer merchantId, String carIds, Integer skuId, Integer num, String type) throws BusinessException {
		Double totalPrice = 0.00;
		List<MerchantItmSkuReq> skuList = new ArrayList<>();
		if ("car".equalsIgnoreCase(type)) {
			List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
					.map(Integer::valueOf).collect(Collectors.toList());
			List<YdUserCartResult> userCarList = ydUserCartService.findYdUserCartByIds(carIdList);

			userCarList.forEach(ydUserCartResult -> {
				MerchantItmSkuReq merchantItmSkuReq = new MerchantItmSkuReq();
				merchantItmSkuReq.setNum(ydUserCartResult.getNum());
				merchantItmSkuReq.setMerchantItemSkuId(ydUserCartResult.getMerchantSkuId());
				skuList.add(merchantItmSkuReq);
			});
		} else {
			MerchantItmSkuReq merchantItmSkuReq = new MerchantItmSkuReq();
			merchantItmSkuReq.setNum(num);
			merchantItmSkuReq.setMerchantItemSkuId(skuId);
			skuList.add(merchantItmSkuReq);
		}
		totalPrice = getGiftSumPrice(merchantId, skuList);
		return totalPrice;
	}

	/**
	 * 校验保存修改参数
	 * @param ydMerchantGiftResult
	 */
	private void checkMerchantGiftParams(YdMerchantGiftResult ydMerchantGiftResult) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantGiftResult.getMerchantId());
		ydMerchantGiftResult.setMerchantId(storeInfo.getId());

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMerchantGiftResult.getTitle()),
				"err_empty_gift_title", "礼品名称不可以为空");
		ValidateBusinessUtils.assertFalse(ydMerchantGiftResult.getSalePrice() == null,
				"err_empty_sale_price", "售价不可以为空");
		ValidateBusinessUtils.assertFalse(ydMerchantGiftResult.getMarketPrice() == null,
				"err_empty_marketing_price", "划线价不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMerchantGiftResult.getIsEnable()),
				"err_empty_is_enable", "上架状态不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMerchantGiftResult.getImageUrl()),
				"err_empty_image", "礼品图片不可以为空");
		// 原型要求描述为空默认的描述
		if (StringUtil.isEmpty(ydMerchantGiftResult.getGiftDesc())) {
			ydMerchantGiftResult.setGiftDesc("礼品款式随机发");
		}
	}


}

