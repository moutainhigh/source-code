package com.yd.service.impl.market;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.market.YdMerchantItemGiftRateResult;
import com.yd.api.service.market.YdMerchantItemGiftRateService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.market.YdMerchantItemGiftRateDao;
import com.yd.service.bean.market.YdMerchantItemGiftRate;

/**
 * @Title:门店商品礼品占比Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:59
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantItemGiftRateServiceImpl implements YdMerchantItemGiftRateService {

	@Resource
	private YdMerchantItemGiftRateDao ydMerchantItemGiftRateDao;

	@Override
	public YdMerchantItemGiftRateResult getYdMerchantItemGiftRateById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult = null;
		YdMerchantItemGiftRate ydMerchantItemGiftRate = this.ydMerchantItemGiftRateDao.getYdMerchantItemGiftRateById(id);
		if (ydMerchantItemGiftRate != null) {
			ydMerchantItemGiftRateResult = new YdMerchantItemGiftRateResult();
			BeanUtilExt.copyProperties(ydMerchantItemGiftRateResult, ydMerchantItemGiftRate);
		}
		
		return ydMerchantItemGiftRateResult;
	}

	@Override
	public Page<YdMerchantItemGiftRateResult> findYdMerchantItemGiftRateListByPage(YdMerchantItemGiftRateResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantItemGiftRateResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantItemGiftRate ydMerchantItemGiftRate = new YdMerchantItemGiftRate();
		BeanUtilExt.copyProperties(ydMerchantItemGiftRate, params);
		
		int amount = this.ydMerchantItemGiftRateDao.getYdMerchantItemGiftRateCount(ydMerchantItemGiftRate);
		if (amount > 0) {
			List<YdMerchantItemGiftRate> dataList = this.ydMerchantItemGiftRateDao.findYdMerchantItemGiftRateListByPage(ydMerchantItemGiftRate, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantItemGiftRateResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantItemGiftRateResult> getAll(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult) {
		YdMerchantItemGiftRate ydMerchantItemGiftRate = null;
		if (ydMerchantItemGiftRateResult != null) {
			ydMerchantItemGiftRate = new YdMerchantItemGiftRate();
			BeanUtilExt.copyProperties(ydMerchantItemGiftRate, ydMerchantItemGiftRateResult);
		}
		List<YdMerchantItemGiftRate> dataList = this.ydMerchantItemGiftRateDao.getAll(ydMerchantItemGiftRate);
		List<YdMerchantItemGiftRateResult> resultList = DTOUtils.convertList(dataList, YdMerchantItemGiftRateResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantItemGiftRate(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult) {
		if (null != ydMerchantItemGiftRateResult) {
			ydMerchantItemGiftRateResult.setCreateTime(new Date());
			ydMerchantItemGiftRateResult.setUpdateTime(new Date());
			YdMerchantItemGiftRate ydMerchantItemGiftRate = new YdMerchantItemGiftRate();
			BeanUtilExt.copyProperties(ydMerchantItemGiftRate, ydMerchantItemGiftRateResult);
			this.ydMerchantItemGiftRateDao.insertYdMerchantItemGiftRate(ydMerchantItemGiftRate);
		}
	}
	
	
	@Override
	public void updateYdMerchantItemGiftRate(YdMerchantItemGiftRateResult ydMerchantItemGiftRateResult) {
		if (null != ydMerchantItemGiftRateResult) {
			ydMerchantItemGiftRateResult.setUpdateTime(new Date());
			YdMerchantItemGiftRate ydMerchantItemGiftRate = new YdMerchantItemGiftRate();
			BeanUtilExt.copyProperties(ydMerchantItemGiftRate, ydMerchantItemGiftRateResult);
			this.ydMerchantItemGiftRateDao.updateYdMerchantItemGiftRate(ydMerchantItemGiftRate);
		}
	}

}

