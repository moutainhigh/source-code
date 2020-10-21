package com.yd.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 判断当前打包环境的工具类
 * @author wuyc
 * created 2020/4/27 13:43
 **/
@Component
public class ProfileUtil implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ProfileUtil.class);

    @Value("${spring.profiles.active}")
    private String profile;

    private static String data;

    public static String getProfile() {
        logger.info("data=" + data);
        return data;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        data = profile;
    }
}
