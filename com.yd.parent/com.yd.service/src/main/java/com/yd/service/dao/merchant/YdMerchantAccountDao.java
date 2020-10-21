package com.yd.service.dao.merchant;

import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.service.bean.merchant.YdMerchantAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title:优度商户账户Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:54
 * @Version:1.1.0
 */
public interface YdMerchantAccountDao {

	/**
	 * 通过id得到优度商户账户YdAdminMerchantAccount
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdMerchantAccount getYdMerchantAccountById(Integer id);

	/**
	 * 通过merchantId得到商户账户信息YdMerchantAccount
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantAccount getYdMerchantAccountByMerchantId(Integer merchantId);

	/**
	 * 得到所有优度商户账户YdAdminMerchantAccount
	 * @param YdAdminMerchantAccount
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdMerchantAccount> getAll(YdMerchantAccount YdAdminMerchantAccount);

	/**
	 * 添加优度商户账户YdAdminMerchantAccount
	 * @param YdAdminMerchantAccount
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdMerchantAccount(YdMerchantAccount YdAdminMerchantAccount);
	
	/**
	 * 通过id修改优度商户账户YdAdminMerchantAccount
	 * @param YdAdminMerchantAccount
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdMerchantAccount(YdMerchantAccount YdAdminMerchantAccount);

	/**
	 * 增加商户余额
	 * @param merchantId
	 * @param price
	 * @return
	 */
	public int addAccountBalance(@Param("merchantId") Integer merchantId,
								 @Param("price") Double price);

	/**
	 * 扣除商户余额
	 * @param merchantId
	 * @param price
	 * @return
	 */
	public int reduceAccountBalance(@Param("merchantId") Integer merchantId,
									@Param("price") Double price);

}
