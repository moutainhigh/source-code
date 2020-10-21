package com.yd.web.controller.app.merchant;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.permission.YdRoleResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.*;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.QrCodeUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 门店账号管理
 * @author wuyc
 * created 2019/11/5 11:54
 **/
@RestController
@AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT,alias="mod_store_permission", name = "店铺权限管理")
@RequestMapping("/app/permission/storeAccount")
public class AppStoreAccountController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private PermissionService permissionService;

    @ApiOperation(value = "店铺账号管理(门店下面的操作员管理)", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true)
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_operate_data",name = "店铺账号管理")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantResult>> findList(HttpServletRequest request, HttpServletResponse response) {
        PagerInfo pagerInfo = getPageInfo(request);
        Page<YdMerchantResult> list = ydMerchantService.findOperateList(pagerInfo, getCurrMerchantId(request));
        return BaseResponse.success(list);
    }

    @ApiOperation(value = "店铺账号管理编辑(门店添加修改操作员)", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "用户昵称"),
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_update_operate",name = "门店添加修改操作员")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, Integer id,
                                             String merchantName, String mobile, String password) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdMerchantResult ydMerchantResult = new YdMerchantResult();
            ydMerchantResult.setMobile(mobile);
            ydMerchantResult.setPassword(password);
            ydMerchantResult.setIsFlag("N");
            ydMerchantResult.setMerchantName(merchantName);
            ydMerchantResult.setPid(getCurrMerchantId(request));
            ydMerchantResult.setRoleIds(YdRoleTypeEnum.ROLE_OPERATOR.getCode() + "");

            if (id == null) {
                ydMerchantService.insertYdMerchant(ydMerchantResult, EnumSiteGroup.MERCHANT);
            } else {
                ydMerchantResult.setId(id);
                ydMerchantService.updateYdMerchant(ydMerchantResult, EnumSiteGroup.MERCHANT);
            }
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询门店详细信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "门店不用传，操作员传"),
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getMerchantDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantResult> getMerchantDetail(HttpServletRequest request, Integer merchantId) {
        BaseResponse<YdMerchantResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (merchantId == null || merchantId == 0) {
                merchantId = getCurrMerchantId(request);
            }
            result.setResult(ydMerchantService.getYdMerchantById(merchantId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {

        }
        return result;
    }

    @ApiOperation(value = "门店删除操作员", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_delete_operate", name = "门店删除操作员")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteStoreOperate(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydMerchantService.deleteStoreOperate(getCurrMerchantId(request), id);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "店铺角色查询", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="store_all_role", name = "店铺角色查询")
    @RequestMapping(value = "/findRoleList", method = {RequestMethod.POST})
    public BaseResponse<List<YdRoleResult>> findRoleList(HttpServletRequest request) {
        BaseResponse<List<YdRoleResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pagerInfo = getPageInfo(request);
        // List<YdRoleResult> roleList = permissionService.getRoleListByGroup(EnumSiteGroup.MERCHANT, pagerInfo);

        YdRoleResult ydRoleResult = new YdRoleResult();
        ydRoleResult.setId(YdRoleTypeEnum.ROLE_OPERATOR.getCode());
        ydRoleResult.setName(YdRoleTypeEnum.ROLE_OPERATOR.getDesc());

        List<YdRoleResult> roleList = new ArrayList<>();
        roleList.add(ydRoleResult);
        result.setResult(roleList);
        return result;
    }

}
