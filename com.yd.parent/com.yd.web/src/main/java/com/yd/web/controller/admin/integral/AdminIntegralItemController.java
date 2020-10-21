package com.yd.web.controller.admin.integral;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.integral.YdIntegralConfigResult;
import com.yd.api.result.integral.YdIntegralItemResult;
import com.yd.api.service.integral.YdIntegralConfigService;
import com.yd.api.service.integral.YdIntegralItemService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 积分商品后台管理
 * @author wuyc
 * created 2019/12/27 11:59
 **/
@RestController
@RequestMapping("/admin/integral/item")
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_integral", name = "积分管理")
public class AdminIntegralItemController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminIntegralItemController.class);

    @Reference
    private YdIntegralItemService ydIntegralItemService;

    @Reference
    private YdIntegralConfigService ydIntegralConfigService;

    @ApiOperation(value = "平台积分商品数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_data", name="平台积分商品数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdIntegralItemResult>> findList(HttpServletRequest request, YdIntegralItemResult params) {
        BaseResponse<Page<YdIntegralItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydIntegralItemService.findYdIntegralItemListByPage(params, getPageInfo(request)));
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台积分商品新增修改", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_update", name="平台积分商品新增修改")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdIntegralItemResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        try {
            if (params.getId() == null || params.getId() == 0) {
                ydIntegralItemService.insertYdIntegralItem(params);
            } else {
                ydIntegralItemService.updateYdIntegralItem(params);
            }
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台积分商品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "积分商品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_detail", name="平台积分商品详情")
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdIntegralItemResult> getItemDetail(HttpServletRequest request, Integer itemId) {
        BaseResponse<YdIntegralItemResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydIntegralItemService.getYdIntegralItemById(itemId));
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台积分商品删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "积分商品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_delete", name="平台积分商品删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer itemId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydIntegralItemService.deleteIntegralItem(itemId);
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台积分商品上下架", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "积分商品id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "操作类型 Y(上架) | N(下架)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_upOrDown", name="平台积分商品上下架")
    @RequestMapping(value = "/upOrDown", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownIntegralItem(Integer itemId, String type) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydIntegralItemService.upOrDownIntegralItem(itemId, type);
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询积分商品配置(包含折扣比率)", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_config", name="平台积分商品配置信息")
    @RequestMapping(value = "/getConfig", method = {RequestMethod.POST})
    public BaseResponse<YdIntegralConfigResult> getIntegralConfig() {
        BaseResponse<YdIntegralConfigResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydIntegralConfigService.getAll(new YdIntegralConfigResult()).get(0));
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "修改积分商品配置(包含折扣比率)", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_integral_item_config_update", name="修改积分商品配置")
    @RequestMapping(value = "/config/update", method = {RequestMethod.POST})
    public BaseResponse<String> updateIntegralSettlementRate(Integer id, Integer settlementRate) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydIntegralConfigService.updateIntegralSettlementRate(id, settlementRate);
        } catch (BusinessException e) {
            result.setValue(e.getErrorCode(), e.getMessage());
        }
        return result;
    }

}
