package com.yd.web.controller.admin.decoration;

/**
 * 门店首页分类管理
 * @author wuyc
 * created 2019/10/31 15:44
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.decoration.PlatBrandResult;
import com.yd.api.result.decoration.YdMerchantBannerHistoryResult;
import com.yd.api.result.decoration.YdMerchantBannerResult;
import com.yd.api.result.decoration.YdMerchantBrandResult;
import com.yd.api.result.item.YdBrandResult;
import com.yd.api.service.decoration.YdMerchantBannerHistoryService;
import com.yd.api.service.decoration.YdMerchantBannerService;
import com.yd.api.service.decoration.YdMerchantBrandService;
import com.yd.api.service.item.YdBrandService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantBannerRequest;
import com.yd.web.request.MerchantBrandRequest;
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

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant/brand")
public class AdminMerchantBrandController extends BaseController {

    @Reference
    private YdBrandService ydBrandService;

    @Reference
    private YdMerchantBrandService ydMerchantBrandService;

    @ApiOperation(value = "平台品牌列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_get_platform_brand_list",name = "商户获取平台品牌列表")
    @RequestMapping(value = "/findPlatformBrandList", method = {RequestMethod.POST})
    public BaseResponse<List<YdBrandResult>> findPlatformBrandList() {
        BaseResponse<List<YdBrandResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydBrandService.getAll(null));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页品牌列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_brand_data", name="商户品牌列表")
    @RequestMapping(value = "/getAll", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantBrandResult>> getBrandAll(HttpServletRequest request) {
        BaseResponse<List<YdMerchantBrandResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantBrandResult params = new YdMerchantBrandResult();
            params.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydMerchantBrandService.getAll(params));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页品牌管理排序", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_brand_sort", name="商户品牌排序")
    @RequestMapping(value = "/sort", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> deleteBrand(@RequestBody MerchantBrandRequest brandData) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantBrandService.sortYdMerchantBrand(brandData.getBrandList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页品牌管理新增修改", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", name = "brandAlias", value = "品牌标识")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_brand_update", name="商户品牌新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, Integer id, String brandAlias) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdMerchantBrandResult params = new YdMerchantBrandResult();
            params.setBrandAlias(brandAlias);
            params.setMerchantId(getCurrMerchantId(request));
            if (id == null) {
                ydMerchantBrandService.insertYdMerchantBrand(params);
            } else {
                ydMerchantBrandService.updateYdMerchantBrand(params);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页品牌管理删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_brand_delete", name="商户品牌删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> deleteBrand(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdMerchantBrandResult params = new YdMerchantBrandResult();
            params.setId(id);
            params.setMerchantId(getCurrMerchantId(request));
            ydMerchantBrandService.deleteYdMerchantBrand(params);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
