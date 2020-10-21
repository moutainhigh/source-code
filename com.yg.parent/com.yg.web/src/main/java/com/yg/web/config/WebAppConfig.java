package com.yg.web.config;

import com.yg.web.interceptor.FrontCheckInterceptor;
import com.yg.web.interceptor.LoginStatusInterceptor;
import com.yg.web.interceptor.MerchantCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	@Autowired
	FrontCheckInterceptor loginCheckInterceptor;
	@Autowired
	LoginStatusInterceptor loginStatusInterceptor;
	@Autowired
	MerchantCheckInterceptor merchantCheckInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
				.maxAge(3600).allowCredentials(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 可添加多个
		registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
		registry.addInterceptor(loginStatusInterceptor).addPathPatterns("/**");
		registry.addInterceptor(merchantCheckInterceptor).addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		if (!registry.hasMappingForPattern("swagger-ui.html")) {
			registry.addResourceHandler("swagger-ui.html")
					.addResourceLocations("classpath:/META-INF/resources/");
		}
	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
			@Override
			public void customize(ConfigurableWebServerFactory factory) {
				ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
				ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
				factory.addErrorPages(errorPage404, errorPage500);
			}
		};
	}
}
