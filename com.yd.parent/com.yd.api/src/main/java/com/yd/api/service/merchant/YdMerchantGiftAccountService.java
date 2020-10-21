package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.core.utils.BusinessException;

/**
 * @Title:商户礼品账户Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:22:27
 * @Version:1.1.0
 */
public interface YdMerchantGiftAccountService {

	/**
	 * 通过merchantId得到商户礼品账户YdMerchantGiftAccount
	 * @param merchantId
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftAccountResult getYdMerchantGiftAccountByMerchantId(Integer merchantId) throws BusinessException;

	/**
	 * 得到所有商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccountResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftAccountResult> getAll(YdMerchantGiftAccountResult ydMerchantGiftAccountResult);


	/**
	 * 添加商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccountResult
	 * @Description:
	 */
	public void insertYdMerchantGiftAccount(YdMerchantGiftAccountResult ydMerchantGiftAccountResult);
	
	/**
	 * 通过id修改商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccountResult
	 * @Description:
	 */
	public void updateYdMerchantGiftAccount(YdMerchantGiftAccountResult ydMerchantGiftAccountResult);
	
}
