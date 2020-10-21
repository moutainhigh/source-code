package com.yd.web.controller.admin.order;

/**
 * 门店订单管理
 * @author wuyc
 * created 2019/10/22 18:17
 **/
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.order.YdGiftOrderDetailService;
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
@RequestMapping("/admin/platform/order")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_platform_order",name = "平台订单管理")
public class AdminPlatformOrderController extends BaseController {

    @Reference
    private YdUserOrderService ydShopOrderService;

    @Reference
    private YdMerchantTransService ydMerchantTransService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @ApiOperation(value = "平台商户订单列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户Id"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "query", name = "itemTitle", value = "商品标题"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_order_data",name = "商户订单列表数据")
    @RequestMapping(value = "/findOrderList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findOrderList(HttpServletRequest request, Integer merchantId, String itemTitle, String orderId,
                                                               String startTime, String endTime, String orderStatus) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydShopOrderService.findOrderListByPage(merchantId, orderId, itemTitle, orderStatus, startTime, endTime, pagerInfo));
        return result;
    }

    @ApiOperation(value = "平台商户订单详情", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_order_detail",name = "商户订单详情")
    @RequestMapping(value = "/getOrderDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdUserOrderResult> getOrderDetail(HttpServletRequest request, String orderId) {
        BaseResponse<YdUserOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydShopOrderService.getOrderDetail(null, orderId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }


}
