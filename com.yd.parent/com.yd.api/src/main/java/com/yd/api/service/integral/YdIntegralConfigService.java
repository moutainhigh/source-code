package com.yd.api.service.integral;

import java.util.List;

import com.yd.api.result.integral.YdIntegralConfigResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:积分配置Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:17:05
 * @Version:1.1.0
 */
public interface YdIntegralConfigService {

	/**
	 * 通过id得到积分配置YdIntegralConfig
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralConfigResult getYdIntegralConfigById(Integer id);

	/**
	 * 得到所有积分配置YdIntegralConfig
	 * @param ydIntegralConfigResult
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralConfigResult> getAll(YdIntegralConfigResult ydIntegralConfigResult);

	/**
	 * 更新积分商品结算比率
	 * @param id
	 * @param settlementRate
	 */
	void updateIntegralSettlementRate(Integer id, Integer settlementRate) throws BusinessException;

}
