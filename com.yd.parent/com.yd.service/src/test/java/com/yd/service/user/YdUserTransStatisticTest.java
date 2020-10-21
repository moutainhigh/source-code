package com.yd.service.user;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserTransStatisticResult;
import com.yd.api.service.user.YdUserTransStatisticService;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.service.Application;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wuyc
 * created 2020/3/20 14:06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class YdUserTransStatisticTest {

    @Reference
    private YdUserTransStatisticService ydUserTransStatisticService;

    @Test
    public void findUserTransStatisticListByPage() {
        Integer merchantId = 10049;
        String startTime = null;
        String endTime = null;
        Integer minBuyNum = 2;
        Integer maxBuyNum = 50;
        Double minTransAmount = null;
        Double maxTransAmount = null;
        String mobile = "";
        String nickname = "&无涯%!==|:^?\uD83D\uDC76^\uD83D\uDCA2#${a1}";
        PagerInfo pagerInfo = new PagerInfo(10, 1);
        Page<YdUserTransStatisticResult> dataList = ydUserTransStatisticService.findUserTransStatisticListByPage(
                merchantId, mobile, nickname, startTime, endTime, minBuyNum, maxBuyNum, minTransAmount, maxTransAmount, pagerInfo);
        System.out.println(JSON.toJSONString(dataList, true));
    }


    @Test
    public void findOrderDetailListByPage() {
        YdUserOrderResult ydUserOrderResult = new YdUserOrderResult();
        ydUserOrderResult.setOrderStatus("SUCCESS");
        ydUserOrderResult.setUserId(13);
        ydUserOrderResult.setMerchantId(10049);
        PagerInfo pagerInfo = new PagerInfo(10, 1);
        Page<YdUserOrderResult> detailList = ydUserTransStatisticService.findOrderDetailListByPage(ydUserOrderResult, pagerInfo);
        System.out.println(JSON.toJSONString(detailList, true));
    }


}
