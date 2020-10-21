package com.yd.web.controller.admin.material;

/**
 * 平台比价商品
 * @author wuyc
 * created 2020/05/20 10:17
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.service.decoration.YdMerchantCompareItemService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_material",name = "素材管理")
@RequestMapping("/admin/item/compare")
public class AdminItemCompareController extends BaseController {

    @Reference
    private YdMerchantCompareItemService ydMerchantCompareItemService;

    @Reference
    private YdItemService ydItemService;

    @ApiOperation(value = "平台比价商品列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "商品标题"),
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌id"),
            @ApiImplicitParam(paramType = "query", name = "isHeadImage", value = "是否上传头图"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "上架状态(Y|N)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_compare_data", name = "平台比价商品列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YdItemResult>> findMerchantItemList(HttpServletRequest request, Integer brandId, String isHeadImage,
                                                                 String isEnable, String title) {
        BaseResponse<Page<YdItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
        try {
            YdItemResult params = new YdItemResult();
            params.setTitle(title);
            params.setIsEnable(isEnable);
            params.setBrandId(brandId);
            params.setIsHeadImage(isHeadImage);
            result.setResult(ydItemService.findItemListByPage(params, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台比价商品图片上传", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商品id"),
            @ApiImplicitParam(paramType = "query", name = "headImageUrl", value = "比较图片地址"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_item_compare_update",name = "平台比价商品图片上传")
    @RequestMapping(value = "/headImage/update", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, Integer id, String headImageUrl) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydItemService.updateIsHeadImage(id, headImageUrl);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
