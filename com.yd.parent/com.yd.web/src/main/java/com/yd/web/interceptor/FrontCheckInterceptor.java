package com.yd.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yd.api.service.login.YdLoginService;
import com.yd.core.enums.YdLoginUserSourceEnums;
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
import com.yd.api.Consts;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.StringUtil;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.entity.SiteContextThreadLocal;

@Component
public class FrontCheckInterceptor implements HandlerInterceptor {

	private final static Logger LOG = LoggerFactory.getLogger(FrontCheckInterceptor.class);

	private ThreadLocal<Long> begin = new ThreadLocal<>();

	@Reference
	private YdLoginService ydLoginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod methodHandler = null;
		if (handler instanceof HandlerMethod) {
			methodHandler = (HandlerMethod) handler;
		} else {
			return true;
		}
		begin.set(System.currentTimeMillis());

		String profile = ProfileUtil.getProfile();
		if ("dev".equalsIgnoreCase(profile) || "uat".equalsIgnoreCase(profile)) {
			return true;
		}

		// 方法上的 权限控制
		FrontCheck methodAuth = methodHandler.getMethodAnnotation(FrontCheck.class);
		// 类上的权限控制
		FrontCheck clzAuth = methodHandler.getMethod().getDeclaringClass().getAnnotation(FrontCheck.class);

		String url = request.getRequestURI();
		String userId = SiteContextThreadLocal.get().getPassport().getPairMap().get("userId");
		String sessionId = SiteContextThreadLocal.get().getPassport().getPairMap().get("sessionId");
		if (methodAuth != null || clzAuth != null) {
			if (!StringUtil.isEmpty(request.getQueryString())) {
				url = url + "?" + request.getQueryString();
			}
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
				processNotLogin(url, response, request,methodHandler);
				return false;
			}
			boolean isLogin = ydLoginService.checkUserLogin(YdLoginUserSourceEnums.YD_SHOP_USER, userId, sessionId);
			if (!isLogin) {
				processNotLogin(url, response, request, methodHandler);
				return false;
			}
		}

		request.setAttribute(Consts.IS_USER_LOGIN_KEY, "true");
		return true;
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
			LOG.info("========跳转到微信登录===============");
			response.sendRedirect(PropertiesHelp.getProperty("webDomain")+"/front/weixin/user/auth/login?sendURL="+url);
			return;
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
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
