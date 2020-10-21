package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantInfoAuditResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:门店信息审核记录表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-05 18:31:13
 * @Version:1.1.0
 */
public interface YdMerchantInfoAuditService {

	/**
	 * 根据审核id查询详情
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantInfoAuditResult getYdMerchantInfoAuditById(Integer id)  throws BusinessException;

	/**
	 * 分页查询门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAuditResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantInfoAuditResult> findYdMerchantInfoAuditListByPage(YdMerchantInfoAuditResult ydMerchantInfoAuditResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 审核商户更新信息
	 * @param id
	 * @param auditStatus	SUCCESS(审核通过), REFUSE(审核拒绝)
	 * @throws BusinessException
	 */
	public void auditMerchantInfo(Integer id, String auditStatus) throws BusinessException;

	/**
	 * 得到所有门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAuditResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantInfoAuditResult> getAll(YdMerchantInfoAuditResult ydMerchantInfoAuditResult);

	/**
	 * 添加门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAuditResult
	 * @Description:
	 */
	public void insertYdMerchantInfoAudit(YdMerchantInfoAuditResult ydMerchantInfoAuditResult) throws BusinessException;
	
}
