package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yd.api.result.merchant.YdMerchantInfoAuditResult;
import com.yd.api.service.merchant.YdMerchantInfoAuditService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.dao.merchant.YdMerchantDao;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.merchant.YdMerchantInfoAuditDao;
import com.yd.service.bean.merchant.YdMerchantInfoAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:门店信息审核记录表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-05 18:31:13
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantInfoAuditServiceImpl implements YdMerchantInfoAuditService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantInfoAuditServiceImpl.class);

	@Resource
	private YdMerchantInfoAuditDao ydMerchantInfoAuditDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	/**
	 * 根据审核id查询详情
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantInfoAuditResult getYdMerchantInfoAuditById(Integer id) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "请传入正确的id");

		YdMerchantInfoAudit merchantInfoAudit = this.ydMerchantInfoAuditDao.getYdMerchantInfoAuditById(id);
		ValidateBusinessUtils.assertFalse(merchantInfoAudit == null,
				"err_empty_id", "数据不存在");

		YdMerchantInfoAuditResult ydMerchantInfoAuditResult = new YdMerchantInfoAuditResult();
		BeanUtilExt.copyProperties(ydMerchantInfoAuditResult, merchantInfoAudit);
		return ydMerchantInfoAuditResult;
	}

	@Override
	public Page<YdMerchantInfoAuditResult> findYdMerchantInfoAuditListByPage(YdMerchantInfoAuditResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantInfoAuditResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantInfoAudit ydMerchantInfoAudit = new YdMerchantInfoAudit();
		BeanUtilExt.copyProperties(ydMerchantInfoAudit, params);
		
		int amount = this.ydMerchantInfoAuditDao.getYdMerchantInfoAuditCount(ydMerchantInfoAudit);
		if (amount > 0) {
			List<YdMerchantInfoAudit> dataList = this.ydMerchantInfoAuditDao.findYdMerchantInfoAuditListByPage(
					ydMerchantInfoAudit, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantInfoAuditResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	/**
	 * 审核商户提交信息
	 * @param id
	 * @param auditStatus	SUCCESS(审核通过), REFUSE(审核拒绝)
	 * @throws BusinessException
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void auditMerchantInfo(Integer id, String auditStatus) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "请传入正确的id");

		YdMerchantInfoAudit merchantInfoAudit = this.ydMerchantInfoAuditDao.getYdMerchantInfoAuditById(id);
		ValidateBusinessUtils.assertFalse(merchantInfoAudit == null,
				"err_empty_id", "数据不存在");

		if ("SUCCESS".equalsIgnoreCase(auditStatus)) {
			YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(merchantInfoAudit.getMobile());
			ValidateBusinessUtils.assertFalse(ydMerchant != null && !ydMerchant.getId().equals(merchantInfoAudit.getMerchantId()),
					"err_exist_mobile", "手机号已存在，请拒绝让商户重新提交申请");
			// 更新审批状态
			merchantInfoAudit.setAuditStatus(auditStatus);
			this.ydMerchantInfoAuditDao.updateYdMerchantInfoAudit(merchantInfoAudit);

			// 将商户修改信息更换到商户表
			ydMerchant = new YdMerchant();
			BeanUtilExt.copyProperties(ydMerchant, merchantInfoAudit);
			ydMerchant.setUpdateTime(new Date());
			ydMerchant.setId(merchantInfoAudit.getMerchantId());
			ydMerchantDao.updateYdMerchant(ydMerchant);
		} else if ("REFUSE".equalsIgnoreCase(auditStatus)) {
			// 更新审批状态
			merchantInfoAudit.setAuditStatus(auditStatus);
			this.ydMerchantInfoAuditDao.updateYdMerchantInfoAudit(merchantInfoAudit);
		}
	}

	@Override
	public List<YdMerchantInfoAuditResult> getAll(YdMerchantInfoAuditResult ydMerchantInfoAuditResult) {
		YdMerchantInfoAudit ydMerchantInfoAudit = null;
		if (ydMerchantInfoAuditResult != null) {
			ydMerchantInfoAudit = new YdMerchantInfoAudit();
			BeanUtilExt.copyProperties(ydMerchantInfoAudit, ydMerchantInfoAuditResult);
		}
		List<YdMerchantInfoAudit> dataList = this.ydMerchantInfoAuditDao.getAll(ydMerchantInfoAudit);
		return DTOUtils.convertList(dataList, YdMerchantInfoAuditResult.class);
	}

	@Override
	public void insertYdMerchantInfoAudit(YdMerchantInfoAuditResult ydMerchantInfoAuditResult) {
		ydMerchantInfoAuditResult.setCreateTime(new Date());
		ydMerchantInfoAuditResult.setUpdateTime(new Date());
		YdMerchantInfoAudit ydMerchantInfoAudit = new YdMerchantInfoAudit();
		BeanUtilExt.copyProperties(ydMerchantInfoAudit, ydMerchantInfoAuditResult);
		this.ydMerchantInfoAuditDao.insertYdMerchantInfoAudit(ydMerchantInfoAudit);
	}

	public void updateYdMerchantInfoAudit(YdMerchantInfoAuditResult ydMerchantInfoAuditResult) {
		ydMerchantInfoAuditResult.setUpdateTime(new Date());
		YdMerchantInfoAudit ydMerchantInfoAudit = new YdMerchantInfoAudit();
		BeanUtilExt.copyProperties(ydMerchantInfoAudit, ydMerchantInfoAuditResult);
		this.ydMerchantInfoAuditDao.updateYdMerchantInfoAudit(ydMerchantInfoAudit);
	}

}

