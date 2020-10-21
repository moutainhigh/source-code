package com.yd.web.controller.admin.Item;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.item.YdBrandResult;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.service.item.YdBrandService;
import com.yd.api.service.item.YdItemService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 平台商品库操作
 * @author wuyc
 * created 2019/10/24 11:58
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_item", name = "商品管理")
@RequestMapping("/admin/item")
public class AdminItemController extends BaseController {

    @Reference
    private YdItemService ydItemService;

    @Reference
    private YdBrandService ydBrandService;

    @ApiOperation(value = "平台品牌列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_brand_list",name = "平台品牌列表")
    @RequestMapping(value = "/findBrandList", method = {RequestMethod.POST})
    public BaseResponse<List<YdBrandResult>> findBrandList() {
        BaseResponse<List<YdBrandResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydBrandService.getAll(null));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "商品标题"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "上架状态(Y|N)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_data", name = "商品列表")
    @RequestMapping(value = "/findItemList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdItemResult>> findMerchantItemList(HttpServletRequest request, Integer brandId, String isEnable, String title) {
        BaseResponse<Page<YdItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        try {
            YdItemResult params = new YdItemResult();
            params.setTitle(title);
            params.setIsEnable(isEnable);
            params.setBrandId(brandId);
            result.setResult(ydItemService.findItemListByPage(params, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品编辑", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_update", name = "商品编辑")
    @RequestMapping(value = "/updateItem", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> updateItem(HttpServletRequest request, @RequestBody YdItemResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydItemService.updateYdItem(params);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "商品id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_detail", name = "商品详情")
    @RequestMapping(value = "/getItemDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdItemResult> getItemDetail(HttpServletRequest request, Integer itemId) {
        BaseResponse<YdItemResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydItemService.getItemDetail(itemId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品上下架", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "商户商品id", required = true),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y(上架),N(下架)", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_upOrDown", name = "商品上下架")
    @RequestMapping(value = "/upOrDownItem", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> upOrDownItem(HttpServletRequest request, Integer itemId, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydItemService.upOrDownItem(itemId, isEnable);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "itemId", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_delete", name = "删除商品")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer itemId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydItemService.deleteItem(itemId);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
