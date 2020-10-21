package com.yd.service.merchant;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserTransStatisticResult;
import com.yd.api.service.item.YdItemBijiaService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.merchant.YdMerchantService;
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
public class YdMerchantTest {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdItemBijiaService ydItemBijiaService;

    @Reference
    private YdMerchantMemberPayRecordService ydMerchantMemberPayRecordService;


    @Test
    public void testBijia() {
        ydItemBijiaService.updateItemCompareConfig(835, "苏宁", "12345671899");
    }

    @Test
    public void findPlatFormMerchantList() {
        PagerInfo pageInfo = new PagerInfo(10, 1);
        YdMerchantResult ydMerchantResult = new YdMerchantResult();
        // ydMerchantResult.setMobile("15166795821");
        // ydMerchantResult.setGroupCode("merchant");
        ydMerchantResult.setMemberStatus("valid");
        // ydMerchantResult.setMemberLevel(3);
//        ydMerchantResult.setMerchantName(merchantName);
         // ydMerchantResult.setIsFlag("Y");
        // 查询全部供应商和门店
        Page<YdMerchantResult> pageData = ydMerchantService.findPlatformMerchantList(ydMerchantResult, pageInfo);
        System.out.println(JSON.toJSONString(pageData, true));
    }

    @Test
    public void updateMerchantStatus() {
        Integer merchantId = 10049;
        String isEnable = "N";
        ydMerchantService.updateMerchantStatus(merchantId, isEnable);
    }


    @Test
    public void paySuccessNotify() {
        ydMerchantMemberPayRecordService.paySuccessNotify("ydMerchantMemberPay-233", "4200000562202003300063408489");
    }

}
