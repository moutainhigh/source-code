package com.yd.web.controller.front.user;

import com.yd.api.result.user.YdUserResult;
import com.yd.api.service.user.YdUserService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息
 * @author wuyc
 * created 2019/11/8 13:37
 **/
@RestController
@RequestMapping("/front/user/info")
public class FrontUserInfoController extends FrontBaseController {

    @Reference
    private YdUserService ydUserService;

    @ApiOperation(value = "查询用户个人信息", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.POST})
    public BaseResponse<YdUserResult> getUserInfo(HttpServletRequest request) {
        BaseResponse<YdUserResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            result.setResult(ydUserService.getYdShopUserById(userId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户绑定手机号发送验证码", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号")
    })
    @FrontCheck
    @RequestMapping(value = "/sendUserBindMobileSms", method = {RequestMethod.POST})
    public BaseResponse<String> sendUserBindMobileSms(HttpServletRequest request, String mobile) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "发送成功");
        try {
            Integer userId = getCurrUserId(request);
            ydUserService.sendUserBindMobileSms(userId, mobile);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户绑定手机号", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "短信验证码"),
    })
    @FrontCheck
    @RequestMapping(value = "/userBindMobile", method = {RequestMethod.POST})
    public BaseResponse<String> userBindMobile(HttpServletRequest request, String mobile, String code) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "绑定成功");
        try {
            Integer userId = getCurrUserId(request);
            ydUserService.userBindMobile(userId, mobile, code);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "修改用户头像", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "imageUrl", value = "头像")
    })
    @FrontCheck
    @RequestMapping(value = "/updateUserImage", method = {RequestMethod.POST})
    public BaseResponse<String> updateUserImage(HttpServletRequest request, String imageUrl) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "绑定成功");
        try {
            Integer userId = getCurrUserId(request);
            ydUserService.updateUserImage(userId, imageUrl);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }


}
