package com.yd.web.controller.app.order;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户app订单管理
 * @author wuyc
 * created 2019/12/10 14:48
 **/

@RestController
@RequestMapping("/app/merchant/order")
@AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_order",name = "订单管理")
public class AppOrderController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdUserOrderService ydShopOrderService;

    @Reference
    private YdUserOrderService ydUserOrderService;

    @Reference
    private YdGiftOrderService ydGiftOrderService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @ApiOperation(value = "查询商户订单数量", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_data",name = "商户订单列表数据")
    @RequestMapping(value = "/getOrderNum", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Integer>> getOrderNum(HttpServletRequest request) {
        BaseResponse<Map<String, Integer>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(getCurrMerchantId(request));
            Integer merchantId = storeInfo.getId();
            Map<String, Integer> hashMap = new HashMap<>();
            hashMap.put("WAIT_PAY", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_PAY.getCode()));
            hashMap.put("WAIT_DELIVER", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_DELIVER.getCode()));
            hashMap.put("WAIT_GOODS", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_GOODS.getCode()));
            hashMap.put("WAIT_HANDLE", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_HANDLE.getCode()));
            result.setResult(hashMap);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户订单列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "不传为全部订单, WAIT_PAY(待付款)," +
                    " CANCEL(订单取消), WAIT_DELIVER(待发货), WAIT_HANDLE(待处理), WAIT_GOODS(待收货), SUCCESS(已完成)")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_data",name = "商户订单列表数据")
    @RequestMapping(value = "/findOrderList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findOrderList(HttpServletRequest request, String orderStatus) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydUserOrderService.findOrderListByPage(merchantId, null, null, orderStatus, null, null, pagerInfo));
        return result;
    }

    @ApiOperation(value = "商户订单详情", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_detail",name = "商户订单详情")
    @RequestMapping(value = "/getOrderDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdUserOrderResult> getOrderDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdUserOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydUserOrderService.getOrderDetail(getCurrMerchantId(request), id + ""));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户订单发货", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "物流单号，String类型", required = true)
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_update_express",name = "商户订单发货")
    @RequestMapping(value = "/updateOrderExpress", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> updateOrderExpress(HttpServletRequest request, Integer orderId, String expressOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.updateOrderExpress(getCurrMerchantId(request), orderId, expressOrderId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "设置旧机抵扣金额", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "price", value = "救济抵扣金额", required = true),
            @ApiImplicitParam(paramType = "query", name = "mobileName", value = "手机型号", required = true)
    })
    @AppMerchantCheck()
    @RequestMapping(value = "/setOldMachineReduce", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> setOldMachineReduce(HttpServletRequest request, Integer orderId, Double price, String mobileName) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.setOldMachineReduce(getCurrMerchantId(request), orderId, price, mobileName);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "设置其他优惠金额", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "price", value = "救济抵扣金额", required = true)
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_confirm_order",name = "商户确认自提订单")
    @RequestMapping(value = "/setOtherPrice", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> setOtherPrice(HttpServletRequest request, Integer orderId, Double price) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.setOtherPrice(getCurrMerchantId(request), orderId, price);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户确认自提订单(APP接口)", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true),
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_confirm_order",name = "商户确认自提订单")
    @RequestMapping(value = "/confirmOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> confirmOrder(HttpServletRequest request, Integer orderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.confirmAppOrder(getCurrMerchantId(request), orderId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品订单发货", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "detailOrderId", value = "礼品详情订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "物流单号，String类型", required = true)
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_order_update_express",name = "商户礼品订单发货")
    @RequestMapping(value = "/updateGiftOrderExpress", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> updateGiftOrderExpress(HttpServletRequest request, Integer detailOrderId, String expressOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydGiftOrderDetailService.updateGiftOrderExpress(getCurrMerchantId(request), detailOrderId, expressOrderId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据订单id查询礼品详情(只有两层)", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_get_gift_order_detail",name = "根据订单id查询礼品详情")
    @RequestMapping(value = "/getGiftOrderDetail", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> getGiftOrderDetail(HttpServletRequest request, Integer id) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            // List<YdGiftOrderDetailResult> list = ydGiftOrderService.getMerchantGiftOrderDetailGroupByExpress(getCurrMerchantId(request), id);
            List<YdGiftOrderDetailResult> list = ydGiftOrderService.getAppGiftOrderDetail(getCurrMerchantId(request), id);
            Map<String, Object> resultMap = ydGiftOrderService.checkGiftOrderIsCharge(getCurrMerchantId(request), id);
            resultMap.put("list", list);
            result.setResult(resultMap);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }


}
