package com.yd.service.impl.merchant;

import java.util.*;

import javax.annotation.Resource;

import com.yd.api.service.decoration.YdVrManagerService;
import com.yd.api.service.member.YdMerchantMemberOpenRecordService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdMerchantMemberApplyTypeEnum;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.*;
import com.yd.service.bean.decoration.YdVrManager;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.member.YdMemberLevelConfig;
import com.yd.service.bean.member.YdMerchantMemberOpenRecord;
import com.yd.service.bean.merchant.YdMerchantInfoAudit;
import com.yd.service.bean.merchant.YdMerchantPayAudit;
import com.yd.service.bean.sys.SysRegion;
import com.yd.service.dao.decoration.YdVrManagerDao;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.member.YdMemberLevelConfigDao;
import com.yd.service.dao.member.YdMerchantMemberOpenRecordDao;
import com.yd.service.dao.merchant.YdMerchantInfoAuditDao;
import com.yd.service.dao.merchant.YdMerchantPayAuditDao;
import com.yd.service.dao.sys.SysRegionDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.permission.YdRole;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.permission.YdRoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:优度后台商户Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:06
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantServiceImpl implements YdMerchantService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdRoleDao	ydRoleDao;

	@Resource
	private SysRegionDao sysRegionDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdVrManagerDao ydVrManagerDao;

	@Resource
	private YdSmsCodeService ydSmsCodeService;

	@Resource
	private YdMerchantPayAuditDao ydMerchantPayAuditDao;

	@Resource
	private YdMerchantInfoAuditDao ydMerchantInfoAuditDao;

	@Resource
	private YdMemberLevelConfigDao ydMemberLevelConfigDao;

	@Resource
	private YdMerchantMemberOpenRecordDao ydMerchantMemberOpenRecordDao;

	@Resource
	private YdMerchantMemberOpenRecordService ydMerchantMemberOpenRecordService;

	@Override
	public YdMerchantResult getYdMerchantById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantResult ydMerchantResult = null;
		YdMerchant ydMerchant = this.ydMerchantDao.getYdMerchantById(id);
		if (ydMerchant != null) {
			ydMerchantResult = new YdMerchantResult();
			BeanUtilExt.copyProperties(ydMerchantResult, ydMerchant);
			// 查询商户对应的角色
			YdRole param= new YdRole();
			param.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
			List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
			Map<String, String> roleNameMap = getRoleNameMap(roleList);
			String roleNames = getRoleNames(ydMerchantResult.getRoleIds(), roleNameMap);
			ydMerchantResult.setRoleNames(roleNames);
			ydMerchantResult.setPassword(null);
		}
		//清空密码和支付密码
		if(StringUtils.isNotEmpty(ydMerchantResult.getPayPassword())) {
			ydMerchantResult.setHasSetPayPassword("Y");
			ydMerchantResult.setPayPassword(null);
		}

		// 查询设置vr是否开启，开启地址
		YdVrManager ydVrManager = ydVrManagerDao.getYdVrManagerByMerchantId(id);
		if (ydVrManager == null) {
			ydMerchantResult.setIsOpenVr("N");
		} else {
			ydMerchantResult.setIsOpenVr("Y");
			ydMerchantResult.setVrJumpUrl(ydVrManager.getJumpUrl());
		}
		return ydMerchantResult;
	}

	@Override
	public List<YdMerchantResult> findYdMerchantListByPid(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		List<YdMerchant> dataList = ydMerchantDao.findYdMerchantListByPid(id);
		return DTOUtils.convertList(dataList, YdMerchantResult.class);
	}

	/**
	 * 查询门店和供应商
	 * @param ydMerchantResult
	 * @param pageInfo
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Page<YdMerchantResult> findPlatformMerchantList(YdMerchantResult ydMerchantResult, PagerInfo pageInfo) throws BusinessException {
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);

		Page<YdMerchantResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
		int amount = this.ydMerchantDao.getPlatformMerchantCount(ydMerchant);
		if (amount > 0) {
			YdRole param = new YdRole();
			List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
			Map<String, String> roleNameMap = getRoleNameMap(roleList);

			// 设置商户的角色名称
			List<YdMerchant> dataList = ydMerchantDao.findPlatformMerchantList(ydMerchant, pageInfo.getStart(), pageInfo.getPageSize());
			List<YdMerchantResult> resultList = DTOUtils.convertList(dataList, YdMerchantResult.class);
			resultList.forEach(merchantResult -> {
				String roleNames = getRoleNames(merchantResult.getRoleIds(), roleNameMap);
				merchantResult.setRoleNames(roleNames);
				merchantResult.setPayPassword(null);
				merchantResult.setPassword(null);
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public Page<YdMerchantResult> findYdMerchantListByPage(YdMerchantResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, params);

		int amount = this.ydMerchantDao.getYdMerchantCount(ydMerchant);
		if (amount > 0) {
			// 查询商户对应的角色
			YdRole param= new YdRole();
			List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
			Map<String, String> roleNameMap = getRoleNameMap(roleList);

			// 设置商户的角色名称
			List<YdMerchant> dataList = ydMerchantDao.findYdMerchantListByPage(ydMerchant, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantResult> resultList = DTOUtils.convertList(dataList, YdMerchantResult.class);
			resultList.forEach(ydMerchantResult -> {
				String roleNames = getRoleNames(ydMerchantResult.getRoleIds(), roleNameMap);
				ydMerchantResult.setRoleNames(roleNames);
				ydMerchantResult.setPayPassword(null);
				ydMerchantResult.setPassword(null);
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantResult> getAll(YdMerchantResult ydMerchantResult) {
		YdMerchant ydMerchant = null;
		if (ydMerchantResult != null) {
			ydMerchant = new YdMerchant();
			BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);
		}
		List<YdMerchant> dataList = this.ydMerchantDao.getAll(ydMerchant);
		List<YdMerchantResult> list= DTOUtils.convertList(dataList, YdMerchantResult.class);
		if(list.size()>0) {
			for(YdMerchantResult item:list) {
				item.setPassword(null);
				item.setPayPassword(null);
			}
		}
		
		return list;
	}

	@Override
	public void insertYdMerchant(YdMerchantResult ydMerchantResult, EnumSiteGroup siteGroup) {
        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMobile()),
                "err_empty_mobile", "手机号不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMerchantName()),
                "err_empty_merchant_name", "商户名称不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getPassword()),
                "err_empty_password", "密码不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(ydMerchantResult.getMobile());
		ValidateBusinessUtils.assertFalse(ydMerchant != null,
				"err_exist_mobile", "手机号码已经被使用");

		ValidateBusinessUtils.assertFalse(ydMerchantResult.getPid() == null,
				"err_pid_empty", "pid不可以为空");

		YdMerchant storeInfo = ydMerchantDao.getYdMerchantById(ydMerchantResult.getPid());

		List<YdMerchant> operateList = ydMerchantDao.findOperateList(ydMerchantResult.getPid(), 0, 100);
		if ((YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "").equals(storeInfo.getRoleIds())) {
			ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(operateList) && operateList.size() >= 5,
					"err_exist_mobile", "普通版商户最多只能添加5个操作员");
		} else {
			throw new BusinessException("err_role_id", "商户角色不正确，不可以添加操作员");
		}

		ydMerchant = new YdMerchant();
        BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);
		ydMerchant.setCreateTime(new Date());
        ydMerchant.setGroupCode(siteGroup.getCode());
        ydMerchant.setPassword(PasswordUtil.encryptPassword(ydMerchant.getPassword()));
        this.ydMerchantDao.insertYdMerchant(ydMerchant);
	}

	@Override
	public void updateYdMerchant(YdMerchantResult ydMerchantResult, EnumSiteGroup siteGroup) {
        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMobile()),
                "err_empty_mobile", "手机号不可以为空");

        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMerchantName()),
                "err_empty_merchant_name", "商户名称不可以为空");

//        ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getPassword()),
//                "err_empty_password", "密码不可以为空");

        ValidateBusinessUtils.assertFalse(ydMerchantResult.getId() == null,
                "err_empty_id", "id不可以为空");
        YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydMerchantResult.getId());
        ValidateBusinessUtils.assertFalse(ydMerchant == null,
                "err_empty_id", "数据不存在");

        ValidateBusinessUtils.assertFalse(!siteGroup.getCode().equals(ydMerchant.getGroupCode()),
                "err_group_code", "编辑的数据groupCode异常");

        YdMerchant merchant = ydMerchantDao.getYdMerchantByMobile(ydMerchantResult.getMobile());
        ValidateBusinessUtils.assertFalse(merchant != null && !merchant.getId().equals(ydMerchantResult.getId()),
                "err_exist_mobile", "手机号码已经被使用");

        ydMerchant = new YdMerchant();
		ydMerchant.setId(ydMerchantResult.getId());
        ydMerchant.setMobile(ydMerchantResult.getMobile());
        ydMerchant.setRoleIds(ydMerchantResult.getRoleIds());
        ydMerchant.setMerchantName(ydMerchantResult.getMerchantName());
        if (StringUtils.isNotEmpty(ydMerchantResult.getPassword())) {
            ydMerchant.setPassword(PasswordUtil.encryptPassword(ydMerchantResult.getPassword()));
        }
        this.ydMerchantDao.updateYdMerchant(ydMerchant);
	}

    /**
     * 查询账户信息
     * @param pagerInfo
     * @param enumSiteGroup sys(系统账户) | merchant(商户账户)
     * @return Page<YdMerchantResult>
     */
    @Override
    public Page<YdMerchantResult> findMerchantList(PagerInfo pagerInfo, EnumSiteGroup enumSiteGroup) {
        Page<YdMerchantResult> pageResult = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());

        // 根据组别查询商户
        YdMerchant ydMerchant = new YdMerchant();
		ydMerchant.setIsFlag("N");
        ydMerchant.setGroupCode(enumSiteGroup.getCode());
        int amount = ydMerchantDao.getYdMerchantCount(ydMerchant);
        if (amount > 0) {
            // 查询商户对应的角色
            YdRole param= new YdRole();
            param.setGroupCode(enumSiteGroup.getCode());
            List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
            Map<String, String> roleNameMap = getRoleNameMap(roleList);

            // 设置商户的角色名称
            List<YdMerchant> dataList = ydMerchantDao.findYdMerchantListByPage(ydMerchant, pagerInfo.getStart(), pagerInfo.getPageSize());
            List<YdMerchantResult> resultList = DTOUtils.convertList(dataList, YdMerchantResult.class);
            resultList.forEach(ydMerchantResult -> {
                String roleNames = getRoleNames(ydMerchantResult.getRoleIds(), roleNameMap);
				ydMerchantResult.setRoleNames(roleNames);
				ydMerchantResult.setPassword(null);
				ydMerchantResult.setPayPassword(null);
            });
            pageResult.setData(resultList);
        }
        pageResult.setTotalRecord(amount);
        return pageResult;
    }

	/**
	 * 查询门店下操作员
	 * @param pagerInfo
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Page<YdMerchantResult> findOperateList(PagerInfo pagerInfo, Integer merchantId) throws BusinessException {
		// 校验当前商户id是否为操作员
		YdMerchantResult ydMerchantResult = getStoreInfo(merchantId);
		ValidateBusinessUtils.assertFalse(!ydMerchantResult.getId().equals(merchantId) ,
				"err_not_exist_merchant", "操作员暂无此权限");

		// 查询操作员
		Page<YdMerchantResult> pageResult = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = ydMerchantDao.getOperateCount(merchantId);
		if (amount > 0) {

			// 查询商户对应的角色
			YdRole param= new YdRole();
			param.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
			List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
			Map<String, String> roleNameMap = getRoleNameMap(roleList);

			// 设置商户的角色名称
			List<YdMerchant> operateList = ydMerchantDao.findOperateList(merchantId, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantResult> resultList = DTOUtils.convertList(operateList, YdMerchantResult.class);
			resultList.forEach(merchantResult -> {
				String roleNames = getRoleNames(ydMerchantResult.getRoleIds(), roleNameMap);
				merchantResult.setRoleNames(roleNames);
				merchantResult.setPassword(null);
				merchantResult.setPayPassword(null);
			});
			pageResult.setData(resultList);
		}
		pageResult.setTotalRecord(amount);
		return pageResult;
	}

	/**
     * 门店信息更改， 增加审核记录
     * @param ydMerchantResult
     * @throws BusinessException
     */
    @Override
    public void updateMerchantInfo(YdMerchantResult ydMerchantResult) throws BusinessException {
    	// 校验入参
    	checkUpdateMerchantInfoParams(ydMerchantResult);

        // 查询门店信息是否正在审核中
		YdMerchantInfoAudit ydMerchantInfoAudit = new YdMerchantInfoAudit();
		ydMerchantInfoAudit.setMerchantId(ydMerchantResult.getId());
		ydMerchantInfoAudit.setAuditStatus("WAIT");
		List<YdMerchantInfoAudit> dataList = ydMerchantInfoAuditDao.getAll(ydMerchantInfoAudit);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isNotEmpty(dataList),
				"err_exist_audit", "正在审核中，请审核通过后在进行编辑");

		ydMerchantInfoAudit = new YdMerchantInfoAudit();
		BeanUtilExt.copyProperties(ydMerchantInfoAudit, ydMerchantResult);
		ydMerchantInfoAudit.setId(null);
		ydMerchantInfoAudit.setCreateTime(new Date());
		ydMerchantInfoAudit.setAuditStatus("WAIT");
		ydMerchantInfoAudit.setMerchantId(ydMerchantResult.getId());
		ydMerchantInfoAudit.setProtocolUrl(ydMerchantResult.getProtocolUrl());
		ydMerchantInfoAuditDao.insertYdMerchantInfoAudit(ydMerchantInfoAudit);
    }

	/**
	 * 更换手机号发送验证码
	 * @param merchantId
	 */
	@Override
	public void updateMerchantMobileSendSms(Integer merchantId) throws BusinessException {
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");
		ydSmsCodeService.sendSmsCode(ydMerchant.getMobile(), YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
				YdSmsResourceEnum.MERCHANT_UPDATE_MOBILE);
	}

	/**
	 * 校验更新手机号发送的验证码
	 * @param merchantId
	 * @param smsCode
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public boolean checkUpdateMerchantMobileSmsCode(Integer merchantId, String smsCode) throws BusinessException {
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");

		return ydSmsCodeService.getLastSmsCode(ydMerchant.getMobile(), smsCode, YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
				YdSmsResourceEnum.MERCHANT_UPDATE_MOBILE.getCode());
	}

	/**
	 * 开通线上支付
	 * @param merchantId
	 */
	@Override
	public void openPay(Integer merchantId) throws BusinessException {
		YdMerchantResult storeInfo = getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantPayAudit ydMerchantPayAudit = this.ydMerchantPayAuditDao.getYdPayAuditByMerchantId(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchantPayAudit != null,
				"err_exist", "您已经申请过");

		storeInfo.setIsOpenPay("N");
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, storeInfo);
		ydMerchantDao.updateYdMerchant(ydMerchant);

		// 保存支付申请记录
		ydMerchantPayAudit = new YdMerchantPayAudit();
		ydMerchantPayAudit.setCreateTime(new Date());
		ydMerchantPayAudit.setAuditStatus("WAIT");
		ydMerchantPayAudit.setMerchantId(merchantId);
		this.ydMerchantPayAuditDao.insertYdMerchantPayAudit(ydMerchantPayAudit);
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	/**
	 * 设置开通比价功能
	 * @param merchantId
	 * @param type
	 * @throws BusinessException
	 */
	@Override
	public void setComparePrice(Integer merchantId, String type) throws BusinessException {
		YdMerchantResult storeInfo = getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(type) ||
						!(type.equalsIgnoreCase("Y") || type.equalsIgnoreCase("N")),
				"err_type", "请传入正确的参数");

		storeInfo.setIsComparePrice(type);
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, storeInfo);
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	@Override
	public void deleteMerchant(Integer merchantId) throws BusinessException {
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");
		ydMerchant.setIsFlag("Y");
		ydMerchantDao.updateYdMerchant(ydMerchant);

		// 如果是供应商的话，将供应商礼品删除, 将商户导入的供应商礼品下架(删除)
		if (ydMerchant.getGroupCode().equalsIgnoreCase(EnumSiteGroup.SUPPLIER.getCode())) {
			YdGift ydGift = new YdGift();
			ydGift.setIsFlag("Y");
			ydGift.setIsEnable("N");
			ydGift.setSupplierId(ydMerchant.getId());
			ydGiftDao.updateYdGift(ydGift);

			YdMerchantGift ydMerchantGift = new YdMerchantGift();
			ydMerchantGift.setMerchantId(ydMerchant.getId());
			ydMerchantGift.setIsEnable("N");
			ydMerchantGift.setIsFlag("Y");
			ydMerchantGiftDao.updateYdMerchantGift(ydMerchantGift);
		}
	}

	@Override
	public YdMerchantResult platformInsertMerchant(YdMerchantResult ydMerchantResult) {
		checkUpdateMerchantInfoParams(ydMerchantResult);
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(ydMerchantResult.getMobile());
		ValidateBusinessUtils.assertFalse(ydMerchant != null,
				"err_exist_mobile", "手机号码已经被使用");

		Date nowDate = new Date();
		boolean isAddMemberRecord = false;
		YdMemberLevelConfig memberLevelConfig = null;
		if (ydMerchantResult.getGroupCode().equalsIgnoreCase(EnumSiteGroup.SUPPLIER.getCode())) {
			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMerchantResult.getMemberValidTimeStr()),
					"err_empty_valid_time", "有效时间不可以为空");
			ydMerchantResult.setMemberValidTime(DateUtils.getDateFromStr(ydMerchantResult.getMemberValidTimeStr()));
			ydMerchantResult.setRoleIds(YdRoleTypeEnum.ROLE_SUPPLIER.getCode() + "");
		} else if (ydMerchantResult.getGroupCode().equalsIgnoreCase(EnumSiteGroup.MERCHANT.getCode())) {
			ValidateBusinessUtils.assertFalse(ydMerchantResult.getMemberLevel() == null,
					"err_empty_member_level", "会员类型不可以为空");

			memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(ydMerchantResult.getMemberLevel());
			ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
					"err_group_code", "请选择正确的会员类型");

			isAddMemberRecord = true;
			ydMerchantResult.setMemberType(memberLevelConfig.getMemberType());
			ydMerchantResult.setRoleIds(memberLevelConfig.getRoleId() + "");
			ydMerchantResult.setMemberValidTime(DateUtils.addMonths(nowDate, memberLevelConfig.getValidLength()));
		} else {
			throw new BusinessException("err_group_code", "请选择正确的商户类型");
		}

		ydMerchant = new YdMerchant();
		ydMerchantResult.setIsFlag("N");
		ydMerchantResult.setCreateTime(nowDate);
		BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);
		this.ydMerchantDao.insertYdMerchant(ydMerchant);

		ydMerchant.setPid(ydMerchant.getId());
		this.ydMerchantDao.updateYdMerchant(ydMerchant);

		// 添加会员开通记录
		if (isAddMemberRecord) {
			Date inValidTime = DateUtils.addMonths(nowDate, memberLevelConfig.getValidLength());
			ydMerchantMemberOpenRecordService.addYdMerchantMemberOpenRecord(ydMerchant.getId(), memberLevelConfig.getRoleId(),
					memberLevelConfig.getMemberLevel(), memberLevelConfig.getMemberType(), memberLevelConfig.getValidLength(), nowDate,
					inValidTime, memberLevelConfig.getMemberPrice(),
					YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYZC.getDesc(),
					YdMerchantMemberApplyTypeEnum.MEMBER_APPLY_METHOD_ZDKT.getDesc());
		}

		BeanUtilExt.copyProperties(ydMerchantResult, ydMerchant);
		return ydMerchantResult;
	}

	@Override
	public YdMerchantResult platformUpdateMerchant(YdMerchantResult ydMerchantResult) {
		ValidateBusinessUtils.assertFalse(ydMerchantResult.getId() == null || ydMerchantResult.getId() <= 0,
				"err_empty_id", "商户id不可以为空");

		if (EnumSiteGroup.SUPPLIER.getCode().equalsIgnoreCase(ydMerchantResult.getGroupCode())) {
			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMerchantResult.getMemberValidTimeStr()),
					"err_empty_valid_time", "有效时间不可以为空");
			ydMerchantResult.setMemberValidTime(DateUtils.getDateFromStr(ydMerchantResult.getMemberValidTimeStr()));
			ydMerchantResult.setRoleIds(YdRoleTypeEnum.ROLE_SUPPLIER.getCode() + "");
		}

		checkUpdateMerchantInfoParams(ydMerchantResult);
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantByMobile(ydMerchantResult.getMobile());
		ValidateBusinessUtils.assertFalse(ydMerchant != null && !ydMerchant.getId().equals(ydMerchantResult.getId()),
				"err_exist_mobile", "手机号码已经被使用");
		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchant.getIsFlag()),
				"err_is_flag", "商户已经被删除");
		if ((YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "").equalsIgnoreCase(ydMerchant.getRoleIds())) {
			ydMerchantResult.setRoleIds(ydMerchant.getRoleIds());
			ydMerchantResult.setGroupCode(ydMerchant.getGroupCode());
			ydMerchantResult.setMemberLevel(ydMerchant.getMemberLevel());
			ydMerchantResult.setMemberType(ydMerchant.getMemberType());
			ydMerchantResult.setMemberValidTime(ydMerchant.getMemberValidTime());
		}

		ydMerchant = new YdMerchant();
		ydMerchantResult.setUpdateTime(new Date());
		BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);
		this.ydMerchantDao.updateYdMerchant(ydMerchant);
		return ydMerchantResult;
	}

	/**
	 * 校验商户是否存在
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantResult checkMerchantInfo(Integer merchantId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(merchantId, "err_empty_merchant_id", "商户id不可以为空");
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertNonNull(ydMerchant, "err_not_exist_merchant", "商户不存在");

		YdMerchantResult ydMerchantResult = new YdMerchantResult();
		BeanUtilExt.copyProperties(ydMerchantResult, ydMerchant);
		return ydMerchantResult;
	}

	/**
	 * 根据门店id或者操作员id，获取门店信息(紧紧对门店和操作员有效)
	 * @param merchantId
	 * @return 门店信息
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantResult getStoreInfo(Integer merchantId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(merchantId, "err_empty_merchant_id", "商户id不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertNonNull(ydMerchant,"err_not_exist_merchant", "商户不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchant.getIsFlag()),
				"err_is_flag_merchant", "商户已被删除");

		ValidateBusinessUtils.assertFalse(!EnumSiteGroup.MERCHANT.getCode().equalsIgnoreCase(ydMerchant.getGroupCode()),
				"err_merchant", "非法的商户，请联系管理员进行处理");

		YdMerchantResult ydMerchantResult = new YdMerchantResult();
		// id == pid 为门店，否则为操作员
		if (ydMerchant.getPid() == null || ydMerchant.getPid().equals(ydMerchant.getId())) {
			BeanUtilExt.copyProperties(ydMerchantResult, ydMerchant);
		} else {
			YdMerchant storeInfo = ydMerchantDao.getYdMerchantById(ydMerchant.getPid());
			ValidateBusinessUtils.assertFalse(storeInfo == null,
					"err_not_exist_merchant", "门店不存在，请联系管理员进行处理");
			BeanUtilExt.copyProperties(ydMerchantResult, storeInfo);
		}
		return ydMerchantResult;
	}

	/**application-dev.yml
	 * 门店删除操作员
	 * @param storeId	门店id
	 * @param operateId	操作员id
	 * @throws BusinessException
	 */
	@Override
	public void deleteStoreOperate(Integer storeId, Integer operateId) throws BusinessException {

		ValidateBusinessUtils.assertFalse(storeId == null || storeId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(operateId == null || operateId <= 0,
				"err_empty_operate_id", "操作员id不可以为空");

		YdMerchant storeInfo = ydMerchantDao.getYdMerchantById(storeId);
		ValidateBusinessUtils.assertFalse(storeInfo == null,
				"err_not_exist_store", "所属门店不存在");

		YdMerchant operateInfo = ydMerchantDao.getYdMerchantById(operateId);
		ValidateBusinessUtils.assertFalse(operateInfo == null,
				"err_not_exist_operate", "操作员不存在");

		ValidateBusinessUtils.assertFalse(operateInfo.getId().equals(storeInfo.getId()),
				"err_delete_merchant", "门店不可以删除");

		ValidateBusinessUtils.assertFalse(!operateInfo.getPid().equals(storeInfo.getId()),
				"err_merchant", "门店暂无此操作员");

		operateInfo.setIsFlag("Y");
		ydMerchantDao.deleteMerchantOperate(operateId,
				YdRoleTypeEnum.ROLE_OPERATOR.getCode() + "",
				EnumSiteGroup.MERCHANT.getCode());
	}

	/**
	 * 查询供应商列表
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<YdMerchantResult> getSupplierList() throws BusinessException {
		YdMerchant ydMerchant = new YdMerchant();
		ydMerchant.setIsFlag("N");
		ydMerchant.setGroupCode(EnumSiteGroup.SUPPLIER.getCode());
		List<YdMerchant> supplierList = ydMerchantDao.getAll(ydMerchant);
		List<YdMerchantResult> list= DTOUtils.convertList(supplierList, YdMerchantResult.class);
		if(list.size()>0) {
			for(YdMerchantResult item:list) {
				item.setPassword(null);
				item.setPayPassword(null);
			}
		}
		
		return list;
	}

	/**
	 * 商户开启关闭旧机抵扣功能
	 * @param merchantId
	 * @param type	Y | N
	 */
	@Override
	public void setOldMachineReduce(Integer merchantId, String type) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(type) ||
						!(type.equalsIgnoreCase("Y") || type.equalsIgnoreCase("N")),
				"err_type", "请传入正确的参数");

		YdMerchantResult storeInfo = getStoreInfo(merchantId);

		storeInfo.setIsOldMachineReduce(type);
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, storeInfo);
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	@Override
	public Boolean bindWechatAccount(Integer merchantId, String openId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(openId),
				"err_empty_merchant", "openId不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_merchant", "商户不存在");

		ValidateBusinessUtils.assertFalse(StringUtils.isNotBlank(ydMerchant.getWxOpenId()),
				"err_empty_merchant", "商户已经绑定过微信");

		ydMerchant.setWxOpenId(openId);
		ydMerchantDao.updateYdMerchant(ydMerchant);
		return true;
	}

	@Override
	public Boolean unbindWechatAccount(Integer merchantId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_merchant", "商户不存在");
		ydMerchant.setWxOpenId("");
		ydMerchantDao.updateMerchantBindWeixin(ydMerchant.getId(), null,null);
		return true;
	}

	/**
	 * 平台升级商户会员
	 * @param merchantId
	 * @param memberLevel
	 * @throws BusinessException
	 */
	@Override
	public void merchantUpgrade(Integer merchantId, Integer memberLevel) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");
        Date nowDate = new Date();

		YdMerchantResult ydMerchant = getStoreInfo(merchantId);
		merchantId = ydMerchant.getId();

		ValidateBusinessUtils.assertFalse(ydMerchant.getMemberLevel() == null || ydMerchant.getMemberLevel() <= 0,
				"err_empty_merchant", "商户尚未开通会员");

        // 如果已经过期了，改成当前类型
        if (ydMerchant.getMemberValidTime().getTime() < nowDate.getTime()) {
           throw new BusinessException("err_member_valid_time", "会员已经过期，无法升级");
        }

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_empty_merchant", "请选择正确的会员等级");

		ValidateBusinessUtils.assertFalse(memberLevel <= ydMerchant.getMemberLevel(),
				"err_member_level", "选择正确的会员等级,升级不可以越升级越低级");

		YdMemberLevelConfig nowLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(ydMerchant.getMemberLevel());

		ydMerchant.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchant.setMemberType(memberLevelConfig.getMemberType());
		ydMerchant.setRoleIds(memberLevelConfig.getRoleId() + "");

		YdMerchant merchant = new YdMerchant();
		BeanUtilExt.copyProperties(merchant, ydMerchant);
		this.ydMerchantDao.updateYdMerchant(merchant);

		// 增加会员记录, 获取会员升级前最近一次的结束时间
		YdMerchantMemberOpenRecord firstValidTime = ydMerchantMemberOpenRecordDao.getFirstValidEndTime(ydMerchant.getId());

		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		ydMerchantMemberOpenRecord.setCreateTime(nowDate);
		ydMerchantMemberOpenRecord.setUpdateTime(nowDate);
		ydMerchantMemberOpenRecord.setMerchantId(merchantId);
		ydMerchantMemberOpenRecord.setRoleId(memberLevelConfig.getRoleId());
		ydMerchantMemberOpenRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberOpenRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberOpenRecord.setValidLength(memberLevelConfig.getValidLength());
		ydMerchantMemberOpenRecord.setStartTime(nowDate);
		ydMerchantMemberOpenRecord.setEndTime(firstValidTime.getEndTime());
		ydMerchantMemberOpenRecord.setPayPrice(memberLevelConfig.getMemberPrice() - nowLevelConfig.getMemberPrice());
		ydMerchantMemberOpenRecord.setOpenType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYSJ.getDesc());
		ydMerchantMemberOpenRecord.setOpenMethod(YdMerchantMemberApplyTypeEnum.MEMBER_APPLY_METHOD_ZDKT.getDesc());
		ydMerchantMemberOpenRecordDao.insertYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);
	}

	/**
	 * 平台续费商户会员
	 * @param merchantId
	 * @param memberLevel
	 * @throws BusinessException
	 */
	@Override
	public void memberRenewal(Integer merchantId, Integer memberLevel) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		YdMerchantResult ydMerchant = getStoreInfo(merchantId);
		merchantId = ydMerchant.getId();

		ValidateBusinessUtils.assertFalse(ydMerchant.getMemberLevel() == null || ydMerchant.getMemberLevel() <= 0,
				"err_empty_merchant", "商户尚未开通会员");

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_empty_merchant", "请选择正确的会员等级");

        Date nowDate = new Date();
		Date startTime = ydMerchant.getMemberValidTime();
		Date endTime = DateUtils.addMonths(ydMerchant.getMemberValidTime(), 12);
		ydMerchant.setMemberValidTime(endTime);

		// 如果已经过期了，改成当前类型
		if (startTime.getTime() < nowDate.getTime()) {
            ydMerchant.setMemberType(memberLevelConfig.getMemberType());
        }
		YdMerchant merchant = new YdMerchant();
		BeanUtilExt.copyProperties(merchant, ydMerchant);
		this.ydMerchantDao.updateYdMerchant(merchant);

		// 增加会员记录
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		ydMerchantMemberOpenRecord.setCreateTime(new Date());
		ydMerchantMemberOpenRecord.setUpdateTime(new Date());
		ydMerchantMemberOpenRecord.setMerchantId(merchantId);
		ydMerchantMemberOpenRecord.setPayPrice(memberLevelConfig.getMemberPrice());
		ydMerchantMemberOpenRecord.setRoleId(memberLevelConfig.getRoleId());
		ydMerchantMemberOpenRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberOpenRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberOpenRecord.setValidLength(memberLevelConfig.getValidLength());
		ydMerchantMemberOpenRecord.setStartTime(startTime);
		ydMerchantMemberOpenRecord.setEndTime(endTime);
		ydMerchantMemberOpenRecord.setOpenType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_HYXF.getDesc());
		ydMerchantMemberOpenRecord.setOpenMethod(YdMerchantMemberApplyTypeEnum.MEMBER_APPLY_METHOD_ZDKT.getDesc());
		ydMerchantMemberOpenRecordDao.insertYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);
	}

	@Override
	public void openMemberAgain(Integer merchantId, Integer memberLevel) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		Date nowDate = new Date();
		YdMerchantResult ydMerchant = getStoreInfo(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant.getMemberLevel() == null
						|| ydMerchant.getMemberLevel() <= 0,
				"err_empty_merchant", "商户尚未开通会员");

		ValidateBusinessUtils.assertFalse(ydMerchant.getMemberValidTime().getTime() > nowDate.getTime(),
				"err_member_not_invalid", "会员尚未过期，不可以重新开通");

		YdMemberLevelConfig memberLevelConfig = ydMemberLevelConfigDao.getYdMemberLevelConfigByMemberLevel(memberLevel);
		ValidateBusinessUtils.assertFalse(memberLevelConfig == null,
				"err_empty_merchant", "请选择正确的会员等级");

		// 更新商户会员时间类型，角色
		Date endTime = DateUtils.addMonths(nowDate, memberLevelConfig.getValidLength());
		ydMerchant.setMemberValidTime(endTime);
		ydMerchant.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchant.setMemberType(memberLevelConfig.getMemberType());
		ydMerchant.setRoleIds(memberLevelConfig.getRoleId() + "");
		YdMerchant merchant = new YdMerchant();
		BeanUtilExt.copyProperties(merchant, ydMerchant);
		this.ydMerchantDao.updateYdMerchant(merchant);

		// 增加会员记录
		merchantId = ydMerchant.getId();
		YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord = new YdMerchantMemberOpenRecord();
		ydMerchantMemberOpenRecord.setCreateTime(nowDate);
		ydMerchantMemberOpenRecord.setUpdateTime(nowDate);
		ydMerchantMemberOpenRecord.setMerchantId(merchantId);
		ydMerchantMemberOpenRecord.setPayPrice(memberLevelConfig.getMemberPrice());
		ydMerchantMemberOpenRecord.setRoleId(memberLevelConfig.getRoleId());
		ydMerchantMemberOpenRecord.setMemberLevel(memberLevelConfig.getMemberLevel());
		ydMerchantMemberOpenRecord.setMemberType(memberLevelConfig.getMemberType());
		ydMerchantMemberOpenRecord.setValidLength(memberLevelConfig.getValidLength());
		ydMerchantMemberOpenRecord.setStartTime(nowDate);
		ydMerchantMemberOpenRecord.setEndTime(endTime);
		ydMerchantMemberOpenRecord.setOpenType(YdMerchantMemberApplyTypeEnum.MERCHANT_MEMBER_CXKT.getDesc());
		ydMerchantMemberOpenRecord.setOpenMethod(YdMerchantMemberApplyTypeEnum.MEMBER_APPLY_METHOD_ZDKT.getDesc());
		ydMerchantMemberOpenRecordDao.insertYdMerchantMemberOpenRecord(ydMerchantMemberOpenRecord);

	}

	/**
	 * 平台启用禁用商户
	 * @param merchantId
	 * @param isFlag
	 * @throws BusinessException
	 */
	@Override
	public void updateMerchantStatus(Integer merchantId, String isFlag) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_merchant", "商户不存在");

		ValidateBusinessUtils.assertFalse(isFlag.equalsIgnoreCase(ydMerchant.getIsFlag()),
				"err_empty_merchant", "不可以重复操作");


		// 如果是禁用商户， 将下面的操作员也禁用掉
		if ((YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "").equals(ydMerchant.getRoleIds())) {
			ydMerchantDao.updateMerchantAndOperate(ydMerchant.getId(), isFlag);
		}

		ydMerchant.setIsFlag(isFlag);
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	/**
	 * 设置商户商城二维码
	 * @param merchantId
	 * @param shopQrcode
	 * @throws BusinessException
	 */
	@Override
	public void updateShopQrCode(Integer merchantId, String shopQrcode) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_merchant", "商户不存在");

		ydMerchant.setShopQrCode(shopQrcode);
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	/**
	 * 同步商户会员是否过期
	 * @throws Exception
	 */
	@Override
	public void synMemberValidTime() throws Exception {
		// 查询所有的商户
		YdMerchant ydMerchant = new YdMerchant();
		ydMerchant.setRoleIds(YdRoleTypeEnum.ROLE_MERCHANT_LEVEL_01.getCode() + "");
		ydMerchant.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
		List<YdMerchant> level1List = ydMerchantDao.getAll(ydMerchant);

		List<YdMerchant> allList = new ArrayList<>();
		allList.addAll(level1List);

		Date nowDate = new Date();
		allList.forEach(merchant -> {
			if (merchant.getMemberValidTime().getTime() < nowDate.getTime()) {
				// 去查看是否还有会员
				YdMerchantMemberOpenRecord openRecord = ydMerchantMemberOpenRecordDao.getOpenValidEndTimeByTime(merchant.getId());
				if (openRecord == null) {
					merchant.setIsFlag("Y");
					ydMerchantDao.updateYdMerchant(merchant);
				}
			} else {
				// 去查看是否还有会员
				YdMerchantMemberOpenRecord openRecord = ydMerchantMemberOpenRecordDao.getOpenValidEndTimeByTime(merchant.getId());
				if (openRecord != null) {
					merchant.setRoleIds(openRecord.getRoleId() + "");
					merchant.setMemberLevel(openRecord.getMemberLevel());
					merchant.setMemberType(openRecord.getMemberType());
					ydMerchantDao.updateYdMerchant(merchant);
				}
			}
		});
	}

	/**
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<YdMerchantResult> findStoreList(YdMerchantResult ydMerchantResult) throws BusinessException {
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);
		List<YdMerchant> merchantList = ydMerchantDao.findStoreList(ydMerchant);
		return DTOUtils.convertList(merchantList, YdMerchantResult.class);
	}

	@Override
	public Page<YdMerchantResult> findStoreListByPage(YdMerchantResult ydMerchantResult, PagerInfo pageInfo) throws BusinessException {
		Page<YdMerchantResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
		YdMerchant ydMerchant = new YdMerchant();
		BeanUtilExt.copyProperties(ydMerchant, ydMerchantResult);

		List<YdMerchant> merchantList = ydMerchantDao.findStoreListByPage(ydMerchant, 0, Integer.MAX_VALUE);
		if (CollectionUtils.isNotEmpty(merchantList)) {
			List<YdMerchant> dataList = ydMerchantDao.findStoreListByPage(ydMerchant, pageInfo.getStart(), pageInfo.getPageSize());
			List<YdMerchantResult> resultList = DTOUtils.convertList(dataList, YdMerchantResult.class);
			pageData.setData(resultList);
			pageData.setTotalRecord(merchantList.size());
		} else {
			pageData.setTotalRecord(0);
		}
		return pageData;
	}

	// ---------------------------     private        ----------------------------------

	/**
	 * 校验修改门店信息入参
	 * @param ydMerchantResult
	 */
	private void checkUpdateMerchantInfoParams(YdMerchantResult ydMerchantResult) {
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMobile()),
				"err_empty_mobile", "手机号不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMerchantName()),
				"err_empty_merchant_name", "商户名称不可以为空");
		/*ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getCard()),
				"err_empty_card", "身份证号码");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getMerchantUrl()),
				"err_empty_merchant_url", "店铺图片不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getLicenseUrls()),
				"err_empty_License_url", "营业执照不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(ydMerchantResult.getDistrict()),
				"err_empty_district", "区不可以为空");*/

		// 根据区id去查询省市区信息, 查询不到可能是app在编辑，distinctId = code，
		if (ydMerchantResult.getDistrictId() != null && ydMerchantResult.getDistrictId() != 0) {
			SysRegion distinct = sysRegionDao.getSysRegionById(ydMerchantResult.getDistrictId());
			if (distinct == null) {
				distinct = sysRegionDao.getSysRegionByCode(ydMerchantResult.getDistrictId());
			}
			ValidateBusinessUtils.assertFalse(distinct == null,
					"err_distinct_id", "请传入正确的区id或者区code");

			ydMerchantResult.setDistrict(distinct.getName());
			ydMerchantResult.setDistrictId(distinct.getId());

			// 查询市的信息
			SysRegion city = sysRegionDao.getSysRegionById(distinct.getPid());
			ydMerchantResult.setCity(city.getName());
			ydMerchantResult.setCityId(city.getId());

			// 查询省的信息
			SysRegion province = sysRegionDao.getSysRegionById(city.getPid());
			ydMerchantResult.setProvince(province.getName());
			ydMerchantResult.setProvinceId(province.getId());
		}
	}

	private String getRoleNames(String roleIds, Map<String, String> roleNameMap) {
		if(StringUtils.isEmpty(roleIds)) {
			return "";
		}else {
			String str="";
			for(String roleId:roleIds.split(",")) {
				if(roleNameMap.containsKey(roleId)) {
					String roleName=roleNameMap.get(roleId);
					str+=roleName+"、";
				}
			}
		
			if(StringUtils.isNotEmpty(str)) {
				str=str.substring(0, str.length()-1);
			}
			
			return str;
		}
	}
	
	private Map<String,String> getRoleNameMap(List<YdRole> roleList){
		Map<String, String> map = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(roleList)) {
			roleList.forEach(ydRole -> {
				map.put(ydRole.getId() + "", ydRole.getName());
			});
		}
		return map;
	}
}

