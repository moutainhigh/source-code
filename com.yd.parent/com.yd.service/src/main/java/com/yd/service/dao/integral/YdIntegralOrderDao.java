package com.yd.service.dao.integral;

import java.util.List;
import com.yd.service.bean.integral.YdIntegralOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:积分订单Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:43:35
 * @Version:1.1.0
 */
public interface YdIntegralOrderDao {

	/**
	 * 通过id得到积分订单YdIntegralOrder
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralOrder getYdIntegralOrderById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydIntegralOrder
	 * @return 
	 * @Description:
	 */
	public int getYdIntegralOrderCount(YdIntegralOrder ydIntegralOrder);
	
	/**
	 * 分页获取数据
	 * @param ydIntegralOrder
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralOrder> findYdIntegralOrderListByPage(@Param("params") YdIntegralOrder ydIntegralOrder,
                                                               @Param("pageStart") Integer pageStart,
                                                               @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有积分订单YdIntegralOrder
	 * @param ydIntegralOrder
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralOrder> getAll(YdIntegralOrder ydIntegralOrder);

	/**
	 * 添加积分订单YdIntegralOrder
	 * @param ydIntegralOrder
	 * @Description:
	 */
	public void insertYdIntegralOrder(YdIntegralOrder ydIntegralOrder);
	
	/**
	 * 通过id修改积分订单YdIntegralOrder
	 * @param ydIntegralOrder
	 * @Description:
	 */
	public void updateYdIntegralOrder(YdIntegralOrder ydIntegralOrder);
	
}
