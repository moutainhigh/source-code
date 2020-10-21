package com.yd.web.controller.front.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.LoginUser;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.user.YdUserAuthResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.login.YdLoginService;
import com.yd.api.service.user.YdUserAuthService;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.WechatUtil;
import com.yd.web.controller.FrontBaseController;
import com.yd.web.entity.PassportContext;
import com.yd.web.util.ControllerUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口controller
 * @author wuyc
 * created 2019/12/11 10:18
 **/
@Controller
@RequestMapping("/front/weixin")
public class FrontWeChatController extends FrontBaseController {

    private static final Logger logger = LoggerFactory.getLogger(FrontWeChatController.class);

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdLoginService ydLoginService;

    @Reference
    private YdUserAuthService ydUserAuthService;


    @RequestMapping("/user/auth/login")
    public ModelAndView authLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("==== come in authLogin ====");
        ModelAndView modelAndView = new ModelAndView();

        String sendURL = request.getParameter("sendURL");
        sendURL = StringUtils.isEmpty(sendURL) ? "/" : sendURL;
        logger.info("====senUrl====" + sendURL);

        // 获取 appId, appSecret
        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String appId = weixinAccountResult.getAppId();
        String appSecret = weixinAccountResult.getAppSecret();

        // 获取code, code 为空，跳转到授权页面, 不为空进行业务处理
        String code = request.getParameter("code");
        logger.info("====微信授权获取的code=" + code);
        if (StringUtils.isNotEmpty(code)) {
            Map<String, String> accessTokenMap = WechatUtil.getAccessToken(appId, appSecret, code);
            logger.info("accessTokenMap===" + JSON.toJSONString(accessTokenMap));
            String openId = accessTokenMap.get("openId");
            String accessToken = accessTokenMap.get("accessToken");
            if (StringUtils.isNotEmpty(openId)) {
                // 根据openId查询用户信息, 不存在保存用户, 将用户id信息写入cookie
                YdUserAuthResult ydUserAuthResult = ydUserAuthService.addUserAuth(openId, accessToken);
                LoginUser loginUser = ydLoginService.frontLogin(ydUserAuthResult.getUserId());
                ControllerUtil.addUserCookie(response, PassportContext.init((JSONObject) JSON.toJSON(loginUser)));
                logger.info("授权登录写入的cookie信息" + JSON.toJSON(ydUserAuthResult));
            } else {
                logger.info("openId不存在");
            }
        } else {
            String autoLoginUrl = PropertiesHelp.getProperty("webDomain") + "/front/weixin/user/auth/login";
            String redirectUrl = WechatUtil.getWeixinAuthorizationUrlForPlatform(autoLoginUrl, sendURL, appId, "N");
            modelAndView.setViewName("redirect:" + redirectUrl);
            return modelAndView;
        }
        logger.info("跳转到路径:" + sendURL);
        modelAndView.setViewName("redirect:" + sendURL);
        return modelAndView;
    }


    @RequestMapping("/auth/getOpenId")
    public ModelAndView getOpenId(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        String sendURL = request.getParameter("sendURL");
        if (StringUtils.isEmpty(sendURL)) {
            sendURL = "/";
        }
        logger.info("====商户注册获取openId的senUrl====" + sendURL);

        String code = request.getParameter("code");
        String memberPayId = request.getParameter("memberPayId");
        logger.info("======微信临时授权获取code:" + code + ",memberPayId=" + memberPayId);
        // 获取 appId, appSecret
        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String appId = weixinAccountResult.getAppId();
        String appSecret = weixinAccountResult.getAppSecret();

        String openId = null;
        if (StringUtils.isNotEmpty(code)) {
            Map<String, String> accessTokenMap = WechatUtil.getAccessToken(appId, appSecret, code);
            logger.info("accessTokenMap===" + JSON.toJSONString(accessTokenMap));
            openId = accessTokenMap.get("openId");
            ControllerUtil.addCookie(response, ControllerUtil.KQ_OPEN_ID,  openId, 1 * 1 * 60 * 60);
        } else {
            String autoLoginUrl = PropertiesHelp.getProperty("webDomain") + "/front/weixin/auth/getOpenId";
            String redirectUrl = WechatUtil.getWeixinOpenIdUrl(autoLoginUrl, sendURL, appId);
            mav.setViewName("redirect:" + redirectUrl);
            return mav;
        }
        if (sendURL.contains("?")) {
            sendURL += "&openId=" + openId;
        } else {
            sendURL += "?openId=" + openId;
        }
        logger.info("======getOpenId的redirect=" + sendURL);
        mav.setViewName("redirect:" + sendURL);
        return mav;
    }

}
