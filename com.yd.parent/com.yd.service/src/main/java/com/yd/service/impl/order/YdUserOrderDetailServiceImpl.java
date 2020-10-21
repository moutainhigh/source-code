package com.yd.service.impl.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdUserOrderDetailResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdUserOrderDetailService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.order.YdUserOrderDetailDao;
import com.yd.service.bean.order.YdUserOrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户订单详情Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:37:13
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserOrderDetailServiceImpl implements YdUserOrderDetailService {

	private static final Logger logger = LoggerFactory.getLogger(YdUserOrderDetailServiceImpl.class);

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdUserOrderDetailDao ydUserOrderDetailDao;

	@Override
	public YdUserOrderDetailResult getYdShopOrderDetailById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdUserOrderDetailResult ydShopOrderDetailResult = null;
		YdUserOrderDetail ydShopOrderDetail = this.ydUserOrderDetailDao.getYdShopOrderDetailById(id);
		if (ydShopOrderDetail != null) {
			ydShopOrderDetailResult = new YdUserOrderDetailResult();
			BeanUtilExt.copyProperties(ydShopOrderDetailResult, ydShopOrderDetail);
		}
		return ydShopOrderDetailResult;
	}

	@Override
	public List<YdUserOrderDetailResult> getAll(YdUserOrderDetailResult ydShopOrderDetailResult) {
		YdUserOrderDetail ydShopOrderDetail = null;
		if (ydShopOrderDetailResult != null) {
			ydShopOrderDetail = new YdUserOrderDetail();
			BeanUtilExt.copyProperties(ydShopOrderDetailResult, ydShopOrderDetail);
		}
		List<YdUserOrderDetail> dataList = this.ydUserOrderDetailDao.getAll(ydShopOrderDetail);
		return DTOUtils.convertList(dataList, YdUserOrderDetailResult.class);
	}

	@Override
	public void insertYdShopOrderDetail(YdUserOrderDetailResult ydShopOrderDetailResult) {
		if (null != ydShopOrderDetailResult) {
			ydShopOrderDetailResult.setCreateTime(new Date());
			ydShopOrderDetailResult.setUpdateTime(new Date());
			YdUserOrderDetail ydShopOrderDetail = new YdUserOrderDetail();
			BeanUtilExt.copyProperties(ydShopOrderDetail, ydShopOrderDetailResult);
			this.ydUserOrderDetailDao.insertYdShopOrderDetail(ydShopOrderDetail);
		}
	}

	@Override
	public void updateYdShopOrderDetail(YdUserOrderDetailResult ydShopOrderDetailResult) {
		if (null != ydShopOrderDetailResult) {
			ydShopOrderDetailResult.setUpdateTime(new Date());
			YdUserOrderDetail ydShopOrderDetail = new YdUserOrderDetail();
			BeanUtilExt.copyProperties(ydShopOrderDetail, ydShopOrderDetailResult);
			this.ydUserOrderDetailDao.updateYdShopOrderDetail(ydShopOrderDetail);
		}
	}

}

