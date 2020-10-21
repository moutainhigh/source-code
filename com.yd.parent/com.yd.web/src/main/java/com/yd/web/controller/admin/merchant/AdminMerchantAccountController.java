package com.yd.web.controller.admin.merchant;

/**
 * 商户账户
 * @author wuyc
 * created 2019/10/22 10:33
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.api.result.merchant.YdMerchantTransResult;
import com.yd.api.service.merchant.YdMerchantAccountService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.merchant.YdMerchantWithdrawService;
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
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/admin/merchant/account")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_account",name = "账户管理")
public class AdminMerchantAccountController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantTransService ydMerchantTransService;

    @Reference
    private YdMerchantAccountService ydMerchantAccountService;

    @Reference
    private YdMerchantWithdrawService ydMerchantWithdrawService;

    @ApiOperation(value = "商户账户数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_data", name="商户账户数据")
    @RequestMapping(value = "/findMerchantAccount", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantAccountResult> findMerchantAccount(HttpServletRequest request) {
        BaseResponse<YdMerchantAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(ydMerchantAccountService.getYdMerchantAccountByMerchantId(merchantId));
        return result;
    }

    @ApiOperation(value = "商户账户流水列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = false),
            @ApiImplicitParam(paramType = "query", name = "transStatus", value = "交易状态", required = false),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", required = false),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = false)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_trans_data", name="商户账户流水列表")
    @RequestMapping(value = "/findMerchantTransList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantTransResult>> findMerchantTransList(HttpServletRequest request, String orderId,
                                                                           String transStatus, String startTime, String endTime) {
        BaseResponse<Page<YdMerchantTransResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydMerchantTransService.getMerchantTransListByPage(merchantId, orderId, transStatus, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "商户账户余额转入礼品账户", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "intoPrice", value = "转入礼品账户的余额")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_trans_into_gift", name="商户账户余额转入礼品账户")
    @RequestMapping(value = "/intoGiftAccount", method = {RequestMethod.POST})
    public BaseResponse<String> intoGiftAccount(HttpServletRequest request,
                                                                 @NotEmpty(message = "密转入金额不能为空") Double intoPrice) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        ydMerchantAccountService.intoGiftAccount(merchantId, intoPrice);
        return result;
    }

    @ApiOperation(value = "商户开通支付服务", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_open_pay", name="查询商户账户数据")
    @RequestMapping(value = "/openPay", method = {RequestMethod.POST})
    public BaseResponse<String> openPay(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.openPay(getCurrMerchantId(request));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户余额提现", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "withdrawAmount", value = "大于1小于2万")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_balance_withdraw", name="商户余额提现")
    @RequestMapping(value = "/merchantWithdraw", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> merchantWithdraw(HttpServletRequest request, Double withdrawAmount) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantWithdrawService.merchantWithdraw(getCurrMerchantId(request), withdrawAmount);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
