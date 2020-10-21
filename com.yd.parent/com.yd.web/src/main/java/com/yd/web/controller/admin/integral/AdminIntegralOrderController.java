package com.yd.web.controller.admin.integral;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.integral.YdIntegralOrderResult;
import com.yd.api.service.integral.YdIntegralOrderService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 积分订单后台管理
 * @author wuyc
 * created 2019/12/27 14:06
 **/
@RestController
@RequestMapping("/admin/integral/order")
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_integral", name = "积分管理")
public class AdminIntegralOrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminIntegralOrderController.class);

    @Reference
    private YdIntegralOrderService ydIntegralOrderService;

    @ApiOperation(value = "平台积分订单数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_order_data", name="平台积分订单数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdIntegralOrderResult>> findList(HttpServletRequest request, YdIntegralOrderResult params) {
        BaseResponse<Page<YdIntegralOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydIntegralOrderService.findYdIntegralOrderListByPage(params, getPageInfo(request)));
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台积分订单详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "积分订单id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_order_detail", name="平台积分订单详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdIntegralOrderResult> getDetail(HttpServletRequest request, Integer orderId) {
        BaseResponse<YdIntegralOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydIntegralOrderService.getYdIntegralOrderById(orderId));
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

}
