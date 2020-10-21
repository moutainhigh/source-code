package com.yd.service.job.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {
    private final static Logger LOG = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(QuartzJobFactory myJobFactory) throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        // 使job实例支持spring 容器管理
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(myJobFactory);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        // 延迟10s启动quartz
        schedulerFactoryBean.setStartupDelay(10);

        return schedulerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        // 在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     *
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException, SchedulerException {
        System.out.println("=======scheduler start===============");
//		SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties());
//		Scheduler scheduler = schedulerFactory.getScheduler();
//		scheduler.start();//初始化bean并启动scheduler
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        scheduler.resumeAll();
        return scheduler;
    }

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setOverwriteExistingJobs(true);// 更新trigger的 表达式时，同步数据到数据库qrtz_cron_triggers表 开启
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");// 注入applicationContext
    }
}
