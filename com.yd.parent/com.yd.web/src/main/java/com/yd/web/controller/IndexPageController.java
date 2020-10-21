package com.yd.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yd.api.result.user.YdUserMerchantResult;
import com.yd.api.service.user.YdUserMerchantService;
import com.yd.core.utils.BusinessException;
import com.yd.web.entity.SiteContextThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.yd.api.Consts;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.anotation.LoginStatus;


@Controller
public class IndexPageController {

    private static final Logger logger = LoggerFactory.getLogger(IndexPageController.class);

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdUserMerchantService ydUserMerchantService;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mav=new ModelAndView("redirect:/admin/index.html");
    	
    	return mav;
    }
	
    @RequestMapping("/store/{storeId}")
    public ModelAndView adminIndex(HttpServletRequest request, HttpServletResponse response,@PathVariable int storeId) {
    	ModelAndView mav=new ModelAndView("front/index");
    	
    	return mav;
    }
	
    @RequestMapping("/admin/index.html")
    public ModelAndView adminIndex(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mav=new ModelAndView("admin/index");
    	
    	return mav;
    }

    @LoginStatus
    @RequestMapping("/store/front/{merchantId}/**")
    public ModelAndView storeIndex(HttpServletRequest request, HttpServletResponse response, @PathVariable int merchantId) {
        ModelAndView modelAndView = new ModelAndView("front/index.html");
        
        String userAgent = request.getHeader("User-Agent");
        String isLogin = (String)request.getAttribute(Consts.IS_USER_LOGIN_KEY);
        String requestURI = request.getRequestURI();
        logger.info("===========merchantId========" + merchantId + ", isLogin=" + isLogin + ", requestURI=" + requestURI);
        if(!"true".equals(isLogin) && userAgent.toLowerCase().contains("micromessenger")) {
        	modelAndView.setViewName("redirect:"+PropertiesHelp.getProperty("webDomain")+"/front/weixin/user/auth/login?sendURL=" + requestURI);
        	return modelAndView;
        }
        
        try {
            YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
            modelAndView.addObject("merchantId", storeInfo.getId());
        } catch (Exception e) {
            e.getLocalizedMessage();
            modelAndView.addObject("merchantId", 0);
        }
        return modelAndView;
    }

    @LoginStatus
    @RequestMapping("/front/merchant/index/select")
    public ModelAndView selectMerchant(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        // 1. 判断用户是否授权，没授权，先授权
        String requestURI = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String isLogin = (String)request.getAttribute(Consts.IS_USER_LOGIN_KEY);
        logger.info("====userAgent.toLowerCase=" + userAgent.toLowerCase() + "isLogin=" + isLogin);
        if(!"true".equals(isLogin) && userAgent.toLowerCase().contains("micromessenger")) {
            modelAndView.setViewName("redirect:"+PropertiesHelp.getProperty("webDomain")+"/front/weixin/user/auth/login?sendURL=" + requestURI);
            return modelAndView;
        }

        try {
            // 2. 根据用户id查询绑定关系，可以查询到绑定关系跳转门店首页，查询不到默认跳转门店选择页面
            String userId = SiteContextThreadLocal.get().getPassport().getPairMap().get("userId");
            logger.info("====userId=" + userId);
            YdUserMerchantResult ydUserMerchantResult = ydUserMerchantService.getYdUserMerchantByUserId(Integer.valueOf(userId));
            if (ydUserMerchantResult == null) {
                modelAndView.setViewName("redirect:" + PropertiesHelp.getProperty("webDomain") + "/store/front/10049/index/allMerchantList");
                return modelAndView;
            } else {
                modelAndView.setViewName("redirect:" + PropertiesHelp.getProperty("webDomain") + "/store/front/" + ydUserMerchantResult.getMerchantId() + "/index");
            }
        } catch (BusinessException e) {
            modelAndView.setViewName("redirect:" + PropertiesHelp.getProperty("webDomain") + "/store/front/10049/index/allMerchantList");
        }
        return modelAndView;
    }



}
