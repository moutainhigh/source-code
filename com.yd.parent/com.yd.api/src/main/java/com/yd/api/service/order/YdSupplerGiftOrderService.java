package com.yd.api.service.order;

import java.util.List;
import com.yd.api.result.order.YdSupplerGiftOrderResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:供货商礼品订单Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 17:26:44
 * @Version:1.1.0
 */
public interface YdSupplerGiftOrderService {

	/**
	 * 通过id得到供货商礼品订单YdSupplerGiftOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdSupplerGiftOrderResult getYdSupplerGiftOrderById(Integer id);

	/**
	 * 分页查询供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrderResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdSupplerGiftOrderResult> findYdSupplerGiftOrderListByPage(YdSupplerGiftOrderResult ydSupplerGiftOrderResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrderResult
	 * @return 
	 * @Description:
	 */
	public List<YdSupplerGiftOrderResult> getAll(YdSupplerGiftOrderResult ydSupplerGiftOrderResult);

	/**
	 * 添加供货商礼品订单YdSupplerGiftOrder
	 * @param ydSupplerGiftOrderResult
	 * @Description:
	 */
	public void insertYdSupplerGiftOrder(YdSupplerGiftOrderResult ydSupplerGiftOrderResult) throws BusinessException;
	

	/**
	 * 通过id修改供货商礼品订单YdSupplerGiftOrder throws BusinessException;
	 * @param ydSupplerGiftOrderResult
	 * @Description:
	 */
	public void updateYdSupplerGiftOrder(YdSupplerGiftOrderResult ydSupplerGiftOrderResult)throws BusinessException;
	
	
}
