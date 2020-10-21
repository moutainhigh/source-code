package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantPayAuditResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户支付申请管理Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-25 11:17:40
 * @Version:1.1.0
 */
public interface YdMerchantPayAuditService {

	/**
	 * 通过id得到商户支付申请管理YdMerchantPayAudit
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantPayAuditResult getYdMerchantPayAuditById(Integer id);

	/**
	 * 分页查询商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAuditResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantPayAuditResult> findYdMerchantPayAuditListByPage(YdMerchantPayAuditResult ydMerchantPayAuditResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAuditResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantPayAuditResult> getAll(YdMerchantPayAuditResult ydMerchantPayAuditResult);

	/**
	 * 添加商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAuditResult
	 * @Description:
	 */
	public void insertYdMerchantPayAudit(YdMerchantPayAuditResult ydMerchantPayAuditResult) throws BusinessException;
	
	/**
	 * 通过id修改商户支付申请管理YdMerchantPayAudit throws BusinessException;
	 * @param ydMerchantPayAuditResult
	 * @Description:
	 */
	public void updateYdMerchantPayAudit(YdMerchantPayAuditResult ydMerchantPayAuditResult)throws BusinessException;

	/**
	 * 商户支付审核
	 * @param id
	 * @param auditStatus
	 */
	public void payAudit(Integer id, String auditStatus) throws BusinessException;
	
}
