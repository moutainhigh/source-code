package com.yd.service.impl.draw;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.draw.YdMerchantDrawPrizeResult;
import com.yd.api.service.draw.YdMerchantDrawPrizeService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.coupon.YdMerchantCoupon;
import com.yd.service.dao.coupon.YdMerchantCouponDao;
import com.yd.service.dao.draw.YdMerchantDrawPrizeDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.bean.draw.YdMerchantDrawPrize;

/**
 * @Title:商户抽奖活动奖品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:34:22
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantDrawPrizeServiceImpl implements YdMerchantDrawPrizeService {

	@Resource
	private YdMerchantCouponDao ydMerchantCouponDao;

	@Resource
	private YdMerchantDrawPrizeDao ydMerchantDrawPrizeDao;

	@Override
	public YdMerchantDrawPrizeResult getYdMerchantDrawPrizeById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult = null;
		YdMerchantDrawPrize ydMerchantDrawPrize = this.ydMerchantDrawPrizeDao.getYdMerchantDrawPrizeById(id);
		if (ydMerchantDrawPrize != null) {
			ydMerchantDrawPrizeResult = new YdMerchantDrawPrizeResult();
			BeanUtilExt.copyProperties(ydMerchantDrawPrizeResult, ydMerchantDrawPrize);
		}
		
		return ydMerchantDrawPrizeResult;
	}

	@Override
	public Page<YdMerchantDrawPrizeResult> findYdMerchantDrawPrizeListByPage(YdMerchantDrawPrizeResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantDrawPrizeResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantDrawPrize ydMerchantDrawPrize = new YdMerchantDrawPrize();
		BeanUtilExt.copyProperties(ydMerchantDrawPrize, params);
		
		int amount = this.ydMerchantDrawPrizeDao.getYdMerchantDrawPrizeCount(ydMerchantDrawPrize);
		if (amount > 0) {
			List<YdMerchantDrawPrize> dataList = this.ydMerchantDrawPrizeDao.findYdMerchantDrawPrizeListByPage(
					ydMerchantDrawPrize, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantDrawPrizeResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantDrawPrizeResult> getAll(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult) {
		YdMerchantDrawPrize ydMerchantDrawPrize = null;
		if (ydMerchantDrawPrizeResult != null) {
			ydMerchantDrawPrize = new YdMerchantDrawPrize();
			BeanUtilExt.copyProperties(ydMerchantDrawPrize, ydMerchantDrawPrizeResult);
		}
		List<YdMerchantDrawPrize> dataList = this.ydMerchantDrawPrizeDao.getAll(ydMerchantDrawPrize);
		List<YdMerchantDrawPrizeResult> resultList = DTOUtils.convertList(dataList, YdMerchantDrawPrizeResult.class);
		resultList.forEach(drawPrize -> {
			if ("YHQ".equalsIgnoreCase(drawPrize.getPrizeType()) && drawPrize.getCouponId() != null) {
				YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(drawPrize.getCouponId());
				if (merchantCoupon != null) {
					drawPrize.setCouponTitle(merchantCoupon.getCouponTitle());
				}
			}
		});
		return resultList;
	}

	@Override
	public void insertYdMerchantDrawPrize(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult) {
		if (null != ydMerchantDrawPrizeResult) {
			ydMerchantDrawPrizeResult.setCreateTime(new Date());
			ydMerchantDrawPrizeResult.setUpdateTime(new Date());
			YdMerchantDrawPrize ydMerchantDrawPrize = new YdMerchantDrawPrize();
			BeanUtilExt.copyProperties(ydMerchantDrawPrize, ydMerchantDrawPrizeResult);
			this.ydMerchantDrawPrizeDao.insertYdMerchantDrawPrize(ydMerchantDrawPrize);
		}
	}
	
	@Override
	public void updateYdMerchantDrawPrize(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult) {
		if (null != ydMerchantDrawPrizeResult) {
			ydMerchantDrawPrizeResult.setUpdateTime(new Date());
			YdMerchantDrawPrize ydMerchantDrawPrize = new YdMerchantDrawPrize();
			BeanUtilExt.copyProperties(ydMerchantDrawPrize, ydMerchantDrawPrizeResult);
			this.ydMerchantDrawPrizeDao.updateYdMerchantDrawPrize(ydMerchantDrawPrize);
		}
	}

}

