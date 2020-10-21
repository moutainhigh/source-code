package com.yg.service.impl.permission;

import com.yg.api.enums.YgSiteGroupEnum;
import com.yg.api.result.permission.YgPermissionResult;
import com.yg.api.result.permission.YgRoleResult;
import com.yg.api.service.permission.YgPermissionService;
import com.yg.core.utils.BeanUtilExt;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.DTOUtils;
import com.yg.core.utils.PagerInfo;
import com.yg.service.bean.merchant.YgMerchant;
import com.yg.service.bean.permission.YgPermission;
import com.yg.service.bean.permission.YgRlRolePermission;
import com.yg.service.bean.permission.YgRole;
import com.yg.service.dao.merchant.YgMerchantDao;
import com.yg.service.dao.permission.YgPermissionDao;
import com.yg.service.dao.permission.YgRlRolePermissionDao;
import com.yg.service.dao.permission.YgRoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service(dynamic = true)
public class PermissionServiceImpl implements YgPermissionService {
	private final static Logger LOG = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Resource
	private YgRoleDao ydRoleDao;

	@Resource
	private YgMerchantDao ydMerchantDao;
	
	@Resource
	private YgPermissionDao ydPermissionDao;
	
	@Resource
	private YgRlRolePermissionDao ydRlRolePermissionDao;
	
	
	@Override
	public List<YgRoleResult> getRoleListByGroup(YgSiteGroupEnum siteGroup, PagerInfo pr) {
		YgRole param=new YgRole();
		param.setGroupCode(siteGroup.getCode());
		List<YgRole> list=ydRoleDao.findYdRolesByPage(param, pr.getStart(), pr.getPageSize());
		return DTOUtils.convertList(list, YgRoleResult.class);
	}


	@Override
	@Transactional
	public void addOrUpdateRole(YgSiteGroupEnum siteGroup, Integer roleId,String roleName, List<Integer> permissionIdList,int currMerchantId) throws BusinessException{
		if(roleId!=null && roleId==0) {
			roleId=null;
		}
		if(roleId==null && StringUtils.isEmpty(roleName)) {
			throw new BusinessException("err_roleName", "角色名称不能为空");
		}
		int topMerchantId=-1;
		if(siteGroup==YgSiteGroupEnum.SYS) {
			topMerchantId=0;
		}else {
			YgMerchant topMerchant = getTopMerchant(currMerchantId);
			if(topMerchant!=null) {
				topMerchantId=topMerchant.getId();
			}
		}
		if(roleId==null) {
			//新增角色
			YgRole role=new YgRole();
			role.setCanDelete("Y");
			role.setGroupCode(siteGroup.getCode());
			role.setMerchantId(topMerchantId);
			role.setName(roleName);
			role.setCreateTime(new Date());
			ydRoleDao.insert(role);
			filterPermissionByMerchantId(siteGroup, permissionIdList);
			if(permissionIdList!=null && permissionIdList.size()>0) {
				for(Integer permissionId:permissionIdList) {
					YgRlRolePermission rl=new YgRlRolePermission();
					rl.setCreateTime(new Date());
					rl.setPermissionId(permissionId);
					rl.setRoleId(role.getId());
					ydRlRolePermissionDao.insert(rl);
				}
			}
			
		}else {
			//编辑角色
			YgRole role=ydRoleDao.findYdRoleById(roleId);
			if(role==null) {
				throw new BusinessException("err_role", "编辑的数据不存在");
			}
			if(!siteGroup.getCode().equals(role.getGroupCode())) {
				throw new BusinessException("err_groupCode", "你没有权限修改");
			}
			if(role.getMerchantId()!=topMerchantId) {
				throw new BusinessException("err_groupCode", "你没有权限修改");
			}
			ydRoleDao.updateRoleName(roleId,roleName);
			filterPermissionByMerchantId(siteGroup, permissionIdList);
			
			ydRlRolePermissionDao.deleteYdRlRolePermissionByRoleId(roleId);
			if(permissionIdList!=null && permissionIdList.size()>0) {
				for(Integer permissionId:permissionIdList) {
					YgRlRolePermission rl = new YgRlRolePermission();
					rl.setCreateTime(new Date());
					rl.setPermissionId(permissionId);
					rl.setRoleId(role.getId());
					ydRlRolePermissionDao.insert(rl);
				}
			}
		}
	}
	
	private YgMerchant getTopMerchant(int currMerchantId) {
		YgMerchant merchant = ydMerchantDao.getYgMerchantById(currMerchantId);
		if(merchant==null) {
			return null;
		}
		return merchant;
	}


	private void filterPermissionByMerchantId(YgSiteGroupEnum siteGroup,List<Integer> permissionIdList){
		YgPermission param = new YgPermission();
		param.setGroupCode(siteGroup.getCode());
		List<YgPermission> list = ydPermissionDao.findYdPermissionsByPage(param, 0, Integer.MAX_VALUE);
		Set<Integer> set=new HashSet<Integer>();
		if(list!=null && list.size()>0) {
			for(YgPermission item : list) {
				set.add(item.getId());
			}
		}
		List<Integer> delList=new ArrayList<Integer>();
		if(permissionIdList!=null && permissionIdList.size()>0) {
			for(Integer id:permissionIdList) {
				if(!set.contains(id)) {
					delList.add(id);
				}
			}
			permissionIdList.removeAll(delList);
		}
	}


