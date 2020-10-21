package com.yd.web.controller.admin.gift;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdGiftOrderService;
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

/**
 * 商户礼品订单
 * @author wuyc
 * created 2019/11/7 15:53
 **/
@RestController
@RequestMapping("/admin/merchant/gift/order")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mode_merchant_gift", name = "礼品管理")
public class AdminMerchantGiftOrderController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdGiftOrderService ydGiftOrderService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @ApiOperation(value = "商户礼品订单列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "outOrderId", value = "商品单号"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "订单起始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "订单结束时间"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "in(入库, 购买礼品商城的) | out(出库,商城下单的礼品)"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_order_data", name="商户礼品订单列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdGiftOrderResult>> findList(HttpServletRequest request, Integer id, String type, String outOrderId, String startTime, String endTime) {
        BaseResponse<Page<YdGiftOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request);
            YdGiftOrderResult ydGiftOrderResult = new YdGiftOrderResult();
            ydGiftOrderResult.setType(type);
            ydGiftOrderResult.setId(id);
            ydGiftOrderResult.setOutOrderId(outOrderId);
            ydGiftOrderResult.setEndTime(endTime);
            ydGiftOrderResult.setStartTime(startTime);

            YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(getCurrMerchantId(request));
            ydGiftOrderResult.setMerchantId(storeInfo.getId());
            result.setResult(ydGiftOrderService.findYdGiftOrderListByPage(ydGiftOrderResult, pageInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品订单详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftOrderId", value = "giftOrderId")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_order_detail", name="商户礼品订单详情")
    @RequestMapping(value = "/findDetailList", method = {RequestMethod.POST})
    public BaseResponse<YdGiftOrderResult> findDetailList(HttpServletRequest request, Integer giftOrderId) {
        BaseResponse<YdGiftOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydGiftOrderService.getGiftOrderDetailList(getCurrMerchantId(request), giftOrderId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品订单发货", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "detailOrderId", value = "礼品订单详情id"),
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "快递单号")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_order_update_express", name="商户礼品订单发货")
    @RequestMapping(value = "/updateExpress", method = {RequestMethod.POST})
    public BaseResponse<String> updateGiftExpress(HttpServletRequest request, Integer detailOrderId, String expressOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydGiftOrderDetailService.updateGiftOrderExpress(merchantId, detailOrderId, expressOrderId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品确认收货", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "detailOrderId", value = "礼品订单详情id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_confirm_goods_gift", name="商户礼品确认收货")
    @RequestMapping(value = "/confirmGoods", method = {RequestMethod.POST})
    public BaseResponse<String> confirmGoods(HttpServletRequest request, Integer detailOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydGiftOrderDetailService.confirmGoods(detailOrderId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
