package com.yd.web.controller.job;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.api.service.user.YdUserTransStatisticService;
import com.yd.core.res.BaseResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuyc
 * created 2020/4/1 9:42
 **/
@Component
@EnableScheduling
public class YdSchedulerTask {

    private static final Logger logger = LoggerFactory.getLogger(YdSchedulerTask.class);

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdUserOrderService ydUserOrderService;

    @Reference
    private YdMerchantItemService ydMerchantItemService;

    @Reference
    private YdUserTransStatisticService ydUserTransStatisticService;

    // 用户数据统计服务, 五分钟一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void synUserTransStatic() {
        try {
            logger.info("====进入synUserTransStatic");
            ydUserTransStatisticService.synUserTransStatic();
        } catch (Exception e) {
            logger.error("====用户数据统计服务同步失败" + e.getLocalizedMessage());
        }
    }

    // 商户会员过期同步, 4小时1次
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void synMemberValidTime() {
        try {
            logger.info("====进入synMemberValidTime");
            ydMerchantService.synMemberValidTime();
        } catch (Exception e) {
            logger.error("====商户会员过期同步失败" + e.getLocalizedMessage());
        }
    }

    // 商户商品销量同步, 4小时1次
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void synItemSales() {
        try {
            logger.info("====synItemSales");
            ydMerchantItemService.synItemSales();
        } catch (Exception e) {
            logger.error("====商户商品销量同步失败" + e.getLocalizedMessage());
        }
    }

    //  取消订单服务, 1小时一次
 /*   @Scheduled(cron = "0 0/2 * * * ?")
    public void synCancelNoPayOrder() {
        try {
            logger.info("====进入取消订单服务synCancelNoPayOrder");
            ydUserOrderService.synCancelNoPayOrder(null);
        } catch (Exception e) {
            logger.error("====取消订单服务同步失败" + e.getLocalizedMessage());
        }
    }
*/
}
