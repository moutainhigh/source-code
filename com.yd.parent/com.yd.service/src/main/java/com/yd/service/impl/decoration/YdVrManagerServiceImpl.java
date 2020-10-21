package com.yd.service.impl.decoration;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.decoration.YdVrManagerResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdVrManagerService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.dao.merchant.YdMerchantDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.decoration.YdVrManagerDao;
import com.yd.service.bean.decoration.YdVrManager;

/**
 * @Title:店铺vr设置Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:36:46
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdVrManagerServiceImpl implements YdVrManagerService {

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdVrManagerDao ydVrManagerDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdVrManagerResult getYdVrManagerById(Integer id) throws BusinessException {
		if (id == null || id <= 0) return null;
		YdVrManagerResult ydVrManagerResult = null;
		YdVrManager ydVrManager = this.ydVrManagerDao.getYdVrManagerById(id);
		if (ydVrManager != null) {
			ydVrManagerResult = new YdVrManagerResult();
			BeanUtilExt.copyProperties(ydVrManagerResult, ydVrManager);
		}	
		return ydVrManagerResult;
	}

	@Override
	public YdVrManagerResult getYdVrManagerByMerchantId(Integer merchantId) throws BusinessException {
		if (merchantId == null || merchantId <= 0) return null;
		YdVrManagerResult ydVrManagerResult = null;
		YdVrManager ydVrManager = this.ydVrManagerDao.getYdVrManagerByMerchantId(merchantId);
		if (ydVrManager != null) {
			ydVrManagerResult = new YdVrManagerResult();
			BeanUtilExt.copyProperties(ydVrManagerResult, ydVrManager);
		}
		return ydVrManagerResult;
	}


	@Override
	public void deleteYdVrManager(Integer id) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0, "err_id_empty", "id不可以为空");

		YdVrManager ydVrManager = ydVrManagerDao.getYdVrManagerById(id);
		ValidateBusinessUtils.assertNonNull(ydVrManager, "err_id_not_exist", "id不存在");
		ydVrManagerDao.deleteYdVrManagerById(id);
	}

	@Override
	public Page<YdVrManagerResult> findYdVrManagerListByPage(YdVrManagerResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdVrManagerResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdVrManager ydVrManager = new YdVrManager();
		BeanUtilExt.copyProperties(ydVrManager, params);
		
		int amount = this.ydVrManagerDao.getYdVrManagerCount(ydVrManager);
		if (amount > 0) {
			List<YdVrManager> dataList = this.ydVrManagerDao.findYdVrManagerListByPage(ydVrManager, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdVrManagerResult> resultList = DTOUtils.convertList(dataList, YdVrManagerResult.class);
			resultList.forEach(ydVrManagerResult -> {
				YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydVrManagerResult.getMerchantId());
				ydVrManagerResult.setMerchantName(ydMerchant.getMerchantName());
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdVrManagerResult> getAll(YdVrManagerResult ydVrManagerResult) throws BusinessException {
		YdVrManager ydVrManager = null;
		if (ydVrManagerResult != null) {
			ydVrManager = new YdVrManager();
			BeanUtilExt.copyProperties(ydVrManager, ydVrManagerResult);
		}
		List<YdVrManager> dataList = this.ydVrManagerDao.getAll(ydVrManager);
		return DTOUtils.convertList(dataList, YdVrManagerResult.class);
	}

	@Override
	public void insertYdVrManager(YdVrManagerResult ydVrManagerResult) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(ydVrManagerResult.getMerchantId(),
				"err_id_empty", "merchantId不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydVrManagerResult.getMerchantId());
		ValidateBusinessUtils.assertNonNull(ydMerchant, "err_merchant_not_exist", "商户不存在");

		ValidateBusinessUtils.assertFalse(!(YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "").equals(ydMerchant.getRoleIds()),
				"err_merchant_role", "请选择正确的商户");

		ValidateBusinessUtils.assertFalse(!ydMerchant.getId().equals(ydMerchant.getPid()),
				"err_operate_role", "请选择正确的商户");

		ValidateBusinessUtils.assertStringNotBlank(ydVrManagerResult.getJumpUrl(),
				"err_jumpUrl_empty", "跳转地址不可以为空");

		YdVrManager vrManager = this.ydVrManagerDao.getYdVrManagerByMerchantId(ydMerchant.getId());
		ValidateBusinessUtils.assertNull(vrManager, "err_more_vr", "一个商户只能配置一个地址");

		ydVrManagerResult.setCreateTime(new Date());
		ydVrManagerResult.setUpdateTime(new Date());
		YdVrManager ydVrManager = new YdVrManager();
		BeanUtilExt.copyProperties(ydVrManager, ydVrManagerResult);
		this.ydVrManagerDao.insertYdVrManager(ydVrManager);
	}
	
	@Override
	public void updateYdVrManager(YdVrManagerResult ydVrManagerResult) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(ydVrManagerResult.getMerchantId(),
				"err_id_empty", "merchantId不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydVrManagerResult.getMerchantId());
		ValidateBusinessUtils.assertNonNull(ydMerchant, "err_merchant_not_exist", "商户不存在");

		ValidateBusinessUtils.assertFalse(!(YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "").equals(ydMerchant.getRoleIds()),
				"err_merchant_role", "请选择正确的商户");

		ValidateBusinessUtils.assertFalse(!ydMerchant.getId().equals(ydMerchant.getPid()),
				"err_operate_role", "请选择正确的商户");

		ValidateBusinessUtils.assertIdNotNull(ydVrManagerResult.getId(), "err_id_empty", "id不可以为空");

		YdVrManager ydVrManager = ydVrManagerDao.getYdVrManagerById(ydVrManagerResult.getId());
		ValidateBusinessUtils.assertNonNull(ydVrManager, "err_id_not_exist", "id不存在");

		YdVrManager vrManager = this.ydVrManagerDao.getYdVrManagerByMerchantId(ydMerchant.getId());
		if (vrManager != null && vrManager.getId().intValue() != ydVrManager.getId()) {
			throw new BusinessException("err_exist_merchant", "商户已经配置过，请选择未配置过得商户");
		}

		ValidateBusinessUtils.assertStringNotBlank(ydVrManagerResult.getJumpUrl(),
				"err_jumpUrl_empty", "跳转地址不可以为空");

		ydVrManagerResult.setUpdateTime(new Date());
		ydVrManager = new YdVrManager();
		BeanUtilExt.copyProperties(ydVrManager, ydVrManagerResult);
		this.ydVrManagerDao.updateYdVrManager(ydVrManager);
	}

}

