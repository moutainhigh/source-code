package com.yd.web.controller.app.merchant;

/**
 * app商户信息
 * @author wuyc
 * created 2019/10/24 16:31
 **/
import com.yd.api.pay.util.WechatAppPayUtils;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.member.YdMerchantMemberOpenRecordResult;
import com.yd.api.result.member.YdMerchantMemberPayRecordResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.member.YdMerchantMemberOpenRecordService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.MathUtils;
import com.yd.core.utils.PropertiesHelp;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/merchant")
public class AppMerchantController extends BaseController {

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantMemberOpenRecordService ydMerchantMemberOpenRecordService;

    @Reference
    private YdMerchantMemberPayRecordService ydMerchantMemberPayRecordService;

    @ApiOperation(value = "修改门店详细信息", httpMethod = "POST")
    @AppMerchantCheck
    @RequestMapping(value = "/updateMerchantInfo", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantInfo(HttpServletRequest request, YdMerchantResult ydMerchantResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(getCurrMerchantId(request));
            ydMerchantResult.setId(storeInfo.getId());
            ydMerchantService.updateMerchantInfo(ydMerchantResult);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询门店详细信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "不传查询当前账号信息"),
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getMerchantDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<YdMerchantResult> getMerchantDetail(HttpServletRequest request, Integer merchantId) {
        BaseResponse<YdMerchantResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (merchantId == null || merchantId == 0) {
                merchantId = getCurrMerchantId(request);
            }
            YdMerchantResult ydMerchantResult = ydMerchantService.getYdMerchantById(merchantId);

            Integer storeId = ydMerchantResult.getId();
            YdMerchantResult storeInfo = new YdMerchantResult();
            if ((YdRoleTypeEnum.ROLE_OPERATOR.getCode() + "").equalsIgnoreCase(ydMerchantResult.getRoleIds())) {
                YdMerchantResult data = ydMerchantService.getYdMerchantById(ydMerchantResult.getPid());
                storeId = ydMerchantResult.getPid();
                BeanUtilExt.copyProperties(storeInfo, data);
                storeInfo.setRoleIds(ydMerchantResult.getRoleIds());
                storeInfo.setId(ydMerchantResult.getId());
                storeInfo.setRoleIds(ydMerchantResult.getRoleIds());
                storeInfo.setMobile(ydMerchantResult.getMobile());
                // 用户名为空，默认取值手机号
                storeInfo.setMerchantName(StringUtils.isEmpty(ydMerchantResult.getMerchantName())
                        ? ydMerchantResult.getMobile() : ydMerchantResult.getMerchantName());
            } else {
                BeanUtilExt.copyProperties(storeInfo, ydMerchantResult);
            }

            String domain = PropertiesHelp.getProperty("webDomain");
            String shopUrl = domain + "/store/front/" + storeId + "/index";

            if (StringUtils.isEmpty(storeInfo.getShopQrCode())) {
                String shopQrCode = QrCodeUtil.makeQrCode(shopUrl, 500, 500);
                ydMerchantService.updateShopQrCode(storeId, shopQrCode);
            }
            storeInfo.setShopUrl(shopUrl);
            result.setResult(storeInfo);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {

        }
        return result;
    }

    @ApiOperation(value = "更换门店手机号发验证码", httpMethod = "POST")
    @RequestMapping(value = "/updateMerchantMobile/sendSms", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantMobileSendSms(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "发送成功");
        try {
            ydMerchantService.updateMerchantMobileSendSms(getCurrMerchantId(request));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "更换门店手机号校验短信验证码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码"),
    })
    @RequestMapping(value = "/updateMobile/checkSmsCode", method = {RequestMethod.POST})
    public BaseResponse<Boolean> checkUpdateMerchantMobileSmsCode(HttpServletRequest request, String smsCode) {
        BaseResponse<Boolean> result = BaseResponse.success(null, "00", "发送成功");
        try {
            boolean checkResult = ydMerchantService.checkUpdateMerchantMobileSmsCode(getCurrMerchantId(request), smsCode);
            result.setResult(checkResult);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户会员开通记录", httpMethod = "POST")
    @AppMerchantCheck
    @RequestMapping(value = "/memberOpenRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<List<YdMerchantMemberOpenRecordResult>> memberOpenRecord(HttpServletRequest request) {
        BaseResponse<List<YdMerchantMemberOpenRecordResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantMemberOpenRecordResult req = new YdMerchantMemberOpenRecordResult();
            req.setMerchantId(getCurrMerchantId(request));
            List<YdMerchantMemberOpenRecordResult> dataList = ydMerchantMemberOpenRecordService.getAll(req);
            result.setResult(dataList);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户升级商户会员", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员等级")
    })
    @AppMerchantCheck
    @RequestMapping(value = "/memberUpgrade", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> memberUpgrade(HttpServletRequest request, Integer memberLevel) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdMerchantMemberPayRecordResult payRecordResult = ydMerchantMemberPayRecordService.addYdMerchantMemberSjRecord(getCurrMerchantId(request), memberLevel);
            result.setResult(wechatAppToPay(payRecordResult));
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户续费商户会员", httpMethod = "POST")
    @AppMerchantCheck
    @RequestMapping(value = "/memberRenewal", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> memberRenewal(HttpServletRequest request, Integer memberLevel) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdMerchantMemberPayRecordResult payRecordResult = ydMerchantMemberPayRecordService.addYdMerchantMemberXfRecord(getCurrMerchantId(request), memberLevel);
            result.setResult(wechatAppToPay(payRecordResult));
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    private Map<String, String> wechatAppToPay(YdMerchantMemberPayRecordResult payRecordResult) throws BusinessException{
        WbWeixinAccountResult weixinConfig = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String macId = weixinConfig.getMchId();
        // String appId = weixinConfig.getAppId();
        String appId = "wx5e2d7030d00c5e19";
        String signKey = weixinConfig.getSignKey();

        String title = "商户会员下单";
        String domain = PropertiesHelp.getProperty("webDomain", "http://prev-saas.guijitech.com");
        String notifyUrl = domain + "/yd/callback/weixin/merchantMemberPay/notify";
        String outOrderId = SystemPrefixConstants.YD_MERCHANT_MEMBER_PAY_PREFIX + payRecordResult.getId();
        int totalFee = MathUtils.multiply(payRecordResult.getMemberPrice(), 100, 0).intValue();

        Map<String, String> resultMap = WechatAppPayUtils.wechatAppPay(appId, macId,signKey,
                outOrderId, title, "127.0.0.1", null, notifyUrl, totalFee);
        return resultMap;
    }

}
