package com.yg.web.interceptor;

import com.yg.web.anotation.LoginStatus;
import com.yg.web.entity.SiteContextThreadLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginStatusInterceptor implements HandlerInterceptor {

	private final static Logger LOG = LoggerFactory.getLogger(LoginStatusInterceptor.class);

	private ThreadLocal<Long> begin = new ThreadLocal<>();

// 	@Reference

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
		// 微信浏览器浏览

		// 方法上的 权限控制
		LoginStatus methodAuth = methodHandler.getMethodAnnotation(LoginStatus.class);
		// 类上的权限控制
		LoginStatus clzAuth = methodHandler.getMethod().getDeclaringClass().getAnnotation(LoginStatus.class);

		if (methodAuth != null || clzAuth != null) {
			String userId = SiteContextThreadLocal.get().getPassport().getPairMap().get("userId");
			String sessionId = SiteContextThreadLocal.get().getPassport().getPairMap().get("sessionId");
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
				// request.setAttribute(Consts.IS_USER_LOGIN_KEY, false);
				return true;
			}
//			boolean isLogin = loginService.isLoginUser(userId, sessionId);
			boolean isLogin = true;
			if (!isLogin) {
				// request.setAttribute(Consts.IS_USER_LOGIN_KEY, "false");
				return true;
			}
			
			// request.setAttribute(Consts.IS_USER_LOGIN_KEY, "true");
			return true;
		}
		return true;
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
