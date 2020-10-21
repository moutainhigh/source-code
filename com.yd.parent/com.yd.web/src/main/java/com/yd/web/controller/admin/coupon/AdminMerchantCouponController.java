package com.yd.web.controller.admin.coupon;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.service.coupon.YdMerchantCouponService;
import com.yd.api.service.coupon.YdUserCouponService;
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
 * 商户优惠券管理
 * @author wuyc
 * created 2019/11/26 11:54
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_market",name = "营销管理")
@RequestMapping("/admin/merchant/coupon")
public class AdminMerchantCouponController extends BaseController {

    @Reference
    private YdUserCouponService ydUserCouponService;

    @Reference
    private YdMerchantCouponService ydMerchantCouponService;

    @ApiOperation(value = "商户优惠券列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "couponTitle", value = "角色id"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "isShelf", value = "是否上架"),
            @ApiImplicitParam(paramType = "query", name = "couponType", value = "ZJ | MJ"),
            @ApiImplicitParam(paramType = "query", name = "resource", value = "适用场景 : 直接领用， 抽奖活动专用")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_data",name = "商户优惠券数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantCouponResult>> findList(HttpServletRequest request,String couponTitle, String isShelf,
                                                               String startTime, String endTime, String resource, String couponType) {
        BaseResponse<Page<YdMerchantCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pageInfo = getPageInfo(request, 1, 10);

        YdMerchantCouponResult params = new YdMerchantCouponResult();
        params.setCouponTitle(couponTitle);
        params.setStartTime(startTime);
        params.setEndTime(endTime);
        params.setIsShelf(isShelf);
        params.setCouponResource(resource);
        params.setCouponType(couponType);
        params.setMerchantId(getCurrMerchantId(request));
        Page<YdMerchantCouponResult> list = ydMerchantCouponService.findYdMerchantCouponListByPage(params, pageInfo);
        return BaseResponse.success(list);
    }

    @ApiOperation(value = "商户优惠券详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_detail",name = "商户优惠券详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantCouponResult> getDetail(Integer id) {
        BaseResponse<YdMerchantCouponResult> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantCouponResult detail = ydMerchantCouponService.getYdMerchantCouponById(id);
        result.setResult(detail);
        return result;
    }

    @ApiOperation(value = "商户优惠券新增修改", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_update",name = "商户优惠券新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdMerchantCouponResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        try {
            params.setMerchantId(getCurrMerchantId(request));
            if (params.getId() == null || params.getId() <= 0) {
                ydMerchantCouponService.insertYdMerchantCoupon(params);
            } else {
                ydMerchantCouponService.updateYdMerchantCoupon(params);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券发放明细", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_send_detail",name = "商户优惠券发放明细")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页数"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(paramType = "query", name = "couponId", value = "优惠券id"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "用户姓名"),
            @ApiImplicitParam(paramType = "query", name = "isUse", value = "Y | N"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单号")
    })
    @RequestMapping(value = "/sendDetail", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserCouponResult>> sendDetail(HttpServletRequest request, Integer couponId, String nickname, String isUse, String orderId) {
        BaseResponse<Page<YdUserCouponResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdUserCouponResult ydUserCouponResult = new YdUserCouponResult();
            ydUserCouponResult.setCouponId(couponId);
            ydUserCouponResult.setUseStatus(isUse);
            ydUserCouponResult.setOutOrderId(orderId);
            ydUserCouponResult.setNickname(nickname);
            ydUserCouponResult.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydUserCouponService.findYdUserCouponListByPage(ydUserCouponResult, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券增加发行量", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id"),
            @ApiImplicitParam(paramType = "query", name = "num", value = "新增加的数量")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_add_amount",name = "商户优惠券增加发行量")
    @RequestMapping(value = "/addCouponAmount", method = {RequestMethod.POST})
    public BaseResponse<String> addCouponAmount(HttpServletRequest request, Integer num, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydMerchantCouponService.addCouponAmount(merchantId, id, num);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户优惠券删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "优惠券id"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_delete",name = "商户优惠券删除")
    @RequestMapping(value = "/deleteCoupon", method = {RequestMethod.POST})
    public BaseResponse<String> deleteCoupon(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydMerchantCouponService.deleteMerchantCoupon(merchantId, id);
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
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_coupon_up_down",name = "商户优惠券上下架")
    @RequestMapping(value = "/upOrDownCoupon", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownCoupon(HttpServletRequest request, Integer id, String isShelf) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydMerchantCouponService.upOrDownMerchantCoupon(merchantId, id, isShelf);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
