package com.yd.service.impl.member;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.member.YdMerchantMemberPayRecordResult;
import com.yd.api.result.merchant.YdMerchantChannelResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.member.YdMerchantMemberOpenRecordService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.merchant.YdMerchantChannelService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdMerchantMemberApplyTypeEnum;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.member.YdMemberLevelConfig;
import com.yd.service.bean.member.YdMerchantMemberOpenRecord;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.dao.member.YdMemberLevelConfigDao;
import com.yd.service.dao.member.YdMerchantMemberOpenRecordDao;
import com.yd.service.dao.member.YdMerchantMemberPayRecordDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.bean.member.YdMerchantMemberPayRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:优度商户会员注册，续费，升级支付记录表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:59:25
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantMemberPayRecordServiceImpl implements YdMerchantMemberPayRecordService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantMemberPayRecordServiceImpl.class);

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdSmsCodeService ydSmsCodeService;

	@Resource
	private YdMerchantChannelService ydMerchantChannelService;

	@Resource
	private YdMemberLevelConfigDao ydMemberLevelConfigDao;

	@Resource
	private YdMerchantMemberOpenRecordDao ydMerchantMemberOpenRecordDao;

	@Resource
	private YdMerchantMemberPayRecordDao ydMerchantMemberPayRecordDao;

	@Resource
	private YdMerchantMemberOpenRecordService ydMerchantMemberOpenRecordService;

	@Override
	public YdMerchantMemberPayRecordResult getYdMerchantMemberPayRecordById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = null;
		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = this.ydMerchantMemberPayRecordDao.getYdMerchantMemberPayRecordById(id);
		if (ydMerchantMemberPayRecord != null) {
			ydMerchantMemberPayRecordResult = new YdMerchantMemberPayRecordResult();
			BeanUtilExt.copyProperties(ydMerchantMemberPayRecordResult, ydMerchantMemberPayRecord);
		}	
		return ydMerchantMemberPayRecordResult;
	}

	@Override
	public Page<YdMerchantMemberPayRecordResult> findYdMerchantMemberPayRecordListByPage(YdMerchantMemberPayRecordResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantMemberPayRecordResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
		BeanUtilExt.copyProperties(ydMerchantMemberPayRecord, params);
		
		int amount = this.ydMerchantMemberPayRecordDao.getYdMerchantMemberPayRecordCount(ydMerchantMemberPayRecord);
		if (amount > 0) {
			List<YdMerchantMemberPayRecord> dataList = this.ydMerchantMemberPayRecordDao.findYdMerchantMemberPayRecordListByPage(
				ydMerchantMemberPayRecord, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantMemberPayRecordResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantMemberPayRecordResult> getAll(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) {
		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = null;
		if (ydMerchantMemberPayRecordResult != null) {
			ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
			BeanUtilExt.copyProperties(ydMerchantMemberPayRecord, ydMerchantMemberPayRecordResult);
		}
		List<YdMerchantMemberPayRecord> dataList = this.ydMerchantMemberPayRecordDao.getAll(ydMerchantMemberPayRecord);
		return DTOUtils.convertList(dataList, YdMerchantMemberPayRecordResult.class);
	}

	@Override
	public void insertYdMerchantMemberPayRecord(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) {
		if (null != ydMerchantMemberPayRecordResult) {
			ydMerchantMemberPayRecordResult.setCreateTime(new Date());
			ydMerchantMemberPayRecordResult.setUpdateTime(new Date());
			YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
			BeanUtilExt.copyProperties(ydMerchantMemberPayRecord, ydMerchantMemberPayRecordResult);
			this.ydMerchantMemberPayRecordDao.insertYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		}
	}
	
	@Override
	public void updateYdMerchantMemberPayRecord(YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult) {
		if (null != ydMerchantMemberPayRecordResult) {
			ydMerchantMemberPayRecordResult.setUpdateTime(new Date());
			YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
			BeanUtilExt.copyProperties(ydMerchantMemberPayRecord, ydMerchantMemberPayRecordResult);
			this.ydMerchantMemberPayRecordDao.updateYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		}
	}

	/**
	 * 保存商户注册记录
	 * @param mobile
	 * @param password
	 * @param smsCode	// 注册发送验证码的platform都用  YdLoginUserSourceEnums.YD_SHOP_USER
	 * @param inviteId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantMemberPayRecordResult addYdMerchantMemberRegisterRecord(String mobile, String password, String smsCode, String channel, Integer inviteId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile),
				"err_empty_param", "手机号码不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(password),
				"err_empty_param", "密码不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(smsCode),
				"err_empty_param", "密码不可以为空");

		if (StringUtils.isNotEmpty(channel) && "H5".equalsIgnoreCase(channel) ){
			ValidateBusinessUtils.assertFalse(inviteId == null,
					"err_inviteId_empty", "请通过正确的方式进入注册页面");

			YdMerchantChannelResult ydMerchantChannel = ydMerchantChannelService.getYdMerchantChannelById(inviteId);
			ValidateBusinessUtils.assertFalse(ydMerchantChannel == null,
					"err_inviteId", "邀请人id不存在");
		}

		if (!ydSmsCodeService.getLastSmsCode(mobile, smsCode, YdLoginUserSourceEnums.YD_SHOP_USER.getCode(),
				YdSmsResourceEnum.MERCHANT_REGISTER_MOBILE.getCode())) {
			ValidateBusinessUtils.assertFalse(true, "err_smsCode", "验证码错误");
		}

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(mobile);
		ValidateBusinessUtils.assertFalse(ydMerchant != null,
				"err_mobile_is_exist", "手机号码已经存在");

		Date nowDate = new Date();
		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
		ydMerchantMemberPayRecord.setCreateTime(nowDate);
		ydMerchantMemberPayRecord.setUpdateTime(nowDate);
		ydMerchantMemberPayRecord.setMobile(mobile);
		ydMerchantMemberPayRecord.setPassword(PasswordUtil.encryptPassword(password));
		ydMerchantMemberPayRecord.setInviteId(inviteId);
		ydMerchantMemberPayRecord.setIsPay("N");
		ydMerchantMemberPayRecord.setApplyType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYZC.getCode());
		this.ydMerchantMemberPayRecordDao.insertYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = new YdMerchantMemberPayRecordResult();
		BeanUtilExt.copyProperties(ydMerchantMemberPayRecordResult, ydMerchantMemberPayRecord);
		return ydMerchantMemberPayRecordResult;
	}

	/**
	 * 更新注册记录，设置会员等级，金钱
	 * @param id
	 * @param memberLevel
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantMemberPayRecordResult updateYdMerchantMemberRegisterRecord(Integer id, Integer memberLevel) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "非法的id");

		ValidateBusinessUtils.assertFalse(memberLevel == null || memberLevel <= 0,
				"err_member_level", "非法的会员等级");

		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = this.ydMerchantMemberPayRecordDao.getYdMerchantMemberPayRecordById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantMemberPayRecord == null,
				"err_record_id_not_exist", "id不存在，请通过正确的流程进入页面");

		ValidateBusinessUtils.assertFalse(("Y".equalsIgnoreCase(ydMerchantMemberPayRecord.getIsPay())),
				"err_record_id_not_exist", "已经支付成功，请刷新重试或者联系管理员处理");

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_member_level_not_exist", "会员等级不存在,请通过正确的流程选择");

		ydMerchantMemberPayRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberPayRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberPayRecord.setMemberPrice(memberLevelConfig.getMemberPrice());
		ydMerchantMemberPayRecord.setValidLength(memberLevelConfig.getValidLength());
		ydMerchantMemberPayRecord.setRoleId(memberLevelConfig.getRoleId());
		this.ydMerchantMemberPayRecordDao.updateYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = new YdMerchantMemberPayRecordResult();
		BeanUtilExt.copyProperties(ydMerchantMemberPayRecordResult, ydMerchantMemberPayRecord);
		return ydMerchantMemberPayRecordResult;
	}

	/**
	 * 保存商户续费记录
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantMemberPayRecordResult addYdMerchantMemberXfRecord(Integer merchantId, Integer memberLevel) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(storeInfo.getMemberLevel() == null || storeInfo.getMemberLevel() <= 0,
				"err_empty_merchant", "商户尚未开通会员");

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_empty_merchant", "请选择正确的会员等级");

		Date nowDate = new Date();

		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
		ydMerchantMemberPayRecord.setCreateTime(nowDate);
		ydMerchantMemberPayRecord.setUpdateTime(nowDate);
		ydMerchantMemberPayRecord.setMobile(storeInfo.getMobile());
		ydMerchantMemberPayRecord.setMerchantId(merchantId);

		ydMerchantMemberPayRecord.setMemberPrice(memberLevelConfig.getMemberPrice());
		ydMerchantMemberPayRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberPayRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberPayRecord.setRoleId(memberLevelConfig.getRoleId());
		ydMerchantMemberPayRecord.setValidLength(memberLevelConfig.getValidLength());

		ydMerchantMemberPayRecord.setIsPay("N");
		ydMerchantMemberPayRecord.setBillNo(null);
		ydMerchantMemberPayRecord.setApplyType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYXF.getCode());

		this.ydMerchantMemberPayRecordDao.insertYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = new YdMerchantMemberPayRecordResult();
		BeanUtilExt.copyProperties(ydMerchantMemberPayRecordResult, ydMerchantMemberPayRecord);
		return ydMerchantMemberPayRecordResult;
	}

	/**
	 * 保存商户升级记录
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantMemberPayRecordResult addYdMerchantMemberSjRecord(Integer merchantId, Integer memberLevel) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_member_level_not_exist", "会员等级不存在");

		ValidateBusinessUtils.assertFalse(storeInfo.getMemberLevel() >= memberLevel,
				"err_member_level", "不能选择低等级升级");

		YdMemberLevelConfig nowLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(storeInfo.getMemberLevel());

		Date nowDate = new Date();
		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = new YdMerchantMemberPayRecord();
		ydMerchantMemberPayRecord.setCreateTime(nowDate);
		ydMerchantMemberPayRecord.setUpdateTime(nowDate);
		ydMerchantMemberPayRecord.setMobile(storeInfo.getMobile());
		ydMerchantMemberPayRecord.setMerchantId(merchantId);

		ydMerchantMemberPayRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberPayRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberPayRecord.setRoleId(memberLevelConfig.getRoleId());
		ydMerchantMemberPayRecord.setMemberPrice(memberLevelConfig.getMemberPrice() - nowLevelConfig.getMemberPrice());
		ydMerchantMemberPayRecord.setValidLength(memberLevelConfig.getValidLength());

		ydMerchantMemberPayRecord.setIsPay("N");
		ydMerchantMemberPayRecord.setBillNo(null);
		ydMerchantMemberPayRecord.setApplyType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYSJ.getCode());

		this.ydMerchantMemberPayRecordDao.insertYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);
		YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = new YdMerchantMemberPayRecordResult();
		BeanUtilExt.copyProperties(ydMerchantMemberPayRecordResult, ydMerchantMemberPayRecord);
		return ydMerchantMemberPayRecordResult;
	}

	/**
	 * 商户会员支付成功微信支付成功回调
	 * @param outOrderId
	 * @throws BusinessException
	 */
	@Override
	public Integer paySuccessNotify(String outOrderId, String billNo) throws BusinessException {
		logger.info("======进入商户注册支付成功回调service接口outOrderId=" + outOrderId);
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(outOrderId),
				"err_member_level_not_exist", "对外订单号不可以为空");
		ValidateBusinessUtils.assertFalse(!outOrderId.startsWith(SystemPrefixConstants.YD_MERCHANT_MEMBER_PAY_PREFIX),
				"err_notify_order_prefix", "错误格式的订单号" + outOrderId);

		String[] split = outOrderId.split(SystemPrefixConstants.YD_MERCHANT_MEMBER_PAY_PREFIX);
		logger.info("====商户注册支付成功回调outOrderId=" + outOrderId + "截取后的值=" + JSON.toJSONString(split));
		Integer id = Integer.valueOf(split[split.length - 1]);

		YdMerchantMemberPayRecord ydMerchantMemberPayRecord = this.ydMerchantMemberPayRecordDao.getYdMerchantMemberPayRecordById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantMemberPayRecord == null,
				"err_notify_order_id", "订单记录不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantMemberPayRecord.getIsPay()),
				"err_already_success", "已经成功处理过");

		// 1. 修改支付成功
		Date nowDate = new Date();
		ydMerchantMemberPayRecord.setUpdateTime(nowDate);
		ydMerchantMemberPayRecord.setIsPay("Y");
		ydMerchantMemberPayRecord.setBillNo(billNo);
		this.ydMerchantMemberPayRecordDao.updateYdMerchantMemberPayRecord(ydMerchantMemberPayRecord);

		// 2. 根据操作类型去处理商户
		Double payPrice = null;
		Date startTime = null, endTime = null;
		YdMerchant ydMerchant = null;
		String openType = null;
		String applyType = ydMerchantMemberPayRecord.getApplyType();
		if (YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYZC.getCode().equalsIgnoreCase(applyType)) {
			ydMerchant = new YdMerchant();
			ydMerchant.setCreateTime(nowDate);
			ydMerchant.setUpdateTime(nowDate);
			ydMerchant.setIsFlag("N");
			ydMerchant.setMobile(ydMerchantMemberPayRecord.getMobile());
			ydMerchant.setMerchantName(ydMerchantMemberPayRecord.getMobile());
			ydMerchant.setPassword(ydMerchantMemberPayRecord.getPassword());

			ydMerchant.setMemberType(ydMerchantMemberPayRecord.getMemberType());
			ydMerchant.setMemberLevel(ydMerchantMemberPayRecord.getMemberLevel());
			ydMerchant.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
			ydMerchant.setRoleIds(ydMerchantMemberPayRecord.getRoleId() + "");

			payPrice = ydMerchantMemberPayRecord.getMemberPrice();
			startTime = nowDate;
			endTime = DateUtils.addMonths(nowDate, ydMerchantMemberPayRecord.getValidLength());
			ydMerchant.setMemberValidTime(endTime);
			ydMerchant.setInviteId(ydMerchantMemberPayRecord.getInviteId());
			ydMerchantDao.insertYdMerchant(ydMerchant);

			ydMerchant.setPid(ydMerchant.getId());
			ydMerchantDao.updateYdMerchant(ydMerchant);

			openType = YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYZC.getDesc();
		} else if (YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYXF.getCode().equalsIgnoreCase(applyType)){
			ydMerchant = ydMerchantDao.getYdMerchantById(ydMerchantMemberPayRecord.getMerchantId());
			payPrice = ydMerchantMemberPayRecord.getMemberPrice();
			startTime = ydMerchant.getMemberValidTime();
			endTime = DateUtils.addMonths(ydMerchant.getMemberValidTime(), ydMerchantMemberPayRecord.getValidLength());
			ydMerchant.setMemberValidTime(endTime);
			ydMerchantDao.updateYdMerchant(ydMerchant);
			openType = YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYXF.getDesc();
		}  else if (YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYSJ.getCode().equalsIgnoreCase(applyType)){
			ydMerchant = ydMerchantDao.getYdMerchantById(ydMerchantMemberPayRecord.getMerchantId());
			ydMerchant.setRoleIds(ydMerchantMemberPayRecord.getRoleId() + "");

			payPrice = getSjPayPrice();
			startTime = nowDate;
			// 获取会员升级前最近一次的结束时间
			YdMerchantMemberOpenRecord firstValidTime = ydMerchantMemberOpenRecordDao.getFirstValidEndTime(ydMerchant.getId());
			endTime = firstValidTime.getEndTime();
			ydMerchantDao.updateYdMerchant(ydMerchant);
			openType = YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYSJ.getDesc();
		} else {
			throw new BusinessException("err_member_level", "回调类型不正确");
		}

		// 3. 根据操作类型去处理商户类型，增加充值记录
		ydMerchantMemberOpenRecordService.addYdMerchantMemberOpenRecord(ydMerchant.getId(), ydMerchantMemberPayRecord.getRoleId(),
				ydMerchantMemberPayRecord.getMemberLevel(), ydMerchantMemberPayRecord.getMemberType(), ydMerchantMemberPayRecord.getValidLength(),
				startTime, endTime, payPrice, openType, YdMerchantMemberApplyTypeEnum.MEMBER_APPLY_METHOD_SDKT.getDesc());

		return ydMerchant.getId();
	}

	private Double getSjPayPrice() {
		YdMemberLevelConfig level2 = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(2);
		YdMemberLevelConfig level1 = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(1);
		return level2.getMemberPrice() - level1.getMemberPrice();
	}

}

