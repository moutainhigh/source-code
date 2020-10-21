package com.yd.web.controller.admin.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.LoginUser;
import com.yd.api.service.login.YdLoginService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.controller.BaseController;
import com.yd.web.entity.PassportContext;
import com.yd.web.util.ControllerUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

/**
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@RestController
@RequestMapping("/admin/login")
public class AdminLoginController extends BaseController {

    @Reference
    private YdLoginService ydAdminLoginService;

    @Reference
    private YdSmsCodeService ydSmsCodeService;

    @ApiOperation(value = "商户登录", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "商户密码")
    })
    @RequestMapping(value = "/loginIn", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> loginByUsernameAndPassword(HttpServletRequest request, HttpServletResponse response,
                                                           String mobile, String password) throws Exception{
        BaseResponse<String> result = BaseResponse.success(null, "00", "登录成功");
        LoginUser loginUser = ydAdminLoginService.adminLogin(mobile, password);
        // 商户信息写入cookie
        ControllerUtil.addUserCookie(response, PassportContext.init((JSONObject) JSON.toJSON(loginUser)));
        return result;
    }

    @ApiOperation(value = "商户登出", httpMethod = "GET")
    @RequestMapping(value = "/loginOut", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> loginOut(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "登出成功");
        // 删除登录cookie信息
        ControllerUtil.delUserCookie(response);
        return result;
    }

    @ApiOperation(value = "商户找回密码", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "商户密码"),
            @ApiImplicitParam(paramType = "query", name = "smsCode", value = "短信验证码密码")
    })
    @RequestMapping(value = "/forgetPassword", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> forgetPassword(HttpServletRequest request, String mobile, String password, String smsCode) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydAdminLoginService.forgetPassword(mobile, password, smsCode);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户找回密码发短信", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号")
    })
    @RequestMapping(value = "/forgetPassword/sendSms", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> sendSms(String mobile) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "短信发送成功");
        try {
            ydSmsCodeService.sendSmsCode(mobile, YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
                    YdSmsResourceEnum.MERCHANT_FORGET_PASSWORD);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }


}
