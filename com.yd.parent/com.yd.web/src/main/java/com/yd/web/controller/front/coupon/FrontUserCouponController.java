package com.yd.web.controller.front.coupon;

import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.service.coupon.YdUserCouponService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuyc
 * created 2019/11/28 12:00
 **/
@RestController
@RequestMapping("/front/user/coupon")
public class FrontUserCouponController extends FrontBaseController {

    @Reference
    private YdUserCouponService ydUserCouponService;

    @ApiOperation(value = "查询用户优惠券列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "couponStatus", value = "CAN_USE(可使用) | NOT_USE(不可使用)")
    })
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserCouponResult>> getUserInfo(HttpServletRequest request, String couponStatus) {
        BaseResponse<Page<YdUserCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdUserCouponResult params = new YdUserCouponResult();
            params.setUserId(getCurrUserId(request));
            params.setMerchantId(getCurrMerchantId(request));
            params.setCouponStatus(couponStatus);
            result.setResult(ydUserCouponService.findYdUserCouponListByPage(params, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }
}
