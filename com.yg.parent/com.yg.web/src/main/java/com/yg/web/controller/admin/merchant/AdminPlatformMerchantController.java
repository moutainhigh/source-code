package com.yg.web.controller.admin.merchant;

/**
 * 后台商户管理
 * @author wuyc
 * created 2019/10/24 16:31
 **/
import com.yg.api.result.merchant.YgMerchantResult;
import com.yg.api.service.merchant.YgMerchantService;
import com.yg.core.res.BaseResponse;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;
import com.yg.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("admin/platform/merchant")
public class AdminPlatformMerchantController extends BaseController {

    @Reference
    private YgMerchantService ygMerchantService;

    @ApiOperation(value = "平台查询商户列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @RequestMapping(value = "/findList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YgMerchantResult>> findList(HttpServletRequest request, YgMerchantResult ygMerchantResult) {
        BaseResponse<Page<YgMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
            result.setResult(ygMerchantService.findYgMerchantListByPage(ygMerchantResult, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台新增修改商户", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(YgMerchantResult ygMerchantResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            if (ygMerchantResult.getId() <= 0 || ygMerchantResult.getId() == null) {
                ygMerchantService.insertYgMerchant(ygMerchantResult);
            } else {
                ygMerchantService.updateYgMerchant(ygMerchantResult);
            }
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台禁用启用商户", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y启用  N禁用"),
    })
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantStatus(Integer merchantId, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "设置成功");
        try {
            ygMerchantService.updateMerchantStatus(merchantId, isEnable);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

}
