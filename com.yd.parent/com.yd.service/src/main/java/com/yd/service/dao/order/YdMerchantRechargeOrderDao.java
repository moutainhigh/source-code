package com.yd.service.dao.order;

import java.util.List;
import com.yd.service.bean.order.YdMerchantRechargeOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户充值订单Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-01-13 15:56:23
 * @Version:1.1.0
 */
public interface YdMerchantRechargeOrderDao {

	/**
	 * 通过id得到商户充值订单YdMerchantRechargeOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantRechargeOrder getYdMerchantRechargeOrderById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantRechargeOrder
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantRechargeOrderCount(YdMerchantRechargeOrder ydMerchantRechargeOrder);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantRechargeOrder
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantRechargeOrder> findYdMerchantRechargeOrderListByPage(@Param("params") YdMerchantRechargeOrder ydMerchantRechargeOrder,
                                                                               @Param("pageStart") Integer pageStart,
                                                                               @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrder
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantRechargeOrder> getAll(YdMerchantRechargeOrder ydMerchantRechargeOrder);

	/**
	 * 添加商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrder
	 * @Description:
	 */
	public void insertYdMerchantRechargeOrder(YdMerchantRechargeOrder ydMerchantRechargeOrder);
	

	/**
	 * 通过id修改商户充值订单YdMerchantRechargeOrder
	 * @param ydMerchantRechargeOrder
	 * @Description:
	 */
	public void updateYdMerchantRechargeOrder(YdMerchantRechargeOrder ydMerchantRechargeOrder);
	
}
