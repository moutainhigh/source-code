package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantGiftTrans;
import com.yd.service.bean.merchant.YdMerchantTrans;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户礼品账户流水Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:27:31
 * @Version:1.1.0
 */
public interface YdMerchantGiftTransDao {

	/**
	 * 通过id得到商户礼品账户流水YdMerchantGiftTrans
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftTrans getYdMerchantGiftTransById(Integer id);

	/**
	 * 得到所有商户礼品账户流水YdMerchantGiftTrans
	 * @param ydMerchantGiftTrans
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftTrans> getAll(YdMerchantGiftTrans ydMerchantGiftTrans);

	public int getMerchantGiftTransListCount(@Param("merchantId") Integer merchantId,
											 @Param("giftOrderId") String giftOrderId,
											 @Param("transType") String transType,
											 @Param("startTime") String startTime,
											 @Param("endTime") String endTime);

	public List<YdMerchantGiftTrans> getMerchantGiftTransListByPage(@Param("merchantId") Integer merchantId,
																	@Param("giftOrderId") String giftOrderId,
																	@Param("transType") String transType,
																	@Param("startTime") String startTime,
																	@Param("endTime") String endTime,
																	@Param("pageStart") Integer pageStart,
																	@Param("pageSize") Integer pageSize);

	/**
	 * 添加商户礼品账户流水YdMerchantGiftTrans
	 * @param ydMerchantGiftTrans
	 * @Description:
	 */
	public void insertYdMerchantGiftTrans(YdMerchantGiftTrans ydMerchantGiftTrans);
	

	/**
	 * 通过id修改商户礼品账户流水YdMerchantGiftTrans
	 * @param ydMerchantGiftTrans
	 * @Description:
	 */
	public void updateYdMerchantGiftTrans(YdMerchantGiftTrans ydMerchantGiftTrans);
	
}
