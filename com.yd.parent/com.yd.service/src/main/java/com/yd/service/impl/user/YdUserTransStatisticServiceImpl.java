package com.yd.service.impl.user;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserTransStatisticResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.api.service.user.YdUserTransStatisticService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.order.YdUserOrder;
import com.yd.service.bean.order.YdUserOrderDetail;
import com.yd.service.bean.user.YdUser;
import com.yd.service.dao.order.YdUserOrderDao;
import com.yd.service.dao.order.YdUserOrderDetailDao;
import com.yd.service.dao.user.YdUserDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.user.YdUserTransStatisticDao;
import com.yd.service.bean.user.YdUserTransStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:用户交易数据统计Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-07 17:05:25
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserTransStatisticServiceImpl implements YdUserTransStatisticService {

	private static final Logger logger = LoggerFactory.getLogger(YdUserTransStatisticServiceImpl.class);

	@Resource
	private YdUserDao ydUserDao;

	@Resource
	private YdUserOrderDao ydUserOrderDao;

	@Resource
	private YdUserOrderService ydUserOrderService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdUserOrderDetailDao ydUserOrderDetailDao;

	@Resource
	private YdUserTransStatisticDao ydUserTransStatisticDao;

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
	@Override
	public Page<YdUserTransStatisticResult> findUserTransStatisticListByPage(Integer merchantId, String mobile, String nickname,
																			 String startTime, String endTime, Integer minBuyNum,
																			 Integer maxBuyNum, Double minTransAmount,
																			 Double maxTransAmount, PagerInfo pageInfo) {
		Page<YdUserTransStatisticResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
		int amount = this.ydUserTransStatisticDao.getUserTransStatisticCount(merchantId, mobile, nickname, startTime, endTime, minBuyNum,
				maxBuyNum, minTransAmount, maxTransAmount);
		if (amount > 0) {
			List<YdUserTransStatistic> dataList = this.ydUserTransStatisticDao.findUserTransStatisticListByPage(merchantId, mobile, nickname, startTime, endTime,
					minBuyNum, maxBuyNum, minTransAmount, maxTransAmount, pageInfo.getStart(), pageInfo.getPageSize());
			List<YdUserTransStatisticResult> statisticResults = DTOUtils.convertList(dataList, YdUserTransStatisticResult.class);
			statisticResults.forEach(data -> {
				YdUser ydUser = ydUserDao.getYdUserById(data.getUserId());
				data.setNickname(ydUser.getNickname());
				data.setMobile(ydUser.getMobile());
			});
			pageData.setData(statisticResults);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public Page<YdUserOrderResult> findOrderDetailListByPage(YdUserOrderResult params, PagerInfo pageInfo) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		Page<YdUserOrderResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());

		// 查询支付成功的订单
		int amount = this.ydUserOrderDao.getUserOrderNumByOrderStatus(params.getUserId(), storeInfo.getId(), params.getOrderStatus());
		if (amount > 0) {
			List<YdUserOrder> dataList = this.ydUserOrderDao.findFrontOrderListByPage(params.getUserId(), storeInfo.getId(),
					params.getOrderStatus(), pageInfo.getStart(), pageInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdUserOrderResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdUserTransStatisticResult> getAll(YdUserTransStatisticResult ydUserTransStatisticResult) {
		YdUserTransStatistic ydUserTransStatistic = null;
		if (ydUserTransStatisticResult != null) {
			ydUserTransStatistic = new YdUserTransStatistic();
			BeanUtilExt.copyProperties(ydUserTransStatistic, ydUserTransStatisticResult);
		}
		List<YdUserTransStatistic> dataList = this.ydUserTransStatisticDao.getAll(ydUserTransStatistic);
		return DTOUtils.convertList(dataList, YdUserTransStatisticResult.class);
	}

	@Override
	public void insertYdUserTransStatistic(YdUserTransStatisticResult ydUserTransStatisticResult) {
		if (null != ydUserTransStatisticResult) {
			ydUserTransStatisticResult.setCreateTime(new Date());
			ydUserTransStatisticResult.setUpdateTime(new Date());
			YdUserTransStatistic ydUserTransStatistic = new YdUserTransStatistic();
			BeanUtilExt.copyProperties(ydUserTransStatistic, ydUserTransStatisticResult);
			this.ydUserTransStatisticDao.insertYdUserTransStatistic(ydUserTransStatistic);
		}
	}

	@Override
	public void updateYdUserTransStatistic(YdUserTransStatisticResult ydUserTransStatisticResult) {
		if (null != ydUserTransStatisticResult) {
			ydUserTransStatisticResult.setUpdateTime(new Date());
			YdUserTransStatistic ydUserTransStatistic = new YdUserTransStatistic();
			BeanUtilExt.copyProperties(ydUserTransStatistic, ydUserTransStatisticResult);
			this.ydUserTransStatisticDao.updateYdUserTransStatistic(ydUserTransStatistic);
		}
	}

	/**
	 * 用户交易数据同步
	 */
	@Override
	public void synUserTransStatic() {
		YdUserOrderResult ydUserOrderResult = new YdUserOrderResult();
		ydUserOrderResult.setOrderStatus("SUCCESS");
		List<YdUserOrderResult> orderList = ydUserOrderService.getAll(ydUserOrderResult);
		if (CollectionUtils.isEmpty(orderList)) return;

		Map<Integer, List<YdUserOrderResult>> userOrderMap = orderList.stream()
				.collect(Collectors.groupingBy(YdUserOrderResult::getUserId));

		for (Map.Entry<Integer, List<YdUserOrderResult>> entry : userOrderMap.entrySet()) {
			Integer userId = entry.getKey();
			List<YdUserOrderResult> userOrderList = entry.getValue();

			Map<Integer, List<YdUserOrderResult>> merchantMap = userOrderList.stream()
					.collect(Collectors.groupingBy(YdUserOrderResult::getMerchantId));
			for (Map.Entry<Integer, List<YdUserOrderResult>> merchantEntry : merchantMap.entrySet()) {
				Integer merchantId = merchantEntry.getKey();
				List<YdUserOrderResult> merchantOrderList = merchantEntry.getValue();

				List<YdUserOrderResult> sortMerchantOrderList = merchantOrderList.stream()
						.filter(data -> data.getPayTime() != null)
						.sorted(Comparator.comparing(YdUserOrderResult::getPayTime).reversed())
						.collect(Collectors.toList());

				Integer itemCount = null;
				try {
					itemCount = merchantOrderList.stream().mapToInt(YdUserOrderResult :: getItemCount).sum();
				} catch (Exception e) {
					itemCount = 0;
				}
				double payPrice = merchantOrderList.stream().mapToDouble(YdUserOrderResult :: getPayPrice).sum();
				double orderPrice = merchantOrderList.stream().mapToDouble(YdUserOrderResult :: getOrderPrice).sum();
				Date lastTransTime = sortMerchantOrderList.get(0).getPayTime();
				YdUserTransStatistic ydUserTransStatistic = new YdUserTransStatistic();

				ydUserTransStatistic.setUserId(userId);
				ydUserTransStatistic.setMerchantId(merchantId);
				ydUserTransStatistic.setTotalBuyCount(merchantOrderList.size());
				ydUserTransStatistic.setTotalBuyItemCount(itemCount);
				ydUserTransStatistic.setTotalPayAmount(payPrice);
				ydUserTransStatistic.setTotalTransAmount(orderPrice);
				ydUserTransStatistic.setLastTransDate(lastTransTime);

				YdUserTransStatistic params = new YdUserTransStatistic();
				params.setUserId(userId);
				params.setMerchantId(merchantId);
				List<YdUserTransStatistic> dataList = this.ydUserTransStatisticDao.getAll(params);
				Date nowDate = new Date();
				if (CollectionUtils.isEmpty(dataList)) {
					ydUserTransStatistic.setCreateTime(nowDate);
					this.ydUserTransStatisticDao.insertYdUserTransStatistic(ydUserTransStatistic);
					logger.info("====交易统计新增的数据=" + JSON.toJSONString(ydUserTransStatistic));
				} else {
					ydUserTransStatistic.setUpdateTime(nowDate);
					ydUserTransStatistic.setId(dataList.get(0).getId());
					this.ydUserTransStatisticDao.updateYdUserTransStatistic(ydUserTransStatistic);
					logger.info("====交易统计修改的数据=" + JSON.toJSONString(ydUserTransStatistic));
				}
			}
		}
	}


	/**
	 * 统计用户下班购买的数量，只同步一次，上线的时候用
	 */
	@Override
	public void synUserOrderItemCount() {
		YdUserOrder params = new YdUserOrder();
		params.setOrderStatus("SUCCESS");
		List<YdUserOrder> orderList = ydUserOrderDao.getAll(params);
		if (CollectionUtils.isEmpty(orderList)) return;

		orderList.forEach(ydUserOrder -> {
			YdUserOrderDetail ydUserOrderDetail = new YdUserOrderDetail();
			ydUserOrderDetail.setOrderId(ydUserOrder.getId());
			List<YdUserOrderDetail> detailList = ydUserOrderDetailDao.getAll(ydUserOrderDetail);
			int itemCount = detailList.stream().mapToInt(YdUserOrderDetail :: getNum).sum();
			ydUserOrder.setItemCount(itemCount);
			this.ydUserOrderDao.updateYdUserOrder(ydUserOrder);
		});
	}
}