	@Override
	public void deleteRole(YgSiteGroupEnum siteGroup, Integer roleId) throws BusinessException {
		YgRole role = ydRoleDao.findYdRoleById(roleId);
		if(role==null) {
			throw new BusinessException("err_roleid", "删除数据不存在");
		}
		if(!"Y".equals(role.getCanDelete())) {
			throw new BusinessException("err_roleid", "该角色不能删除");
		}
		if(!siteGroup.getCode().equals(role.getGroupCode())) {
			throw new BusinessException("err_roleid", "你没有权限");
		}
		ydRoleDao.deleteYdRoleById(roleId);
	}


	@Override
	public void initVisitPermission(String url,String methodName, String methodAlias, String controllerName,String controllerAlias, YgSiteGroupEnum groupCode) {
	       if (StringUtils.isEmpty(methodName)) {
	            LOG.info("【初始化权限  参数  methodName 不能为空 】 url=" + url);
	            return;
	        }
	        if (StringUtils.isEmpty(methodAlias)) {
	            LOG.info("【初始化权限  参数  methodAlias 不能为空 】 url=" + url);
	            return;
	        }
	        if (StringUtils.isEmpty(controllerName)) {
	            LOG.info("【初始化权限  参数  controllerName 不能为空 】 url=" + url);
	            return;
	        }
	        if (StringUtils.isEmpty(controllerAlias)) {
	            LOG.info("【初始化权限  参数  controllerAlias 不能为空 】 url=" + url);
	            return;
	        }
	        // step 1:查看模块权限是否已经创建
	        YgPermission parentPermission = ydPermissionDao.findPermissionByAliasAndPid(controllerAlias, 0);
	        if (parentPermission == null) {
	            parentPermission = new YgPermission();
	            parentPermission.setAlias(controllerAlias);
	            parentPermission.setName(controllerName);
	            parentPermission.setPid(0);
	            parentPermission.setCreateTime(new Date());
	            parentPermission.setGroupCode(groupCode==null?null:groupCode.getCode());
	            ydPermissionDao.insert(parentPermission);
	        }
	        // step 2:查看操作权限是否已经存在
	        YgPermission childPermission = ydPermissionDao.findPermissionByAlias(methodAlias);
	        if (childPermission == null) {
	            childPermission = new YgPermission();
	            childPermission.setAlias(methodAlias);
	            childPermission.setName(methodName);
	            childPermission.setPid(parentPermission.getId());
	            childPermission.setCreateTime(new Date());
	            childPermission.setGroupCode(groupCode==null?null:groupCode.getCode());
	            ydPermissionDao.insert(childPermission);
	        } else {
	            if (!childPermission.getName().equals(methodName) || childPermission.getPid().intValue() != parentPermission.getId()) {
	                childPermission.setPid(parentPermission.getId());
	                childPermission.setName(methodName);
	                ydPermissionDao.updateById(childPermission);
	            }
	        }
	}


	@Override
	public Set<String> getMerchantPermissionSet(int merchantId) {
		Set<String> set = new HashSet<String>();
		YgMerchant merchant = ydMerchantDao.getYgMerchantById(merchantId);
		if(merchant == null) {
			return set;
		}

		String roleIds = merchant.getRoleId() + "";
		if(StringUtils.isEmpty(roleIds)) {
			return set;
		}

		List<Integer> roleIdList = Arrays.stream(StringUtils.split(roleIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());

		List<String> permissionList = ydRoleDao.getPermissionListByRoles(roleIdList);
		if(CollectionUtils.isEmpty(permissionList)) {
			return set;
		}

		for(String permission : permissionList) {
			set.add(permission);
		}
		return set;
	}
	
	public List<Integer> getRoleIdList(String roleIds){
		List<Integer> list=new ArrayList<Integer>();
		for(String roleId:roleIds.split(",")) {
			list.add(Integer.parseInt(roleId));
		}
		
		return list;
	}


	@Override
	public List<YgPermissionResult> getPermissionListByGroupCode(YgSiteGroupEnum groupCode, String roleIds) {
		List<YgPermissionResult> result=new ArrayList<YgPermissionResult>();
		// 查询所有的权限
		List<YgPermission> list = ydPermissionDao.findYdPermissionByGroupCode(groupCode.getCode());
		if(CollectionUtils.isEmpty(list)) {
			return result;
		}

		// 查询角色所有的权限
		Map<String, String> hashMap = new HashMap<>();
		if (StringUtils.isNotEmpty(roleIds)) {
			List<Integer> roleIdList = Arrays.stream(org.apache.commons.lang3.StringUtils.split(roleIds, ","))
					.map(Integer::valueOf).collect(Collectors.toList());
			List<String> permissionList = ydRoleDao.getPermissionListByRoles(roleIdList);
			for (String permission : permissionList) {
				hashMap.put(permission, permission);
			}
		}

		Map<String, YgPermissionResult> map = new HashMap<String,YgPermissionResult>();
		for(YgPermission item:list) {
			if(item.getPid() == 0) {
				YgPermissionResult itemResult = new YgPermissionResult();
				BeanUtilExt.copyProperties(itemResult, item);
				result.add(itemResult);
				map.put(item.getId()+"", itemResult);
			}
		}

		for(YgPermission item : list) {
			if(map.containsKey(item.getPid()+"")) {
				YgPermissionResult itemResult = new YgPermissionResult();
				BeanUtilExt.copyProperties(itemResult, item);
				if (hashMap.get(itemResult.getAlias()) != null) {
					itemResult.setCheck(true);
				} else {
					itemResult.setCheck(false);
				}
				map.get(item.getPid()+"").getChildren().add(itemResult);
			}
		}
		return result;
	}
}
