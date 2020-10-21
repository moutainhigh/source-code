package com.yd.web.controller.admin.order;

/**
 * 门店订单管理
 * @author wuyc
 * created 2019/10/22 18:17
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.order.YdUserOrderConfigResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdMerchantOrderConfigService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/merchant/order")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_trans",name = "交易管理")
public class AdminUserOrderController extends BaseController {

    @Reference
    private YdUserOrderService ydShopOrderService;

    @Reference
    private YdMerchantTransService ydMerchantTransService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @Reference
    private YdMerchantOrderConfigService ydShopOrderConfigService;

    @ApiOperation(value = "商户订单列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "query", name = "itemTitle", value = "商品标题"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_data",name = "商户订单列表数据")
    @RequestMapping(value = "/findOrderList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findOrderList(HttpServletRequest request, String itemTitle, String orderId,
                                                               String startTime, String endTime, String orderStatus) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydShopOrderService.findOrderListByPage(merchantId, orderId, itemTitle, orderStatus, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "商户订单详情", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_detail",name = "商户订单详情")
    @RequestMapping(value = "/getOrderDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdUserOrderResult> getOrderDetail(HttpServletRequest request, String orderId) {
        BaseResponse<YdUserOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydShopOrderService.getOrderDetail(getCurrMerchantId(request), orderId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户确认自提订单", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_confirm_order",name = "商户确认自提订单")
    @RequestMapping(value = "/confirmOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> confirmOrder(HttpServletRequest request, Integer orderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.merchantConfirmUserOrder(getCurrMerchantId(request), orderId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户设置其它优惠金额", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true),
            @ApiImplicitParam(paramType = "query", name = "price", value = "大于0的金钱", required = true),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_confirm_order",name = "商户确认自提订单")
    @RequestMapping(value = "/setOtherPrice", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> confirmOrder(HttpServletRequest request, Integer orderId, Double price) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopOrderService.setOtherPrice(getCurrMerchantId(request), orderId, price);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户订单发货", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "物流单号，String类型", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_update_express",name = "商户订单发货")
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

    @ApiOperation(value = "商户礼品订单发货", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "detailOrderId", value = "礼品详情订单id,int类型", required = true),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "物流单号，String类型", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_order_update_express",name = "商户礼品订单发货")
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

    @ApiOperation(value = "分页查询商户账单流水", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_trans_data",name = "商户账单流水数据")
    @RequestMapping(value = "/findOrderTransList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findOrderTransList(HttpServletRequest request, String orderId,
                                                                    String startTime, String endTime) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydShopOrderService.findOrderTransDetailListByPage(merchantId, orderId, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "查询商户订单自动取消时间", httpMethod = "GET")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_get_auto_cancel_time",name = "查询商户订单自动取消时间")
    @RequestMapping(value = "/findOrderAutoCancelTime", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdUserOrderConfigResult> findOrderAutoCancelTime(HttpServletRequest request) {
        BaseResponse<YdUserOrderConfigResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        try {
            result.setResult(ydShopOrderConfigService.getYdShopOrderConfigByMerchantId(merchantId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户设置订单自动取消时间", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单自动取消配置的id", required = true),
            @ApiImplicitParam(paramType = "query", name = "orderAutoCancelTime", value = "订单自动取消时间(分钟，必须是整数)"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_set_auto_cancel_time",name = "商户设置订单自动取消时间")
    @RequestMapping(value = "/updateOrderAutoCancelTime", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> updateOrderAutoCancelTime(HttpServletRequest request, Integer id, Integer orderAutoCancelTime) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydShopOrderConfigService.updateYdShopOrderConfig(getCurrMerchantId(request), id, orderAutoCancelTime);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
