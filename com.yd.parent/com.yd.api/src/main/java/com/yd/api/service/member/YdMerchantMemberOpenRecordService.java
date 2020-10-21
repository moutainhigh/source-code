package com.yd.api.service.member;

import com.yd.api.result.member.YdMerchantMemberOpenRecordResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.Date;
import java.util.List;

/**
 * @Title:优度商户会员开通记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:58:04
 * @Version:1.1.0
 */
public interface YdMerchantMemberOpenRecordService {

	/**
	 * 通过id得到优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMemberOpenRecordResult getYdMerchantMemberOpenRecordById(Integer id) throws BusinessException;

	/**
	 * 分页查询优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecordResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantMemberOpenRecordResult> findYdMerchantMemberOpenRecordListByPage(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecordResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberOpenRecordResult> getAll(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) throws BusinessException;

	/**
	 * 添加优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecordResult
	 * @Description:
	 */
	public void insertYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) throws BusinessException;
	
	/**
	 * 通过id修改优度商户会员开通记录YdMerchantMemberOpenRecord throws BusinessException;
	 * @param ydMerchantMemberOpenRecordResult
	 * @Description:
	 */
	public void updateYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecordResult ydMerchantMemberOpenRecordResult) throws BusinessException;

	/**
	 * 添加商户会员记录
	 * @param merchantId	商户id
	 * @param roleId		角色
	 * @param memberLevel	会员等级
	 * @param memberType	会员类型 (升级版，普通版)
	 * @param validLength	有效时长(月为单位)
	 * @param startTime		会员有效开始日期
	 * @param endTime		会员有效结束日期
	 * @param payPrice		支付金额
	 * @param openType		开通类型: SJ(会员升级), XF(会员续费),ZC(会员开通,注册)
	 * @param openMethod	开通方式 (自动开通, 人工开通)
	 * @throws BusinessException
	 */
	void addYdMerchantMemberOpenRecord(Integer merchantId, Integer roleId, Integer memberLevel, String memberType, Integer validLength, Date startTime,
									   Date endTime, Double payPrice, String openType, String openMethod) throws BusinessException;


}
