package com.yd.api.service.member;

import com.yd.api.result.member.YdMerchantMemberPayRecordResult;
import com.yd.core.enums.YdMerchantMemberApplyTypeEnum;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import java.util.List;

/**
 * @Title:优度商户会员注册，续费，升级支付记录表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:59:25
 * @Version:1.1.0
 */
public interface YdMerchantMemberPayRecordService {

	/**
	 * 通过id得到优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMemberPayRecordResult getYdMerchantMemberPayRecordById(Integer id) throws BusinessException;

	/**
	 * 分页查询优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecordResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantMemberPayRecordResult> findYdMerchantMemberPayRecordListByPage(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecordResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberPayRecordResult> getAll(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) throws BusinessException;

	/**
	 * 添加优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecordResult
	 * @Description:
	 */
	public void insertYdMerchantMemberPayRecord(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) throws BusinessException;

	/**
	 * 通过id修改优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord throws BusinessException;
	 * @param ydMerchantMemberPayRecordResult
	 * @Description:
	 */
	public void updateYdMerchantMemberPayRecord(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) throws BusinessException;

	/**
	 * 保存商户续费记录
	 * @param mobile
	 * @param password
	 * @param smsCode
	 * @param inviteId
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantMemberPayRecordResult addYdMerchantMemberRegisterRecord(String mobile, String password, String smsCode, String channel, Integer inviteId) throws BusinessException;

	/**
	 * 更新注册记录，设置会员等级，金钱
	 * @param id
	 * @param memberLevel
	 * @throws BusinessException
	 */
	YdMerchantMemberPayRecordResult updateYdMerchantMemberRegisterRecord(Integer id, Integer memberLevel)  throws BusinessException;

	/**
	 * 保存商户续费记录
	 * @param merchantId
	 * @param memberLevel
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantMemberPayRecordResult addYdMerchantMemberXfRecord(Integer merchantId, Integer memberLevel) throws BusinessException;

	/**
	 * 保存商户升级记录
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantMemberPayRecordResult addYdMerchantMemberSjRecord(Integer merchantId, Integer memberLevel) throws BusinessException;

	/**
	 * 商户会员支付成功微信支付成功回调
	 * @param outOrderId
	 * @param billNo
	 * @throws BusinessException
	 */
	Integer paySuccessNotify(String outOrderId, String billNo) throws BusinessException;

}
