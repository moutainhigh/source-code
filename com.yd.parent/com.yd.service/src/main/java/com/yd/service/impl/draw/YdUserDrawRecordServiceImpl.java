package com.yd.service.impl.draw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdUserDrawRecordResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.draw.YdMerchantDrawActivityService;
import com.yd.api.service.draw.YdUserDrawRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.coupon.YdUserCoupon;
import com.yd.service.dao.coupon.YdUserCouponDao;
import com.yd.service.dao.draw.YdUserDrawRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.bean.draw.YdUserDrawRecord;

/**
 * @Title:用户抽奖记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:35:07
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserDrawRecordServiceImpl implements YdUserDrawRecordService {

	@Resource
	private YdUserCouponDao ydUserCouponDao;

	@Resource
	private YdUserDrawRecordDao ydUserDrawRecordDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdMerchantDrawActivityService ydMerchantDrawActivityService;

	@Override
	public YdUserDrawRecordResult getYdUserDrawRecordById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdUserDrawRecordResult ydUserDrawRecordResult = null;
		YdUserDrawRecord ydUserDrawRecord = this.ydUserDrawRecordDao.getYdUserDrawRecordById(id);
		if (ydUserDrawRecord != null) {
			ydUserDrawRecordResult = new YdUserDrawRecordResult();
			BeanUtilExt.copyProperties(ydUserDrawRecordResult, ydUserDrawRecord);
		}
		return ydUserDrawRecordResult;
	}

	@Override
	public Page<YdUserDrawRecordResult> findYdUserDrawRecordListByPage(YdUserDrawRecordResult params, PagerInfo pagerInfo) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		Page<YdUserDrawRecordResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
		BeanUtilExt.copyProperties(ydUserDrawRecord, params);
		
		int amount = this.ydUserDrawRecordDao.getYdUserDrawRecordCount(ydUserDrawRecord);
		if (amount > 0) {
			List<YdUserDrawRecord> dataList = this.ydUserDrawRecordDao.findYdUserDrawRecordListByPage(
					ydUserDrawRecord, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdUserDrawRecordResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdUserDrawRecordResult> getAll(YdUserDrawRecordResult ydUserDrawRecordResult) {
		YdUserDrawRecord ydUserDrawRecord = null;
		if (ydUserDrawRecordResult != null) {
			ydUserDrawRecord = new YdUserDrawRecord();
			BeanUtilExt.copyProperties(ydUserDrawRecord, ydUserDrawRecordResult);
		}
		List<YdUserDrawRecord> dataList = this.ydUserDrawRecordDao.getAll(ydUserDrawRecord);
		return DTOUtils.convertList(dataList, YdUserDrawRecordResult.class);
	}

	@Override
	public void insertYdUserDrawRecord(YdUserDrawRecordResult ydUserDrawRecordResult) {
		if (null != ydUserDrawRecordResult) {
			ydUserDrawRecordResult.setCreateTime(new Date());
			ydUserDrawRecordResult.setUpdateTime(new Date());
			YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
			BeanUtilExt.copyProperties(ydUserDrawRecord, ydUserDrawRecordResult);
			this.ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
		}
	}

	@Override
	public void updateYdUserDrawRecord(YdUserDrawRecordResult ydUserDrawRecordResult) {
		if (null != ydUserDrawRecordResult) {
			ydUserDrawRecordResult.setUpdateTime(new Date());
			YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
			BeanUtilExt.copyProperties(ydUserDrawRecord, ydUserDrawRecordResult);
			this.ydUserDrawRecordDao.updateYdUserDrawRecord(ydUserDrawRecord);
		}
	}

	@Override
	public List<YdUserCouponResult> getUserDrawRecord(Integer currUserId,  String uuid) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(uuid),
				"err_empty_uuid", "uuid不可以为空");
		YdMerchantDrawActivityResult ydMerchantDrawActivity = ydMerchantDrawActivityService.getYdMerchantDrawActivityByUdid(uuid);
		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
				"err_not_exist", "活动不存在");

		YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
		ydUserDrawRecord.setUserId(currUserId);
		ydUserDrawRecord.setPrizeType("YHQ");
		ydUserDrawRecord.setActivityId(ydMerchantDrawActivity.getId());
		List<YdUserDrawRecord> dataList = this.ydUserDrawRecordDao.getAll(ydUserDrawRecord);

		if (CollectionUtils.isEmpty(dataList)) {
			return null;
		}
		List<YdUserCoupon> couponList = new ArrayList<>();
		dataList.forEach(userDrawRecord -> {
			couponList.add(ydUserCouponDao.getYdUserCouponById(userDrawRecord.getUserCouponId()));
		});
		return DTOUtils.convertList(couponList, YdUserCouponResult.class);
	}

}

