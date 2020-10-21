package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantGiftTransResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:商户礼品账户流水Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:27:31
 * @Version:1.1.0
 */
public interface YdMerchantGiftTransService {

	/**
	 * 通过id得到商户礼品账户流水YdMerchantGiftTrans
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchantGiftTransResult getYdMerchantGiftTransById(Integer id);

	/**
	 * 分页查询商户礼品账户流水
	 * @param merchantId	商户id
	 * @param giftOrderId	礼品订单id
	 * @param transType		交易类型
	 * @param startTime		交易开始时间
	 * @param endTime		交易结束时间
	 * @param pagerInfo		分页信息
	 * @return Page<YdMerchantGiftTransResult>
	 */
	Page<YdMerchantGiftTransResult> getMerchantTransListByPage(Integer merchantId, String giftOrderId, String transType,
															   String startTime, String endTime, PagerInfo pagerInfo);

	/**
	 * 得到所有商户礼品账户流水YdMerchantGiftTrans
	 * @param ydMerchantGiftTransResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftTransResult> getAll(YdMerchantGiftTransResult ydMerchantGiftTransResult);


	/**
	 * 添加商户礼品账户交流流水
	 * @param merchantId	 	 商户ID
	 * @param outOrderId	 	 外部订单ID
	 * @param transSource	  	交易流水来源(MerchantTransSourceEnum对应code)
	 * @param transType		  	交易类型(IN|OUT)
	 * @param transDesc		  	交易流水描述信息
	 * @param outOrderStatus  	外部订单状态
	 * @param transAmount		交易金额(单位元)
	 * @param transBeforeAmount	交易前金额
	 * @param transAfterAmount	交易后金额
	 * @param transAfterAmount	手续费
	 */
	public YdMerchantGiftTransResult addMerchantGiftTrans(Integer merchantId, String outOrderId, String transSource,
														  String transType, String transDesc, String outOrderStatus,
														  Double transAmount, Double transBeforeAmount, Double transAfterAmount);

	/**
	 * 商户设置支付密码发送短信
	 * @param merchantId 商户id
	 * @throws BusinessException
	 */
	public void sendPayPasswordSms(Integer merchantId) throws BusinessException;

	/**
	 * 商户设置支付密码
	 * @param merchantId 商户id
	 * @param smsCode	短信验证码
	 * @param password	第一次输入密码
	 * @param password2	第二次输入的密码
	 * @throws BusinessException
	 */
	public void setPayPassword(Integer merchantId, String smsCode, String password, String password2) throws BusinessException;

	/**
	 * 校验商户的密码
	 * @param merchantId	商户id
	 * @param oldPassword	商户的密码
	 * @return
	 */
	public boolean checkPassword(Integer merchantId, String oldPassword) throws BusinessException;

	/**
	 * 商户修改支付密码
	 * @param merchantId 商户id
	 * @param password	第一次输入密码
	 * @param password2	第二次输入的密码
	 * @throws BusinessException
	 */
	public void updatePayPassword(Integer merchantId, String password, String password2) throws BusinessException;

	/**
	 * 商户礼品余额转入账户中心
	 * @param merchantId 商户id
	 * @param transPrice 转移的金额
	 */
	void transIntoAccount(Integer merchantId, Double transPrice) throws BusinessException;
}
