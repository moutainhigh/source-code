package com.yd.service.impl.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yd.api.result.order.YdSupplerGiftOrderResult;
import com.yd.api.service.order.YdSupplerGiftOrderService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.order.YdSupplerGiftOrderDao;
import com.yd.service.bean.order.YdSupplerGiftOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:供货商礼品订单Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 17:26:44
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdSupplerGiftOrderServiceImpl implements YdSupplerGiftOrderService {

	private static final Logger logger = LoggerFactory.getLogger(YdSupplerGiftOrderServiceImpl.class);

	@Resource
	private YdSupplerGiftOrderDao ydSupplerGiftOrderDao;

	@Override
	public YdSupplerGiftOrderResult getYdSupplerGiftOrderById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdSupplerGiftOrderResult ydSupplerGiftOrderResult = null;
		YdSupplerGiftOrder ydSupplerGiftOrder = this.ydSupplerGiftOrderDao.getYdSupplerGiftOrderById(id);
		if (ydSupplerGiftOrder != null) {
			ydSupplerGiftOrderResult = new YdSupplerGiftOrderResult();
			BeanUtilExt.copyProperties(ydSupplerGiftOrderResult, ydSupplerGiftOrder);
		}
		return ydSupplerGiftOrderResult;
	}

	@Override
	public Page<YdSupplerGiftOrderResult> findYdSupplerGiftOrderListByPage(YdSupplerGiftOrderResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdSupplerGiftOrderResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdSupplerGiftOrder ydSupplerGiftOrder = new YdSupplerGiftOrder();
		BeanUtilExt.copyProperties(ydSupplerGiftOrder, params);
		
		int amount = this.ydSupplerGiftOrderDao.getYdSupplerGiftOrderCount(ydSupplerGiftOrder);
		if (amount > 0) {
			List<YdSupplerGiftOrder> dataList = this.ydSupplerGiftOrderDao.findYdSupplerGiftOrderListByPage(ydSupplerGiftOrder, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdSupplerGiftOrderResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdSupplerGiftOrderResult> getAll(YdSupplerGiftOrderResult ydSupplerGiftOrderResult) {
		YdSupplerGiftOrder ydSupplerGiftOrder = null;
		if (ydSupplerGiftOrderResult != null) {
			ydSupplerGiftOrder = new YdSupplerGiftOrder();
			BeanUtilExt.copyProperties(ydSupplerGiftOrder, ydSupplerGiftOrderResult);
		}
		List<YdSupplerGiftOrder> dataList = this.ydSupplerGiftOrderDao.getAll(ydSupplerGiftOrder);
		return DTOUtils.convertList(dataList, YdSupplerGiftOrderResult.class);
	}

	@Override
	public void insertYdSupplerGiftOrder(YdSupplerGiftOrderResult ydSupplerGiftOrderResult) {
		if (null != ydSupplerGiftOrderResult) {
			ydSupplerGiftOrderResult.setCreateTime(new Date());
			ydSupplerGiftOrderResult.setUpdateTime(new Date());
			YdSupplerGiftOrder ydSupplerGiftOrder = new YdSupplerGiftOrder();
			BeanUtilExt.copyProperties(ydSupplerGiftOrder, ydSupplerGiftOrderResult);
			this.ydSupplerGiftOrderDao.insertYdSupplerGiftOrder(ydSupplerGiftOrder);
		}
	}

	@Override
	public void updateYdSupplerGiftOrder(YdSupplerGiftOrderResult ydSupplerGiftOrderResult) {
		if (null != ydSupplerGiftOrderResult) {
			ydSupplerGiftOrderResult.setUpdateTime(new Date());
			YdSupplerGiftOrder ydSupplerGiftOrder = new YdSupplerGiftOrder();
			BeanUtilExt.copyProperties(ydSupplerGiftOrder, ydSupplerGiftOrderResult);
			this.ydSupplerGiftOrderDao.updateYdSupplerGiftOrder(ydSupplerGiftOrder);
		}
	}

}

