package com.yd.service.job;

import com.yd.api.service.order.YdUserOrderService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import javax.annotation.Resource;

/**
 * 取消未付款订单job
 * @author wuyc
 * created 2020/5/27 14:19
 **/
public class YdOrderCancelJob extends QuartzJobBean{

	private final static Logger logger = LoggerFactory.getLogger(YdOrderCancelJob.class);
	
	@Resource
	private YdUserOrderService ydUserOrderService;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		try {
			logger.info("====进入取消订单job");
			ydUserOrderService.synCancelNoPayOrder(null);
			logger.info("====取消订单job完成");
		} catch (Exception e) {
			logger.info("====取消订单job出错" + e.getLocalizedMessage());
		}
	}
	
}
