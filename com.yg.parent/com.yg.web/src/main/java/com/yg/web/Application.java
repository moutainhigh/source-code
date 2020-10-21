package com.yg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import javax.servlet.MultipartConfigElement;

@ServletComponentScan
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("==============启动了===web====================");
    }


    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            connector.setProperty("relaxedQueryChars", "|*^_(){}[]?\\");
            connector.setProperty("relaxedPathChars", "?<>[\\]^`{|}");
        });
        return factory;
    }
    
    
    @Bean 
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //允许上传的文件最大值
        factory.setMaxFileSize(DataSize.parse("50MB")); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize(DataSize.parse("50MB"));  
        return factory.createMultipartConfig();  
    }
}