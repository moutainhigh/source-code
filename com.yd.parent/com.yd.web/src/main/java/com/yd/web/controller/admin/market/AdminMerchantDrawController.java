package com.yd.web.controller.admin.market;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.req.MerchantDrawReq;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdUserDrawRecordResult;
import com.yd.api.service.draw.YdMerchantDrawActivityService;
import com.yd.api.service.draw.YdUserDrawRecordService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wuyc
 * created 2019/12/4 11:52
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_market",name = "营销管理")
@RequestMapping("/admin/merchant/draw")
public class AdminMerchantDrawController extends BaseController {

    @Reference
    private YdUserDrawRecordService ydUserDrawRecordService;

    @Reference
    private YdMerchantDrawActivityService ydMerchantDrawActivityService;

    @ApiOperation(value = "商户抽奖活动列表数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "activityType", value = "DZP. 大转盘活动 JJG.九宫格活动, CHB 猜红包", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_activity_data",name = "商户抽奖活动列表数据")
    @RequestMapping(value = "/findActivityList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantDrawActivityResult>> findActivityList(HttpServletRequest request, String activityType) {
        BaseResponse<Page<YdMerchantDrawActivityResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantDrawActivityResult params = new YdMerchantDrawActivityResult();
            params.setMerchantId(getCurrMerchantId(request));
            params.setActivityType(activityType);
            params.setIsFlag("N");
            result.setResult(ydMerchantDrawActivityService.findYdMerchantDrawActivityListByPage(params, getPageInfo(request)));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户抽奖活动详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "活动id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_activity_detail",name = "商户抽奖活动列表数据")
    @RequestMapping(value = "/getActivityDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantDrawActivityResult> getActivityDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantDrawActivityResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantDrawActivityService.getActivityDetail(id));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户抽奖活动新增修改", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_activity_update",name = "商户抽奖活动新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> findList(HttpServletRequest request, @RequestBody MerchantDrawReq req) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantDrawActivityService.saveOrUpdate(getCurrMerchantId(request), req);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户抽奖活动删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "活动id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_activity_delete", name = "商户抽奖活动删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantDrawActivityService.deleteYdMerchantDrawActivity(getCurrMerchantId(request), id);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户抽奖活动删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "上架时传Y，下架传N", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_activity_delete", name = "商户抽奖活动删除")
    @RequestMapping(value = "/upOrDownActivity", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownActivity(HttpServletRequest request, Integer id, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantDrawActivityService.upOrDownActivity(getCurrMerchantId(request), id, isEnable);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户抽奖活动记录数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "activityId", value = "活动id", required = true),
            @ApiImplicitParam(paramType = "query", name = "prizeType", value = "YHQ(优惠券) | WZJ(未中奖)", required = false),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_draw_record_data",name = "商户抽奖活动记录数据")
    @RequestMapping(value = "/findDrawRecordList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserDrawRecordResult>> findDrawRecordList(HttpServletRequest request, Integer activityId, String prizeType) {
        BaseResponse<Page<YdUserDrawRecordResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdUserDrawRecordResult params = new YdUserDrawRecordResult();
            params.setActivityId(activityId);
            params.setPrizeType(prizeType);
            params.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydUserDrawRecordService.findYdUserDrawRecordListByPage(params, getPageInfo(request)));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
