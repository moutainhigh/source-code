package com.yd.service.impl.market;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.market.YdMerchantFirstCategoryGiftRateResult;
import com.yd.api.service.market.YdMerchantFirstCategoryGiftRateService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.market.YdMerchantFirstCategoryGiftRateDao;
import com.yd.service.bean.market.YdMerchantFirstCategoryGiftRate;

/**
 * @Title:门店一级分类礼品占比Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 14:12:58
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantFirstCategoryGiftRateServiceImpl implements YdMerchantFirstCategoryGiftRateService {

	@Resource
	private YdMerchantFirstCategoryGiftRateDao ydMerchantFirstCategoryGiftRateDao;

	@Override
	public YdMerchantFirstCategoryGiftRateResult getYdMerchantFirstCategoryGiftRateById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult = null;
		YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = this.ydMerchantFirstCategoryGiftRateDao.getYdMerchantFirstCategoryGiftRateById(id);
		if (ydMerchantFirstCategoryGiftRate != null) {
			ydMerchantFirstCategoryGiftRateResult = new YdMerchantFirstCategoryGiftRateResult();
			BeanUtilExt.copyProperties(ydMerchantFirstCategoryGiftRateResult, ydMerchantFirstCategoryGiftRate);
		}
		
		return ydMerchantFirstCategoryGiftRateResult;
	}

	@Override
	public Page<YdMerchantFirstCategoryGiftRateResult> findYdMerchantFirstCategoryGiftRateListByPage(YdMerchantFirstCategoryGiftRateResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantFirstCategoryGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
		BeanUtilExt.copyProperties(ydMerchantFirstCategoryGiftRate, params);
		
		int amount = this.ydMerchantFirstCategoryGiftRateDao.getYdMerchantFirstCategoryGiftRateCount(ydMerchantFirstCategoryGiftRate);
		if (amount > 0) {
			List<YdMerchantFirstCategoryGiftRate> dataList = this.ydMerchantFirstCategoryGiftRateDao.findYdMerchantFirstCategoryGiftRateListByPage(ydMerchantFirstCategoryGiftRate, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantFirstCategoryGiftRateResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantFirstCategoryGiftRateResult> getAll(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult) {
		YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = null;
		if (ydMerchantFirstCategoryGiftRateResult != null) {
			ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantFirstCategoryGiftRate, ydMerchantFirstCategoryGiftRateResult);
		}
		List<YdMerchantFirstCategoryGiftRate> dataList = this.ydMerchantFirstCategoryGiftRateDao.getAll(ydMerchantFirstCategoryGiftRate);
		List<YdMerchantFirstCategoryGiftRateResult> resultList = DTOUtils.convertList(dataList, YdMerchantFirstCategoryGiftRateResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult) {
		if (null != ydMerchantFirstCategoryGiftRateResult) {
			ydMerchantFirstCategoryGiftRateResult.setCreateTime(new Date());
			ydMerchantFirstCategoryGiftRateResult.setUpdateTime(new Date());
			YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantFirstCategoryGiftRate, ydMerchantFirstCategoryGiftRateResult);
			this.ydMerchantFirstCategoryGiftRateDao.insertYdMerchantFirstCategoryGiftRate(ydMerchantFirstCategoryGiftRate);
		}
	}

	@Override
	public void updateYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRateResult ydMerchantFirstCategoryGiftRateResult) {
		if (null != ydMerchantFirstCategoryGiftRateResult) {
			ydMerchantFirstCategoryGiftRateResult.setUpdateTime(new Date());
			YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate = new YdMerchantFirstCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantFirstCategoryGiftRate, ydMerchantFirstCategoryGiftRateResult);
			this.ydMerchantFirstCategoryGiftRateDao.updateYdMerchantFirstCategoryGiftRate(ydMerchantFirstCategoryGiftRate);
		}
	}

}

