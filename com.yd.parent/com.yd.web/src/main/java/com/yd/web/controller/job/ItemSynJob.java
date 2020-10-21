package com.yd.web.controller.job;

import com.yd.api.service.crawer.YdCrawerService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.api.service.user.YdUserTransStatisticService;
import com.yd.core.res.BaseResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 商品销量同步服务
 * @author wuyc
 * created 2019/12/12 19:54
 **/
@RestController
@RequestMapping("/yd/item/syn/")
public class ItemSynJob {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdCrawerService ydCrawerService;

    @Reference
    private YdUserOrderService ydUserOrderService;

    @Reference
    private YdUserTransStatisticService ydUserTransStatisticService;

    @RequestMapping(value = "/sysAll", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> sysAll(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synAll();
        return result;
    }

    @RequestMapping(value = "/synCrawerBrand", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synCrawerBrand(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synCrawerBrand();
        return result;
    }

    @RequestMapping(value = "/synCrawerItem", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synCrawerItem(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步爬虫商品成功");
        ydCrawerService.synCrawerItem();
        return result;
    }

    @RequestMapping(value = "/synYdItemAndSku", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synYdItemAndSku(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synYdItemAndSku();
        return result;
    }

    @RequestMapping(value = "/synItemPublicTime", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synItemPublicTime(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synItemPublicTime();
        return result;
    }

    @RequestMapping(value = "/synYdImage", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synYdImage(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synYdImage();
        return result;
    }

    @RequestMapping(value = "/synYdContent", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> synYdContent(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "同步成功");
        ydCrawerService.synYdContent();
        return result;
    }
}
