package com.yd.service.impl.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.order.YdUserOrderConfigResult;
import com.yd.api.service.order.YdMerchantOrderConfigService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.ValidateBusinessUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.order.YdMerchantOrderConfigDao;
import com.yd.service.bean.order.YdMerchantOrderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户自动取消订单时间配置Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:36:37
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantOrderConfigServiceImpl implements YdMerchantOrderConfigService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantOrderConfigServiceImpl.class);

	@Resource
	private YdMerchantOrderConfigDao ydShopOrderConfigDao;

	@Override
	public YdUserOrderConfigResult getYdShopOrderConfigById(Integer id) {
		if (id == null || id <= 0) return null;
		YdUserOrderConfigResult ydShopOrderConfigResult = null;
		YdMerchantOrderConfig ydShopOrderConfig = this.ydShopOrderConfigDao.getYdShopOrderConfigById(id);
		if (ydShopOrderConfig != null) {
			ydShopOrderConfigResult = new YdUserOrderConfigResult();
			BeanUtilExt.copyProperties(ydShopOrderConfigResult, ydShopOrderConfig);
		}
		return ydShopOrderConfigResult;
	}

	/**
	 * 通过商户id得到商户自动取消订单时间配置YdShopOrderConfig
	 * @param merchantId
	 * @return
	 */
	public YdUserOrderConfigResult getYdShopOrderConfigByMerchantId(Integer merchantId) {
		if (merchantId == null || merchantId <= 0) {
			return null;
		}
		YdUserOrderConfigResult ydShopOrderConfigResult = null;
		YdMerchantOrderConfig ydShopOrderConfig = this.ydShopOrderConfigDao.getYdShopOrderConfigByMerchantId(merchantId);
		if (ydShopOrderConfig != null) {
			ydShopOrderConfigResult = new YdUserOrderConfigResult();
			BeanUtilExt.copyProperties(ydShopOrderConfigResult, ydShopOrderConfig);
		} else {
			ydShopOrderConfig = new YdMerchantOrderConfig();
			ydShopOrderConfig.setCreateTime(new Date());
			ydShopOrderConfig.setUpdateTime(new Date());
			ydShopOrderConfig.setMerchantId(merchantId);
			ydShopOrderConfig.setOrderAutoCancelTime(48);
			this.ydShopOrderConfigDao.insertYdShopOrderConfig(ydShopOrderConfig);
			ydShopOrderConfigResult = new YdUserOrderConfigResult();
			BeanUtilExt.copyProperties(ydShopOrderConfigResult, ydShopOrderConfig);
		}
		return ydShopOrderConfigResult;
	}

	@Override
	public List<YdUserOrderConfigResult> getAll(YdUserOrderConfigResult ydShopOrderConfigResult) {
		YdMerchantOrderConfig ydShopOrderConfig = null;
		if (ydShopOrderConfigResult != null) {
			ydShopOrderConfig = new YdMerchantOrderConfig();
			BeanUtilExt.copyProperties(ydShopOrderConfigResult, ydShopOrderConfig);
		}
		List<YdMerchantOrderConfig> dataList = this.ydShopOrderConfigDao.getAll(ydShopOrderConfig);
		List<YdUserOrderConfigResult> resultList = DTOUtils.convertList(dataList, YdUserOrderConfigResult.class);
		return resultList;
	}

	@Override
	public void insertYdShopOrderConfig(YdUserOrderConfigResult ydShopOrderConfigResult) {
		if (null != ydShopOrderConfigResult) {
			ydShopOrderConfigResult.setCreateTime(new Date());
			ydShopOrderConfigResult.setUpdateTime(new Date());
			YdMerchantOrderConfig ydShopOrderConfig = new YdMerchantOrderConfig();
			BeanUtilExt.copyProperties(ydShopOrderConfig, ydShopOrderConfigResult);
			this.ydShopOrderConfigDao.insertYdShopOrderConfig(ydShopOrderConfig);
		}
	}

	/**
	 * 通过id修改商户自动取消订单时间
	 * @param merchantId	商户id
	 * @param id	id
	 * @param orderAutoCancelTime 订单自动取消时间
	 * @throws BusinessException
	 */
	@Override
	public void updateYdShopOrderConfig(Integer merchantId, Integer id, Integer orderAutoCancelTime) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_order_auto_cancel_id", "非法的订单取消id");

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");

		ValidateBusinessUtils.assertFalse(orderAutoCancelTime == null || orderAutoCancelTime < 0,
				"err_cancel_time", "非法的取消时间");

		YdMerchantOrderConfig ydShopOrderConfig = new YdMerchantOrderConfig();
		ydShopOrderConfig.setId(id);
		ydShopOrderConfig.setMerchantId(merchantId);
		ydShopOrderConfig.setUpdateTime(new Date());
		ydShopOrderConfig.setOrderAutoCancelTime(orderAutoCancelTime);
		this.ydShopOrderConfigDao.updateYdShopOrderConfig(ydShopOrderConfig);
	}

}

