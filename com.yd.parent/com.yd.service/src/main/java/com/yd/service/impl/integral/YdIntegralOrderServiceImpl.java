package com.yd.service.impl.integral;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.result.integral.YdIntegralOrderResult;
import com.yd.api.service.integral.YdIntegralOrderService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.order.YdUserOrder;
import com.yd.service.dao.order.YdUserOrderDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.integral.YdIntegralOrderDao;
import com.yd.service.bean.integral.YdIntegralOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:积分订单Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:43:35
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdIntegralOrderServiceImpl implements YdIntegralOrderService {

	private static final Logger logger = LoggerFactory.getLogger(YdIntegralOrderServiceImpl.class);

	@Resource
	private YdUserOrderDao ydUserOrderDao;

	@Resource
	private YdIntegralOrderDao ydIntegralOrderDao;

	@Override
	public YdIntegralOrderResult getYdIntegralOrderById(Integer id) {
		if (id == null || id <= 0) return null;
		YdIntegralOrderResult ydIntegralOrderResult = new YdIntegralOrderResult();
		YdIntegralOrder ydIntegralOrder = this.ydIntegralOrderDao.getYdIntegralOrderById(id);
		if (ydIntegralOrder != null) {
			BeanUtilExt.copyProperties(ydIntegralOrderResult, ydIntegralOrder);
		}
		return ydIntegralOrderResult;
	}

	@Override
	public Page<YdIntegralOrderResult> findYdIntegralOrderListByPage(YdIntegralOrderResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdIntegralOrderResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdIntegralOrder ydIntegralOrder = new YdIntegralOrder();
		BeanUtilExt.copyProperties(ydIntegralOrder, params);
		
		int amount = this.ydIntegralOrderDao.getYdIntegralOrderCount(ydIntegralOrder);
		if (amount > 0) {
			List<YdIntegralOrder> dataList = this.ydIntegralOrderDao.findYdIntegralOrderListByPage(
					ydIntegralOrder, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdIntegralOrderResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdIntegralOrderResult> getAll(YdIntegralOrderResult ydIntegralOrderResult) {
		YdIntegralOrder ydIntegralOrder = null;
		if (ydIntegralOrderResult != null) {
			ydIntegralOrder = new YdIntegralOrder();
			BeanUtilExt.copyProperties(ydIntegralOrder, ydIntegralOrderResult);
		}
		List<YdIntegralOrder> dataList = this.ydIntegralOrderDao.getAll(ydIntegralOrder);
		return DTOUtils.convertList(dataList, YdIntegralOrderResult.class);
	}

	@Override
	public void insertYdIntegralOrder(YdIntegralOrderResult ydIntegralOrderResult) {
		if (null != ydIntegralOrderResult) {
			ydIntegralOrderResult.setCreateTime(new Date());
			ydIntegralOrderResult.setUpdateTime(new Date());
			YdIntegralOrder ydIntegralOrder = new YdIntegralOrder();
			BeanUtilExt.copyProperties(ydIntegralOrder, ydIntegralOrderResult);
			this.ydIntegralOrderDao.insertYdIntegralOrder(ydIntegralOrder);
		}
	}

	@Override
	public void updateYdIntegralOrder(YdIntegralOrderResult ydIntegralOrderResult) {
		if (null != ydIntegralOrderResult) {
			ydIntegralOrderResult.setUpdateTime(new Date());
			YdIntegralOrder ydIntegralOrder = new YdIntegralOrder();
			BeanUtilExt.copyProperties(ydIntegralOrder, ydIntegralOrderResult);
			this.ydIntegralOrderDao.updateYdIntegralOrder(ydIntegralOrder);
		}
	}

	/**
	 * 订单积分核销(汉堡王)
	 * @param orderId
	 * @param channelId
	 * @param mobile
	 * @throws BusinessException
	 */
	@Override
	public void checkMobile(Integer orderId, Integer channelId, String mobile) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile),
				"err_empty_mobile", "手机号不可以为空");

		ValidateBusinessUtils.assertFalse(orderId == null,
				"err_empty_mobile", "订单id不可以为空");

		ValidateBusinessUtils.assertFalse(channelId == null,
				"err_empty_mobile", "渠道id不可以为空");

		YdUserOrder ydUserOrder = ydUserOrderDao.getYdUserOrderById(orderId);
		ValidateBusinessUtils.assertFalse(ydUserOrder == null,
				"err_empty_mobile", "订单不存在");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getIsCheckIntegralReduce() == null
				|| "N".equalsIgnoreCase(ydUserOrder.getIsCheckIntegralReduce()),
				"err_empty_mobile", "订单未开启积分抵扣");

		ValidateBusinessUtils.assertFalse(ydUserOrder.getIntegralReducePrice() != null
						&& ydUserOrder.getIntegralReducePrice() > 0,
				"err_empty_mobile", "该订单已经使用过积分抵扣");

		JSONObject jsonObject = YunxiaokejiUtil.checkMobile(mobile, "10");
		logger.info("====积分核销返回的json值===" + JSON.toJSONString(jsonObject));
		if (jsonObject != null && jsonObject.getInteger("code") == 200) {
			// 订单状态 （1：核销成功）
			JSONObject outOrderData = jsonObject.getJSONObject("result");
			Integer status = outOrderData.getInteger("status");
			Integer price = outOrderData.getInteger("price");
			if (status == 1) {
				// 修改订单状态
				String outOrderId = outOrderData.getString("orderId");
				ydUserOrder.setIntegralReducePrice(Double.valueOf(price));
				ydUserOrderDao.updateYdUserOrder(ydUserOrder);
			}
		}

	}

}

