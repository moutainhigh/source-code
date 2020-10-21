package com.yd.service.impl.permission;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.permission.YdPermissionResult;
import com.yd.api.result.permission.YdRoleResult;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.PagerInfo;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.permission.YdPermission;
import com.yd.service.bean.permission.YdRlRolePermission;
import com.yd.service.bean.permission.YdRole;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.permission.YdPermissionDao;
import com.yd.service.dao.permission.YdRlRolePermissionDao;
import com.yd.service.dao.permission.YdRoleDao;

@Service(dynamic = true)
public class PermissionServiceImpl implements PermissionService {
	private final static Logger LOG = LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	@Resource
	private YdRoleDao	ydRoleDao;
	@Resource
	private YdPermissionDao	ydPermissionDao;
	@Resource
	private YdRlRolePermissionDao	ydRlRolePermissionDao;
	@Resource
	private YdMerchantDao	ydMerchantDao;
	
	
	@Override
	public List<YdRoleResult> getRoleListByGroup(EnumSiteGroup siteGroup, PagerInfo pr) {
		YdRole param=new YdRole();
		param.setGroupCode(siteGroup.getCode());
		List<YdRole> list=ydRoleDao.findYdRolesByPage(param, pr.getStart(), pr.getPageSize());
		return DTOUtils.convertList(list, YdRoleResult.class);
	}


	@Override
	@Transactional
	public void addOrUpdateRole(EnumSiteGroup siteGroup, Integer roleId,String roleName, List<Integer> permissionIdList,int currMerchantId) throws BusinessException{
		if(roleId!=null && roleId==0) {
			roleId=null;
		}
		if(roleId==null && StringUtils.isEmpty(roleName)) {
			throw new BusinessException("err_roleName", "角色名称不能为空");
		}
		int topMerchantId=-1;
		if(siteGroup==EnumSiteGroup.SYS) {
			topMerchantId=0;
		}else {
			YdMerchant topMerchant=getTopMerchant(currMerchantId);
			if(topMerchant!=null) {
				topMerchantId=topMerchant.getId();
			}
		}
		if(roleId==null) {
			//新增角色
			YdRole role=new YdRole();
			role.setCanDelete("Y");
			role.setGroupCode(siteGroup.getCode());
			role.setMerchantId(topMerchantId);
			role.setName(roleName);
			role.setCreateTime(new Date());
			ydRoleDao.insert(role);
			filterPermissionByMerchantId(siteGroup, permissionIdList);
			if(permissionIdList!=null && permissionIdList.size()>0) {
				for(Integer permissionId:permissionIdList) {
					YdRlRolePermission rl=new YdRlRolePermission();
					rl.setCreateTime(new Date());
					rl.setPermissionId(permissionId);
					rl.setRoleId(role.getId());
					ydRlRolePermissionDao.insert(rl);
				}
			}
			
		}else {
			//编辑角色
			YdRole role=ydRoleDao.findYdRoleById(roleId);
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
					YdRlRolePermission rl=new YdRlRolePermission();
					rl.setCreateTime(new Date());
					rl.setPermissionId(permissionId);
					rl.setRoleId(role.getId());
					ydRlRolePermissionDao.insert(rl);
				}
			}
		}
	}
	
	private YdMerchant getTopMerchant(int currMerchantId) {
		YdMerchant merchant=ydMerchantDao.getYdMerchantById(currMerchantId);
		if(merchant==null) {
			return null;
		}
		if(merchant.getPid()==0) {
			return merchant;
		}
		return getTopMerchant(merchant.getPid());
	}


	private void filterPermissionByMerchantId(EnumSiteGroup siteGroup,List<Integer> permissionIdList){
		YdPermission param=new YdPermission();
		param.setGroupCode(siteGroup.getCode());
		List<YdPermission> list=ydPermissionDao.findYdPermissionsByPage(param, 0, Integer.MAX_VALUE);
		Set<Integer> set=new HashSet<Integer>();
		if(list!=null && list.size()>0) {
			for(YdPermission item:list) {
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
	public void deleteRole(EnumSiteGroup siteGroup, Integer roleId) throws BusinessException {
		YdRole role=ydRoleDao.findYdRoleById(roleId);
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
	public void initVisitPermission(String url,String methodName, String methodAlias, String controllerName,String controllerAlias, EnumSiteGroup groupCode) {
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
	        YdPermission parentPermission = ydPermissionDao.findPermissionByAliasAndPid(controllerAlias, 0);
	        if (parentPermission == null) {
	            parentPermission = new YdPermission();
	            parentPermission.setAlias(controllerAlias);
	            parentPermission.setName(controllerName);
	            parentPermission.setPid(0);
	            parentPermission.setCreateTime(new Date());
	            parentPermission.setGroupCode(groupCode==null?null:groupCode.getCode());
	            ydPermissionDao.insert(parentPermission);
	        }
	        // step 2:查看操作权限是否已经存在
	        YdPermission childPermission = ydPermissionDao.findPermissionByAlias(methodAlias);
	        if (childPermission == null) {
	            childPermission = new YdPermission();
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
		YdMerchant merchant = ydMerchantDao.getYdMerchantById(merchantId);
		if(merchant == null) {
			return set;
		}

		String roleIds = merchant.getRoleIds();
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
	public List<YdPermissionResult> getPermissionListByGroupCode(EnumSiteGroup groupCode, String roleIds) {
		List<YdPermissionResult> result=new ArrayList<YdPermissionResult>();
		// 查询所有的权限
		List<YdPermission> list = ydPermissionDao.findYdPermissionByGroupCode(groupCode.getCode());
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

		Map<String, YdPermissionResult> map = new HashMap<String,YdPermissionResult>();
		for(YdPermission item:list) {
			if(item.getPid() == 0) {
				YdPermissionResult itemResult = new YdPermissionResult();
				BeanUtilExt.copyProperties(itemResult, item);
				result.add(itemResult);
				map.put(item.getId()+"", itemResult);
			}
		}

		for(YdPermission item : list) {
			if(map.containsKey(item.getPid()+"")) {
				YdPermissionResult itemResult=new YdPermissionResult();
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
