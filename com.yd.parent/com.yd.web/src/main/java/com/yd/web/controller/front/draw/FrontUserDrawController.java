package com.yd.web.controller.front.draw;

import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdMerchantDrawPrizeResult;
import com.yd.api.service.draw.YdMerchantDrawActivityService;
import com.yd.api.service.draw.YdMerchantDrawPrizeService;
import com.yd.api.service.draw.YdUserDrawRecordService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户抽奖接口
 * @author wuyc
 * created 2019/12/4 15:24
 **/
@RestController
@RequestMapping("/front/user/draw")
public class FrontUserDrawController extends FrontBaseController {

    @Reference
    private YdUserDrawRecordService ydUserDrawRecordService;

    @Reference
    private YdMerchantDrawPrizeService ydMerchantDrawPrizeService;

    @Reference
    private YdMerchantDrawActivityService ydMerchantDrawActivityService;

    @ApiOperation(value = "小游戏模板页", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "活动uuid")
    })
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, String uuid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("uuid", uuid);
        if (StringUtils.isEmpty(uuid)) {
            modelAndView.setViewName("front/draw/errorIndex.html");
        }

        YdMerchantDrawActivityResult ydMerchantDrawActivityResult = ydMerchantDrawActivityService.getYdMerchantDrawActivityByUdid(uuid);
        if (ydMerchantDrawActivityResult == null) {
            modelAndView.setViewName("front/draw/errorIndex.html");
        } else {
            String type = ydMerchantDrawActivityResult.getActivityType();
            if ("DZP".equalsIgnoreCase(type)) {
                modelAndView.setViewName("front/draw/dzpIndex.html");
            } else if ("JGG".equalsIgnoreCase(type)) {
                modelAndView.setViewName("front/draw/jggIndex.html");
            } else if ("CHB".equalsIgnoreCase(type)) {
                modelAndView.setViewName("front/draw/chbIndex.html");
            }
        }
        return modelAndView;
    }

    @ApiOperation(value = "抽奖活动列表", httpMethod = "POST")
    // @FrontCheck
    @RequestMapping(value = "/findDrawList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdMerchantDrawActivityResult>> findDrawList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantDrawActivityResult>> result = BaseResponse.success(null, "00", "抽奖成功");
        try {
            YdMerchantDrawActivityResult params = new YdMerchantDrawActivityResult();
            params.setIsFlag("N");
            params.setIsEnable("Y");
            params.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydMerchantDrawActivityService.getAll(params));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "抽奖活动奖品列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "活动uuid")
    })
    // @FrontCheck
    @RequestMapping(value = "/findPrizeList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdMerchantDrawPrizeResult>> findPrizeList(String uuid) {
        BaseResponse<List<YdMerchantDrawPrizeResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantDrawActivityResult ydMerchantDrawActivityResult = ydMerchantDrawActivityService.getYdMerchantDrawActivityByUdid(uuid);
        if (ydMerchantDrawActivityResult != null) {
            YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult1 = new YdMerchantDrawPrizeResult();
            ydMerchantDrawPrizeResult1.setActivityId(ydMerchantDrawActivityResult.getId());
            result.setResult(ydMerchantDrawPrizeService.getAll(ydMerchantDrawPrizeResult1));
        }
        return result;
    }

    @ApiOperation(value = "抽奖活动详情信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "活动uuid")
    })
    // @FrontCheck
    @RequestMapping(value = "/getDrawActivityDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdMerchantDrawActivityResult> getDrawActivityDetail(String uuid) {
        BaseResponse<YdMerchantDrawActivityResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (StringUtils.isNotEmpty(uuid)) {
                result.setResult(ydMerchantDrawActivityService.getYdMerchantDrawActivityByUdid(uuid));
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户点击抽奖", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "抽奖活动uuid")
    })
    // @FrontCheck
    @RequestMapping(value = "/clickDraw", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Integer> userDraw(HttpServletRequest request, String uuid) {
        BaseResponse<Integer> result = BaseResponse.success(null, "00", "抽奖成功");
        try {
            result.setResult(ydMerchantDrawActivityService.clickDraw(getCurrUserId(request), uuid));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户抽奖次数", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "抽奖活动uuid")
    })
    // @FrontCheck
    @RequestMapping(value = "/getUserDrawCount", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Integer> getUserDrawCount(HttpServletRequest request, String uuid) {
        BaseResponse<Integer> result = BaseResponse.success(0, "00", "抽奖成功");
        try {
            int count = ydMerchantDrawActivityService.getUserDrawCount(getCurrUserId(request), uuid);
            result.setResult(count);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户抽奖奖品记录(抽中的优惠券列表)", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "uuid", value = "抽奖活动uuid")
    })
    // @FrontCheck
    @RequestMapping(value = "/getUserDrawRecord", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdUserCouponResult>> getUserDrawRecord(HttpServletRequest request, String uuid) {
        BaseResponse<List<YdUserCouponResult>> result = BaseResponse.success(null, "00", "抽奖成功");
        try {
            result.setResult(ydUserDrawRecordService.getUserDrawRecord(getCurrUserId(request), uuid));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
