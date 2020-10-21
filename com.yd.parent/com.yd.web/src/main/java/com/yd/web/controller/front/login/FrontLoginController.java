package com.yd.web.controller.front.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.LoginUser;
import com.yd.api.service.login.YdLoginService;
import com.yd.core.res.BaseResponse;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.controller.FrontBaseController;
import com.yd.web.entity.PassportContext;
import com.yd.web.util.ControllerUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@Controller
@RequestMapping("/front/login")
public class FrontLoginController extends FrontBaseController {

    @Reference
    private YdLoginService ydAdminLoginService;

    @RequestMapping(value = "/loginIn", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResponse<String> frontLoginIn(HttpServletResponse response, String mobile,String password) throws Exception{
        BaseResponse<String> result = BaseResponse.success(null, "00", "登录成功");
        LoginUser loginUser = ydAdminLoginService.frontLogin(mobile, password);
        // 用户信息信息写入cookie
        ControllerUtil.addUserCookie(response, PassportContext.init((JSONObject) JSON.toJSON(loginUser)));
        return result;
    }

    @FrontCheck
    @RequestMapping(value = "/loginOut", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResponse<String> frontLoginOut(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "登出成功");
        // 删除登录cookie信息
        ControllerUtil.delUserCookie(response);
        return result;
    }

}
