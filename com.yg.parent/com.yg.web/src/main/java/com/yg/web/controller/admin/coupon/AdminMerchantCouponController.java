package com.yg.web.controller.admin.coupon;

import com.yg.api.enums.YgSiteGroupEnum;
import com.yg.api.result.coupon.YgMerchantCouponResult;
import com.yg.api.result.coupon.YgUserCouponResult;
import com.yg.api.service.coupon.YgMerchantCouponService;
import com.yg.api.service.coupon.YgUserCouponService;
import com.yg.core.res.BaseResponse;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;
import com.yg.web.anotation.MerchantCheck;
import com.yg.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 商户优惠券管理
 * @author wuyc
 * created 2019/11/26 11:54
 **/
@RestController
@MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="mod_merchant_market",name = "营销管理")
@RequestMapping("/admin/merchant/coupon")
public class AdminMerchantCouponController extends BaseController {

    @Reference
    private YgUserCouponService ygUserCouponService;

    @Reference
    private YgMerchantCouponService ygMerchantCouponService;

    @ApiOperation(value = "商户优惠券列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "couponTitle", value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "isShelf", value = "是否上架"),
            @ApiImplicitParam(paramType = "query", name = "couponType", value = "ZJ | MJ")
    })
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_data",name = "商户优惠券数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YgMerchantCouponResult>> findList(HttpServletRequest request,String couponTitle, String isShelf,
                                                               String startTime, String endTime, String couponType) {
        BaseResponse<Page<YgMerchantCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pageInfo = getPageInfo(request, 1, 10);

        YgMerchantCouponResult params = new YgMerchantCouponResult();
        params.setCouponTitle(couponTitle);
        params.setStartTime(startTime);
        params.setEndTime(endTime);
        params.setIsShelf(isShelf);
        params.setCouponType(couponType);
        params.setMerchantId(getCurrMerchantId(request));
        Page<YgMerchantCouponResult> list = ygMerchantCouponService.findYgMerchantCouponListByPage(params, pageInfo);
        return BaseResponse.success(list);
    }

    @ApiOperation(value = "商户优惠券详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id")
    })
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_detail",name = "商户优惠券详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YgMerchantCouponResult> getDetail(Integer id) {
        BaseResponse<YgMerchantCouponResult> result = BaseResponse.success(null, "00", "查询成功");
        YgMerchantCouponResult detail = ygMerchantCouponService.getYgMerchantCouponById(id);
        result.setResult(detail);
        return result;
    }

    @ApiOperation(value = "商户优惠券新增修改", httpMethod = "POST")
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_update",name = "商户优惠券新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YgMerchantCouponResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        try {
            params.setMerchantId(getCurrMerchantId(request));
            if (params.getId() == null || params.getId() <= 0) {
                ygMerchantCouponService.insertYgMerchantCoupon(params);
            } else {
                ygMerchantCouponService.updateYgMerchantCoupon(params);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券发放明细", httpMethod = "POST")
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_send_detail",name = "商户优惠券发放明细")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页数"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(paramType = "query", name = "couponId", value = "优惠券id"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "用户姓名"),
            @ApiImplicitParam(paramType = "query", name = "isUse", value = "Y | N"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单号")
    })
    @RequestMapping(value = "/sendDetail", method = {RequestMethod.POST})
    public BaseResponse<Page<YgUserCouponResult>> sendDetail(HttpServletRequest request, Integer couponId, String nickname, String isUse, String orderId) {
        BaseResponse<Page<YgUserCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YgUserCouponResult ydUserCouponResult = new YgUserCouponResult();
            ydUserCouponResult.setCouponId(couponId);
            ydUserCouponResult.setUseStatus(isUse);
            ydUserCouponResult.setOutOrderId(orderId);
            ydUserCouponResult.setNickname(nickname);
            ydUserCouponResult.setMerchantId(getCurrMerchantId(request));
            result.setResult(ygUserCouponService.findYdUserCouponListByPage(ydUserCouponResult, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id"),
    })
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_delete",name = "商户优惠券删除")
    @RequestMapping(value = "/deleteCoupon", method = {RequestMethod.POST})
    public BaseResponse<String> deleteCoupon(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
//            ygMerchantCouponService.deleteMerchantCoupon(merchantId, id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券上下架", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id"),
            @ApiImplicitParam(paramType = "query", name = "isShelf", value = "Y(上架) N(下架)")
    })
    @MerchantCheck(groupCode = YgSiteGroupEnum.MERCHANT, alias="merchant_coupon_up_down",name = "商户优惠券上下架")
    @RequestMapping(value = "/upOrDownCoupon", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownCoupon(HttpServletRequest request, Integer id, String isShelf) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
//            ygMerchantCouponService.upOrDownMerchantCoupon(merchantId, id, isShelf);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
