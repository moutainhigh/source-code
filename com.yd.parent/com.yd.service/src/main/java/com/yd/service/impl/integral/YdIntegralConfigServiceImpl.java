package com.yd.service.impl.integral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yd.api.result.integral.YdIntegralConfigResult;
import com.yd.api.service.integral.YdIntegralConfigService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.dao.integral.YdIntegralItemDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.integral.YdIntegralConfigDao;
import com.yd.service.bean.integral.YdIntegralConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:积分配置Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:17:05
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdIntegralConfigServiceImpl implements YdIntegralConfigService {

	@Resource
	private YdIntegralItemDao ydIntegralItemDao;

	@Resource
	private YdIntegralConfigDao ydIntegralConfigDao;

	@Override
	public YdIntegralConfigResult getYdIntegralConfigById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdIntegralConfigResult ydIntegralConfigResult = null;
		YdIntegralConfig ydIntegralConfig = this.ydIntegralConfigDao.getYdIntegralConfigById(id);
		if (ydIntegralConfig != null) {
			ydIntegralConfigResult = new YdIntegralConfigResult();
			BeanUtilExt.copyProperties(ydIntegralConfigResult, ydIntegralConfig);
		}
		return ydIntegralConfigResult;
	}

	@Override
	public List<YdIntegralConfigResult> getAll(YdIntegralConfigResult ydIntegralConfigResult) {
		YdIntegralConfig ydIntegralConfig = null;
		if (ydIntegralConfigResult != null) {
			ydIntegralConfig = new YdIntegralConfig();
			BeanUtilExt.copyProperties(ydIntegralConfig, ydIntegralConfigResult);
		}
		List<YdIntegralConfig> dataList = this.ydIntegralConfigDao.getAll(ydIntegralConfig);
		return DTOUtils.convertList(dataList, YdIntegralConfigResult.class);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateIntegralSettlementRate(Integer id, Integer settlementRate) {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "id不可以为空");
		ValidateBusinessUtils.assertFalse(settlementRate == null || settlementRate <= 0 || settlementRate > 100,
				"err_settlement_rate", "清输入正确的结算比例");
		YdIntegralConfig ydIntegralConfig  = this.ydIntegralConfigDao.getYdIntegralConfigById(id);

		ValidateBusinessUtils.assertFalse(ydIntegralConfig == null,
				"err_not_exist_config", "配置信息不存在");
		// 修改配置结算比率
		ydIntegralConfig.setSettlementRate(settlementRate);
		this.ydIntegralConfigDao.updateYdIntegralConfig(ydIntegralConfig);

		// 修改商品结算比率
		Double rate = MathUtils.divide(settlementRate, 100, 2);
		ydIntegralItemDao.updateIntegralItemSettlePrice(rate);
	}

}

