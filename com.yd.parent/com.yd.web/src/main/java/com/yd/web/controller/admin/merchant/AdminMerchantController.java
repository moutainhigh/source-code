package com.yd.web.controller.admin.merchant;

/**
 * 后台商户管理
 * @author wuyc
 * created 2019/10/24 16:31
 **/
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.enums.YdRoleTypeEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.util.QrCodeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@MerchantCheck
@RequestMapping("/admin/merchant")
public class AdminMerchantController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @ApiOperation(value = "修改门店详细信息", httpMethod = "POST")
    @MerchantCheck
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
    @MerchantCheck
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "不传查询当前账号信息"),
    })
    @RequestMapping(value = "/getMerchantDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantResult> getMerchantDetail(HttpServletRequest request, Integer merchantId) {
        BaseResponse<YdMerchantResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (merchantId == null || merchantId == 0) {
                merchantId = getCurrMerchantId(request);
            }
            result.setResult(ydMerchantService.getYdMerchantById(merchantId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {

        }
        return result;
    }

    @ApiOperation(value = "更换门店手机号发验证码", httpMethod = "POST")
    @MerchantCheck
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

    @ApiOperation(value = "更换门店手机号发验证码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码"),
    })
    @MerchantCheck
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
}
