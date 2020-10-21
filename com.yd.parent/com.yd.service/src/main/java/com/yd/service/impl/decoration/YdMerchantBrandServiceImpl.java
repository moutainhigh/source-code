package com.yd.service.impl.decoration;

import java.util.*;

import javax.annotation.Resource;
import com.yd.api.result.decoration.YdMerchantBrandResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdMerchantBrandService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdBrand;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.dao.item.YdBrandDao;
import com.yd.service.dao.item.YdMerchantItemDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.decoration.YdMerchantBrandDao;
import com.yd.service.bean.decoration.YdMerchantBrand;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:门店品牌管理Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:08:16
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantBrandServiceImpl implements YdMerchantBrandService {

	@Resource
	private YdBrandDao ydBrandDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdMerchantBrandDao ydMerchantBrandDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantBrandResult getYdMerchantBrandById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantBrandResult ydMerchantBrandResult = new YdMerchantBrandResult();
		YdMerchantBrand ydMerchantBrand = this.ydMerchantBrandDao.getYdMerchantBrandById(id);
		if (ydMerchantBrand != null) {
			BeanUtilExt.copyProperties(ydMerchantBrandResult, ydMerchantBrand);
		}
		return ydMerchantBrandResult;
	}

	@Override
	public Page<YdMerchantBrandResult> findYdMerchantBrandListByPage(YdMerchantBrandResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantBrandResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantBrand ydMerchantBrand = new YdMerchantBrand();
		BeanUtilExt.copyProperties(ydMerchantBrand, params);
		
		int amount = this.ydMerchantBrandDao.getYdMerchantBrandCount(ydMerchantBrand);
		if (amount > 0) {
			List<YdMerchantBrand> dataList = this.ydMerchantBrandDao.findYdMerchantBrandListByPage(ydMerchantBrand, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantBrandResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantBrandResult> getAll(YdMerchantBrandResult ydMerchantBrandResult) {
		if (ydMerchantBrandResult == null) return new ArrayList<>();
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(ydMerchantBrandResult.getMerchantId());
		ydMerchantBrandResult.setMerchantId(storeInfo.getId());

		YdMerchantBrand ydMerchantBrand = new YdMerchantBrand();
		BeanUtilExt.copyProperties(ydMerchantBrand, ydMerchantBrandResult);

		List<YdMerchantBrand> dataList = this.ydMerchantBrandDao.getAll(ydMerchantBrand);
		List<YdMerchantBrandResult> resultList = DTOUtils.convertList(dataList, YdMerchantBrandResult.class);

		// 查询品牌下面的上架商品数量
		resultList.forEach(brand -> {
			YdMerchantItem ydMerchantItem = new YdMerchantItem();
			ydMerchantItem.setMerchantId(storeInfo.getId());
			ydMerchantItem.setIsEnable("Y");
			ydMerchantItem.setBrandId(brand.getBrandId());
			List<YdMerchantItem> itemList = ydMerchantItemDao.getAll(ydMerchantItem);
			if (CollectionUtils.isEmpty(itemList)) {
				brand.setCanUseItemCount(0);
			} else {
				brand.setCanUseItemCount(itemList.size());
			}
		});
		return resultList;
	}

	@Override
	public void insertYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) {
		Integer merchantId = ydMerchantBrandResult.getMerchantId();
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantBrandResult.getBrandAlias()),
				"err_empty_brand_alias", "品牌标识不可以为空");

		YdBrand ydBrand = ydBrandDao.getYdBrandByBrandAlias(ydMerchantBrandResult.getBrandAlias());
		ValidateBusinessUtils.assertFalse(ydBrand == null,
				"err_brand_alias", "错误的品牌标识");

		List<YdMerchantBrand> brandList = this.ydMerchantBrandDao.getYdMerchantBrandByMerchantId(ydMerchantBrandResult.getMerchantId());
		ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(brandList) && brandList.size() >= 8,
				"err_max_brand", "最多只可以添加八个品牌");

		YdMerchantBrand ydMerchantBrand = this.ydMerchantBrandDao.getYdMerchantBrandByMerchantIdAndAlias(
					ydMerchantBrandResult.getMerchantId(), ydMerchantBrandResult.getBrandAlias());
		ValidateBusinessUtils.assertFalse(ydMerchantBrand != null,
					"err_exist_brand_alias", "品牌已经存在");
		ydMerchantBrand = new YdMerchantBrand();
		ydMerchantBrand.setCreateTime(new Date());
		ydMerchantBrand.setSort(999);
		ydMerchantBrand.setMerchantId(merchantId);
		ydMerchantBrand.setIcon(ydBrand.getIcon());
		ydMerchantBrand.setBrandId(ydBrand.getId());
		ydMerchantBrand.setBrandName(ydBrand.getBrandName());
		ydMerchantBrand.setBrandAlias(ydBrand.getBrandAlias());
		this.ydMerchantBrandDao.insertYdMerchantBrand(ydMerchantBrand);
	}

	@Override
	public void updateYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) {
		ValidateBusinessUtils.assertFalse(ydMerchantBrandResult.getId() == null,
				"err_empty_id", "品牌id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantBrandResult.getBrandAlias()),
				"err_empty_brand_alias", "品牌标识不可以为空");

		YdBrand ydBrand = ydBrandDao.getYdBrandByBrandAlias(ydMerchantBrandResult.getBrandAlias());
		ValidateBusinessUtils.assertFalse(ydBrand == null,
				"err_brand_alias", "错误的品牌标识");

		YdMerchantBrand brand = this.ydMerchantBrandDao.getYdMerchantBrandById(ydMerchantBrandResult.getId());
		ValidateBusinessUtils.assertFalse(brand == null,
				"err_empty_brand", "商户品牌不存在");

		Integer merchantId = ydMerchantBrandResult.getMerchantId();
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		YdMerchantBrand ydMerchantBrand = this.ydMerchantBrandDao.getYdMerchantBrandByMerchantIdAndAlias(
				ydMerchantBrandResult.getMerchantId(), ydMerchantBrandResult.getBrandAlias());
		ValidateBusinessUtils.assertFalse(ydMerchantBrand != null && ydMerchantBrand.getId()
				.equals(ydMerchantBrandResult.getId()),"err_exist_brand_alias", "品牌已经存在");

		ydMerchantBrand = new YdMerchantBrand();
		ydMerchantBrand.setUpdateTime(new Date());
		ydMerchantBrand.setSort(brand.getSort());
		ydMerchantBrand.setBrandAlias(ydBrand.getBrandAlias());
		ydMerchantBrand.setBrandName(ydBrand.getBrandName());
		ydMerchantBrand.setBrandId(ydBrand.getId());
		ydMerchantBrand.setIcon(ydBrand.getIcon());
		ydMerchantBrand.setMerchantId(storeInfo.getId());
		this.ydMerchantBrandDao.updateYdMerchantBrand(ydMerchantBrand);
	}

	@Override
	public void deleteYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) throws BusinessException {
		this.ydMerchantBrandDao.deleteYdMerchantBrand(ydMerchantBrandResult.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void sortYdMerchantBrand(List<YdMerchantBrandResult> brandResultList) throws BusinessException {
		brandResultList.forEach(merchantBrand -> {
			ValidateBusinessUtils.assertFalse(merchantBrand.getId() == null,
					"err_empty_id", "品牌id不可以为空");
			ValidateBusinessUtils.assertFalse(merchantBrand.getSort() == null,
					"err_empty_sort", "排序号不可以为空");
			YdMerchantBrand ydMerchantBrand = new YdMerchantBrand();
			ydMerchantBrand.setId(merchantBrand.getId());
			ydMerchantBrand.setSort(merchantBrand.getSort());
			this.ydMerchantBrandDao.updateYdMerchantBrand(ydMerchantBrand);
		});
	}

//	@Override
//	public List<PlatBrandResult> findPlatformBrandList(Integer merchantId) throws BusinessException {
//		// 获取所有平台品牌，去重
//		Map<String, YdPlatformBrandEnum> hashMap = YdPlatformBrandEnum.getAllPlatformBrandEnum();
//		List<YdMerchantBrand> merchantBrandList = this.ydMerchantBrandDao.getYdMerchantBrandByMerchantId(merchantId);
//		merchantBrandList.forEach(data -> {
//			hashMap.remove(data.getBrandAlias());
//		});
//
//		// 组装返回结果
//		List<PlatBrandResult> resultList = new ArrayList<>();
//		for (String key : hashMap.keySet()) {
//			PlatBrandResult result = new PlatBrandResult();
//			YdPlatformBrandEnum platformBrandEnum = hashMap.get(key);
//			result.setBrandAlias(platformBrandEnum.getCode());
//			result.setBrandName(platformBrandEnum.getName());
//			resultList.add(result);
//		}
//		return resultList;
//	}

}

