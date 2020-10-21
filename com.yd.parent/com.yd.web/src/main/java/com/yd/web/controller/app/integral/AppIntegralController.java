package com.yd.web.controller.app.integral;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.service.integral.YdIntegralOrderService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优度积分接口类
 * @author wuyc
 * created 2019/12/24 14:16
 **/
@RestController
@AppMerchantCheck
@RequestMapping("/app/integral/order")
public class AppIntegralController extends BaseController {

    @Reference
    private YdIntegralOrderService ydIntegralOrderService;


    @ApiOperation(value = "用户订单积分核销(汉堡王)", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "用户订单id"),
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_order_integral_cancel", name="用户订单积分核销")
    @RequestMapping(value = "/integral/checkMobile", method = {RequestMethod.POST})
    public BaseResponse<String> checkMobile(Integer orderId, String mobile) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer channelId = 110;
            ydIntegralOrderService.checkMobile(orderId, channelId, mobile);
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

}
