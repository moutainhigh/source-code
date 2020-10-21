package com.yd.web.controller.admin.decoration;

/**
 * 门店商品分类管理
 * @author wuyc
 * created 2019/10/22 10:33
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.decoration.YdMerchantBannerHistoryResult;
import com.yd.api.result.decoration.YdMerchantBannerResult;
import com.yd.api.result.material.YdMaterialPictureResult;
import com.yd.api.service.decoration.YdMerchantBannerHistoryService;
import com.yd.api.service.decoration.YdMerchantBannerService;
import com.yd.api.service.material.YdMaterialPictureService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantBannerRequest;
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

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant/banner")
public class AdminMerchantBannerController extends BaseController {

    @Reference
    private YdMerchantBannerService ydMerchantBannerService;

    @Reference
    private YdMaterialPictureService ydMaterialPictureService;

    @Reference
    private YdMerchantBannerHistoryService ydShopMerchantBannerHistoryService;


    @ApiOperation(value = "素材库列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_banner_data",name = "店铺轮播图列表")
    @RequestMapping(value = "/findMaterialList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdMaterialPictureResult>> findMaterialList(HttpServletRequest request) {
        BaseResponse<Page<YdMaterialPictureResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, Integer.MAX_VALUE);
            result.setResult(ydMaterialPictureService.findMaterialListByPage(pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询首页banner列表", httpMethod = "GET")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_banner_data",name = "店铺轮播图列表")
    @RequestMapping(value = "/findBannerList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdMerchantBannerResult>> findBannerList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantBannerResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantBannerResult ydMerchantBannerResult = new YdMerchantBannerResult();
        ydMerchantBannerResult.setMerchantId(getCurrMerchantId(request));
        result.setResult(ydMerchantBannerService.getAll(ydMerchantBannerResult));
        return result;
    }

    @ApiOperation(value = "首页banner新增修改", httpMethod = "GET")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_banner_update",name = "店铺轮播图新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request,
                                             @RequestBody MerchantBannerRequest params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        Integer merchantId = getCurrMerchantId(request);
        ydMerchantBannerService.saveOrUpdate(merchantId, params.getBannerList());
        return result;
    }

    @ApiOperation(value = "门店banner历史图片", httpMethod = "GET")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_banner_history_picture",name = "门店banner历史图片")
    @RequestMapping(value = "/findHistoryPictureList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdMerchantBannerHistoryResult>> findHistoryPictureList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantBannerHistoryResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantBannerHistoryResult params = new YdMerchantBannerHistoryResult();
        Integer merchantId = getCurrMerchantId(request);
        params.setMerchantId(merchantId);
        result.setResult(ydShopMerchantBannerHistoryService.getAll(params));
        return result;
    }


}
