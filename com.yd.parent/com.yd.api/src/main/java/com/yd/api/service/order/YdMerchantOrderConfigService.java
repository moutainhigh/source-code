package com.yd.api.service.order;

import com.yd.api.result.order.YdUserOrderConfigResult;
import com.yd.core.utils.BusinessException;

import java.util.List;

/**
 * @Title:商户自动取消订单时间配置Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:36:37
 * @Version:1.1.0
 */
public interface YdMerchantOrderConfigService {

	/**
	 * 通过id得到商户自动取消订单时间配置YdShopOrderConfig
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserOrderConfigResult getYdShopOrderConfigById(Integer id);

	/**
	 * 通过商户id得到商户自动取消订单时间配置YdShopOrderConfig
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdUserOrderConfigResult getYdShopOrderConfigByMerchantId(Integer merchantId) throws BusinessException;

	/**
	 * 得到所有商户自动取消订单时间配置YdShopOrderConfig
	 * @param ydShopOrderConfigResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserOrderConfigResult> getAll(YdUserOrderConfigResult ydShopOrderConfigResult);


	/**
	 * 添加商户自动取消订单时间配置YdShopOrderConfig
	 * @param ydShopOrderConfigResult
	 * @Description:
	 */
	public void insertYdShopOrderConfig(YdUserOrderConfigResult ydShopOrderConfigResult);


	/**
	 * 通过id修改商户自动取消订单时间
	 * @param merchantId	商户id
	 * @param id	id
	 * @param orderAutoCancelTime 订单自动取消时间
	 * @throws BusinessException
	 */
	public void updateYdShopOrderConfig(Integer merchantId, Integer id, Integer orderAutoCancelTime) throws BusinessException;
	
	
}
