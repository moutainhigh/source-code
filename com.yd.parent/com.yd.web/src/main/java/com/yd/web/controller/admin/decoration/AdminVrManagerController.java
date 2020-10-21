package com.yd.web.controller.admin.decoration;

/**
 * 门店首页分类管理
 * @author wuyc
 * created 2019/10/31 15:44
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.decoration.YdVrManagerResult;
import com.yd.api.service.decoration.YdVrManagerService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_material", name = "店铺管理")
@RequestMapping("/admin/platform/vr")
public class AdminVrManagerController extends BaseController {

    @Reference
    private YdVrManagerService ydVrManagerService;

    @ApiOperation(value = "平台vr列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "merchantId")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_vr_data",name = "平台vr列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdVrManagerResult>> findList(HttpServletRequest request, Integer merchantId) {
        BaseResponse<Page<YdVrManagerResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request, 1, 10);
            YdVrManagerResult ydVrManagerResult = new YdVrManagerResult();
            ydVrManagerResult.setMerchantId(merchantId);
            result.setResult(ydVrManagerService.findYdVrManagerListByPage(ydVrManagerResult, pageInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台vr详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_vr_detail",name = "平台vr详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdVrManagerResult> getDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdVrManagerResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydVrManagerService.getYdVrManagerById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台vr编辑", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "merchantId"),
            @ApiImplicitParam(paramType = "query", name = "jumpUrl", value = "跳转链接地址")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_vr_update",name = "平台vr编辑")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(Integer id, Integer merchantId, String jumpUrl) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdVrManagerResult ydVrManagerResult = new YdVrManagerResult();
            ydVrManagerResult.setMerchantId(merchantId);
            ydVrManagerResult.setJumpUrl(jumpUrl);
            if (id != null && id > 0) {
                ydVrManagerResult.setId(id);
                ydVrManagerService.updateYdVrManager(ydVrManagerResult);
            } else {
                ydVrManagerService.insertYdVrManager(ydVrManagerResult);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台vr删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_vr_delete",name = "平台vr删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydVrManagerService.deleteYdVrManager(id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
