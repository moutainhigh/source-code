package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户礼品账户Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:22:27
 * @Version:1.1.0
 */
public interface YdMerchantGiftAccountDao {

	/**
	 * 通过id得到商户礼品账户YdMerchantGiftAccount
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchantGiftAccount getYdMerchantGiftAccountById(Integer id);

	/**
	 * 通过merchantId得到商户礼品账户YdMerchantGiftAccount
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantGiftAccount getYdMerchantGiftAccountByMerchantId(Integer merchantId);

	/**
	 * 得到所有商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccount
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftAccount> getAll(YdMerchantGiftAccount ydMerchantGiftAccount);


	/**
	 * 添加商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccount
	 * @Description:
	 */
	public void insertYdMerchantGiftAccount(YdMerchantGiftAccount ydMerchantGiftAccount);
	
	/**
	 * 通过id修改商户礼品账户YdMerchantGiftAccount
	 * @param ydMerchantGiftAccount
	 * @Description:
	 */
	public void updateYdMerchantGiftAccount(YdMerchantGiftAccount ydMerchantGiftAccount);

	/**
	 * 增加商户礼品余额
	 * @param merchantId
	 * @param price
	 * @return
	 */
	public int addGiftAccountBalance(@Param("merchantId") Integer merchantId,
									 @Param("price") Double price);

	/**
	 * 扣除商户礼品余额
	 * @param merchantId
	 * @param price
	 * @return
	 */
	public int reduceGiftAccountBalance(@Param("merchantId") Integer merchantId,
										@Param("price") Double price);
}
