package com.yg.web.controller.admin.item;

/**
 * 后台商户管理
 * @author wuyc
 * created 2019/10/24 16:31
 **/
import com.yg.api.result.item.YgMerchantIntegralItemResult;
import com.yg.api.result.merchant.YgMerchantResult;
import com.yg.api.service.item.YgMerchantIntegralItemService;
import com.yg.api.service.merchant.YgMerchantService;
import com.yg.core.res.BaseResponse;
import com.yg.core.utils.BusinessException;
import com.yg.core.utils.Page;
import com.yg.core.utils.PagerInfo;
import com.yg.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("admin/merchant/item")
public class AdminMerchantItemController extends BaseController {

    @Reference
    private YgMerchantIntegralItemService ygMerchantIntegralItemService;

    @ApiOperation(value = "商户商品列表", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "title", value = "商品标题")
    })
    @RequestMapping(value = "/findList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<Page<YgMerchantIntegralItemResult>> findList(HttpServletRequest request, String title) {
        BaseResponse<Page<YgMerchantIntegralItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YgMerchantIntegralItemResult req = new YgMerchantIntegralItemResult();
            req.setTitle(title);
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
            result.setResult(ygMerchantIntegralItemService.findYgMerchantIntegralItemListByPage(req, pagerInfo));
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户编辑商品", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "商户手机号"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数")
    })
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(YgMerchantIntegralItemResult req) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            if (req.getId() <= 0 || req.getId() == null) {
                ygMerchantIntegralItemService.insertYgMerchantIntegralItem(req);
            } else {
                ygMerchantIntegralItemService.updateYgMerchantIntegralItem(req);
            }
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户上下架商品", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y启用  N禁用"),
    })
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    public BaseResponse<String> updateMerchantStatus(Integer merchantId, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "设置成功");
        try {
            ygMerchantIntegralItemService.updateMerchantItemStatus(merchantId, isEnable);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户上删除商品", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y启用  N禁用"),
    })
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteMerchant(Integer merchantId, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "设置成功");
        try {
            ygMerchantIntegralItemService.deleteMerchantItemStatus(merchantId, isEnable);
        } catch (BusinessException exception) {
            result = BaseResponse.fail(exception.getCode(), exception.getMessage());
        }
        return result;
    }

}
