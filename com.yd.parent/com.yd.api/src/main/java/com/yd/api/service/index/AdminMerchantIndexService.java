package com.yd.api.service.index;

import com.yd.api.result.index.*;
import java.util.List;
import java.util.Map;

/**
 * @Title:首页数据接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 13:34:30
 * @Version:1.1.0
 */
public interface AdminMerchantIndexService {


	/**
	 * 首页我的账户
	 * @param merchantId merchantId
	 * @return
	 */
	public IndexMyAccountResult getMyAccount(Integer merchantId);

	/**
	 * 首页待处理数据
	 * @param merchantId merchantId
	 * @return
	 */
	public IndexWaitHandleResult getWaitHandleData(Integer merchantId);

	/**
	 * 首页数据统计
	 * @param merchantId merchantId
	 * @param dateType	day | week | month
	 * @return
	 */
	public IndexDataStatisticsResult getIndexDataStatistics(Integer merchantId, String dateType);

	/**
	 * 首页热销数据
	 * @param merchantId merchantId
	 * @param platform admin | merchant
	 * @param dateType week | month | year
	 * @return
	 */
	List<IndexHotRankResult> getHotRankData(Integer merchantId, String platform, String dateType);

	/**
	 * index首页数据 (APP)
	 * @param merchantId
	 * @param startTime		yyyy-MM-dd HH:mm:ss
	 * @param endTime		yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	AppIndexResult getAppIndexData(Integer merchantId, String startTime, String endTime);

	/**
	 * app 营收统计
	 * @param merchantId
	 * @param startTime		yyyy-MM-dd
	 * @param endTime		yyyy-MM-dd
	 * @param type			online(线上), offline(线下), all(全部), 不传默认全部
	 * @return
	 */
	Map<String, Object> getAppSaleDetail(Integer merchantId, String startTime, String endTime, String type);

}
