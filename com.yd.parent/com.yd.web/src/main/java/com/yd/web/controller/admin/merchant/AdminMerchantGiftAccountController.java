package com.yd.web.controller.admin.merchant;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantGiftTransResult;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
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

/**
 * 商户礼品账户controller
 * @author wuyc
 * created 2019/11/5 10:06
 **/
@RestController
@RequestMapping("/admin/merchant/gift/account")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mode_merchant_gift", name = "礼品管理")
public class AdminMerchantGiftAccountController extends BaseController {

    @Reference
    private YdMerchantGiftAccountService ydMerchantGiftAccountService;

    @Reference
    private YdMerchantGiftTransService ydMerchantGiftTransService;

    @ApiOperation(value = "查询商户礼品账户数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_account_data", name="查询商户礼品账户数据")
    @RequestMapping(value = "/findMerchantGiftAccount", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantGiftAccountResult> findMerchantGiftAccount(HttpServletRequest request) {
        BaseResponse<YdMerchantGiftAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(merchantId));
        return result;
    }

    @ApiOperation(value = "商户礼品账户流水", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "giftOrderId", value = "礼品订单id"),
            @ApiImplicitParam(paramType = "query", name = "transType", value = "交易类型"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "交易开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "交易结束时间")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_account_trans_data", name="商户礼品账户流水")
    @RequestMapping(value = "/findMerchantGiftTransList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantGiftTransResult>> findMerchantGiftTransList(HttpServletRequest request, String giftOrderId,
                                                                           String transType, String startTime, String endTime) {
        BaseResponse<Page<YdMerchantGiftTransResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydMerchantGiftTransService.getMerchantTransListByPage(merchantId, giftOrderId, transType, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "商户设置支付密码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "支付密码", required = true),
            @ApiImplicitParam(paramType = "query", name = "password2", value = "支付密码", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_set_pay_password", name="商户设置支付密码")
    @RequestMapping(value = "/setPayPassword", method = {RequestMethod.POST})
    public BaseResponse<String> setPayPassword(HttpServletRequest request, String smsCode, String password, String password2) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantGiftTransService.setPayPassword(getCurrMerchantId(request), smsCode, password, password2);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户设置支付密码发送短信", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_send_pay_password_sms", name="商户设置支付密码发送短信")
    @RequestMapping(value = "/sendPayPasswordSms", method = {RequestMethod.POST})
    public BaseResponse<String> sendPayPasswordSms(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantGiftTransService.sendPayPasswordSms(getCurrMerchantId(request));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品余额转入账户余额", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "transPrice", value = "转入账户余额的钱")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_trans_into_account", name="商户礼品余额转入账户余额")
    @RequestMapping(value = "/transIntoAccount", method = {RequestMethod.POST})
    public BaseResponse<String> transIntoAccount(HttpServletRequest request, Double transPrice) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantGiftTransService.transIntoAccount(getCurrMerchantId(request), transPrice);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }


}
