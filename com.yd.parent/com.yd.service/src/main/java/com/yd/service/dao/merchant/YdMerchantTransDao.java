package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantTrans;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户账单流水Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-22 20:33:42
 * @Version:1.1.0
 */
public interface YdMerchantTransDao {

	/**
	 * 通过id得到商户账单流水YdMerchantTrans
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantTrans getYdMerchantTransById(Integer id);

	/**
	 * 得到所有商户账单流水YdMerchantTrans
	 * @param ydMerchantTrans
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantTrans> getAll(YdMerchantTrans ydMerchantTrans);

	public int getMerchantTransListCount(@Param("merchantId") Integer merchantId,
										 @Param("orderId") String orderId,
										 @Param("transStatus") String transStatus,
										 @Param("startTime") String startTime,
										 @Param("endTime") String endTime);

	public List<YdMerchantTrans> getMerchantTransListByPage(@Param("merchantId") Integer merchantId,
															@Param("orderId") String orderId,
															@Param("transStatus") String transStatus,
															@Param("startTime") String startTime,
															@Param("endTime") String endTime,
															@Param("pageStart") Integer pageStart,
															@Param("pageSize") Integer pageSize);


	/**
	 * 添加商户账单流水YdMerchantTrans
	 * @param ydMerchantTrans
	 * @Description:
	 */
	public void insertYdMerchantTrans(YdMerchantTrans ydMerchantTrans);

	/**
	 * 通过id修改商户账单流水YdMerchantTrans
	 * @param ydMerchantTrans
	 * @Description:
	 */
	public void updateYdMerchantTrans(YdMerchantTrans ydMerchantTrans);

	}
