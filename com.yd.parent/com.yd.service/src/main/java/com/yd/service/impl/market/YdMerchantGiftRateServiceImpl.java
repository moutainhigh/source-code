package com.yd.service.impl.market;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yd.api.result.market.YdMerchantGiftRateResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.market.YdMerchantGiftRateService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.bean.item.YdMerchantItemCategory;
import com.yd.service.bean.market.YdMerchantFirstCategoryGiftRate;
import com.yd.service.bean.market.YdMerchantItemGiftRate;
import com.yd.service.bean.market.YdMerchantSecondCategoryGiftRate;
import com.yd.service.dao.item.YdMerchantItemCategoryDao;
import com.yd.service.dao.item.YdMerchantItemDao;
import com.yd.service.dao.market.YdMerchantFirstCategoryGiftRateDao;
import com.yd.service.dao.market.YdMerchantItemGiftRateDao;
import com.yd.service.dao.market.YdMerchantSecondCategoryGiftRateDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.market.YdMerchantGiftRateDao;
import com.yd.service.bean.market.YdMerchantGiftRate;

/**
 * @Title:门店礼品占比Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:09
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantGiftRateServiceImpl implements YdMerchantGiftRateService {

	@Resource
	private YdMerchantGiftRateDao ydMerchantGiftRateDao;

	@Resource
	private YdMerchantItemGiftRateDao ydMerchantItemGiftRateDao;

	@Resource
	private YdMerchantFirstCategoryGiftRateDao ydMerchantFirstCategoryGiftRateDao;

	@Resource
	private YdMerchantSecondCategoryGiftRateDao ydMerchantSecondCategoryGiftRateDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdMerchantItemCategoryDao ydMerchantItemCategoryDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantGiftRateResult getYdMerchantGiftRateById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantGiftRateResult ydMerchantGiftRateResult = null;
		YdMerchantGiftRate ydMerchantGiftRate = this.ydMerchantGiftRateDao.getYdMerchantGiftRateById(id);
		if (ydMerchantGiftRate != null) {
			ydMerchantGiftRateResult = new YdMerchantGiftRateResult();
			BeanUtilExt.copyProperties(ydMerchantGiftRateResult, ydMerchantGiftRate);
		}
		return ydMerchantGiftRateResult;
	}

	@Override
	public Page<YdMerchantGiftRateResult> findYdMerchantGiftRateListByPage(YdMerchantGiftRateResult params, PagerInfo pagerInfo) throws BusinessException {

		YdMerchantGiftRate ydMerchantGiftRate = new YdMerchantGiftRate();
		BeanUtilExt.copyProperties(ydMerchantGiftRate, params);


		if (params.getFirstCategoryId() == null && params.getSecondCategoryId() == null) {
			// 查询一级分类比率
			return findFirstCategoryRateByPage(params.getMerchantId(), pagerInfo);
		} else if (params.getFirstCategoryId() != null && params.getSecondCategoryId() == null) {
			// 查询二级分类比率
			return findSecondCategoryRateByPage(params.getMerchantId(), params.getFirstCategoryId(), pagerInfo);
		} else {
			// 查询商品比率
			return findMerchantItemRateByPage(params.getMerchantId(), params.getSecondCategoryId(), pagerInfo);
		}
	}

	@Override
	public List<YdMerchantGiftRateResult> getAll(YdMerchantGiftRateResult ydMerchantGiftRateResult) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantGiftRateResult.getMerchantId());
		Integer merchantId = storeInfo.getId();

		if (ydMerchantGiftRateResult.getFirstCategoryId() == null && ydMerchantGiftRateResult.getSecondCategoryId() == null) {
			// 查询一级分类比率
			return findFirstCategoryRateList(merchantId);
		} else if (ydMerchantGiftRateResult.getFirstCategoryId() != null && ydMerchantGiftRateResult.getSecondCategoryId() == null) {
			// 查询二级分类比率
			return findSecondCategoryRateList(merchantId, ydMerchantGiftRateResult.getFirstCategoryId());
		} else {
			// 查询商品比率
			return findMerchantItemRateList(merchantId, ydMerchantGiftRateResult.getSecondCategoryId());
		}
	}

	/**
	 * 更新设置礼品比率
	 * @param type	store | first | second | item
	 * @param merchantId	礼品比率
	 * @param rate	礼品比率
	 * @param firstCategoryId 一级分类id
	 * @param secondCategoryId	二级分类id
	 * @param merchantItemId	商户商品id
	 */
	@Override
	public void updateGiftRate(String type, Integer merchantId, Integer rate, Integer firstCategoryId, Integer secondCategoryId, Integer merchantItemId) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(rate == null || rate <= 0,
				"err_rate", "礼品比率不可以为空");
		if ("store".equals(type)) {
			updateMerchantRate(merchantId, rate);
		} else if ("first".equals(type)) {
			updateFirstCategoryRate(merchantId, firstCategoryId, rate);

		} else if ("second".equals(type)) {
			updateSecondCategoryRate(merchantId, secondCategoryId, rate);

		} else if ("item".equals(type)) {
			updateMerchantItemRate(merchantId, merchantItemId, rate);
		}
	}

	//------------------------------------------------------ private method


	private Page<YdMerchantGiftRateResult> findFirstCategoryRateByPage(Integer merchantId, PagerInfo pagerInfo) {
		Page<YdMerchantGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());

		Integer amount = ydMerchantFirstCategoryGiftRateDao.getFirstCategoryRateCount(merchantId);
		if (amount > 0) {
			// 查询门店礼品占比
			YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

			// 查询一级分类礼品占比
			List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.findFirstCategoryRateListByPage(
					merchantId, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantGiftRateResult> resultList = DTOUtils.convertList(firstCategoryRateList, YdMerchantGiftRateResult.class);

			// 一级分类占比为空, 如果有门店礼品占比, 设置一级分类占比为门店占比
			if (ydMerchantGiftRate != null && ydMerchantGiftRate.getRate() != 0) {
				resetGiftRate(resultList, ydMerchantGiftRate.getRate());
			}
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	private Page<YdMerchantGiftRateResult> findSecondCategoryRateByPage(Integer merchantId, Integer firstCategoryId, PagerInfo pagerInfo) {
		Page<YdMerchantGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());

		// 查询二级分类礼品占比
		Integer amount = ydMerchantSecondCategoryGiftRateDao.getSecondCategoryRateCount(merchantId, firstCategoryId);
		if (amount > 0) {
			pageData.setTotalRecord(amount);

			// 查询门店礼品占比
			YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

			// 查询一级分类礼品占比
			YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			ydMerchantFirstCategoryGiftRate.setFirstCategoryId(firstCategoryId);
			ydMerchantFirstCategoryGiftRate.setMerchantId(merchantId);
			List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.getAll(ydMerchantFirstCategoryGiftRate);

			List<YdMerchantSecondCategoryGiftRate> secondCategoryRateList = ydMerchantSecondCategoryGiftRateDao.findSecondCategoryRateByPage(
					merchantId, firstCategoryId, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantGiftRateResult> resultList = DTOUtils.convertList(secondCategoryRateList, YdMerchantGiftRateResult.class);
			if (CollectionUtils.isNotEmpty(firstCategoryRateList)) {
				resetGiftRate(resultList, firstCategoryRateList.get(0).getRate());
				return pageData;
			}
			if (ydMerchantGiftRate != null) {
				resetGiftRate(resultList, ydMerchantGiftRate.getRate());
				return pageData;
			}
		}
		return pageData;
	}

	private Page<YdMerchantGiftRateResult> findMerchantItemRateByPage(Integer merchantId, Integer secondCategoryId, PagerInfo pagerInfo) {
		Page<YdMerchantGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		// 查询一级分类
		YdMerchantItemCategory ydShopMerchantItemCategory = ydMerchantItemCategoryDao.getYdShopMerchantItemCategoryById(secondCategoryId);
		// 查询商品占比
		int amount = ydMerchantItemGiftRateDao.getMerchantItemRateCount(merchantId, secondCategoryId);
		if (amount > 0) {
			// 查询门店礼品占比
			YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

			// 查询一级分类礼品占比
			YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			ydMerchantFirstCategoryGiftRate.setMerchantId(merchantId);
			ydMerchantFirstCategoryGiftRate.setFirstCategoryId(ydShopMerchantItemCategory.getId());
			List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.getAll(ydMerchantFirstCategoryGiftRate);

			// 查询二级分类礼品占比
			YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
			ydMerchantSecondCategoryGiftRate.setMerchantId(merchantId);
			ydMerchantSecondCategoryGiftRate.setSecondCategoryId(secondCategoryId);
			List<YdMerchantSecondCategoryGiftRate> secondCategoryRateList = ydMerchantSecondCategoryGiftRateDao.getAll(ydMerchantSecondCategoryGiftRate);

			List<YdMerchantItemGiftRate> itemRateList = ydMerchantItemGiftRateDao.findMerchantItemRateByPage(
					merchantId, secondCategoryId, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantGiftRateResult> itemRateResultList = DTOUtils.convertList(itemRateList, YdMerchantGiftRateResult.class);
			if (CollectionUtils.isNotEmpty(secondCategoryRateList)) {
				resetGiftRate(itemRateResultList, secondCategoryRateList.get(0).getRate());
				return pageData;
			}
			if (CollectionUtils.isNotEmpty(firstCategoryRateList)) {
				resetGiftRate(itemRateResultList, firstCategoryRateList.get(0).getRate());
				return pageData;
			}
			if (ydMerchantGiftRate != null) {
				resetGiftRate(itemRateResultList, ydMerchantGiftRate.getRate());
				return pageData;
			}
		}
		return pageData;
	}


	/**
	 * 查询设置一级分类占比
	 * @param merchantId
	 * @return
	 */
	private List<YdMerchantGiftRateResult> findFirstCategoryRateList(Integer merchantId) {
		// 查询门店礼品占比
		YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

		// 查询一级分类礼品占比
		List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.findFirstCategoryRateListByPage(merchantId, 0, Integer.MAX_VALUE);
		List<YdMerchantGiftRateResult> resultList = DTOUtils.convertList(firstCategoryRateList, YdMerchantGiftRateResult.class);

		// 一级分类占比为空, 如果有门店礼品占比, 设置一级分类占比为门店占比
		if (ydMerchantGiftRate != null && ydMerchantGiftRate.getRate() != 0) {
			resetGiftRate(resultList, ydMerchantGiftRate.getRate());
		}
		return resultList;
	}

	/**
	 * 查询设置二级分类占比
	 * @param merchantId
	 * @param firstCategoryId
	 * @return
	 */
	private List<YdMerchantGiftRateResult> findSecondCategoryRateList(Integer merchantId, Integer firstCategoryId) {
		// 查询门店礼品占比
		YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

		// 查询一级分类礼品占比
		YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
		ydMerchantFirstCategoryGiftRate.setFirstCategoryId(firstCategoryId);
		ydMerchantFirstCategoryGiftRate.setMerchantId(merchantId);
		List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.getAll(ydMerchantFirstCategoryGiftRate);

		// 查询二级分类礼品占比
		List<YdMerchantSecondCategoryGiftRate> secondCategoryRateList = ydMerchantSecondCategoryGiftRateDao.findSecondCategoryRateByPage(
				merchantId, firstCategoryId, 0, Integer.MAX_VALUE);
		List<YdMerchantGiftRateResult> resultList = DTOUtils.convertList(secondCategoryRateList, YdMerchantGiftRateResult.class);
		if (CollectionUtils.isNotEmpty(firstCategoryRateList)) {
			resetGiftRate(resultList, firstCategoryRateList.get(0).getRate());
			return resultList;
		}
		if (ydMerchantGiftRate != null) {
			resetGiftRate(resultList, ydMerchantGiftRate.getRate());
			return resultList;
		}
		return resultList;
	}

	/**
	 * 查询商品比率
	 * @param merchantId
	 * @param secondCategoryId
	 * @return
	 */
	private List<YdMerchantGiftRateResult> findMerchantItemRateList(Integer merchantId, Integer secondCategoryId) {
		// 查询一级分类
		YdMerchantItemCategory ydShopMerchantItemCategory = ydMerchantItemCategoryDao.getYdShopMerchantItemCategoryById(secondCategoryId);
		// 查询门店礼品占比
		YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);

		// 查询一级分类礼品占比
		YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
		ydMerchantFirstCategoryGiftRate.setMerchantId(merchantId);
		ydMerchantFirstCategoryGiftRate.setFirstCategoryId(ydShopMerchantItemCategory.getId());
		List<YdMerchantFirstCategoryGiftRate> firstCategoryRateList = ydMerchantFirstCategoryGiftRateDao.getAll(ydMerchantFirstCategoryGiftRate);

		// 查询二级分类礼品占比
		YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
		ydMerchantSecondCategoryGiftRate.setMerchantId(merchantId);
		ydMerchantSecondCategoryGiftRate.setSecondCategoryId(secondCategoryId);
		List<YdMerchantSecondCategoryGiftRate> secondCategoryRateList = ydMerchantSecondCategoryGiftRateDao.getAll(ydMerchantSecondCategoryGiftRate);

		// 查询商品占比
		List<YdMerchantItemGiftRate> itemRateList = ydMerchantItemGiftRateDao.findMerchantItemRateByPage(merchantId, secondCategoryId, 0, Integer.MAX_VALUE);
		List<YdMerchantGiftRateResult> itemRateResultList = DTOUtils.convertList(itemRateList, YdMerchantGiftRateResult.class);

		if (CollectionUtils.isNotEmpty(secondCategoryRateList)) {
			resetGiftRate(itemRateResultList, secondCategoryRateList.get(0).getRate());
			return itemRateResultList;
		}
		if (CollectionUtils.isNotEmpty(firstCategoryRateList)) {
			resetGiftRate(itemRateResultList, firstCategoryRateList.get(0).getRate());
			return itemRateResultList;
		}
		if (ydMerchantGiftRate != null) {
			resetGiftRate(itemRateResultList, ydMerchantGiftRate.getRate());
			return itemRateResultList;
		}
		return itemRateResultList;
	}

	/**
	 * 礼品比率为空的话根据条件充值比率
	 * @param itemRateResultList
	 * @param newRate
	 */
	private void resetGiftRate(List<YdMerchantGiftRateResult> itemRateResultList, Integer newRate) {
		itemRateResultList.forEach(data -> {
			if (data.getRate() == null || data.getRate() == 0 ) {
				data.setRate(newRate);
			}
		});
	}


	/**
	 * 设置门店礼品礼品占比
	 * @param merchantId
	 * @param rate
	 */
	private void updateMerchantRate(Integer merchantId, Integer rate) {
		YdMerchantGiftRate ydMerchantGiftRate = ydMerchantGiftRateDao.getYdMerchantGiftRateByMerchantId(merchantId);
		if (ydMerchantGiftRate == null) {
			ydMerchantGiftRate = new YdMerchantGiftRate();
			ydMerchantGiftRate.setCreateTime(new Date());
			ydMerchantGiftRate.setMerchantId(merchantId);
			ydMerchantGiftRate.setRate(rate);
			ydMerchantGiftRateDao.insertYdMerchantGiftRate(ydMerchantGiftRate);
		} else {
			ydMerchantGiftRate.setRate(rate);
			ydMerchantGiftRateDao.updateYdMerchantGiftRate(ydMerchantGiftRate);
		}
	}

	/**
	 * 设置一级分类礼品礼品占比
	 * @param merchantId
	 * @param rate
	 */
	private void updateFirstCategoryRate(Integer merchantId, Integer firstCategoryId, Integer rate) {
		YdMerchantFirstCategoryGiftRate firstCategoryGiftRate = ydMerchantFirstCategoryGiftRateDao.getGiftRateByFirstCategoryId(firstCategoryId);
		if (firstCategoryGiftRate == null) {
			firstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			firstCategoryGiftRate.setCreateTime(new Date());
			firstCategoryGiftRate.setRate(rate);
			firstCategoryGiftRate.setMerchantId(merchantId);
			firstCategoryGiftRate.setFirstCategoryId(firstCategoryId);
			ydMerchantFirstCategoryGiftRateDao.insertYdMerchantFirstCategoryGiftRate(firstCategoryGiftRate);
		} else {
			firstCategoryGiftRate.setRate(rate);
			ydMerchantFirstCategoryGiftRateDao.updateYdMerchantFirstCategoryGiftRate(firstCategoryGiftRate);
		}
	}

	/**
	 * 设置二级分类礼品礼品占比
	 * @param merchantId
	 * @param rate
	 */
	private void updateSecondCategoryRate(Integer merchantId, Integer secondCategoryId, Integer rate) {
		YdMerchantSecondCategoryGiftRate secondCategoryGiftRate = ydMerchantSecondCategoryGiftRateDao.getGiftRateBySecondCategoryId(secondCategoryId);
		if (secondCategoryGiftRate == null) {
			secondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
			secondCategoryGiftRate.setCreateTime(new Date());
			secondCategoryGiftRate.setRate(rate);
			secondCategoryGiftRate.setMerchantId(merchantId);
			secondCategoryGiftRate.setSecondCategoryId(secondCategoryId);
			// 查询一级分类
			YdMerchantItemCategory itemCategory = ydMerchantItemCategoryDao.getYdShopMerchantItemCategoryById(secondCategoryId);
			secondCategoryGiftRate.setFirstCategoryId(itemCategory.getPid());
			ydMerchantSecondCategoryGiftRateDao.insertYdMerchantSecondCategoryGiftRate(secondCategoryGiftRate);
		} else {
			secondCategoryGiftRate.setRate(rate);
			ydMerchantSecondCategoryGiftRateDao.updateYdMerchantSecondCategoryGiftRate(secondCategoryGiftRate);
		}
	}

	/**
	 * 设置商品礼品占比
	 * @param merchantId
	 * @param merchantItemId
	 * @param rate
	 */
	private void updateMerchantItemRate(Integer merchantId, Integer merchantItemId, Integer rate) {
		YdMerchantItemGiftRate itemGiftRate = ydMerchantItemGiftRateDao.getGiftRateByMerchantItemId(merchantItemId);
		if (itemGiftRate == null) {
			itemGiftRate = new YdMerchantItemGiftRate();
			itemGiftRate.setCreateTime(new Date());
			itemGiftRate.setRate(rate);
			itemGiftRate.setMerchantId(merchantId);
			itemGiftRate.setMerchantItemId(merchantItemId);

			// 查询商品分类
			YdMerchantItem merchantItem = ydMerchantItemDao.getYdShopMerchantItemById(merchantItemId);
			itemGiftRate.setSecondCategoryId(merchantItem.getFirstCategoryId());
			itemGiftRate.setFirstCategoryId(merchantItem.getSecondCategoryId());
			ydMerchantItemGiftRateDao.insertYdMerchantItemGiftRate(itemGiftRate);
		} else {
			itemGiftRate.setRate(rate);
			ydMerchantItemGiftRateDao.updateYdMerchantItemGiftRate(itemGiftRate);
		}
	}

}

