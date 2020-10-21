package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.yd.api.result.merchant.YdMerchantPayAuditResult;
import com.yd.api.service.merchant.YdMerchantPayAuditService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.permission.YdRole;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.permission.YdRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.merchant.YdMerchantPayAuditDao;
import com.yd.service.bean.merchant.YdMerchantPayAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户支付申请管理Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-25 11:17:40
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantPayAuditServiceImpl implements YdMerchantPayAuditService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantPayAuditServiceImpl.class);


	@Resource
	private YdRoleDao ydRoleDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdMerchantPayAuditDao ydMerchantPayAuditDao;

	@Override
	public YdMerchantPayAuditResult getYdMerchantPayAuditById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantPayAuditResult ydMerchantPayAuditResult = null;
		YdMerchantPayAudit ydMerchantPayAudit = this.ydMerchantPayAuditDao.getYdMerchantPayAuditById(id);
		if (ydMerchantPayAudit != null) {
			ydMerchantPayAuditResult = new YdMerchantPayAuditResult();
			BeanUtilExt.copyProperties(ydMerchantPayAuditResult, ydMerchantPayAudit);
		}
		return ydMerchantPayAuditResult;
	}

	@Override
	public Page<YdMerchantPayAuditResult> findYdMerchantPayAuditListByPage(YdMerchantPayAuditResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantPayAuditResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantPayAudit ydMerchantPayAudit = new YdMerchantPayAudit();
		BeanUtilExt.copyProperties(ydMerchantPayAudit, params);
		
		int amount = this.ydMerchantPayAuditDao.getYdMerchantPayAuditCount(ydMerchantPayAudit);
		if (amount > 0) {
			// 查询商户对应的角色
			YdRole param= new YdRole();
			List<YdRole> roleList = ydRoleDao.findYdRolesByPage(param, 0, Integer.MAX_VALUE);
			Map<String, String> roleNameMap = getRoleNameMap(roleList);

			List<YdMerchantPayAudit> dataList = this.ydMerchantPayAuditDao.findYdMerchantPayAuditListByPage(
					ydMerchantPayAudit, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantPayAuditResult> resultList = DTOUtils.convertList(dataList, YdMerchantPayAuditResult.class);
			resultList.forEach(payAuditResult -> {
				String roleNames = getRoleNames(payAuditResult.getRoleIds(), roleNameMap);
				payAuditResult.setRoleNames(roleNames);
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantPayAuditResult> getAll(YdMerchantPayAuditResult ydMerchantPayAuditResult) {
		YdMerchantPayAudit ydMerchantPayAudit = null;
		if (ydMerchantPayAuditResult != null) {
			ydMerchantPayAudit = new YdMerchantPayAudit();
			BeanUtilExt.copyProperties(ydMerchantPayAudit, ydMerchantPayAuditResult);
		}
		List<YdMerchantPayAudit> dataList = this.ydMerchantPayAuditDao.getAll(ydMerchantPayAudit);
		List<YdMerchantPayAuditResult> resultList = DTOUtils.convertList(dataList, YdMerchantPayAuditResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantPayAudit(YdMerchantPayAuditResult ydMerchantPayAuditResult) {
		ydMerchantPayAuditResult.setCreateTime(new Date());
		YdMerchantPayAudit ydMerchantPayAudit = new YdMerchantPayAudit();
		BeanUtilExt.copyProperties(ydMerchantPayAudit, ydMerchantPayAuditResult);
		this.ydMerchantPayAuditDao.insertYdMerchantPayAudit(ydMerchantPayAudit);
	}

	@Override
	public void updateYdMerchantPayAudit(YdMerchantPayAuditResult ydMerchantPayAuditResult) {
		ydMerchantPayAuditResult.setUpdateTime(new Date());
		YdMerchantPayAudit ydMerchantPayAudit = new YdMerchantPayAudit();
		BeanUtilExt.copyProperties(ydMerchantPayAudit, ydMerchantPayAuditResult);
		this.ydMerchantPayAuditDao.updateYdMerchantPayAudit(ydMerchantPayAudit);
	}

	@Override
	public void payAudit(Integer id, String auditStatus) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "请传入正确的id");
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(auditStatus),
				"err_empty_audit_status", "审核状态不可以为空");
		YdMerchantPayAudit ydMerchantPayAudit = this.ydMerchantPayAuditDao.getYdMerchantPayAuditById(id);

		if ("WAIT".equalsIgnoreCase(ydMerchantPayAudit.getAuditStatus())) {
			ydMerchantPayAudit.setAuditStatus(auditStatus);
			this.ydMerchantPayAuditDao.updateYdMerchantPayAudit(ydMerchantPayAudit);
		}

		// 修改开通状态
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydMerchantPayAudit.getMerchantId());
		ydMerchant.setIsOpenPay("Y");
		ydMerchantDao.updateYdMerchant(ydMerchant);
	}

	private Map<String,String> getRoleNameMap(List<YdRole> roleList){
		Map<String,String> map = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(roleList)) {
			for(YdRole role : roleList) {
				map.put(role.getId() + "", role.getName());
			}
		}
		return map;
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
				str = str.substring(0, str.length()-1);
			}

			return str;
		}
	}


}

