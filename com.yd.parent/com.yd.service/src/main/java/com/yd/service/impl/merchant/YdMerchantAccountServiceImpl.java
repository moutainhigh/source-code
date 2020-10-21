package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.List;

import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantAccountService;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchantAccount;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import com.yd.service.dao.merchant.YdMerchantAccountDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Title:优度商户账户Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:54
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantAccountServiceImpl implements YdMerchantAccountService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantAccountServiceImpl.class);

	@Resource
	private YdMerchantAccountDao ydMerchantAccountDao;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdMerchantTransService ydMerchantTransService;

	@Resource
	private YdMerchantGiftTransService ydMerchantGiftTransService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantAccountResult getYdMerchantAccountByMerchantId(Integer merchantId) {
		if (merchantId == null || merchantId <= 0) return null;
		// 校验是商户还是操作员
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantAccount ydMerchantAccount = this.ydMerchantAccountDao.getYdMerchantAccountByMerchantId(merchantId);
		if (ydMerchantAccount == null) {
			ydMerchantAccount = new YdMerchantAccount();
			ydMerchantAccount.setCreateTime(new Date());
			ydMerchantAccount.setBalance(0.00);
			ydMerchantAccount.setMerchantId(merchantId);
			this.ydMerchantAccountDao.insertYdMerchantAccount(ydMerchantAccount);
		}
		YdMerchantAccountResult ydMerchantAccountResult = new YdMerchantAccountResult();
		BeanUtilExt.copyProperties(ydMerchantAccountResult, ydMerchantAccount);
		return ydMerchantAccountResult;
	}

	@Override
	public List<YdMerchantAccountResult> getAll(YdMerchantAccountResult ydMerchantAccountResult) {
		YdMerchantAccount ydMerchantAccount = null;
		if (ydMerchantAccountResult != null) {
			ydMerchantAccount = new YdMerchantAccount();
			BeanUtilExt.copyProperties(ydMerchantAccount, ydMerchantAccountResult);
		}
		List<YdMerchantAccount> dataList = this.ydMerchantAccountDao.getAll(ydMerchantAccount);
		return  DTOUtils.convertList(dataList, YdMerchantAccountResult.class);
	}


	@Override
	public void insertYdMerchantAccount(YdMerchantAccountResult ydMerchantAccountResult) {
		ydMerchantAccountResult.setCreateTime(new Date());
		ydMerchantAccountResult.setUpdateTime(new Date());
		YdMerchantAccount ydMerchantAccount = new YdMerchantAccount();
		BeanUtilExt.copyProperties(ydMerchantAccount, ydMerchantAccountResult);
		this.ydMerchantAccountDao.insertYdMerchantAccount(ydMerchantAccount);
	}

	@Override
	public void updateYdMerchantAccount(YdMerchantAccountResult ydMerchantAccountResult) {
		ydMerchantAccountResult.setUpdateTime(new Date());
		YdMerchantAccount ydMerchantAccount = new YdMerchantAccount();
		BeanUtilExt.copyProperties(ydMerchantAccount, ydMerchantAccountResult);
		this.ydMerchantAccountDao.updateYdMerchantAccount(ydMerchantAccount);
	}

	/**
	 * 账户余额转入礼品账户
	 * @param merchantId	商户id
	 * @param intoPrice	转入的金额
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void intoGiftAccount(Integer merchantId, Double intoPrice) throws BusinessException {
		ValidateBusinessUtils.assertFalse(intoPrice == null || intoPrice <= 0,
				"err_into_price", "请输入正确的转入金额");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantAccount ydMerchantAccount = this.ydMerchantAccountDao.getYdMerchantAccountByMerchantId(merchantId);
		YdMerchantGiftAccount ydMerchantGiftAccount = this.ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(merchantId);
		if (ydMerchantAccount.getBalance() >= intoPrice) {
			// 余额账户扣除余额
			int result = this.ydMerchantAccountDao.reduceAccountBalance(merchantId, intoPrice);
			ValidateBusinessUtils.assertFalse(result <= 0,
					"err_balance", "账户余额不足");
			// 礼品账户增加余额
			this.ydMerchantGiftAccountDao.addGiftAccountBalance(merchantId, intoPrice);

			// 账户余额流水
			Double afterBalance = MathUtils.subtract(ydMerchantAccount.getBalance(), intoPrice, 2);
			ydMerchantTransService.addMerchantTransDetail(merchantId, null, YdMerchantTransSourceEnum.TRANS_GIFT_ACCOUNT.getCode(),
					"in", YdMerchantTransSourceEnum.TRANS_GIFT_ACCOUNT.getDescription(), "SUCCESS", intoPrice,
					ydMerchantAccount.getBalance(), afterBalance, 0.00);

			// 礼品账户余额流水
			Double giftAfterBalance = MathUtils.add(ydMerchantGiftAccount.getBalance(), intoPrice, 2);
			ydMerchantGiftTransService.addMerchantGiftTrans(merchantId, null, YdMerchantTransSourceEnum.ACCOUNT_TRANS.getCode(),
					"out", YdMerchantTransSourceEnum.ACCOUNT_TRANS.getDescription(), "SUCCESS", intoPrice,
					ydMerchantGiftAccount.getBalance(), giftAfterBalance);

		} else {
			throw new BusinessException("err_balance", "账户余额不足");
		}

	}
}

