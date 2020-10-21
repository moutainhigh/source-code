package com.yd.web.controller.admin.gift;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.gift.YdGiftCategoryResult;
import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.service.gift.YdGiftCategoryService;
import com.yd.api.service.gift.YdGiftService;
import com.yd.api.service.order.YdGiftOrderDetailService;
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
 * 平台礼品管理
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@RestController
@RequestMapping("/admin/gift/platform")
@MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="mod_platform_gift", name = "礼品管理")
public class AdminGiftController extends BaseController {

    @Reference
    private YdGiftService ydGiftService;

    @Reference
    private YdGiftCategoryService ydGiftCategoryService;

    @Reference
    private YdGiftOrderDetailService ydGiftOrderDetailService;

    @ApiOperation(value = "平台礼品分类列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "categoryName", value = "分类名称")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_category_data", name="礼品分类列表")
    @RequestMapping(value = "/category/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdGiftCategoryResult>> findGiftCategoryListByPage(HttpServletRequest request, String categoryName) {
        BaseResponse<Page<YdGiftCategoryResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
            result.setResult(ydGiftCategoryService.findCategoryListByPage(categoryName, pagerInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品分类新增修改", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "分类id"),
            @ApiImplicitParam(paramType = "query", name = "categoryName", value = "分类名称", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_category_saveOrUpdate", name="平台礼品分类新增修改")
    @RequestMapping(value = "/category/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdateGiftCategory(HttpServletRequest request, Integer id, String categoryName) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdGiftCategoryResult categoryResult = new YdGiftCategoryResult();
            categoryResult.setCategoryName(categoryName);
            if (id == null) {
                ydGiftCategoryService.insertYdGiftCategory(categoryResult);
            } else {
                categoryResult.setId(id);
                ydGiftCategoryService.updateYdGiftCategory(categoryResult);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品分类删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "分类id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_category_delete", name="平台礼品分类删除")
    @RequestMapping(value = "/category/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteGiftCategory(HttpServletRequest request,
                                             @NotEmpty(message = "分类名称不能为空") Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            YdGiftCategoryResult categoryResult = new YdGiftCategoryResult();
            categoryResult.setId(id);
            ydGiftCategoryService.deleteYdGiftCategory(categoryResult);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品标题"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "礼品状态: Y(上架)N(下架)"),
            @ApiImplicitParam(paramType = "query", name = "supplierId", value = "供应商id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_data", name="平台礼品库列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdGiftResult>> findGiftListByPage(HttpServletRequest request, String title,
                                                               String isEnable, Integer supplierId) {
        BaseResponse<Page<YdGiftResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pagerInfo = new PagerInfo(request, 1, 10);
            YdGiftResult ydGiftResult = new YdGiftResult();
            ydGiftResult.setTitle(title);
            ydGiftResult.setIsEnable(isEnable);
            ydGiftResult.setSupplierId(supplierId);
            ydGiftResult.setIsFlag("N");
            result.setResult(ydGiftService.findYdGiftListByPage(ydGiftResult, pagerInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库礼品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "礼品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_detail", name="平台礼品库礼品详情")
    @RequestMapping(value = "/getGiftDetail", method = {RequestMethod.POST})
    public BaseResponse<YdGiftResult> getGiftDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdGiftResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydGiftService.getYdGiftById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库新增修改", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品标题"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "礼品状态: Y(上架)N(下架)"),
            @ApiImplicitParam(paramType = "query", name = "supplierId", value = "供应商id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_saveOrUpdate", name="礼品库新增修改")
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdateGift(HttpServletRequest request, @RequestBody YdGiftResult giftResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            if (giftResult.getId() == null) {
                ydGiftService.insertYdGift(giftResult);
            } else {
                ydGiftService.updateYdGift(giftResult);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库上下架", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "礼品id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y(上架)N(上架)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_upOrDown", name="平台礼品库上下架")
    @RequestMapping(value = "/upOrDownYdGift", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownYdGift(HttpServletRequest request,
                                               @NotEmpty(message = "礼品id不可以为空") Integer id,
                                               @NotEmpty(message = "操作状态不可以为空") String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydGiftService.upOrDownYdGift(id, isEnable);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "礼品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_delete", name="平台礼品库删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteYdGift(HttpServletRequest request,
                                               @NotEmpty(message = "礼品id不可以为空") Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydGiftService.deleteYdGift(id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库礼品记录", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品名称"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "settlementStatus", value = "打款状态 (WAIT | SUCCESS)"),
            @ApiImplicitParam(paramType = "query", name = "supplierId", value = "供应商id"),
    })
    @MerchantCheck(groupCode = EnumSiteGroup.SYS, alias="platform_gift_record", name="平台礼品库礼品记录")
    @RequestMapping(value = "/findGiftRecord", method = {RequestMethod.POST})
    public BaseResponse<Page<YdGiftOrderDetailResult>> findGiftRecord(HttpServletRequest request, String settlementStatus, String title,
                                                                      String startTime, String endTime, Integer supplierId) {
        BaseResponse<Page<YdGiftOrderDetailResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdGiftOrderDetailResult giftOrderDetailResult = new YdGiftOrderDetailResult();
            giftOrderDetailResult.setTitle(title);
            giftOrderDetailResult.setSupplierId(supplierId);
            giftOrderDetailResult.setStartTime(startTime);
            giftOrderDetailResult.setEndTime(endTime);
            giftOrderDetailResult.setSettlementStatus(settlementStatus);
            PagerInfo pagerInfo = getPageInfo(request, 1, 10);
            result.setResult(ydGiftOrderDetailService.findSupplierGiftOrderDetailListByPage(giftOrderDetailResult, pagerInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库新增下改采信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftId", value = "礼品id"),
            @ApiImplicitParam(paramType = "query", name = "supplierId", value = "供应商id"),
            @ApiImplicitParam(paramType = "query", name = "purchasePrice", value = "采购数量"),
            @ApiImplicitParam(paramType = "query", name = "purchasePrice", value = "采购单价")
    })
    @MerchantCheck(alias="platform_gift_insert_purchase_info", name="平台礼品库新增下改采信息")
    @RequestMapping(value = "/insertPurchaseInfo", method = {RequestMethod.POST})
    public BaseResponse<String> insertPurchaseInfo(Integer giftId, Integer supplierId, Integer num, Double purchasePrice) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            ydGiftService.insertPurchaseInfo(giftId, supplierId, num, purchasePrice);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品库修改下改采信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftOrderDetailId", value = "礼品订单详情id"),
            @ApiImplicitParam(paramType = "query", name = "num", value = "采购数量"),
            @ApiImplicitParam(paramType = "query", name = "purchasePrice", value = "采购单价")
    })
    @MerchantCheck(alias="platform_gift_update_purchase_info", name="平台礼品库修改下改采信息")
    @RequestMapping(value = "/updatePurchaseInfo", method = {RequestMethod.POST})
    public BaseResponse<String> updatePurchasePrice(Integer giftOrderDetailId, Integer num, Double purchasePrice) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydGiftService.updatePurchaseInfo(giftOrderDetailId, num, purchasePrice);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台礼品订单修改打款状态", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "giftOrderDetailIds", value = "礼品订单详情id")
    })
    @MerchantCheck(alias="platform_gift_order_update_settlement_status", name="平台礼品订单修改打款状态")
    @RequestMapping(value = "/updateSettlementStatus", method = {RequestMethod.POST})
    public BaseResponse<String> updatePurchasePrice(String giftOrderDetailIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        try {
            ydGiftService.updateSettlementStatus(giftOrderDetailIds);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }



}
