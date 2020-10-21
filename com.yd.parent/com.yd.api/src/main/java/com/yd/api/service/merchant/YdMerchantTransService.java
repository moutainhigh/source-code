package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantTransResult;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户账单流水Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-22 20:33:42
 * @Version:1.1.0
 */
public interface YdMerchantTransService {

	/**
	 * 得到所有商户账单流水YdMerchantTrans
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchantTransResult getYdMerchantTransById(Integer id);

	/**
	 * 得到所有商户账单流水YdMerchantTrans
	 * @param ydMerchantTransResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantTransResult> getAll(YdMerchantTransResult ydMerchantTransResult);

	/**
	 * 分页查询商户交易流水
	 * @param merchantId
	 * @param orderId
	 * @param transStatus
	 * @param startTime
	 * @param endTime
	 * @param pagerInfo
	 * @return
	 */
	public Page<YdMerchantTransResult> getMerchantTransListByPage(Integer merchantId, String orderId, String transStatus,
																  String startTime,String endTime, PagerInfo pagerInfo);

	/**
	 * 添加商户账单流水YdMerchantTrans
	 * @param ydMerchantTransResult
	 * @Description:
	 */
	public void insertYdMerchantTrans(YdMerchantTransResult ydMerchantTransResult);

	/**
	 * 添加商户账户交流流水
	 * @param merchantId	 	 商户ID
	 * @param outOrderId	 	 外部订单ID
	 * @param transSource	  	交易流水来源(MerchantTransSourceEnum对应code)
	 * @param transType		  	交易类型(IN|OUT)
	 * @param transDesc		  	交易流水描述信息
	 * @param outOrderStatus  	外部订单状态
	 * @param transAmount		交易金额(单位元)
	 * @param transBeforeAmount	交易前金额
	 * @param transAfterAmount	交易后金额
	 * @param rateAmount		手续费
	 */
	public void addMerchantTransDetail(Integer merchantId, String outOrderId, String transSource,
									   String transType, String transDesc, String outOrderStatus,
									   Double transAmount, Double transBeforeAmount,
									   Double transAfterAmount, Double rateAmount);


}
