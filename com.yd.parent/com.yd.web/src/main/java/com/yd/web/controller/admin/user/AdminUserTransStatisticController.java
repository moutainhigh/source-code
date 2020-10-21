package com.yd.web.controller.admin.user;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.result.user.YdUserTransStatisticResult;
import com.yd.api.service.user.YdUserTransStatisticService;
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
 * 用户管理
 * @author wuyc
 * created 2019/11/7 16:05
 **/
@RestController
@RequestMapping("/admin/merchant/user")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_user",name = "用户管理")
public class AdminUserTransStatisticController extends BaseController {

    @Reference
    private YdUserTransStatisticService ydUserTransStatisticService;

    @ApiOperation(value = "用户管理数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号"),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "昵称"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "最近交易起始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "最近交易结束时间"),
            @ApiImplicitParam(paramType = "query", name = "minBuyNum", value = "购买最小次数"),
            @ApiImplicitParam(paramType = "query", name = "maxBuyNum", value = "购买最大次数"),
            @ApiImplicitParam(paramType = "query", name = "minTransAmount", value = "交易最小金额"),
            @ApiImplicitParam(paramType = "query", name = "maxTransAmount", value = "交易最大金额"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_user_trans_statistic_data", name="用户管理数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserTransStatisticResult>> findList(HttpServletRequest request, String mobile, String nickname, String startTime,
                                                                   String endTime, Integer minBuyNum, Integer maxBuyNum,
                                                                   Double minTransAmount, Double maxTransAmount) {
        BaseResponse<Page<YdUserTransStatisticResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request);
            Page<YdUserTransStatisticResult> dataList = ydUserTransStatisticService.findUserTransStatisticListByPage(
                    getCurrMerchantId(request), mobile, nickname, startTime, endTime, minBuyNum, maxBuyNum, minTransAmount, maxTransAmount, pageInfo);
            result.setResult(dataList);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户交易详情数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_user_trans_detail_data", name="用户交易详情数据")
    @RequestMapping(value = "/findDetailList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findDetailList(HttpServletRequest request, Integer userId) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request);
            YdUserOrderResult ydUserOrderResult = new YdUserOrderResult();
            ydUserOrderResult.setOrderStatus("SUCCESS");
            ydUserOrderResult.setUserId(userId);
            ydUserOrderResult.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydUserTransStatisticService.findOrderDetailListByPage(ydUserOrderResult, pageInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
