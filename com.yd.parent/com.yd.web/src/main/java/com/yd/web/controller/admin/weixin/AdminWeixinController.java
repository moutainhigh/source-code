package com.yd.web.controller.admin.weixin;

import com.yd.api.pay.util.WechatPaymentCodePayUtils;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.order.YdMerchantRechargeOrderResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdMerchantRechargeOrderService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.MathUtils;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.WechatUtil;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.QrCodeUtil;
import com.yd.web.util.WebUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wuyc
 * created 2020/1/13 15:08
 **/
@Controller
// @MerchantCheck()
public class AdminWeixinController extends BaseController {

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantRechargeOrderService ydMerchantRechargeOrderService;

    @ApiOperation(value = "获取展示微信绑定二维码的ticket", httpMethod = "POST")
    @MerchantCheck()
    @ResponseBody
    @RequestMapping(value = "/admin/wechat/getTicket", method = {RequestMethod.POST})
        public BaseResponse<String> findList(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        String type=WebUtil.getParameter(request, "type","bind");
        try {
            WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
            // 获取accessToken
            String accessToken = WechatUtil.getAccessToken(weixinAccountResult.getAppId(), weixinAccountResult.getAppSecret());
            // 获取ticket
            if("bind".equals(type)) {
            	result.setResult(WechatUtil.getWxQrcodeTicket(accessToken, "bindWithdraw-"+getCurrMerchantId(request)));
            }else if("reBind".equals(type)){
            	result.setResult(WechatUtil.getWxQrcodeTicket(accessToken, "reBindWithdraw-"+getCurrMerchantId(request)));
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "解除绑定微信账号", httpMethod = "POST")
    @MerchantCheck()
    @ResponseBody
    @RequestMapping(value = "/admin/wechat/unbindWechatAccount", method = {RequestMethod.POST})
    public BaseResponse<Boolean> unbindWechatAccount(HttpServletRequest request) {
        BaseResponse<Boolean> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantService.unbindWechatAccount(getCurrMerchantId(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "生成充值二维码，用户扫码后会充值", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "payPrice", value = "充值金额")
    // @MerchantCheck()
    @RequestMapping(value = "/admin/wechat/getRechargeQrcode", method = {RequestMethod.GET, RequestMethod.POST})
    public void getRechargeQrcode(HttpServletRequest request, HttpServletResponse response, Double payPrice) {
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
            String outOrderId = SystemPrefixConstants.YD_RECHARGE_PREFIX + rechargeOrderInfo.getId();
            String domain = PropertiesHelp.getProperty("webDomain");
            String notifyUrl = domain + "/yd/callback/weixin/rechargePay/notify";
            int totalFee = MathUtils.multiply(rechargeOrderInfo.getPayPrice(), 100, 0).intValue();

            WechatPaymentCodePayUtils.WechatSubmitH5PayResponse result = WechatPaymentCodePayUtils.submitPaymentCodeToPay(outOrderId, totalFee,
                    "商户扫码充值", "127.0.0.1", notifyUrl, sceneWapUrl, sceneWapName, macId, appId, signKey);

            if (result.getSuccess() && StringUtils.isNotEmpty(result.getCodeUrl())) {
                QrCodeUtil.makeQrCode(result.getCodeUrl(), 500, 500, response);
            }
        } catch (Exception e) {

        }
    }

}
