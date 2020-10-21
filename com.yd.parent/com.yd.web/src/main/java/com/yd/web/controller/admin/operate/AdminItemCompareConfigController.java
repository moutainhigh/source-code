package com.yd.web.controller.admin.operate;

import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.service.item.YdItemBijiaService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
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
import java.util.List;
import java.util.Map;

/**
 * 商品比价链接配置
 * @author wuyc
 * created 2020/5/26 10:10
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_operate",name = "运营管理")
@RequestMapping("/admin/platform/item/compare/config")
public class AdminItemCompareConfigController extends BaseController {

    @Reference
    private YdItemBijiaService ydItemBijiaService;

    @ApiOperation(value = "配置比价链接数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "skuId", value = "平台商品skuId"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="item_compare_config_data",name = "配置比价链接数据")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<List<CrawerSiteSkuResult>> findList(HttpServletRequest request, Integer skuId) {
        BaseResponse<List<CrawerSiteSkuResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            List<CrawerSiteSkuResult> data = ydItemBijiaService.getBijiaBySkuId(skuId);
            result.setResult(data);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "配置比价链接修改", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "skuId", value = "平台商品skuId"),
            @ApiImplicitParam(paramType = "query", name = "siteName", value = "苏宁,天猫,京东,国美"),
            @ApiImplicitParam(paramType = "query", name = "link", value = "抓取地址")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="item_compare_config_update",name = "配置比价链接修改")
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<String> update(HttpServletRequest request, Integer skuId, String siteName, String link) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydItemBijiaService.updateItemCompareConfig(skuId, siteName, link);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }
}
