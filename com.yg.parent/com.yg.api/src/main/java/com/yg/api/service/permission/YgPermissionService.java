package com.yg.api.service.permission;

import com.yg.api.enums.YgSiteGroupEnum;
import com.yg.api.result.permission.YgPermissionResult;
import com.yg.api.result.permission.YgRoleResult;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.PagerInfo;

import java.util.List;
import java.util.Set;

public interface YgPermissionService {

	List<YgRoleResult> getRoleListByGroup(YgSiteGroupEnum siteGroup, PagerInfo pr);

	void addOrUpdateRole(YgSiteGroupEnum siteGroup, Integer roleId, String roleName, List<Integer> permissionIdList, int currMerchantId) throws BusinessException;

	void deleteRole(YgSiteGroupEnum siteGroup, Integer roleId) throws BusinessException;

	void initVisitPermission(String url, String methodName, String methodAlias, String controllerName, String controllerAlias, YgSiteGroupEnum groupCode);

	Set<String> getMerchantPermissionSet(int merchantId);

	List<YgPermissionResult> getPermissionListByGroupCode(YgSiteGroupEnum groupCode, String roleIds);

}
