package com.yg.web.resolver;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yg.web.entity.PassportContext;
import com.yg.web.entity.SiteContext;
import com.yg.web.entity.SiteContextThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Web层的接口访问日志切面
 *
 * @author Xulg
 * Created in 2019-03-25 11:30
 */
@Component
@Aspect
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    // @Value("${spring.profiles.active}")
    private String profile;

    private static final ThreadLocal<VisitInfo> VISIT_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * controller层的切点
     * controller包及其子包下的所有controller
     */
    @Pointcut("execution(public * com.yg.web.controller.front..*.*(..))")
    public void webLog() {
    }

    private static final String[] IGNORE_LIST = new String[]{
            "com.yg.web.controller.IndexPageController.index"
    };

    /**
     * 前置记录日志信息
     *
     * @param point 切点信息
     */
    @SuppressWarnings("ConstantConditions")
    @Before("webLog()")
    public void doLogBefore(JoinPoint point) {
        // 预发环境需要记录
//        if (!"dev".equalsIgnoreCase(profile) && !"uat".equalsIgnoreCase(profile)) {
//            return;
//        }

        if (false) {
            this.printJoinPointInfo(point);
        }

        if (this.shouldIgnore(point.toLongString())) {
            return;
        }

        // 获取request请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 请求信息
            String requestMethod = request.getMethod();
            String requestUrl = request.getRequestURL().toString();
            String requestUri = request.getRequestURI().toString();
            String visitorIp = request.getRemoteAddr();
            // 用户信息
            String loginUserInfo = this.loginUserInfo();
            // 请求参数
            Object requestParameter = this.fetchRequestParameter(request, point);

            VisitInfo visitInfo = new VisitInfo();
            visitInfo.setRequestMethod(requestMethod);
            visitInfo.setVisitorUserId(loginUserInfo);
            visitInfo.setRequestUrl(requestUri);
            visitInfo.setRequestUri(requestUrl);
            visitInfo.setVisitorClientIp(visitorIp);
            visitInfo.setRequestParameter(requestParameter);
            visitInfo.setMethodInfo(point.toLongString());
            visitInfo.setStartTimestamp(System.currentTimeMillis());
            visitInfo.setAjax(this.isAjaxRequest(request, point));
            VISIT_INFO_THREAD_LOCAL.set(visitInfo);
            setVisitorLog(visitInfo);
        }
    }

    /**
     * 后置返回结果记录日志信息
     *
     * @param result 返回结果
     */
    @SuppressWarnings("Duplicates")
    @AfterReturning(pointcut = "webLog()", returning = "result")
    public void doLogAfterReturning(Object result) {
        // 预发环境需要记录
//        if (!"dev".equalsIgnoreCase(profile) && !"uat".equalsIgnoreCase(profile)) {
//            return;
//        }

        VisitInfo visitInfo = VISIT_INFO_THREAD_LOCAL.get();
        if (visitInfo != null) {
            if (this.shouldIgnore(visitInfo.getMethodInfo())) {
                return;
            }
            try {
                // 接口执行结束时间戳
                visitInfo.setEndTimestamp(System.currentTimeMillis());
                // 计算接口耗时
                visitInfo.setTimeConsume(visitInfo.getEndTimestamp() - visitInfo.getStartTimestamp());
                // 请求成功
                visitInfo.setSuccess(true);
                // 接口返回结果
                if (result instanceof ModelAndView) {
                    Map<String, Object> map = new HashMap<>(4);
                    map.put("viewName", ((ModelAndView) result).getViewName());
                    map.put("attributes", ((ModelAndView) result).getModelMap());
                    visitInfo.setReturnResult(map);
                } else {
                    visitInfo.setReturnResult(result);
                }
                // logger.info("接口访问信息: \n" + JSON.toJSONString(visitInfo, true));
            } finally {
                VISIT_INFO_THREAD_LOCAL.remove();
            }
        }
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doLogAfterThrowing(Throwable e) {
        // 预发环境需要记录
//        if (!"dev".equalsIgnoreCase(profile) && !"uat".equalsIgnoreCase(profile)) {
//            return;
//        }

        VisitInfo visitInfo = VISIT_INFO_THREAD_LOCAL.get();
        if (visitInfo != null) {
            if (this.shouldIgnore(visitInfo.getMethodInfo())) {
                return;
            }
            try {
                // 接口执行结束时间戳
                visitInfo.setEndTimestamp(System.currentTimeMillis());
                // 计算接口耗时
                visitInfo.setTimeConsume(visitInfo.getEndTimestamp() - visitInfo.getStartTimestamp());
                // 请求失败
                visitInfo.setSuccess(false);
                // 错误信息
                visitInfo.setErrorMsg(e == null ? null : e.getMessage());
                // logger.info("接口访问信息: \n" + JSON.toJSONString(visitInfo, true));
            } finally {
                VISIT_INFO_THREAD_LOCAL.remove();
            }
        }
    }

    private String loginUserInfo() {
        String adminUserId = "unknown";
        SiteContext siteContext = SiteContextThreadLocal.get();
        if (siteContext != null) {
            PassportContext passportContext = siteContext.getPassport();
            if (passportContext != null) {
                String id = passportContext.getPairMap().get("userId");
                if (id != null) {
                    adminUserId = id;
                }
            }
        }
        return adminUserId;
    }

    private Object fetchRequestParameter(HttpServletRequest request, JoinPoint point) {
        Object requestParam;
        String requestMethod = request.getMethod();
        if ("GET".equalsIgnoreCase(requestMethod) || "HEAD".equalsIgnoreCase(requestMethod)
                || StringUtils.containsIgnoreCase(request.getContentType(), "x-www-form-urlencoded")) {
            requestParam = request.getParameterMap();
        } else {
            // 读取请求体了要
            requestParam = JSON.parseObject(this.parseMethodParameters(point.getSignature(), point.getArgs()));
        }
        return requestParam;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isAjaxRequest(HttpServletRequest request, JoinPoint point) {
        Method targetMethod = null;
        Class<?> clazz = null;
        try {
            // 目标类class
            clazz = point.getTarget().getClass();
            // 目标方法
            targetMethod = ReflectUtil.getMethodByName(clazz, point.getSignature().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (StringUtils.containsIgnoreCase(accept, "application/json")) {
            return true;
        } else if (StringUtils.containsIgnoreCase(xRequestedWith, "XMLHttpRequest")) {
            return true;
        } else if (targetMethod != null && targetMethod.isAnnotationPresent(ResponseBody.class)) {
            return true;
        } else if (clazz != null && clazz.isAnnotationPresent(ResponseBody.class)) {
            return true;
        } else if (clazz != null && clazz.isAnnotationPresent(RestController.class)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean shouldIgnore(String methodInfo) {
        if (methodInfo == null) {
            return false;
        }
        for (String info : IGNORE_LIST) {
            if (methodInfo.contains(info)) {
                return true;
            }
        }
        return false;
    }

    private void printJoinPointInfo(JoinPoint point) {
        try {
            Signature signature = point.getSignature();
            Map<String, Object> signatureInfo = new HashMap<>(16);
            signatureInfo.put("modifiers", signature.getModifiers());
            signatureInfo.put("name", signature.getName());
            signatureInfo.put("declaringType", signature.getDeclaringType().getName());
            signatureInfo.put("declaringTypeName", signature.getDeclaringTypeName());

            JoinPoint.StaticPart staticPart = point.getStaticPart();
            Map<String, Object> staticPartInfo = new HashMap<>(16);
            staticPartInfo.put("id", staticPart.getId());
            staticPartInfo.put("kind", staticPart.getKind());
            staticPartInfo.put("signature", staticPart.getSignature().toLongString());
            staticPartInfo.put("sourceLocation", staticPart.getSourceLocation().toString());

            SourceLocation sourceLocation = point.getSourceLocation();
            Map<String, Object> sourceLocationInfo = new HashMap<>(16);
            //sourceLocationInfo.put("fileName", sourceLocation.getFileName());
            //sourceLocationInfo.put("line", sourceLocation.getLine());
            sourceLocationInfo.put("withinType", sourceLocation.getWithinType().getName());
            //sourceLocationInfo.put("column", sourceLocation.getColumn());

            Map<String, Object> joinPointInfo = new HashMap<>(16);
            joinPointInfo.put("target", point.getTarget().getClass().getName());
            joinPointInfo.put("kind", point.getKind());
            joinPointInfo.put("args", parseArgs(point.getArgs()));
            joinPointInfo.put("this", point.getThis().getClass().getName());
            joinPointInfo.put("signature", signatureInfo);
            joinPointInfo.put("staticPart", staticPartInfo);
            joinPointInfo.put("sourceLocation", sourceLocationInfo);

            logger.info("\n===========================================JoinPoint==========================================\n"
                    + "joinPointInfo" + JSON.toJSONString(joinPointInfo, true) + "\n"
                    + "===========================================JoinPoint==========================================\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final List<Class<?>> IGNORE_CLASS_TYPE = new CopyOnWriteArrayList<>(Arrays.asList(
            HttpServletRequest.class, HttpServletResponse.class, MultipartFile.class
    ));

    private static final Object EMPTY_VALUE = new Object();

    @SuppressWarnings("Duplicates")
    private String parseMethodParameters(Signature signature, Object[] args) {
        if (!(signature instanceof MethodSignature)) {
            return this.parseArgs(args);
        }
        // 获取参数名称
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null || args == null || parameterNames.length != args.length) {
            return this.parseArgs(args);
        }
        Map<String, Object> parameterIndexMap = new LinkedHashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            Object value = args[i];
            if (value == null) {
                parameterIndexMap.put(parameterNames[i], null);
                continue;
            }
            if (!Serializable.class.isAssignableFrom(value.getClass())
                    || IGNORE_CLASS_TYPE.stream().anyMatch((clazz) -> clazz.isAssignableFrom(value.getClass()))) {
                parameterIndexMap.put(parameterNames[i], EMPTY_VALUE);
                continue;
            }
            parameterIndexMap.put(parameterNames[i], value);
        }
        try {
            return JSON.toJSONString(parameterIndexMap, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue);
        } catch (RuntimeException e) {
            logger.error(this.getClass().getName() + "#parseMethodParameters()错误", e);
        }
        return null;
    }

    private String parseArgs(Object[] args) {
        List<Object> values = Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter((value) -> Serializable.class.isAssignableFrom(value.getClass()))
                .filter((value) -> IGNORE_CLASS_TYPE.stream().noneMatch((clazz) -> clazz.isAssignableFrom(value.getClass())))
                .collect(Collectors.toList());
        try {
            return JSON.toJSONString(values, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue);
        } catch (RuntimeException e) {
            logger.error(this.getClass().getName() + "#parseArgs()错误", e);
        }
        return null;
    }

    /**
     * 将访问记录存到数据库
     * @param visitInfo
     */
    private void setVisitorLog(VisitInfo visitInfo) {
        // logger.info("====进入setVisitorLog方法");
        if (!visitInfo.getRequestUrl().equals("/front/item/getItemDetail")) return;
        if (visitInfo.getVisitorUserId().equals("unknown") || visitInfo.getVisitorUserId() == null) return;

        try {
            Object requestParameter = visitInfo.getRequestParameter();
            Map hashMap = JSONObject.parseObject(JSONObject.toJSONString(requestParameter), Map.class);
            Object itemObject = hashMap.get("merchantItemId");
            if (itemObject == null) return;
            Integer merchantItemId = null;
            if (itemObject instanceof JSONArray) {
                merchantItemId = Integer.valueOf(((JSONArray) itemObject).get(0).toString());
            } else {
                merchantItemId = Integer.valueOf(itemObject.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    private static class VisitInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 请求方式
         */
        private String requestMethod;

        /**
         * 请求用户id信息
         */
        private String visitorUserId;

        /**
         * 请求接口url
         */
        private String requestUrl;

        private String requestUri;

        /**
         * 客户端ip
         */
        private String visitorClientIp;

        /**
         * 请求参数
         */
        private Object requestParameter;

        /**
         * 拦截接口的方法信息
         */
        private String methodInfo;

        /**
         * 接口开始执行的时间戳
         */
        private long startTimestamp;

        /**
         * 接口结束执行的时间戳
         */
        private long endTimestamp;

        /**
         * 接口执行消耗的毫秒数
         */
        private long timeConsume;

        /**
         * 是否是ajax请求
         */
        private Boolean isAjax;

        /**
         * 接口返回结果
         */
        private Object returnResult;

        /**
         * 请求是否成功
         */
        private Boolean isSuccess;

        /**
         * 错误信息
         */
        private String errorMsg;

        public String getRequestMethod() {
            return requestMethod;
        }

        public void setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
        }

        public String getVisitorUserId() {
            return visitorUserId;
        }

        public void setVisitorUserId(String visitorUserId) {
            this.visitorUserId = visitorUserId;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public void setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        public String getVisitorClientIp() {
            return visitorClientIp;
        }

        public void setVisitorClientIp(String visitorClientIp) {
            this.visitorClientIp = visitorClientIp;
        }

        public Object getRequestParameter() {
            return requestParameter;
        }

        public void setRequestParameter(Object requestParameter) {
            this.requestParameter = requestParameter;
        }

        public String getMethodInfo() {
            return methodInfo;
        }

        public void setMethodInfo(String methodInfo) {
            this.methodInfo = methodInfo;
        }

        public long getStartTimestamp() {
            return startTimestamp;
        }

        public void setStartTimestamp(long startTimestamp) {
            this.startTimestamp = startTimestamp;
        }

        public long getEndTimestamp() {
            return endTimestamp;
        }

        public void setEndTimestamp(long endTimestamp) {
            this.endTimestamp = endTimestamp;
        }

        public Boolean getAjax() {
            return isAjax;
        }

        public void setAjax(Boolean ajax) {
            isAjax = ajax;
        }

        public Object getReturnResult() {
            return returnResult;
        }

        public void setReturnResult(Object returnResult) {
            this.returnResult = returnResult;
        }

        public long getTimeConsume() {
            return timeConsume;
        }

        public void setTimeConsume(long timeConsume) {
            this.timeConsume = timeConsume;
        }

        public Boolean getSuccess() {
            return isSuccess;
        }

        public void setSuccess(Boolean success) {
            isSuccess = success;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public void setRequestUri(String requestUri) {
            this.requestUri = requestUri;
        }

        public String getRequestUri() {
            return requestUri;
        }
    }
}