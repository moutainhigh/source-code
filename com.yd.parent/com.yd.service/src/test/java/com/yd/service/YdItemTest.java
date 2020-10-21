package com.yd.service;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.service.crawer.YdCrawerService;
import com.yd.api.service.item.YdItemService;
import com.yd.api.service.item.YdMerchantItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author wuyc
 * created 2020/3/17 17:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class YdItemTest {

    @Resource
    YdItemService ydItemService;

    @Resource
    YdCrawerService ydCrawerService;

    @Resource
    YdMerchantItemService ydMerchantItemService;

    @Test
    public void findFrontCompareItemList() throws IOException {
        List<YdMerchantItemResult> resultList = ydMerchantItemService.findFrontCompareItemList(10049);
        System.out.println("resultList=" + JSON.toJSONString(resultList));
    }

    // 删除重复爬虫商品
    @Test
    public void deleteRepeatItem() throws IOException {
        ydCrawerService.deleteRepeatItem();
    }

    // 抓取爬虫商品
    @Test
    public void sysNewCrawerItem() throws IOException {
        ydCrawerService.synCrawerItem();
    }

    // 抓取爬虫商品
    @Test
    public void initItem() throws IOException {
        ydCrawerService.synCrawerItem();
        System.out.println("====ok");
    }

    // 抓取优度商品
    @Test
    public void synYdItemAndSku() throws IOException {
        ydCrawerService.synYdItemAndSku();
        System.out.println("====ok");
    }

    // 抓取发布时间
    @Test
    public void initPubTime() throws IOException {
        ydCrawerService.synItemPublicTime();
        System.out.println("====ok");
    }

    // 抓取图片
    @Test
    public void initItemImage() throws IOException {
        ydItemService.sysItemImage();
    }

    // 抓取优度商品详情
    @Test
    public void synYdContent() throws IOException {
        ydCrawerService.synYdContent();
        System.out.println("====ok");
    }

   // 同步销量
    @Test
    public void itemSalesCalc() throws IOException {
        ydMerchantItemService.synItemSales();
        System.out.println("====ok");
    }

}
