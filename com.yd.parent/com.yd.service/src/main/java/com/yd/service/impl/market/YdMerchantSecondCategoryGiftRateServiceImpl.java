package com.yd.service.impl.market;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.market.YdMerchantSecondCategoryGiftRateResult;
import com.yd.api.service.market.YdMerchantSecondCategoryGiftRateService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.market.YdMerchantSecondCategoryGiftRateDao;
import com.yd.service.bean.market.YdMerchantSecondCategoryGiftRate;

/**
 * @Title:门店二级分类礼品占比Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:52:52
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantSecondCategoryGiftRateServiceImpl implements YdMerchantSecondCategoryGiftRateService {

	@Resource
	private YdMerchantSecondCategoryGiftRateDao ydMerchantSecondCategoryGiftRateDao;

	@Override
	public YdMerchantSecondCategoryGiftRateResult getYdMerchantSecondCategoryGiftRateById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult = null;
		YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = this.ydMerchantSecondCategoryGiftRateDao.getYdMerchantSecondCategoryGiftRateById(id);
		if (ydMerchantSecondCategoryGiftRate != null) {
			ydMerchantSecondCategoryGiftRateResult = new YdMerchantSecondCategoryGiftRateResult();
			BeanUtilExt.copyProperties(ydMerchantSecondCategoryGiftRateResult, ydMerchantSecondCategoryGiftRate);
		}
		
		return ydMerchantSecondCategoryGiftRateResult;
	}

	@Override
	public Page<YdMerchantSecondCategoryGiftRateResult> findYdMerchantSecondCategoryGiftRateListByPage(YdMerchantSecondCategoryGiftRateResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantSecondCategoryGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
		BeanUtilExt.copyProperties(ydMerchantSecondCategoryGiftRate, params);
		
		int amount = this.ydMerchantSecondCategoryGiftRateDao.getYdMerchantSecondCategoryGiftRateCount(ydMerchantSecondCategoryGiftRate);
		if (amount > 0) {
			List<YdMerchantSecondCategoryGiftRate> dataList = this.ydMerchantSecondCategoryGiftRateDao.findYdMerchantSecondCategoryGiftRateListByPage(ydMerchantSecondCategoryGiftRate, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantSecondCategoryGiftRateResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantSecondCategoryGiftRateResult> getAll(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult) {
		YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = null;
		if (ydMerchantSecondCategoryGiftRateResult != null) {
			ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantSecondCategoryGiftRate, ydMerchantSecondCategoryGiftRateResult);
		}
		List<YdMerchantSecondCategoryGiftRate> dataList = this.ydMerchantSecondCategoryGiftRateDao.getAll(ydMerchantSecondCategoryGiftRate);
		List<YdMerchantSecondCategoryGiftRateResult> resultList = DTOUtils.convertList(dataList, YdMerchantSecondCategoryGiftRateResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult) {
		if (null != ydMerchantSecondCategoryGiftRateResult) {
			ydMerchantSecondCategoryGiftRateResult.setCreateTime(new Date());
			ydMerchantSecondCategoryGiftRateResult.setUpdateTime(new Date());
			YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantSecondCategoryGiftRate, ydMerchantSecondCategoryGiftRateResult);
			this.ydMerchantSecondCategoryGiftRateDao.insertYdMerchantSecondCategoryGiftRate(ydMerchantSecondCategoryGiftRate);
		}
	}
	
	@Override
	public void updateYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRateResult ydMerchantSecondCategoryGiftRateResult) {
		if (null != ydMerchantSecondCategoryGiftRateResult) {
			ydMerchantSecondCategoryGiftRateResult.setUpdateTime(new Date());
			YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate = new YdMerchantSecondCategoryGiftRate();
			BeanUtilExt.copyProperties(ydMerchantSecondCategoryGiftRate, ydMerchantSecondCategoryGiftRateResult);
			this.ydMerchantSecondCategoryGiftRateDao.updateYdMerchantSecondCategoryGiftRate(ydMerchantSecondCategoryGiftRate);
		}
	}

}

