package com.yd.web.controller.admin.Item;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.service.item.YdItemService;
import com.yd.api.service.item.YdMerchantItemService;
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
import javax.validation.constraints.NotEmpty;

/**
 * 商品接口
 * @author wuyc
 * created 2019/10/23 16:27
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_item", name = "商品管理")
@RequestMapping("/admin/merchant/item")
public class AdminMerchantItemController extends BaseController {

    @Reference
    private YdItemService ydItemService;

    @Reference
    private YdMerchantItemService ydMerchantItemService;

    @ApiOperation(value = "商户商品列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "商户商品标题"),
            @ApiImplicitParam(paramType = "query", name = "firstCategoryId", value = "一级分类id"),
            @ApiImplicitParam(paramType = "query", name = "secondCategoryId", value = "二级分类id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "是否上架(Y|N)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_data", name = "商户商品列表数据")
    @RequestMapping(value = "/findMerchantItemList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMerchantItemResult>> findMerchantItemList(HttpServletRequest request, String isEnable, String title,
                                                                         Integer firstCategoryId, Integer secondCategoryId) {
        BaseResponse<Page<YdMerchantItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);

            YdMerchantItemResult params = new YdMerchantItemResult();
            params.setTitle(title);
            params.setIsEnable(isEnable);
            params.setMerchantId(merchantId);
            params.setFirstCategoryId(firstCategoryId);
            params.setSecondCategoryId(secondCategoryId);
            result.setResult(ydMerchantItemService.findMerchantItemListByPage(params, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户商品详情", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户商品id"),
            @ApiImplicitParam(paramType = "query", name = "itemId", value = "商品库商品id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_detail", name = "商户商品详情")
    @RequestMapping(value = "/getMerchantItemDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<YdMerchantItemResult> getMerchantItemDetail(HttpServletRequest request, Integer id, Integer itemId) {
        BaseResponse<YdMerchantItemResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydMerchantItemService.getMerchantItemDetail(merchantId, id, itemId));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户商品保存修改", httpMethod = "GET")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_update", name = "商户商品新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, @RequestBody YdMerchantItemResult itemInfo) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            itemInfo.setMerchantId(getCurrMerchantId(request));
            ydMerchantItemService.saveOrUpdate(itemInfo);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户商品上下架", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户商品id", required = true),
            @ApiImplicitParam(paramType = "query", name = "type", value = "up(上架),down(下架)", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_upOrDown", name = "商户商品上下架")
    @RequestMapping(value = "/upOrDownMerchantItem", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> upOrDownMerchantItem(HttpServletRequest request,
                                                     @NotEmpty(message = "商品id不可以为空") Integer id,
                                                     @NotEmpty(message = "操作类型不可以为空") String type) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer merchantId = getCurrMerchantId(request);
            ydMerchantItemService.upOrDownMerchantItem(merchantId, id, type);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户商品删除", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户商品id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_delete", name = "商户商品删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, @NotEmpty(message = "商品id不可以为空") Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        Integer merchantId = getCurrMerchantId(request);
        try {
            ydMerchantItemService.deleteMerchantItem(merchantId, id);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台商品库", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "平台库品牌id", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_get_platform_item_data", name = "商户获取平台商品库")
    @RequestMapping(value = "/findItemList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdItemResult>> findItemList(HttpServletRequest request, Integer brandId) {
        BaseResponse<Page<YdItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
            YdItemResult params = new YdItemResult();
            params.setIsEnable("Y");
            params.setBrandId(brandId);
            result.setResult(ydItemService.findItemListByPage(params, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
