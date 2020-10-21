package com.yd.web.controller.app.index;

import com.yd.api.result.index.AppIndexResult;
import com.yd.api.service.index.AdminMerchantIndexService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DateUtils;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author wuyc
 * created 2019/11/7 19:55
 **/
@RestController
@AppMerchantCheck
@RequestMapping("/app/index")
public class AppIndexController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppIndexController.class);

    @Reference
    private AdminMerchantIndexService adminMerchantIndexService;

    @ApiOperation(value = "app首页数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间")
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getAppIndexData", method = {RequestMethod.POST})
    public BaseResponse<AppIndexResult> getAppSaleDetail(HttpServletRequest request, String startTime, String endTime) {
        BaseResponse<AppIndexResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                Date today = new Date();
                // 默认当天的数据
                startTime = DateUtils.getDateTime(today, DateUtils.TODAY_START_DATETIME_FORMAT);
                endTime = DateUtils.getDateTime(today, DateUtils.TODAY_END_DATETIME_FORMAT);
            } else {
                startTime = startTime + " 00:00:00";
                endTime = endTime + " 59:59:59";
            }
            result.setResult(adminMerchantIndexService.getAppIndexData(getCurrMerchantId(request), startTime, endTime));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "app营销统计", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "online(线上), offline(线下), all(全部), 不传默认全部"),
    })
    @AppMerchantCheck
    @RequestMapping(value = "/getAppSaleDetail", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> getAppSaleDetail(HttpServletRequest request, String startTime, String endTime, String type) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                Date today = new Date();
                // 默认查询最近七天的数据
                startTime = DateUtils.getDateTime(DateUtils.addDays(today, -7), DateUtils.DEFAULT_DATE_FORMAT);
                endTime = DateUtils.getDateTime(today, DateUtils.DEFAULT_DATE_FORMAT);
            }
            logger.info("====getAppSaleDetail的startTime=" + startTime + ",endTime=" + endTime);
            result.setResult(adminMerchantIndexService.getAppSaleDetail(getCurrMerchantId(request), startTime, endTime, type));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
