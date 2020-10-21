package com.yd.service.impl.user;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.user.YdUserMerchantResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.user.YdUserMerchantService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.user.YdUserMerchantDao;
import com.yd.service.bean.user.YdUserMerchant;

/**
 * @Title:用户商户绑定关系表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-29 10:11:01
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserMerchantServiceImpl implements YdUserMerchantService {

	@Resource
	private YdUserMerchantDao ydUserMerchantDao;

	@Resource
	private YdUserService ydUserService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdUserMerchantResult getYdUserMerchantById(Integer id) throws BusinessException {
		if (id == null || id <= 0) return null;
		YdUserMerchantResult ydUserMerchantResult = null;
		YdUserMerchant ydUserMerchant = this.ydUserMerchantDao.getYdUserMerchantById(id);
		if (ydUserMerchant != null) {
			ydUserMerchantResult = new YdUserMerchantResult();
			BeanUtilExt.copyProperties(ydUserMerchantResult, ydUserMerchant);
		}	
		return ydUserMerchantResult;
	}

	@Override
	public YdUserMerchantResult getYdUserMerchantByUserId(Integer userId) throws BusinessException {
		if (userId == null || userId <= 0) return null;
		YdUserMerchantResult ydUserMerchantResult = null;
		YdUserMerchant ydUserMerchant = this.ydUserMerchantDao.getYdUserMerchantByUserId(userId);
		if (ydUserMerchant != null) {
			ydUserMerchantResult = new YdUserMerchantResult();
			BeanUtilExt.copyProperties(ydUserMerchantResult, ydUserMerchant);
		}
		return ydUserMerchantResult;
	}

	@Override
	public List<YdUserMerchantResult> getAll(YdUserMerchantResult ydUserMerchantResult) throws BusinessException {
		YdUserMerchant ydUserMerchant = null;
		if (ydUserMerchantResult != null) {
			ydUserMerchant = new YdUserMerchant();
			BeanUtilExt.copyProperties(ydUserMerchant, ydUserMerchantResult);
		}
		List<YdUserMerchant> dataList = this.ydUserMerchantDao.getAll(ydUserMerchant);
		return DTOUtils.convertList(dataList, YdUserMerchantResult.class);
	}

	@Override
	public void insertYdUserMerchant(YdUserMerchantResult ydUserMerchantResult) throws BusinessException {
		ydUserMerchantResult.setCreateTime(new Date());
		ydUserMerchantResult.setUpdateTime(new Date());
		YdUserMerchant ydUserMerchant = new YdUserMerchant();
		BeanUtilExt.copyProperties(ydUserMerchant, ydUserMerchantResult);
		this.ydUserMerchantDao.insertYdUserMerchant(ydUserMerchant);
	}
	
	@Override
	public void updateYdUserMerchant(YdUserMerchantResult ydUserMerchantResult) throws BusinessException {
		ydUserMerchantResult.setUpdateTime(new Date());
		YdUserMerchant ydUserMerchant = new YdUserMerchant();
		BeanUtilExt.copyProperties(ydUserMerchant, ydUserMerchantResult);
		this.ydUserMerchantDao.updateYdUserMerchant(ydUserMerchant);
	}

	@Override
	public void saveOrUpdate(Integer userId, Integer merchantId) throws BusinessException {
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);
		YdUserMerchant userMerchant = this.ydUserMerchantDao.getYdUserMerchantByUserId(userId);
		if (userMerchant == null) {
			userMerchant = new YdUserMerchant();
			userMerchant.setCreateTime(new Date());
			userMerchant.setUserId(userId);
			userMerchant.setMerchantId(merchantId);
			this.ydUserMerchantDao.insertYdUserMerchant(userMerchant);

		} else {
			userMerchant = new YdUserMerchant();
			userMerchant.setUpdateTime(new Date());
			userMerchant.setUserId(userId);
			userMerchant.setMerchantId(merchantId);
			this.ydUserMerchantDao.updateYdUserMerchant(userMerchant);
		}
	}

}

