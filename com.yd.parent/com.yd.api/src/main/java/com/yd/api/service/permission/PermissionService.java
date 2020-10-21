package com.yd.api.service.permission;

import java.util.List;
import java.util.Set;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.permission.YdPermissionResult;
import com.yd.api.result.permission.YdRoleResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PagerInfo;

public interface PermissionService {

	List<YdRoleResult> getRoleListByGroup(EnumSiteGroup siteGroup, PagerInfo pr);

	void addOrUpdateRole(EnumSiteGroup siteGroup, Integer roleId,String roleName, List<Integer> permissionIdList,int currMerchantId) throws BusinessException;

	void deleteRole(EnumSiteGroup siteGroup, Integer roleId) throws BusinessException;

	void initVisitPermission(String url,String methodName, String methodAlias, String controllerName, String controllerAlias,EnumSiteGroup groupCode);

	Set<String> getMerchantPermissionSet(int merchantId);

	List<YdPermissionResult> getPermissionListByGroupCode(EnumSiteGroup groupCode, String roleIds);

}
