package com.yg.service.impl.merchant;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yg.api.result.merchant.YgMerchantResult;
import com.yg.api.service.merchant.YgMerchantService;
import com.yg.core.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yg.service.dao.merchant.YgMerchantDao;
import com.yg.service.bean.merchant.YgMerchant;

/**
 * @Title:商户信息Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-25 16:44:35
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YgMerchantServiceImpl implements YgMerchantService {

	@Resource
	private YgMerchantDao ygMerchantDao;

	@Override
	public YgMerchantResult getYgMerchantById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YgMerchantResult ygMerchantResult = null;
		YgMerchant ygMerchant = this.ygMerchantDao.getYgMerchantById(id);
		if (ygMerchant != null) {
			ygMerchantResult = new YgMerchantResult();
			BeanUtilExt.copyProperties(ygMerchantResult, ygMerchant);
		}	
		return ygMerchantResult;
	}

	@Override
	public Page<YgMerchantResult> findYgMerchantListByPage(YgMerchantResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YgMerchantResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YgMerchant ygMerchant = new YgMerchant();
		BeanUtilExt.copyProperties(ygMerchant, params);
		
		int amount = this.ygMerchantDao.getYgMerchantCount(ygMerchant);
		if (amount > 0) {
			List<YgMerchant> dataList = this.ygMerchantDao.findYgMerchantListByPage(
				ygMerchant, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YgMerchantResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YgMerchantResult> getAll(YgMerchantResult ygMerchantResult) {
		YgMerchant ygMerchant = null;
		if (ygMerchantResult != null) {
			ygMerchant = new YgMerchant();
			BeanUtilExt.copyProperties(ygMerchant, ygMerchantResult);
		}
		List<YgMerchant> dataList = this.ygMerchantDao.getAll(ygMerchant);
		return DTOUtils.convertList(dataList, YgMerchantResult.class);
	}

	@Override
	public void insertYgMerchant(YgMerchantResult ygMerchantResult) throws BusinessException {
		ygMerchantResult.setCreateTime(new Date());
		ygMerchantResult.setUpdateTime(new Date());
		YgMerchant ygMerchant = new YgMerchant();
		BeanUtilExt.copyProperties(ygMerchant, ygMerchantResult);
		this.ygMerchantDao.insertYgMerchant(ygMerchant);
	}
	@Override
	public void updateYgMerchant(YgMerchantResult ygMerchantResult) throws BusinessException {
		ygMerchantResult.setUpdateTime(new Date());
		YgMerchant ygMerchant = new YgMerchant();
		BeanUtilExt.copyProperties(ygMerchant, ygMerchantResult);
		this.ygMerchantDao.updateYgMerchant(ygMerchant);
	}

	@Override
	public void updateMerchantStatus(Integer merchantId, String isEnable) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(isEnable),
				"err_empty_is_enable", "状态不可以为空");

		YgMerchant ydMerchant = ygMerchantDao.getYgMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_is_enable", "商户不存在");
		ValidateBusinessUtils.assertFalse(isEnable.equalsIgnoreCase(ydMerchant.getIsEnable()),
				"err_is_enable", "不可设置重复设置商户状态");

		ydMerchant.setIsEnable(isEnable);
		this.ygMerchantDao.updateYgMerchant(ydMerchant);
	}

}

