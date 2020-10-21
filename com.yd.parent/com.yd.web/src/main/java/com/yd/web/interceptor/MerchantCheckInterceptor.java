package com.yd.web.interceptor;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yd.api.service.login.YdLoginService;
import com.yd.api.service.permission.PermissionService;
import com.yd.core.constants.AdminConstants;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.utils.PropertiesHelp;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.core.utils.StringUtil;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.entity.SiteContextThreadLocal;

@Component
public class MerchantCheckInterceptor implements HandlerInterceptor {

    private final static Logger LOG = LoggerFactory.getLogger(FrontCheckInterceptor.class);

    private ThreadLocal<Long> begin = new ThreadLocal<>();

    @Reference
    private YdLoginService ydLoginService;

    @Reference
    private PermissionService permissionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod handlerMethod = null;
        if (handler instanceof HandlerMethod) {
        	handlerMethod = (HandlerMethod) handler;
        } else {
            return true;
        }
        begin.set(System.currentTimeMillis());

        // 方法上的 权限控制
        MerchantCheck methodAuth = handlerMethod.getMethodAnnotation(MerchantCheck.class);
        // 类上的权限控制
        MerchantCheck clzAuth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(MerchantCheck.class);
        // 是否是ajax
        ResponseBody responseBodyAnn = handlerMethod.getMethodAnnotation(ResponseBody.class);

        if (methodAuth != null || clzAuth!=null) {
            String merchantId = SiteContextThreadLocal.get().getPassport().getPairMap().get("merchantId");
            String sessionId = SiteContextThreadLocal.get().getPassport().getPairMap().get("sessionId");
            System.out.println("admin-merchantId=" + merchantId + ",sessionId = " + sessionId);
            String url = request.getRequestURI();
            System.out.println("admin-请求全路径url = " + url);
            if (!StringUtil.isEmpty(request.getQueryString())) {
                url = url + "?" + request.getQueryString();
            }
            System.out.println("admin-请求url = " + url);
            if (StringUtils.isEmpty(merchantId)||StringUtils.isEmpty(sessionId)) {
                processNotLogin(url, response, request,handlerMethod);
                return false;
            }
            boolean isLogin = ydLoginService.checkUserLogin(YdLoginUserSourceEnums.YD_ADMIN_MERCHANT, merchantId, sessionId);
            if (!isLogin) {
                processNotLogin(url, response, request,handlerMethod);
                return false;
            }

            // 校验权限
            if (!checkPermission(Integer.parseInt(merchantId), url, methodAuth, clzAuth)) {
                processNotPermission(url, responseBodyAnn, response);
                return false;
            }
        }

        request.setAttribute("isMerchantLogin", true);
        return true;
    }

    /**
     * 验证初始化权限
     * @param merchantId  商户ID
     * @param url         权限url
     * @param methodAuth  方法的权限
     * @param clzAuth     类的权限
     * @return
     */
    private boolean checkPermission(Integer merchantId, String url, MerchantCheck methodAuth, MerchantCheck clzAuth) {
        // 如果没有设置特殊的权限 (没有设置loginCheck的alias) 直接通过
        String methodName = methodAuth.name();
        String methodAlias = methodAuth.alias();
        EnumSiteGroup groupCode=methodAuth.groupCode();
        if (StringUtils.isEmpty(methodAlias)) {
            return true;
        }
        if(groupCode!=null) {
        	groupCode=clzAuth.groupCode();
        }

        // 类的权限
        String controllerName = null;
        String controllerAlias = null;
        if (clzAuth != null) {
            controllerName = clzAuth.name();
            controllerAlias = clzAuth.alias();
        }

        // 校验权限
        if (AdminConstants.adminMerchantId.intValue()==merchantId) {
        	permissionService.initVisitPermission(url,methodName, methodAlias, controllerName, controllerAlias,groupCode);
        	return true;
        } else {
            Set<String> permissionSet = permissionService.getMerchantPermissionSet(merchantId);
            LOG.info("=======methodAlias: " + methodAlias, "=======permissionSet: " + JSONObject.toJSONString(permissionSet));
            if (permissionSet.contains(methodAlias)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void processNotLogin(String url, HttpServletResponse response, HttpServletRequest request,HandlerMethod handlerMethod)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        JSONObject js = new JSONObject();
        if (isAjaxRequest(request,response,handlerMethod)) {
            js.put("code", "unLogin");
            js.put("message", "请先登录");
            js.put("result", url);
            PrintWriter out = response.getWriter();
            out.write(js.toString());
            out.flush();
            out.close();
        } else {
            System.out.println("别拦我，我要去登录");
        }
    }

    private void processNotPermission(String url, ResponseBody responseBodyAnn, HttpServletResponse response)
            throws IOException {
        if (responseBodyAnn != null) {
            JSONObject js = new JSONObject();
            js.put("code", "noPermission");
            js.put("message", "你没有权限");
            PrintWriter out = response.getWriter();
            out.write(js.toJSONString());
            out.flush();
            out.close();
        } else {
            response.sendRedirect(PropertiesHelp.getProperty("admin.cookie.domain"));
        }
    }
    
	private boolean isAjaxRequest(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");
		String contentType = response.getContentType();
		Method method = handlerMethod.getMethod();
		Class<?> clazz = method.getDeclaringClass();
		return StringUtils.containsIgnoreCase(accept, "application/json")
				|| StringUtils.containsIgnoreCase(contentType, "application/json")
				|| StringUtils.containsIgnoreCase(xRequestedWith, "XMLHttpRequest")
				|| method.isAnnotationPresent(ResponseBody.class) || clazz.isAnnotationPresent(ResponseBody.class)
				|| clazz.isAnnotationPresent(RestController.class);
	}
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
