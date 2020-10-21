package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户礼品账户Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:22:27
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantGiftAccountServiceImpl implements YdMerchantGiftAccountService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantGiftAccountServiceImpl.class);

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Override
	public YdMerchantGiftAccountResult getYdMerchantGiftAccountByMerchantId(Integer merchantId) throws BusinessException {
		if (merchantId == null || merchantId <= 0) return null;

		// 校验是商户还是操作员
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();
		YdMerchantGiftAccount ydMerchantGiftAccount = this.ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(merchantId);

		// 商户礼品账户不存在的话创建
		if (ydMerchantGiftAccount == null) {
			ydMerchantGiftAccount = new YdMerchantGiftAccount();
			ydMerchantGiftAccount.setCreateTime(new Date());
			ydMerchantGiftAccount.setBalance(0.00);
			ydMerchantGiftAccount.setMerchantId(merchantId);
			ydMerchantGiftAccountDao.insertYdMerchantGiftAccount(ydMerchantGiftAccount);
		}
		YdMerchantGiftAccountResult ydMerchantGiftAccountResult = new YdMerchantGiftAccountResult();
		BeanUtilExt.copyProperties(ydMerchantGiftAccountResult, ydMerchantGiftAccount);
		return ydMerchantGiftAccountResult;
	}

	@Override
	public List<YdMerchantGiftAccountResult> getAll(YdMerchantGiftAccountResult ydMerchantGiftAccountResult) {
		YdMerchantGiftAccount ydMerchantGiftAccount = null;
		if (ydMerchantGiftAccountResult != null) {
			ydMerchantGiftAccount = new YdMerchantGiftAccount();
			BeanUtilExt.copyProperties(ydMerchantGiftAccount, ydMerchantGiftAccountResult);
		}
		List<YdMerchantGiftAccount> dataList = this.ydMerchantGiftAccountDao.getAll(ydMerchantGiftAccount);
		List<YdMerchantGiftAccountResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftAccountResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantGiftAccount(YdMerchantGiftAccountResult ydMerchantGiftAccountResult) {
		ydMerchantGiftAccountResult.setCreateTime(new Date());
		YdMerchantGiftAccount ydMerchantGiftAccount = new YdMerchantGiftAccount();
		BeanUtilExt.copyProperties(ydMerchantGiftAccount, ydMerchantGiftAccountResult);
		this.ydMerchantGiftAccountDao.insertYdMerchantGiftAccount(ydMerchantGiftAccount);
	}

	@Override
	public void updateYdMerchantGiftAccount(YdMerchantGiftAccountResult ydMerchantGiftAccountResult) {
		if (null != ydMerchantGiftAccountResult) {
			ydMerchantGiftAccountResult.setUpdateTime(new Date());
			YdMerchantGiftAccount ydMerchantGiftAccount = new YdMerchantGiftAccount();
			BeanUtilExt.copyProperties(ydMerchantGiftAccount, ydMerchantGiftAccountResult);
			this.ydMerchantGiftAccountDao.updateYdMerchantGiftAccount(ydMerchantGiftAccount);
		}
	}

}

