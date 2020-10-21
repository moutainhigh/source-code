package com.yd.web.controller.front.register;

import com.yd.api.pay.util.WechatAppPayUtils;
import com.yd.api.pay.util.WexinPublicPayUtils;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.member.YdMemberLevelConfigResult;
import com.yd.api.result.member.YdMerchantMemberPayRecordResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.login.YdLoginService;
import com.yd.api.service.member.YdMemberLevelConfigService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.MathUtils;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.web.controller.FrontBaseController;
import com.yd.web.util.ControllerUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@RestController
@RequestMapping("/front")
public class FrontMerchantRegisterController extends FrontBaseController {

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdLoginService ydAdminLoginService;

    @Reference
    private YdSmsCodeService ydSmsCodeService;

    @Reference
    private YdMemberLevelConfigService ydMemberLevelConfigService;

    @Reference
    private YdMerchantMemberPayRecordService ydMerchantMemberPayRecordService;

    @ApiOperation(value = "商户H5注册页面模板", httpMethod = "POST")
    @RequestMapping("/merchant/register/registerPage")
    public ModelAndView index(HttpServletRequest request, Integer inviteId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("front/register/index.html");
        modelAndView.addObject("inviteId", inviteId);
        return modelAndView;
    }

    @ApiOperation(value = "商户H5开通页面模板", httpMethod = "POST")
    @RequestMapping("/merchant/register/openShop")
    public ModelAndView openShop(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("front/register/openShop.html");
        return modelAndView;
    }

    @ApiOperation(value = "商户微信H5注册成功页面模板", httpMethod = "POST")
    @RequestMapping("/merchant/register/successPage")
    public ModelAndView successPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("front/register/successPage.html");
        return modelAndView;
    }

    @ApiOperation(value = "商户提交注册页面", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "md5加密后的密码"),
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码(String)"),
            @ApiImplicitParam(paramType = "query", name = "inviteId", value = "邀请人id(int)"),
            @ApiImplicitParam(paramType = "query", name = "channel", value = "前台填写H5"),
    })
    @RequestMapping(value = "/merchant/register/saveInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveInfo(String mobile, String password, String smsCode, String channel, Integer inviteId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "提交成功");
        try {
            YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = ydMerchantMemberPayRecordService.
                    addYdMerchantMemberRegisterRecord(mobile, password, smsCode, channel, inviteId);
            result.setResult(ydMerchantMemberPayRecordResult.getId() + "");
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户提交注册页面发验证码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号")
    })
    @RequestMapping(value = "/merchant/register/sendSms", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantMobileSendSms(String mobile) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "发送成功");
        try {
            ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(mobile),
                    "err_mobile_is_empty", "手机号码不可以为空");
            ydSmsCodeService.sendSmsCode(mobile, YdLoginUserSourceEnums.YD_SHOP_USER.getCode(),
                    YdSmsResourceEnum.MERCHANT_REGISTER_MOBILE);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取平台会员等级配置", httpMethod = "POST")
    @RequestMapping(value = "/platform/member/getAll", method = {RequestMethod.POST})
    public BaseResponse<List<YdMemberLevelConfigResult>> getAllMemberConfig() {
        BaseResponse<List<YdMemberLevelConfigResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMemberLevelConfigResult req = new YdMemberLevelConfigResult();
            List<YdMemberLevelConfigResult> dataList = ydMemberLevelConfigService.getAll(req);
            result.setResult(dataList);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户公众号注册点击支付", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "memberPayId", value = "上个页面返回的id"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员id")
    })
    @RequestMapping(value = "/order/platform/member/toPay", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> memberToPay(HttpServletRequest request, Integer memberPayId, Integer memberLevel) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            String openId = ControllerUtil.getCookieValue(request, ControllerUtil.KQ_OPEN_ID);
            ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(openId),
                    "err_empty_openId", "openId不可以为空");

            WbWeixinAccountResult weixinConfig = weixinService.getByWeixinAccountByType("91kuaiqiang");
            String macId = weixinConfig.getMchId();
            String appId = weixinConfig.getAppId();
            String signKey = weixinConfig.getSignKey();

            String sceneWapName = "快抢商城会员充值";
            String outOrderId = SystemPrefixConstants.YD_MERCHANT_MEMBER_PAY_PREFIX + memberPayId;
            String domain = PropertiesHelp.getProperty("webDomain");
            String notifyUrl = domain + "/yd/callback/weixin/merchantMemberPay/notify";
            String returnUrl = "/front/merchant/register/successPage";

            YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = ydMerchantMemberPayRecordService.
                    updateYdMerchantMemberRegisterRecord(memberPayId, memberLevel);
            int totalFee = MathUtils.multiply(ydMerchantMemberPayRecordResult.getMemberPrice(), 100, 0).intValue();

            Map<String,String> resultMap = WexinPublicPayUtils.wechatPay(totalFee, appId, macId, signKey,
                    outOrderId, sceneWapName, "127.0.0.1", openId, returnUrl, notifyUrl);

            result.setResult(resultMap);
        } catch (BusinessException e) {
            e.printStackTrace();
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "商户App注册点击支付", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "memberPayId", value = "上个页面返回的id"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员id")
    })
    @RequestMapping(value = "/order/platform/member/appToPay", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> memberAppToPay(HttpServletRequest request, Integer memberPayId, Integer memberLevel) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantMemberPayRecordResult ydMerchantMemberPayRecordResult = ydMerchantMemberPayRecordService.
                    updateYdMerchantMemberRegisterRecord(memberPayId, memberLevel);
            int totalFee = MathUtils.multiply(ydMerchantMemberPayRecordResult.getMemberPrice(), 100, 0).intValue();

            WbWeixinAccountResult weixinConfig = weixinService.getByWeixinAccountByType("91kuaiqiang");
            String macId = weixinConfig.getMchId();
            // String appId = weixinConfig.getAppId();
            String appId = "wx5e2d7030d00c5e19";
            String signKey = weixinConfig.getSignKey();

            String title = "商户会员下单";
            String outOrderId = SystemPrefixConstants.YD_MERCHANT_MEMBER_PAY_PREFIX + memberPayId;
            String domain = PropertiesHelp.getProperty("webDomain");
            String notifyUrl = domain + "/yd/callback/weixin/merchantMemberPay/notify";

            Map<String, String> resultMap = WechatAppPayUtils.wechatAppPay(appId, macId,signKey,
                    outOrderId, title, "127.0.0.1", null, notifyUrl, totalFee);
            result.setResult(resultMap);
        } catch (BusinessException e) {
            e.printStackTrace();
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
