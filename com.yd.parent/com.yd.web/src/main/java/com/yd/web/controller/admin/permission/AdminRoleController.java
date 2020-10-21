package com.yd.web.controller.admin.permission;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.permission.YdRoleResult;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.WebUtil;

/**
 * 系统角色管理
 *
 */
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="mod_platform_permission",name = "权限管理")
@RequestMapping("/admin/permission/role")
public class AdminRoleController extends BaseController {

    @Reference
    private PermissionService permissionService;

    @ApiOperation(value = "角色管理列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="role_manager_data",name = "角色管理列表")
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdRoleResult>> list(HttpServletRequest request, HttpServletResponse response) {
    	PagerInfo pr=getPageInfo(request);
    	List<YdRoleResult> list=permissionService.getRoleListByGroup(EnumSiteGroup.SYS,pr);
        return BaseResponse.success(list);
    }

    @ApiOperation(value = "角色管理新增修改", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "roleId", value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "roleName", value = "角色名称"),
            @ApiImplicitParam(paramType = "query", name = "permissionIds", value = "权限ids")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="role_manager_saveOrUpdate",name = "角色管理新增修改")
    @RequestMapping(value = "/addOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
    	Integer roleId=getIntAttr(request, "roleId");
    	String roleName=WebUtil.getParameter(request, "roleName");
    	String  permissionIds=WebUtil.getParameter(request, "permissionIds");
        List<Integer> permissionIdList = Arrays.stream(StringUtils.split(permissionIds, ",")).map(Integer::valueOf).collect(Collectors.toList());
        
        try {
        	permissionService.addOrUpdateRole(EnumSiteGroup.SYS,roleId,roleName,permissionIdList,getCurrMerchantId(request));
        	return BaseResponse.success(null, (roleId==null||roleId==0)?"添加成功":"修改成功");
        }catch(BusinessException e) {
        	return BaseResponse.fail(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "角色管理删除角色", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "roleId", value = "角色id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="role_manager_delete", name = "角色管理删除角色")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request) {
    	Integer roleId=getIntAttr(request, "roleId");
    	permissionService.deleteRole(EnumSiteGroup.SYS,roleId);
    	
        return BaseResponse.success(null);
    }

}
