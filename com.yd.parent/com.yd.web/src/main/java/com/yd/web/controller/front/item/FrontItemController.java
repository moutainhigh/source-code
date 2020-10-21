package com.yd.web.controller.front.item;

import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.decoration.YdMerchantHotItemResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.service.coupon.YdMerchantCouponService;
import com.yd.api.service.decoration.YdMerchantHotItemService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.controller.FrontBaseController;
import com.yd.web.request.MerchantItemSkuRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
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
@RequestMapping("/front/item")
public class FrontItemController extends FrontBaseController {

    @Reference
    private YdMerchantItemService ydMerchantItemService;

    @Reference
    private YdMerchantGiftService ydMerchantGiftService;

    @Reference
    private YdMerchantCouponService ydMerchantCouponService;

    @Reference
    private YdMerchantHotItemService ydMerchantHotItemService;

    @ApiOperation(value = "商城首页热门商品列表", httpMethod = "POST")
    @RequestMapping(value = "/findHotItemList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantHotItemResult>> findHotItemList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantHotItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantHotItemService.findFrontHotItemList(merchantId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商城商品列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "time(时间倒序) | sales(销量倒序) | price(价格排序)"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "asc(价格升序) | desc (价格降序)")
    })
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

    @ApiOperation(value = "比价商品列表", httpMethod = "POST")
    @RequestMapping(value = "/compare/findList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantItemResult>> findCompareList(HttpServletRequest request, Integer brandId, String type, String sort) {
        BaseResponse<List<YdMerchantItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantItemService.findFrontCompareItemList(merchantId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商城商品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantItemId", value = "商城商品id")
    })
    @RequestMapping(value = "/getItemDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantItemResult> getItemDetail(HttpServletRequest request, Integer merchantItemId) {
        BaseResponse<YdMerchantItemResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantItemService.getFrontMerchantItemDetail(merchantItemId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据商品查询礼品列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantSkuIds", value = "逗号隔开")
    })
    @RequestMapping(value = "/findGiftList", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> findGiftList(HttpServletRequest request, @RequestBody MerchantItemSkuRequest req) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = req.getMerchantId();
            Map<String, Object> resultData = new HashMap<>(4);

            // 查询礼品集合
            List<YdMerchantGiftResult> list = ydMerchantGiftService.findGiftListBySkuList(merchantId, req.getSkuList());
            resultData.put("list", list);

            // 查询礼品占比
            Double giftMoney = ydMerchantGiftService.getGiftSumPrice(merchantId, req.getSkuList());
            resultData.put("giftMoney", giftMoney);
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
    @RequestMapping(value = "/getGiftDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantGiftResult> getGiftDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantGiftResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantGiftService.getYdMerchantGiftDetail(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询商户可用优惠券列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "商品id")
    })
    @RequestMapping(value = "/findCouponList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantCouponResult>> findGiftList(HttpServletRequest request, Integer itemId) {
        BaseResponse<List<YdMerchantCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            Integer userId = getCurrUserId(request);
            result.setResult(ydMerchantCouponService.findFrontMerchantCouponList(itemId, userId, merchantId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户领取优惠券", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "couponId", value = "优惠券id")
    })
    @RequestMapping(value = "/receiveCoupon", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantCouponResult>> receiveCoupon(HttpServletRequest request, Integer couponId) {
        BaseResponse<List<YdMerchantCouponResult>> result = BaseResponse.success(null, "00", "领取成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            Integer userId = getCurrUserId(request);
            ydMerchantCouponService.receiveCoupon(couponId, userId, merchantId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }


}
