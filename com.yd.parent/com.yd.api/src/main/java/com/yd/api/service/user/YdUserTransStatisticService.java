package com.yd.api.service.user;

import java.util.List;

import com.yd.api.result.order.YdUserOrderDetailResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserTransStatisticResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户交易数据统计Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-07 17:05:25
 * @Version:1.1.0
 */
public interface YdUserTransStatisticService {

	/**
	 * 查询用户交易数据
	 * @param merchantId 商户id
	 * @param startTime	交易开始时间
	 * @param endTime	交易结束时间
	 * @param minBuyNum	购买最小次数
	 * @param maxBuyNum	购买最大次数
	 * @param minTransAmount	交易最小金额
	 * @param maxTransAmount	最大金额
	 * @param pageInfo	分页信息
	 * @return
	 */
	Page<YdUserTransStatisticResult> findUserTransStatisticListByPage(Integer merchantId, String mobile, String nickname, String startTime, String endTime, Integer minBuyNum,
																	  Integer maxBuyNum, Double minTransAmount, Double maxTransAmount, PagerInfo pageInfo);

	/**
	 * 查询用户交易信息详情
	 * @param ydUserOrderResult
	 * @param pageInfo
	 * @return
	 * @throws BusinessException
	 */
	public Page<YdUserOrderResult> findOrderDetailListByPage(YdUserOrderResult ydUserOrderResult, PagerInfo pageInfo) throws BusinessException;

	/**
	 * 得到所有用户交易数据统计YdUserTransStatistic
	 * @param ydUserTransStatisticResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserTransStatisticResult> getAll(YdUserTransStatisticResult ydUserTransStatisticResult);

	/**
	 * 添加用户交易数据统计YdUserTransStatistic
	 * @param ydUserTransStatisticResult
	 * @Description:
	 */
	public void insertYdUserTransStatistic(YdUserTransStatisticResult ydUserTransStatisticResult) throws BusinessException;
	
	/**
	 * 通过id修改用户交易数据统计YdUserTransStatistic throws BusinessException;
	 * @param ydUserTransStatisticResult
	 * @Description:
	 */
	public void updateYdUserTransStatistic(YdUserTransStatisticResult ydUserTransStatisticResult)throws BusinessException;

	/**
	 * 定时统计用户交易信息
	 */
	public void synUserTransStatic();

	/**
	 * 统计用户下班购买的数量，只同步一次，上线的时候用
	 */
	public void synUserOrderItemCount();

}
