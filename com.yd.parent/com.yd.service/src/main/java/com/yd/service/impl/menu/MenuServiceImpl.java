package com.yd.service.impl.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.api.result.menu.YdMenuResult;
import com.yd.api.service.menu.MenuService;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.constants.AdminConstants;
import com.yd.core.utils.BeanUtilExt;
import com.yd.service.bean.menu.YdMenu;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.dao.menu.YdMenuDao;
import com.yd.service.dao.merchant.YdMerchantDao;

@Service(dynamic = true)
public class MenuServiceImpl implements MenuService {
	@Resource
	private YdMenuDao	ydMenuDao;
	@Resource
	private YdMerchantDao	ydMerchantDao;
	@Resource
	private PermissionService	permissionService;

	@Override
	public List<YdMenuResult> getMenuList(Integer merchantId) {
		YdMerchant merchant = ydMerchantDao.getYdMerchantById(merchantId);
		String groupCode = merchant.getGroupCode();
		
		List<YdMenuResult> result = new ArrayList<YdMenuResult>();
		Map<Integer, YdMenuResult> map = new HashMap<Integer,YdMenuResult>();
		List<YdMenu> list =ydMenuDao.getAllListByGroupCode(groupCode);
		if(CollectionUtils.isEmpty(list)) {
			return result;
		}
		
		// 校验权限
        Set<String> permSet = permissionService.getMerchantPermissionSet(merchantId);
		
		for(YdMenu item:list) {
			if(item.getParentMenuId()==0) {
				if(StringUtils.isEmpty(item.getPermission())) {
					YdMenuResult itemResult=new YdMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}else if(StringUtils.isNotEmpty(item.getPermission()) && hasPermission(merchantId, item.getPermission(), permSet)) {
					YdMenuResult itemResult=new YdMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}
			}
		}
		for(YdMenu item:list) {
			Integer pid=item.getParentMenuId();
			if(map.containsKey(pid)) {
				if(StringUtils.isEmpty(item.getPermission())) {
					YdMenuResult itemResult=new YdMenuResult();
					BeanUtilExt.copyProperties(itemResult, item);
					result.add(itemResult);
					map.put(itemResult.getId(), itemResult);
				}else if(StringUtils.isNotEmpty(item.getPermission()) && hasPermission(merchantId, item.getPermission(), permSet)) {
					YdMenuResult itemResult=new YdMenuResult();
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
