package com.yd.web.controller.admin.operate;

/**
 * 运营管理-账户审核管理
 * @author wuyc
 * created 2019/10/24 16:31
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantInfoAuditResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantInfoAuditService;
import com.yd.api.service.merchant.YdMerchantService;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_operate", name = "运营管理")
@RequestMapping("/admin/merchant/operate/info/audit")
public class AdminMerchantInfoAuditController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantInfoAuditService ydMerchantInfoAuditService;

    @ApiOperation(value = "账户审核管理列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "商户名称"),
            @ApiImplicitParam(paramType = "query", name = "auditStatus", value = "审核状态(SUCCESS | REFUSE)"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_info_list",name = "账户审核管理列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantInfoAuditResult>> findList(HttpServletRequest request, String merchantName, String auditStatus) {
        BaseResponse<Page<YdMerchantInfoAuditResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request);
            YdMerchantInfoAuditResult param = new YdMerchantInfoAuditResult();
            param.setMerchantName(merchantName);
            param.setAuditStatus(auditStatus);
            Page<YdMerchantInfoAuditResult> resultData = ydMerchantInfoAuditService.findYdMerchantInfoAuditListByPage(param, pageInfo);
            result.setResult(resultData);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "账户审核新旧数据对比", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_info_click_audit",name = "账户审核新旧数据对比")
    @RequestMapping(value = "/clickAudit", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> clickAudit(HttpServletRequest request, Integer id) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Map<String, Object> resultData = new HashMap<>(4);
            YdMerchantInfoAuditResult ydMerchantInfoAuditResult = ydMerchantInfoAuditService.getYdMerchantInfoAuditById(id);
            YdMerchantResult ydMerchantResult = ydMerchantService.getYdMerchantById(ydMerchantInfoAuditResult.getMerchantId());
            resultData.put("oldData", ydMerchantResult);
            resultData.put("newData", ydMerchantInfoAuditResult);
            result.setResult(resultData);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "账户审核", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "auditStatus", value = "SUCCESS(审核通过), REFUSE(审核拒绝)"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_merchant_info_audit",name = "账户审核")
    @RequestMapping(value = "/audit", method = {RequestMethod.POST})
    public BaseResponse<String> audit(HttpServletRequest request, Integer id, String auditStatus) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "审核成功");
        try {
            ydMerchantInfoAuditService.auditMerchantInfo(id, auditStatus);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
