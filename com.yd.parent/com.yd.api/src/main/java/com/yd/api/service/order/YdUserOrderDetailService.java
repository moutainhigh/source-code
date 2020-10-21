package com.yd.api.service.order;

import com.yd.api.result.order.YdUserOrderDetailResult;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.List;

/**
 * @Title:商户订单详情Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:37:13
 * @Version:1.1.0
 */
public interface YdUserOrderDetailService {

	/**
	 * 通过id得到商户订单详情YdShopOrderDetail
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserOrderDetailResult getYdShopOrderDetailById(Integer id);

	/**
	 * 得到所有商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetailResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserOrderDetailResult> getAll(YdUserOrderDetailResult ydShopOrderDetailResult);

	/**
	 * 添加商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetailResult
	 * @Description:
	 */
	public void insertYdShopOrderDetail(YdUserOrderDetailResult ydShopOrderDetailResult);
	

	/**
	 * 通过id修改商户订单详情YdShopOrderDetail
	 * @param ydShopOrderDetailResult
	 * @Description:
	 */
	public void updateYdShopOrderDetail(YdUserOrderDetailResult ydShopOrderDetailResult);

}
