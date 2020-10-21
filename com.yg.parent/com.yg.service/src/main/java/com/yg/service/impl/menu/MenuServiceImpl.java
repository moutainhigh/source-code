package com.yg.service.impl.menu;

import com.yg.api.result.menu.YgMenuResult;
import com.yg.api.service.menu.YgMenuService;
import com.yg.api.service.permission.YgPermissionService;
import com.yg.core.constants.AdminConstants;
import com.yg.core.utils.BeanUtilExt;
import com.yg.service.bean.menu.YgMenu;
import com.yg.service.bean.merchant.YgMerchant;
import com.yg.service.dao.menu.YgMenuDao;
import com.yg.service.dao.merchant.YgMerchantDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.*;

@Service(dynamic = true)
public class MenuServiceImpl implements YgMenuService {
	@Resource
	private YgMenuDao	ydMenuDao;
	@Resource
	private YgMerchantDao	ydMerchantDao;
	@Resource
	private YgPermissionService	permissionService;

	@Override
	public List<YgMenuResult> getMenuList(Integer merchantId) {
		YgMerchant merchant = ydMerchantDao.getYgMerchantById(merchantId);
		String groupCode = merchant.getGroupCode();
		
		List<YgMenuResult> result = new ArrayList<YgMenuResult>();
		Map<Integer, YgMenuResult> map = new HashMap<Integer, YgMenuResult>();
		List<YgMenu> list = ydMenuDao.getAllListByGroupCode(groupCode);
		if(CollectionUtils.isEmpty(list)) {
			return result;
		}
		
		// 校验权限
        Set<String> permSet = permissionService.getMerchantPermissionSet(merchantId);
		
		for(YgMenu item:list) {
			if(item.getParentMenuId()==0) {
				if(StringUtils.isEmpty(item.getPermission())) {
					YgMenuResult itemResult = new YgMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}else if(StringUtils.isNotEmpty(item.getPermission()) && hasPermission(merchantId, item.getPermission(), permSet)) {
					YgMenuResult itemResult = new YgMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}
			}
		}
		for(YgMenu item:list) {
			Integer pid=item.getParentMenuId();
			if(map.containsKey(pid)) {
				if(StringUtils.isEmpty(item.getPermission())) {
					YgMenuResult itemResult=new YgMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}else if(StringUtils.isNotEmpty(item.getPermission()) && hasPermission(merchantId, item.getPermission(), permSet)) {
					YgMenuResult itemResult=new YgMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					map.get(pid).getChildren().add(itemResult);
				}
			}
		}
		return result;
	}
	
	boolean hasPermission(Integer merchantId,String permission,Set<String> permSet) {
		if(AdminConstants.adminMerchantId.intValue()==merchantId) {
			return true;
		}
		if(permSet.contains(permission)) {
			return true;
		}
		return false;
	}
	
}
