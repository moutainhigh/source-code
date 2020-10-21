package com.yd.service.dao.order;

import java.util.Collection;
import java.util.List;
import com.yd.service.bean.order.YdMerchantOrderConfig;
import com.yd.service.bean.order.YdUserOrder;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户自动取消订单时间配置Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:36:37
 * @Version:1.1.0
 */
public interface YdMerchantOrderConfigDao {

	/**
	 * 通过id得到商户自动取消订单时间配置YdShopOrderConfig
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantOrderConfig getYdShopOrderConfigById(Integer id);

	List<YdMerchantOrderConfig> getYdShopOrderConfigByMerchantList(@Param("list")  List<Integer> merchantList);

	/**
	 * 通过merchantId得到商户自动取消订单时间配置YdShopOrderConfig
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantOrderConfig getYdShopOrderConfigByMerchantId(Integer merchantId);

	/**
	 * 得到所有商户自动取消订单时间配置YdShopOrderConfig
	 * @param ydShopOrderConfig
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantOrderConfig> getAll(YdMerchantOrderConfig ydShopOrderConfig);


	/**
	 * 添加商户自动取消订单时间配置YdShopOrderConfig
	 * @param ydShopOrderConfig
	 * @Description:
	 */
	public void insertYdShopOrderConfig(YdMerchantOrderConfig ydShopOrderConfig);
	

	/**
	 * 通过id修改商户自动取消订单时间配置YdShopOrderConfig
	 * @param ydShopOrderConfig
	 * @Description:
	 */
	public void updateYdShopOrderConfig(YdMerchantOrderConfig ydShopOrderConfig);

}
