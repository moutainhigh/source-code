package com.yd.web.controller.admin.permission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.permission.YdPermissionResult;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.res.BaseResponse;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.WebUtil;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="mod_platform_permission",name = "权限管理")
@RequestMapping("/admin/permission")
public class AdminPermissionController extends BaseController{
	@Reference
	private PermissionService	permissionService;

	@ApiOperation(value = "角色管理查询权限数据", httpMethod = "GET")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(paramType = "query", name = "groupCode", value = "页码"),
			@ApiImplicitParam(paramType = "query", name = "roleIds", value = "每页显示个数")
	})
    @MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="role_manager_find_permission_list",name = "角色管理查询权限数据")
    @RequestMapping(value = "/groupPermissionList", method = {RequestMethod.POST})
    public BaseResponse<List<YdPermissionResult>> list(HttpServletRequest request, HttpServletResponse response) {
    	String groupCodeStr = WebUtil.getParameter(request, "groupCode");
		String roleIds = WebUtil.getParameter(request, "roleIds");
    	EnumSiteGroup groupCode=EnumSiteGroup.getByCode(groupCodeStr);
    	if(groupCode==null) {
    		return BaseResponse.fail("err_groupCode", "groupCode不能为空");
    	}
    	List<YdPermissionResult> list=permissionService.getPermissionListByGroupCode(groupCode, roleIds);
        return BaseResponse.success(list);
    }

}
