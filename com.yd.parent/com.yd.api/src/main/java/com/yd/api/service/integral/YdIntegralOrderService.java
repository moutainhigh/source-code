package com.yd.api.service.integral;

import java.util.List;

import com.yd.api.result.integral.YdIntegralOrderResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:积分订单Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:43:35
 * @Version:1.1.0
 */
public interface YdIntegralOrderService {

	/**
	 * 通过id得到积分订单YdIntegralOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralOrderResult getYdIntegralOrderById(Integer id);

	/**
	 * 分页查询积分订单YdIntegralOrder
	 * @param ydIntegralOrderResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdIntegralOrderResult> findYdIntegralOrderListByPage(YdIntegralOrderResult ydIntegralOrderResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有积分订单YdIntegralOrder
	 * @param ydIntegralOrderResult
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralOrderResult> getAll(YdIntegralOrderResult ydIntegralOrderResult);

	/**
	 * 添加积分订单YdIntegralOrder
	 * @param ydIntegralOrderResult
	 * @Description:
	 */
	public void insertYdIntegralOrder(YdIntegralOrderResult ydIntegralOrderResult) throws BusinessException;
	
	/**
	 * 通过id修改积分订单YdIntegralOrder throws BusinessException;
	 * @param ydIntegralOrderResult
	 * @Description:
	 */
	public void updateYdIntegralOrder(YdIntegralOrderResult ydIntegralOrderResult) throws BusinessException;

	/**
	 * 订单积分核销(汉堡王)
	 * @param orderId
	 * @param channelId
	 * @param mobile
	 * @throws BusinessException
	 */
    void checkMobile(Integer orderId, Integer channelId, String mobile) throws BusinessException;
}
