package com.yd.web.controller.admin.gift;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.gift.YdGiftCategoryResult;
import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.gift.YdMerchantGiftCartResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.gift.YdGiftCategoryService;
import com.yd.api.service.gift.YdGiftService;
import com.yd.api.service.gift.YdMerchantGiftCartService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.anotation.MerchantCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.export.YdMerchantGiftExcel;
import com.yd.web.util.ExcelUtiles;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商户礼品接口
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@Controller
@RequestMapping("/admin/merchant/gift")
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mode_merchant_gift", name = "礼品管理")
public class AdminMerchantGiftController extends BaseController {

    @Reference
    private YdGiftService ydGiftService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantGiftService ydMerchantGiftService;

    @Reference
    private YdGiftCategoryService ydGiftCategoryService;

    @Reference
    private YdMerchantGiftCartService ydMerchantGiftCartService;

    @Reference
    private YdMerchantGiftAccountService ydMerchantGiftAccountService;

    @ApiOperation(value = "商户礼品商城供应商列表", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_supplier_list", name="商户礼品商城供应商列表")
    @ResponseBody
    @RequestMapping(value = "/platform/getSupplierList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantResult>> getSupplierList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantService.getSupplierList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "分类Id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_platform_data", name="商户礼品商城数据")
    @ResponseBody
    @RequestMapping(value = "/platform/getData", method = {RequestMethod.POST})
    public BaseResponse<List<YdGiftCategoryResult>> getPlatformData(HttpServletRequest request, Integer categoryId) {
        BaseResponse<List<YdGiftCategoryResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdGiftCategoryResult ydGiftCategoryResult = new YdGiftCategoryResult();
            ydGiftCategoryResult.setId(categoryId);
            result.setResult(ydGiftCategoryService.getAll(ydGiftCategoryResult));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城页面购物车商品数量", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_num", name="商户礼品商城页面购物车商品数量")
    @ResponseBody
    @RequestMapping(value = "/cart/getMerchantGiftCartNum", method = {RequestMethod.POST})
    public BaseResponse<Integer> getMerchantGiftCartNum(HttpServletRequest request) {
        BaseResponse<Integer> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantGiftCartService.getMerchantGiftCartCount(getCurrMerchantId(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城购物车选中后计算总价钱", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_check_total_price", name="商户礼品商城购物车选中后计算总价钱")
    @ResponseBody
    @RequestMapping(value = "/cart/getGiftCartCheckTotalPrice", method = {RequestMethod.POST})
    public BaseResponse<Double> getMerchantGiftCartNum(HttpServletRequest request, String carIds) {
        BaseResponse<Double> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydMerchantGiftCartService.getGiftCartTotalPrice(getCurrMerchantId(request), carIds));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城购物车数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_data", name="商户礼品商城购物车数据")
    @ResponseBody
    @RequestMapping(value = "/cart/getMerchantGiftCart", method = {RequestMethod.POST})
    public BaseResponse<List<YdGiftResult>> getMerchantGiftCart(HttpServletRequest request) {
        BaseResponse<List<YdGiftResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            List<YdGiftResult> cartList = ydMerchantGiftCartService.getMerchantGiftCart(getCurrMerchantId(request));
            result.setResult(cartList);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城礼品添加礼品到购物车", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "num", value = "礼品数量"),
            @ApiImplicitParam(paramType = "query", name = "giftId", value = "礼品Id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "shop(页面加入购物车) cart(购物车继续添加修改)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_add", name="商户礼品商城礼品添加礼品到购物车")
    @ResponseBody
    @RequestMapping(value = "/cart/addMerchantGiftCart", method = {RequestMethod.POST})
    public BaseResponse<Integer> addMerchantGiftCart(HttpServletRequest request, String type, Integer num, Integer giftId) {
        BaseResponse<Integer> result = BaseResponse.success(null, "00", "添加成功");
        try {
            YdMerchantGiftCartResult giftCartResult = new YdMerchantGiftCartResult();
            giftCartResult.setNum(num);
            giftCartResult.setGiftId(giftId);
            giftCartResult.setMerchantId(getCurrMerchantId(request));
            ydMerchantGiftCartService.addMerchantGiftCart(getCurrMerchantId(request), giftId, num, type);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品购物车删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_delete", name="商户礼品购物车删除")
    @ResponseBody
    @RequestMapping(value = "/cart/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request,
                                                      @NotEmpty(message = "carIds") String carIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydMerchantGiftCartService.deleteMerchantGiftCart(getCurrMerchantId(request), carIds);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品清空购物车", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_clear", name="商户礼品商城页面清空购物车")
    @ResponseBody
    @RequestMapping(value = "/cart/clearMerchantGiftCart", method = {RequestMethod.POST})
    public BaseResponse<String> clearMerchantGiftCart(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantGiftCartService.clearMerchantGiftCart(getCurrMerchantId(request));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品购物车选中礼品导入", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @MerchantCheck(alias="merchant_gift_cart_export", name="商户礼品购物车选中礼品导入")
    @ResponseBody
    @RequestMapping(value = "/cart/export", method = {RequestMethod.POST})
    public BaseResponse<String> exportMerchantGiftCart(HttpServletRequest request, String carIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "导入成功");
        try {
            List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            ydMerchantGiftService.exportMerchantGift(getCurrMerchantId(request), carIdList);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品购物车提交", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_submit", name="商户礼品购物车提交")
    @ResponseBody
    @RequestMapping(value = "/cart/submitMerchantGiftCart", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Double>> clearMerchantGiftCart(HttpServletRequest request,
                                                                   @NotEmpty(message = "carIds") String carIds) {
        BaseResponse<Map<String, Double>> result = BaseResponse.success(null, "00", "提交成功");
        try {
            Map<String, Double> data = ydMerchantGiftCartService.submitMerchantGiftCart(getCurrMerchantId(request), carIds);
            result.setResult(data);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品购物车支付", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "支付密码")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_to_pay", name="商户礼品购物车支付")
    @ResponseBody
    @RequestMapping(value = "/cart/toPay", method = {RequestMethod.POST})
    public BaseResponse<Boolean> toPay(HttpServletRequest request, String carIds, String password) {
        BaseResponse<Boolean> result = BaseResponse.success(null, "00", "提交成功");
        try {
            Boolean data = ydMerchantGiftCartService.toPay(getCurrMerchantId(request), password, carIds);
            result.setResult(data);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    // ------------------------------  店铺礼品 ----------------------------
    @ApiOperation(value = "商户礼品列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品名称"),
            @ApiImplicitParam(paramType = "query", name = "minPrice", value = "礼品最低价"),
            @ApiImplicitParam(paramType = "query", name = "maxPrice", value = "礼品最高价"),
            @ApiImplicitParam(paramType = "query", name = "giftStatus", value = "礼品状态 (in_shelves | no_shelves | invalid)")

    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_data", name="商户礼品列表")
    @ResponseBody
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantGiftResult>> findMerchantGiftList(HttpServletRequest request, String title,
                                                                         String giftStatus, Double minPrice, Double maxPrice) {
        BaseResponse<Page<YdMerchantGiftResult>> result = BaseResponse.success(null, "00", "提交成功");
        try {
            YdMerchantGiftResult ydMerchantGiftResult = new YdMerchantGiftResult();
            ydMerchantGiftResult.setTitle(title);
            ydMerchantGiftResult.setMaxPrice(maxPrice);
            ydMerchantGiftResult.setMinPrice(minPrice);
            ydMerchantGiftResult.setGiftStatus(giftStatus);
            ydMerchantGiftResult.setMerchantId(getCurrMerchantId(request));
            Page<YdMerchantGiftResult> merchantGiftList = ydMerchantGiftService.findYdMerchantGiftListByPage(ydMerchantGiftResult,
                    getPageInfo(request, 1, 10));
            result.setResult(merchantGiftList);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户礼品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_detail", name="商户礼品详情")
    @ResponseBody
    @RequestMapping(value = "/getMerchantGiftDetail", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantGiftResult> getMerchantGiftDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdMerchantGiftResult> result = BaseResponse.success(null, "00", "提交成功");
        try {
            result.setResult(ydMerchantGiftService.getYdMerchantGiftById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品新增修改(json传值)", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_update", name="商户礼品新增修改")
    @ResponseBody
    @RequestMapping(value = "saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> addMerchantGiftCart(HttpServletRequest request, @RequestBody YdMerchantGiftResult merchantGiftResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            merchantGiftResult.setMerchantId(getCurrMerchantId(request));
            merchantGiftResult.setGiftType("merchant");
            if (merchantGiftResult.getId() == null || merchantGiftResult.getId() == 0) {
                ydMerchantGiftService.insertYdMerchantGift(merchantGiftResult);
            } else {
                ydMerchantGiftService.updateYdMerchantGift(merchantGiftResult);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品上下架", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户礼品id"),
            @ApiImplicitParam(paramType = "query", name = "isEnable", value = "Y(上架)N(上架)")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_upOrDown", name="商户礼品上下架")
    @ResponseBody
    @RequestMapping(value = "upOrDownYdMerchantGift", method = {RequestMethod.POST})
    public BaseResponse<String> upOrDownYdMerchantGift(HttpServletRequest request, Integer id, String isEnable) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            ydMerchantGiftService.upOrDownYdMerchantGift(getCurrMerchantId(request), id, isEnable);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户礼品id")
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_delete", name="商户礼品删除")
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> deleteYdGift(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydMerchantGiftService.deleteYdMerchantGift(id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "查询商户礼品账户数据", httpMethod = "POST")
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_data", name="商户礼品列表")
    @ResponseBody
    @RequestMapping(value = "/getMerchantGiftAccount", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantGiftAccountResult> findMerchantGiftAccount(HttpServletRequest request) {
        BaseResponse<YdMerchantGiftAccountResult> result = BaseResponse.success(null, "00", "查询成功");
        Integer merchantId = getCurrMerchantId(request);
        result.setResult(ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(merchantId));
        return result;
    }

    @ApiOperation(value = "店铺礼品导出", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品名称"),
            @ApiImplicitParam(paramType = "query", name = "minPrice", value = "礼品最低价"),
            @ApiImplicitParam(paramType = "query", name = "maxPrice", value = "礼品最高价"),
            @ApiImplicitParam(paramType = "query", name = "giftStatus", value = "礼品状态 (in_shelves | no_shelves | invalid)")

    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_data", name="商户礼品列表")
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    public void export(HttpServletRequest request, HttpServletResponse response, String title, String giftStatus, Double minPrice, Double maxPrice) {
        List<YdMerchantGiftExcel> excelList = new ArrayList<>();
        try {
            YdMerchantGiftResult ydMerchantGiftResult = new YdMerchantGiftResult();
            ydMerchantGiftResult.setTitle(title);
            ydMerchantGiftResult.setMaxPrice(maxPrice);
            ydMerchantGiftResult.setMinPrice(minPrice);
            ydMerchantGiftResult.setGiftStatus(giftStatus);
            ydMerchantGiftResult.setMerchantId(getCurrMerchantId(request));

            PagerInfo pagerInfo = new PagerInfo(Integer.MAX_VALUE, 1);
            Page<YdMerchantGiftResult> data = ydMerchantGiftService.findYdMerchantGiftListByPage(ydMerchantGiftResult,pagerInfo);
            excelList.addAll(DTOUtils.convertList(data.getData(), YdMerchantGiftExcel.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CollectionUtils.isNotEmpty(excelList)) {
            excelList.forEach(data -> {
                // in_shelves | no_shelves | invalid
                if (data.getGiftStatus().equalsIgnoreCase("in_shelves") ){
                    data.setGiftStatus("上架中");
                } else if (data.getGiftStatus().equalsIgnoreCase("no_shelves")) {
                    data.setGiftStatus("待上架");
                } else if(data.getGiftStatus().equalsIgnoreCase("in_shelves")) {
                    data.setGiftStatus("已失效");
                }
            });
        }

//        ExcelUtiles.exportExcel(excelList, "店铺礼品导出", "店铺礼品导出",
//                YdMerchantGiftExcel.class, "店铺礼品导出.xls", response);

        try {
            String fileName = "店铺礼品导出.xls";
            ExportParams params = new ExportParams("店铺礼品导出", "店铺礼品导出");

            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            Workbook workbook =  ExcelExportUtil.exportExcel(params, YdMerchantGiftExcel.class, excelList);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {

        }
    }


}
