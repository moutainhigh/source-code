package com.yd.service.dao.permission;

import java.util.List;
import java.lang.Integer;
import org.apache.ibatis.annotations.Param;
import com.yd.service.bean.permission.YdRlRolePermission;

/**
 *
 * This tools just a simple useful tools, gen table to javabean
 *
 * "I hope this tools can save your time"
 *
 * Generated by <tt>Generate</tt> 
 *
 * @author com.ypn.mapi.generate
 * @version v1.0
 */
public interface YdRlRolePermissionDao {

	public YdRlRolePermission findYdRlRolePermissionById(Integer id);
	
	public Integer insert(YdRlRolePermission param);
	
	public Integer getYdRlRolePermissionCount(YdRlRolePermission param);
	
	public List<YdRlRolePermission> findYdRlRolePermissionsByPage(@Param("param")YdRlRolePermission param,@Param("offset")Integer offset,@Param("rows")Integer rows);
	
	//if not use,pls delete it
	public Integer deleteYdRlRolePermissionById(Integer id);

	public void deleteYdRlRolePermissionByRoleId(@Param("roleId")Integer roleId);
}