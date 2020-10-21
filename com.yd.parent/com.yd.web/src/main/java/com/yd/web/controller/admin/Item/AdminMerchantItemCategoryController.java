package com.yd.web.controller.admin.Item;

/**
 * 门店商品分类管理
 * @author wuyc
 * created 2019/10/22 10:33
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.item.YdMerchantItemCategoryResult;
import com.yd.api.service.item.YdMerchantItemCategoryService;
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

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant/item/category")
public class AdminMerchantItemCategoryController extends BaseController {

    @Reference
    private YdMerchantItemCategoryService ydShopMerchantItemCategoryService;

    @ApiOperation(value = "商户商品分类列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true),
            @ApiImplicitParam(paramType = "query", name = "categoryName", value = "分类名称", required = false)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_category_data", name="商户商品分类列表")
    @RequestMapping(value = "/findCategoryList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMerchantItemCategoryResult>> findCategoryList(HttpServletRequest request, String categoryName) {
        BaseResponse<Page<YdMerchantItemCategoryResult>> result = BaseResponse.success(null, "00", "查询成功");

        YdMerchantItemCategoryResult params = new YdMerchantItemCategoryResult();
        params.setMerchantId(getCurrMerchantId(request));
        params.setCategoryName(categoryName);
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        result.setResult(ydShopMerchantItemCategoryService.findCategoryListByPage(params, pagerInfo));
        return result;
    }

    @ApiOperation(value = "商户商品分类新增修改", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "分类id，修改时用到", required = false),
            @ApiImplicitParam(paramType = "query", name = "pid", value = "上级分类id，顶级为0", required = true),
            @ApiImplicitParam(paramType = "query", name = "categoryName", value = "分类名称", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_category_update", name="商户商品分类新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request,
                                             YdMerchantItemCategoryResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        params.setMerchantId(getCurrMerchantId(request));
        if (params.getId() == null) {
            ydShopMerchantItemCategoryService.insertYdShopMerchantItemCategory(params);
        } else {
            ydShopMerchantItemCategoryService.updateYdShopMerchantItemCategory(params);
        }
        return result;
    }

    @ApiOperation(value = "商户商品分类删除", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "分类id", required = true),
            @ApiImplicitParam(paramType = "query", name = "pid", value = "顶级分类id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_category_delete", name="商户商品分类删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request,YdMerchantItemCategoryResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        params.setMerchantId(getCurrMerchantId(request));
        try {
            ydShopMerchantItemCategoryService.deleteYdShopMerchantItemCategory(params);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户商品分类排序", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "分类id", required = true),
            @ApiImplicitParam(paramType = "query", name = "pid", value = "顶级分类id", required = true),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "up(升序),down(降序)", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_category_sort", name="商户商品分类排序")
    @RequestMapping(value = "/sort", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> sort(HttpServletRequest request, Integer id, Integer pid, String sortType) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydShopMerchantItemCategoryService.SortYdShopMerchantItemCategory(id, pid, getCurrMerchantId(request), sortType);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
