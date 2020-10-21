package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantWithdrawResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户提现记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 16:39:54
 * @Version:1.1.0
 */
public interface YdMerchantWithdrawService {

	/**
	 * 通过id得到商户提现记录YdMerchantWithdraw
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantWithdrawResult getYdMerchantWithdrawById(Integer id);

	/**
	 * 分页查询商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdrawResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantWithdrawResult> findYdMerchantWithdrawListByPage(YdMerchantWithdrawResult ydMerchantWithdrawResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdrawResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantWithdrawResult> getAll(YdMerchantWithdrawResult ydMerchantWithdrawResult);

	/**
	 * 添加商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdrawResult
	 * @Description:
	 */
	public void insertYdMerchantWithdraw(YdMerchantWithdrawResult ydMerchantWithdrawResult) throws BusinessException;

	/**
	 * 通过id修改商户提现记录YdMerchantWithdraw throws BusinessException;
	 * @param ydMerchantWithdrawResult
	 * @Description:
	 */
	public void updateYdMerchantWithdraw(YdMerchantWithdrawResult ydMerchantWithdrawResult)throws BusinessException;

	/**
	 * 商户余额提现
	 * @param merchantId	商户id
	 * @param withdrawAmount 提现金额
	 */
    void merchantWithdraw(Integer merchantId, Double withdrawAmount) throws BusinessException;
}
