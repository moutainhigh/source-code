package com.yd.web.controller.app.item;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.service.coupon.YdMerchantCouponService;
import com.yd.api.service.decoration.YdMerchantHotItemService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantItemSkuRequest;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城商品
 * @author wuyc
 * created 2019/11/8 13:36
 **/
@RestController
@AppMerchantCheck
@RequestMapping("/app/item")
public class AppItemController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppItemController.class);

    @Reference
    private YdMerchantItemService ydMerchantItemService;

    @Reference
    private YdMerchantGiftService ydMerchantGiftService;

    @Reference
    private YdMerchantCouponService ydMerchantCouponService;

    @Reference
    private YdMerchantHotItemService ydMerchantHotItemService;

    @ApiOperation(value = "商城商品列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "time(时间倒序) | sales(销量倒序) | price(价格排序)"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "asc(价格升序) | desc (价格降序)")
    })
    @AppMerchantCheck
    @RequestMapping(value = "/findItemList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantItemResult>> findItemList(HttpServletRequest request, Integer brandId, String type, String sort) {
        BaseResponse<Page<YdMerchantItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantItemService.findFrontMerchantItemList(merchantId, brandId, type, sort, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商城商品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantItemId", value = "商城商品id")
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getItemDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantItemResult> getItemDetail(HttpServletRequest request, Integer merchantItemId) {
        BaseResponse<YdMerchantItemResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantItemResult data = ydMerchantItemService.getFrontMerchantItemDetail(merchantItemId);
            logger.info("====查询订单详情返回的数据=" + JSON.toJSONString(data));
            result.setResult(data);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据商品查询礼品列表", httpMethod = "POST")
    @AppMerchantCheck
    @RequestMapping(value = "/findGiftList", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> findGiftList(HttpServletRequest request, @RequestBody MerchantItemSkuRequest req) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            Map<String, Object> resultData = new HashMap<>(4);

            // 查询礼品占比
            Double giftMoney = ydMerchantGiftService.getGiftSumPrice(merchantId, req.getSkuList());
            resultData.put("giftMoney", giftMoney);

            // 查询礼品集合
            List<YdMerchantGiftResult> list = ydMerchantGiftService.findGiftListBySkuList(merchantId, req.getSkuList());
            resultData.put("list", list);

            result.setResult(resultData);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据礼品id查询礼品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "礼品id")
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getGiftDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantGiftResult> getGiftDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantGiftResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantGiftService.getYdMerchantGiftDetail(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
