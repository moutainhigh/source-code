package com.yd.api.service.message;

import java.util.List;

import com.yd.api.result.message.YdMerchantMessageResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户消息通知Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-17 17:33:49
 * @Version:1.1.0
 */
public interface YdMerchantMessageService {

	/**
	 * 通过id得到商户消息通知YdMerchantMessage
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMessageResult getYdMerchantMessageById(Integer id);

	/**
	 * 分页查询商户消息通知YdMerchantMessage
	 * @param ydMerchantMessageResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantMessageResult> findYdMerchantMessageListByPage(YdMerchantMessageResult ydMerchantMessageResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户消息通知YdMerchantMessage
	 * @param ydMerchantMessageResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMessageResult> getAll(YdMerchantMessageResult ydMerchantMessageResult);

	/**
	 * 添加商户订单消息通知
	 * @param messageType
	 * @param outOrderId
	 * @param merchantId
	 * @throws BusinessException
	 */
	public void insertOrderMessage(String messageType, Integer outOrderId, Integer merchantId) throws BusinessException;

	/**
	 * 添加商户订单消息通知
	 * @param messageType
	 * @param outOrderId
	 * @throws BusinessException
	 */
	public void insertGiftMessage(String messageType, Integer outOrderId) throws BusinessException;

	/**
	 * 通过id修改商户消息通知YdMerchantMessage throws BusinessException;
	 * @param ydMerchantMessageResult
	 * @Description:
	 */
	public void updateYdMerchantMessage(YdMerchantMessageResult ydMerchantMessageResult) throws BusinessException;

	/**
	 * 商户消息已读
	 * @param merchantId
	 * @param id
	 */
	void updateMessage(Integer merchantId, Integer id) throws BusinessException;

	/**
	 * 商户消息全部已读
	 * @param merchantId
	 */
	void updateAllMessage(Integer merchantId) throws BusinessException;

}
