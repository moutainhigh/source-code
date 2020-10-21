package com.yd.service.impl.member;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.yd.api.result.member.YdMerchantMemberOpenRecordResult;
import com.yd.api.service.member.YdMerchantMemberOpenRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.member.YdMerchantMemberOpenRecordDao;
import com.yd.service.bean.member.YdMerchantMemberOpenRecord;

/**
 * @Title:优度商户会员开通记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:58:04
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantMemberOpenRecordServiceImpl implements YdMerchantMemberOpenRecordService {

	@Resource
	private YdMerchantMemberOpenRecordDao ydMerchantMemberOpenRecordDao;

	@Override
	public YdMerchantMemberOpenRecordResult getYdMerchantMemberOpenRecordById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult = null;
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = this.ydMerchantMemberOpenRecordDao.getYdMerchantMemberOpenRecordById(id);
		if (ydMerchantMemberOpenRecord != null) {
			ydMerchantMemberOpenRecordResult = new YdMerchantMemberOpenRecordResult();
			BeanUtilExt.copyProperties(ydMerchantMemberOpenRecordResult, ydMerchantMemberOpenRecord);
		}	
		return ydMerchantMemberOpenRecordResult;
	}

	@Override
	public Page<YdMerchantMemberOpenRecordResult> findYdMerchantMemberOpenRecordListByPage(YdMerchantMemberOpenRecordResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantMemberOpenRecordResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		BeanUtilExt.copyProperties(ydMerchantMemberOpenRecord, params);
		
		int amount = this.ydMerchantMemberOpenRecordDao.getYdMerchantMemberOpenRecordCount(ydMerchantMemberOpenRecord);
		if (amount > 0) {
			List<YdMerchantMemberOpenRecord> dataList = this.ydMerchantMemberOpenRecordDao.findYdMerchantMemberOpenRecordListByPage(
				ydMerchantMemberOpenRecord, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantMemberOpenRecordResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantMemberOpenRecordResult> getAll(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) {
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		BeanUtilExt.copyProperties(ydMerchantMemberOpenRecord, ydMerchantMemberOpenRecordResult);
		List<YdMerchantMemberOpenRecord> dataList = this.ydMerchantMemberOpenRecordDao.getAll(ydMerchantMemberOpenRecord);
		return DTOUtils.convertList(dataList, YdMerchantMemberOpenRecordResult.class);
	}

	@Override
	public void insertYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) {
		if (null != ydMerchantMemberOpenRecordResult) {
			ydMerchantMemberOpenRecordResult.setCreateTime(new Date());
			ydMerchantMemberOpenRecordResult.setUpdateTime(new Date());
			YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
			BeanUtilExt.copyProperties(ydMerchantMemberOpenRecord, ydMerchantMemberOpenRecordResult);
			this.ydMerchantMemberOpenRecordDao.insertYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);
		}
	}
	
	@Override
	public void updateYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) {
		if (null != ydMerchantMemberOpenRecordResult) {
			ydMerchantMemberOpenRecordResult.setUpdateTime(new Date());
			YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
			BeanUtilExt.copyProperties(ydMerchantMemberOpenRecord, ydMerchantMemberOpenRecordResult);
			this.ydMerchantMemberOpenRecordDao.updateYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);
		}
	}

	/**
	 * 添加商户会员记录
	 * @param merchantId	商户id
	 * @param roleId		会员等级
	 * @param memberLevel	会员等级
	 * @param memberType	会员类型 (升级版，普通版)
	 * @param validLength	有效时长(月为单位)
	 * @param startTime		会员有效开始时间
	 * @param endTime		会员有效结束时间
	 * @param payPrice		支付金额
	 * @param openType		开通类型: SJ(会员升级), XF(会员续费),ZC(会员开通,注册)
	 * @param openMethod	开通方式 (自动开通, 人工开通)
	 * @throws BusinessException
	 */
	@Override
	public void addYdMerchantMemberOpenRecord(Integer merchantId, Integer roleId, Integer memberLevel, String memberType, Integer validLength,
											  Date startTime, Date endTime, Double payPrice, String openType, String openMethod) throws BusinessException {
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		ydMerchantMemberOpenRecord.setCreateTime(new Date());
		ydMerchantMemberOpenRecord.setUpdateTime(new Date());
		ydMerchantMemberOpenRecord.setMerchantId(merchantId);
		ydMerchantMemberOpenRecord.setRoleId(roleId);
		ydMerchantMemberOpenRecord.setMemberLevel(memberLevel);
		ydMerchantMemberOpenRecord.setMemberType(memberType);
		ydMerchantMemberOpenRecord.setValidLength(validLength);
		ydMerchantMemberOpenRecord.setPayPrice(payPrice);
		ydMerchantMemberOpenRecord.setStartTime(startTime);
		ydMerchantMemberOpenRecord.setEndTime(endTime);
		ydMerchantMemberOpenRecord.setOpenType(openType);
		ydMerchantMemberOpenRecord.setOpenMethod(openMethod);
		this.ydMerchantMemberOpenRecordDao.insertYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);

	}

}

