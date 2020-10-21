package com.yd.web.controller.admin.index;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.index.IndexDataStatisticsResult;
import com.yd.api.result.index.IndexHotRankResult;
import com.yd.api.result.index.IndexMyAccountResult;
import com.yd.api.result.index.IndexWaitHandleResult;
import com.yd.api.service.index.AdminMerchantIndexService;
import com.yd.core.res.BaseResponse;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商户首页数据查询
 * @author wuyc
 * created 2019/10/23 13:32
 **/
@RestController
@RequestMapping("/admin/merchant")
// @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_admin_index", name="商户后台首页")
public class AdminMerchantIndexController extends BaseController {

    @Reference
    private AdminMerchantIndexService adminMerchantIndexService;

    @ApiOperation(value = "首页我的账户", httpMethod = "GET")
    @MerchantCheck
    @RequestMapping(value = "/index/myAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<IndexMyAccountResult> getMyAccount(HttpServletRequest request) {

        BaseResponse<IndexMyAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(adminMerchantIndexService.getMyAccount(merchantId));
        return result;
    }

    @ApiOperation(value = "首页待处理数据", httpMethod = "GET")
    @MerchantCheck
    @RequestMapping(value = "/index/waitHandleData", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<IndexWaitHandleResult> getWaitHandleData(HttpServletRequest request) {

        BaseResponse<IndexWaitHandleResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(adminMerchantIndexService.getWaitHandleData(merchantId));
        return result;
    }

    @ApiOperation(value = "首页数据统计", httpMethod = "GET")
    @MerchantCheck
    @RequestMapping(value = "/index/dataStatistics", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<IndexDataStatisticsResult> dataStatistics(HttpServletRequest request, String dateType) {

        BaseResponse<IndexDataStatisticsResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(adminMerchantIndexService.getIndexDataStatistics(merchantId, dateType));
        return result;
    }

    /**
     * 首页热销数据
     * @param request
     * @param dateType week | month | year
     * @param platform admin | shop
     * @return
     */
    @ApiOperation(value = "首页热销数据", httpMethod = "GET")
    // @MerchantCheck
    @RequestMapping(value = "/index/hotRankData", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<IndexHotRankResult>> getHotRankData(HttpServletRequest request, String dateType, String platform) {
        BaseResponse<List<IndexHotRankResult>> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(adminMerchantIndexService.getHotRankData(merchantId, platform, dateType));
        return result;
    }

}
