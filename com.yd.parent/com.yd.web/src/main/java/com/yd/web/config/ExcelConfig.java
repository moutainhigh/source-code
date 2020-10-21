package com.yd.web.config;

import org.jeecgframework.poi.excel.view.JeecgMapExcelView;
import org.jeecgframework.poi.excel.view.JeecgSingleExcelView;
import org.jeecgframework.poi.excel.view.JeecgTemplateExcelView;
import org.jeecgframework.poi.excel.view.JeecgTemplateWordView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger框架配置
 * http://localhost:8080/swagger-ui.html
 *
 * @author Xulg
 * Created in 2019-05-31 10:20
 */
@Configuration
public class ExcelConfig {

    @Bean
    public JeecgSingleExcelView jeecgExcelView() {
        return new JeecgSingleExcelView();
    }

    @Bean
    public JeecgTemplateExcelView jeecgTemplateExcelView() {
        return new JeecgTemplateExcelView();
    }

    @Bean
    public JeecgTemplateWordView jeecgTemplateWordView() {
        return new JeecgTemplateWordView();
    }

    @Bean
    public JeecgMapExcelView jeecgMapExcelView() {
        return new JeecgMapExcelView();
    }

}
