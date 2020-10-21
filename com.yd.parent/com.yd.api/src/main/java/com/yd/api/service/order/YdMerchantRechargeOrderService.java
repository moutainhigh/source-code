package com.yd.api.service.order;


import com.yd.api.pay.util.WechatH5PayUtils;
import com.yd.api.result.order.YdMerchantRechargeOrderResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.List;

/**
 * @Title:商户充值订单Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-01-13 15:56:23
 * @Version:1.1.0
 */
public interface YdMerchantRechargeOrderService {

	/**
	 * 通过id得到商户充值订单YdMerchantRechargeOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantRechargeOrderResult getYdMerchantRechargeOrderById(Integer id);

	/**
	 * 分页查询商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrderResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantRechargeOrderResult> findYdMerchantRechargeOrderListByPage(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrderResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantRechargeOrderResult> getAll(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult);

	/**
	 * 添加商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrderResult
	 * @Description:
	 */
	public YdMerchantRechargeOrderResult insertYdMerchantRechargeOrder(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult) throws BusinessException;
	
	/**
	 * 通过id修改商户充值订单YdMerchantRechargeOrder throws BusinessException;
	 * @param ydMerchantRechargeOrderResult
	 * @Description:
	 */
	public void updateYdMerchantRechargeOrder(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult) throws BusinessException;

	/**
	 * 微信回调修改充值状态
	 * @param outOrderId
	 * @param billNo
	 * @throws BusinessException
	 */
	void updateRechargeStatus(String outOrderId, String billNo) throws BusinessException;

	/**
	 * 微信回调修改扫一扫付钱状态
	 * @param outOrderId
	 * @param billNo
	 * @throws BusinessException
	 */
	void updateQrCodeStatus(String outOrderId, String billNo) throws BusinessException;
}
