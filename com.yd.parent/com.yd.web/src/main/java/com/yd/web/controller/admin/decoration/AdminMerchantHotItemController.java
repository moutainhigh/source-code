package com.yd.web.controller.admin.decoration;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.decoration.YdMerchantBrandResult;
import com.yd.api.result.decoration.YdMerchantHotItemResult;
import com.yd.api.service.decoration.YdMerchantHotItemService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantHotItemRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 商户首页热门推荐商品
 * @author wuyc
 * created 2019/10/31 18:26
 **/

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant/hot/item")
public class AdminMerchantHotItemController extends BaseController {

    @Reference
    private YdMerchantHotItemService ydMerchantHotItemService;

    @ApiOperation(value = "商户首页热门商品列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_hot_item_list", name="商户热门商品列表")
    @RequestMapping(value = "/getAll", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantHotItemResult>> findList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantHotItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantHotItemResult params = new YdMerchantHotItemResult();
            params.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydMerchantHotItemService.getAll(params));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页热门商品排序", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "hotItemList", value = "商品list"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_hot_item_sort", name="商户首页热门商品排序")
    @RequestMapping(value = "/sort", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> sort(HttpServletRequest request, @RequestBody MerchantHotItemRequest hotItemData) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantHotItemService.sortHotItem(hotItemData.getHotItemList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户热门商品新增修改", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantItemId", value = "商户商品ID"),
            @ApiImplicitParam(paramType = "query", name = "firstCategoryId", value = "一级分类ID"),
            @ApiImplicitParam(paramType = "query", name = "secondCategoryId", value = "二级分类ID"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT,  alias="merchant_hot_item_update", name="商户热门商品新增修改")
    @RequestMapping(value = "/saveHotItem", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, @RequestBody MerchantHotItemRequest hotItemData) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            ydMerchantHotItemService.insertYdMerchantHotItemList(getCurrMerchantId(request), hotItemData.getHotItemList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户热门商品删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_hot_item_delete", name="商户热门商品删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request,
                                            @NotEmpty(message = "ID不可以为空") Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            YdMerchantHotItemResult params = new YdMerchantHotItemResult();
            params.setId(id);
            params.setMerchantId(getCurrMerchantId(request));
            ydMerchantHotItemService.deleteYdMerchantHotItem(params);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
