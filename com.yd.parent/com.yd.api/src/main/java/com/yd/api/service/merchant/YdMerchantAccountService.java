package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.core.utils.BusinessException;

/**
 * @Title:优度商户账户Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:54
 * @Version:1.1.0
 */
public interface YdMerchantAccountService {

	/**
	 * 通过merchantId得到商户账户信息YdMerchantAccount
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantAccountResult getYdMerchantAccountByMerchantId(Integer merchantId);

	/**
	 * 得到所有商户账户信息YdMerchantAccount
	 * @param ydMerchantAccountResult
	 * @return
	 * @Description:
	 */
	public List<YdMerchantAccountResult> getAll(YdMerchantAccountResult ydMerchantAccountResult);


	/**
	 * 添加商户账户信息YdMerchantAccount
	 * @param ydMerchantAccountResult
	 * @Description:
	 */
	public void insertYdMerchantAccount(YdMerchantAccountResult ydMerchantAccountResult);

	/**
	 * 通过id修改商户账户信息YdMerchantAccount
	 * @param ydMerchantAccountResult
	 * @Description:
	 */
	public void updateYdMerchantAccount(YdMerchantAccountResult ydMerchantAccountResult);

	/**
	 * 账户余额转入礼品账户
	 * @param merchantId	商户id
	 * @param intoPrice	转入的金额
	 */
    void intoGiftAccount(Integer merchantId, Double intoPrice) throws BusinessException;
}
