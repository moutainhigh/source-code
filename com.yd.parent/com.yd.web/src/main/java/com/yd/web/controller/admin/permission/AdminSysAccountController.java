package com.yd.web.controller.admin.permission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yd.core.utils.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;

/**
 * 系统账号管理
 *
 */
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_permission", name = "权限管理")
@RequestMapping("/admin/permission/sysAccount")
public class AdminSysAccountController  extends BaseController{

	 @Reference
	 private YdMerchantService ydMerchantService;

	@MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="sysAccount_manager_data",name = "系统账号管理列表")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantResult>> list(HttpServletRequest request, HttpServletResponse response) {
		BaseResponse<Page<YdMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
    	PagerInfo pagerInfo = getPageInfo(request);
    	Page<YdMerchantResult> list=ydMerchantService.findMerchantList(pagerInfo, EnumSiteGroup.SYS);
        return BaseResponse.success(list);
    }

	@ApiOperation(value = "系统账号管理修新增修改", httpMethod = "POST")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(paramType = "query", name = "id", value = "id"),
			@ApiImplicitParam(paramType = "query", name = "roleIds", value = "角色ids"),
			@ApiImplicitParam(paramType = "query", name = "merchantName", value = "用户昵称"),
			@ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
			@ApiImplicitParam(paramType = "query", name = "password", value = "密码")
	})
    @MerchantCheck(groupCode = EnumSiteGroup.SYS,alias="sysAccount_manager_update",name = "系统账号管理修新增修改")
    @RequestMapping(value = "/addOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> addOrUpdate(Integer id, String roleIds, String merchantName, String mobile, String password) {
		BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");

		try {
			YdMerchantResult ydMerchantResult = new YdMerchantResult();
			ydMerchantResult.setId(id);
			ydMerchantResult.setMobile(mobile);
			ydMerchantResult.setRoleIds(roleIds);
			ydMerchantResult.setPassword(password);
			ydMerchantResult.setIsFlag("N");
			ydMerchantResult.setMerchantName(merchantName);

			if (id == null) {
				ydMerchantService.insertYdMerchant(ydMerchantResult, EnumSiteGroup.SYS);
			} else {
				ydMerchantService.updateYdMerchant(ydMerchantResult, EnumSiteGroup.SYS);
			}
		} catch (BusinessException exception) {
			result = BaseResponse.fail(exception.getCode(), exception.getMessage());
		}
		return result;
    }

	@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="sysAccount_manager_delete", name = "系统账号管理删除")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id")
	})
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public BaseResponse<Page<YdMerchantResult>> delete(Integer merchantId) {
		BaseResponse<Page<YdMerchantResult>> result = BaseResponse.success(null, "00", "删除成功");
		try {
			ydMerchantService.deleteMerchant(merchantId);
		} catch (BusinessException exception) {
			result = BaseResponse.fail(exception.getCode(), exception.getMessage());
		}
		return result;
	}
    
}
