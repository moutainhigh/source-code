package com.yd.web.controller.admin.operate;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantPayAuditResult;
import com.yd.api.service.merchant.YdMerchantPayAuditService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
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

/**
 * 用户支付记录申请
 * @author wuyc
 * created 2019/11/25 11:43
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_operate",name = "运营管理")
@RequestMapping("/admin/merchant/operate/pay/audit")
public class AdminMerchantPayAuditController extends BaseController {

    @Reference
    private YdMerchantPayAuditService ydMerchantPayAuditService;

    @ApiOperation(value = "商户支付申请列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "商户名称"),
            @ApiImplicitParam(paramType = "query", name = "auditStatus", value = "审核状态"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="merchant_pay_list",name = "账户审核管理列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantPayAuditResult>> findMerchantGiftAccount(HttpServletRequest request, String auditStatus,
                                                                                   String merchantName) {
        BaseResponse<Page<YdMerchantPayAuditResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            YdMerchantPayAuditResult params = new YdMerchantPayAuditResult();
            params.setMerchantId(merchantId);
            params.setAuditStatus(auditStatus);
            params.setMerchantName(merchantName);
            result.setResult(ydMerchantPayAuditService.findYdMerchantPayAuditListByPage(params, getPageInfo(request, 1, 10)));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户支付申请审核", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "auditStatus", value = "REFUSE|SUCCESS"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="merchant_pay_update_status",name = "商户支付申请审核")
    @RequestMapping(value = "/payAudit", method = {RequestMethod.POST})
    public BaseResponse<String> payAudit(Integer id, String auditStatus) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantPayAuditService.payAudit(id, auditStatus);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }
}
