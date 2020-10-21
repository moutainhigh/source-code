package com.yd.web.controller.app.account;

/**
 * 商户账户
 * @author wuyc
 * created 2019/10/22 10:33
 **/
import com.alibaba.fastjson.JSON;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.pay.util.WechatPaymentCodePayUtils;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.api.result.merchant.YdMerchantTransResult;
import com.yd.api.result.order.YdMerchantRechargeOrderResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.merchant.YdMerchantAccountService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.merchant.YdMerchantWithdrawService;
import com.yd.api.service.order.YdMerchantRechargeOrderService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.*;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.controller.front.order.FrontOrderController;
import com.yd.web.util.QrCodeUtil;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@RestController
// @Api(value = "APP-余额账户-controller", tags={"APP-余额账户-controller"})
@AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_account",name = "账户管理")
@RequestMapping("/app/merchant/account")
public class AppMerchantAccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppMerchantAccountController.class);

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantTransService ydMerchantTransService;

    @Reference
    private YdMerchantAccountService ydMerchantAccountService;

    @Reference
    private YdMerchantWithdrawService ydMerchantWithdrawService;

    @Reference
    private YdMerchantRechargeOrderService ydMerchantRechargeOrderService;

    @ApiOperation(value = "查询商户账户数据", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_data", name="查询商户账户数据")
    @RequestMapping(value = "/findMerchantAccount", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantAccountResult> findMerchantAccount(HttpServletRequest request) {
        BaseResponse<YdMerchantAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(ydMerchantAccountService.getYdMerchantAccountByMerchantId(merchantId));
        return result;
    }

    @ApiOperation(value = "查询商户账户流水", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "transType", value = "交易类型"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "交易开始时间")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_trans_data", name="查询商户账户流水")
    @RequestMapping(value = "/findMerchantTransList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantTransResult>> findMerchantTransList(HttpServletRequest request, String transType, String startTime) {
        BaseResponse<Page<YdMerchantTransResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);

        String endTime = null;
        if (org.apache.commons.lang3.StringUtils.isEmpty(startTime)) {
            Date today = new Date();
            startTime = DateUtils.getDateTime(DateUtils.getMinDateByMonth(today), DateUtils.DEFAULT_DATE_FORMAT);
            endTime = DateUtils.getDateTime(DateUtils.getMaxDateByMonth(today), DateUtils.DEFAULT_DATE_FORMAT);
        } else {
            startTime = startTime + "-01";
            endTime = DateUtils.getDateTime(DateUtils.getMaxDateByMonth(DateUtils.getDateTime(startTime)), DateUtils.DEFAULT_DATE_FORMAT);
        }
        logger.info("====startTime=" + startTime + ", endTime=" + endTime);
        result.setResult(ydMerchantTransService.getMerchantTransListByPage(merchantId, null, transType, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "查询商户账户流水详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "列表里的id，int类型")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_trans_data", name="查询商户账户流水")
    @RequestMapping(value = "/getMerchantTransDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantTransResult> getMerchantTransDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantTransResult> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantTransResult ydMerchantTransResult = new YdMerchantTransResult();
        ydMerchantTransResult.setId(id);
        List<YdMerchantTransResult> dataList = ydMerchantTransService.getAll(ydMerchantTransResult);
        if (CollectionUtils.isNotEmpty(dataList)) {
            result.setResult(dataList.get(0));
        }
        return result;
    }

    @ApiOperation(value = "账户余额转入礼品账户", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "intoPrice", value = "转入礼品账户的余额")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_trans_into_gift", name="查询商户账户数据")
    @RequestMapping(value = "/intoGiftAccount", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantAccountResult> intoGiftAccount(HttpServletRequest request,
                                                                 @NotEmpty(message = "密转入金额不能为空") Double intoPrice) {
        BaseResponse<YdMerchantAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        ydMerchantAccountService.intoGiftAccount(merchantId, intoPrice);
        return result;
    }

    @ApiOperation(value = "开通支付服务", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_open_pay", name="查询商户账户数据")
    @RequestMapping(value = "/openPay", method = {RequestMethod.POST})
    public BaseResponse<String> openPay(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydMerchantService.openPay(merchantId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户余额提现", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "withdrawAmount", value = "大于1小于2万")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_account_balance_withdraw", name="商户余额提现")
    @RequestMapping(value = "/merchantWithdraw", method = {RequestMethod.POST})
    public BaseResponse<String> merchantWithdraw(HttpServletRequest request, Double withdrawAmount) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantWithdrawService.merchantWithdraw(getCurrMerchantId(request), withdrawAmount);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "生成充值二维码，用户扫码后会充值", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "payPrice", value = "充值金额")
    @AppMerchantCheck()
    @RequestMapping(value = "/wechat/getQrcode", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Map<String, Object>> getRechargeQrcode(HttpServletRequest request, HttpServletResponse response, Double payPrice) {
        BaseResponse<Map<String, Object>> baseResponse = BaseResponse.success(null, "00", "生成成功");
        try {
            YdMerchantRechargeOrderResult params = new YdMerchantRechargeOrderResult();
            params.setMerchantId(getCurrMerchantId(request));
            params.setPayPrice(payPrice);
            YdMerchantRechargeOrderResult rechargeOrderInfo = ydMerchantRechargeOrderService.insertYdMerchantRechargeOrder(params);

            WbWeixinAccountResult weixinConfig = weixinService.getByWeixinAccountByType("91kuaiqiang");
            String macId = weixinConfig.getMchId();
            String appId = weixinConfig.getAppId();
            String signKey = weixinConfig.getSignKey();

            String sceneWapName = "优度商城";
            String sceneWapUrl = "http://prev-saas.guijitech.com/";
            String outOrderId = SystemPrefixConstants.YD_APP_QR_CODE_PREFIX + rechargeOrderInfo.getId();
            String domain = PropertiesHelp.getProperty("webDomain");
            String notifyUrl = domain + "/yd/callback/weixin/qrCode/notify";
            int totalFee = MathUtils.multiply(rechargeOrderInfo.getPayPrice(), 100, 0).intValue();

            WechatPaymentCodePayUtils.WechatSubmitH5PayResponse result = WechatPaymentCodePayUtils.submitPaymentCodeToPay(outOrderId, totalFee,
                    "商户扫码充值", "127.0.0.1", notifyUrl, sceneWapUrl, sceneWapName, macId, appId, signKey);

            logger.info("====APP扫一扫调用微信下单返回数据=" + JSON.toJSONString(result));
            if (result.getSuccess() && StringUtils.isNotEmpty(result.getCodeUrl())) {
                Map<String, Object> data = new HashMap<>();
                String qrCodeUrl = QrCodeUtil.makeQrCode(result.getCodeUrl(), 500, 500);
                data.put("payPrice", payPrice);
                data.put("qrCodeUrl", qrCodeUrl);
                baseResponse.setResult(data);
            } else {
                baseResponse = BaseResponse.success(null, "00", "生成失败，调用微信失败");
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("====APP扫一扫生成二维码失败" + e.getLocalizedMessage());
            baseResponse = BaseResponse.success(null, "00", "APP扫一扫生成二维码失败");
        }
        return baseResponse;
    }

}
