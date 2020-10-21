package com.yd.web.controller.admin.decoration;

/**
 * 门店首页分类管理
 * @author wuyc
 * created 2019/10/31 15:44
 **/

import com.alibaba.fastjson.JSON;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.decoration.YdMerchantCompareItemResult;
import com.yd.api.service.decoration.YdMerchantCompareItemService;
import com.yd.api.service.item.YdItemService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.request.MerchantCompareItemRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant/item/compare")
public class AdminMerchantItemCompareController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminMerchantItemCompareController.class);

    @Reference
    private YdMerchantCompareItemService ydMerchantCompareItemService;

    @Reference
    private YdItemService ydItemService;

    @ApiOperation(value = "门店比价商品列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_compare_data",name = "门店比价商品列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantCompareItemResult>> findMerchantItemList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantCompareItemResult>> result = BaseResponse.success(null, "00", "查询成功");
        logger.info("====进入门店比价商品列表");
        try {
            YdMerchantCompareItemResult params = new YdMerchantCompareItemResult();
            params.setMerchantId(getCurrMerchantId(request));
            List<YdMerchantCompareItemResult> resultList = ydMerchantCompareItemService.getAll(params);
            result.setResult(resultList);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        logger.info("====门店比价商品列表返回值=" + JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "门店比价商品编辑", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantItemIds", value = "merchantItemId")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_compare_update",name = "门店比价页头部编辑")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, String merchantItemIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantCompareItemService.batchInsertYdMerchantCompareItem(getCurrMerchantId(request), merchantItemIds);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "门店比价商品删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantItemId", value = "商户商品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_item_compare_delete",name = "门店比价商品删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(Integer merchantItemId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantCompareItemService.deleteYdMerchantCompareItem(merchantItemId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "门店比价商品列表排序", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT,  alias="merchant_item_compare_list_sort", name="门店比价商品列表排序")
    @RequestMapping(value = "/listSort", method = {RequestMethod.POST})
    public BaseResponse<String> listSort(HttpServletRequest request, @RequestBody MerchantCompareItemRequest dataList) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            ydMerchantCompareItemService.listSort(getCurrMerchantId(request), dataList.getDataList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
