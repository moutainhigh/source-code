package com.yd.service;

import com.alibaba.fastjson.JSON;
import com.yd.api.service.crawer.YdCrawerService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.api.service.user.YdUserTransStatisticService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author wuyc
 * created 2020/3/16 16:59
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class YdOrderTest {

    @Resource
    private YdCrawerService ydCrawerService;

    @Resource
    private YdUserOrderService ydUserOrderService;

    @Resource
    private YdUserTransStatisticService ydUserTransStatisticService;


    @Test
    public void cancelNoPayOrder() throws IOException {
        // 定期取消订单服务
        try {
            ydUserOrderService.synCancelNoPayOrder(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void synUserTransStatic() {
        try {
            ydUserTransStatisticService.synUserTransStatic();
            System.out.println("====同步完成");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("====统计交易信息失败");
        }
    }

    @Test
    public void synUserOrderItemCount() {
        try {
            ydUserTransStatisticService.synUserOrderItemCount();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("====统计商品数量失败");
        }
    }


}
