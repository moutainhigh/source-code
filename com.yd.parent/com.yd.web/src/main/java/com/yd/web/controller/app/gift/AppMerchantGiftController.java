package com.yd.web.controller.app.gift;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.gift.YdGiftCategoryResult;
import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.gift.YdMerchantGiftCartResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.service.gift.YdGiftCategoryService;
import com.yd.api.service.gift.YdGiftService;
import com.yd.api.service.gift.YdMerchantGiftCartService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商户礼品接口
 * @author wuyc
 * created 2019/10/16 18:11
 **/
@RestController
@RequestMapping("/app/merchant/gift")
@AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mode_merchant_gift", name = "商户礼品管理")
public class AppMerchantGiftController extends BaseController {

    @Reference
    private YdGiftService ydGiftService;

    @Reference
    private YdMerchantGiftService ydMerchantGiftService;

    @Reference
    private YdGiftCategoryService ydGiftCategoryService;

    @Reference
    private YdMerchantGiftCartService ydMerchantGiftCartService;

    @ApiOperation(value = "商户礼品商城数据", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "分类Id"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "礼品标题"),
            @ApiImplicitParam(paramType = "query", name = "minPrice", value = "最小价钱"),
            @ApiImplicitParam(paramType = "query", name = "maxPrice", value = "最大价钱")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_platform_data", name="商户礼品商城数据")
    @RequestMapping(value = "/platform/getData", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> getPlatformData(HttpServletRequest request, Integer categoryId,
                                                             Double minPrice, Double maxPrice, String title) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        Map<String, Object> resultData = new HashMap<>(4);
        try {
            // 设置分类
            Page<YdGiftCategoryResult> categoryData = ydGiftCategoryService.findCategoryListByPage(
                    null, getPageInfo(request, 1, Integer.MAX_VALUE));
            resultData.put("categoryList", categoryData.getData());

            // 查询设置礼品
            YdGiftResult ydGiftResult = new YdGiftResult();
            ydGiftResult.setTitle(title);
            ydGiftResult.setCategoryId(categoryId);
            ydGiftResult.setMinPrice(minPrice);
            ydGiftResult.setMaxPrice(maxPrice);
            ydGiftResult.setIsFlag("N");
            ydGiftResult.setIsEnable("Y");
            Page<YdGiftResult> giftListByPage = ydGiftService.findYdGiftListByPage(ydGiftResult,
                    getPageInfo(request, 1, Integer.MAX_VALUE));
            resultData.put("giftList", giftListByPage);
            result.setResult(resultData);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城礼品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "商城礼品id")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_platform_data", name="商户礼品商城数据")
    @RequestMapping(value = "/platform/getGiftDetail", method = {RequestMethod.POST})
    public BaseResponse<YdGiftResult> getPlatformData(HttpServletRequest request, Integer id) {
        BaseResponse<YdGiftResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydGiftService.getYdGiftById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品商城页面购物车商品数量", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_num", name="商户礼品商城页面购物车商品数量")
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_check_total_price", name="商户礼品商城购物车选中后计算总价钱")
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_data", name="商户礼品商城页面添加礼品到购物车")
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

    @ApiOperation(value = "商户礼品添加礼品到购物车", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "num", value = "礼品数量"),
            @ApiImplicitParam(paramType = "query", name = "giftId", value = "礼品Id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "shop(页面加入购物车) cart(购物车继续添加修改)")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_add", name="商户礼品商城页面添加礼品到购物车")
    @RequestMapping(value = "/cart/addMerchantGiftCart", method = {RequestMethod.POST})
    public BaseResponse<String> addMerchantGiftCart(HttpServletRequest request, String type, Integer num, Integer giftId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_delete", name="商户礼品购物车删除")
    @RequestMapping(value = "/cart/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, String carIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            ydMerchantGiftCartService.deleteMerchantGiftCart(getCurrMerchantId(request), carIds);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户礼品清空购物车", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_clear", name="商户礼品商城页面清空购物车")
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
    @AppMerchantCheck(alias="merchant_gift_cart_export", name="商户礼品购物车选中礼品导入")
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_submit", name="商户礼品购物车提交")
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_cart_to_pay", name="商户礼品购物车支付")
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
            @ApiImplicitParam(paramType = "query", name = "maxPrice", value = "礼品最高价")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_data", name="商户礼品列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantGiftResult>> findMerchantGiftList(HttpServletRequest request, String title,
                                                                         Double minPrice, Double maxPrice) {
        BaseResponse<Page<YdMerchantGiftResult>> result = BaseResponse.success(null, "00", "提交成功");
        try {
            YdMerchantGiftResult ydMerchantGiftResult = new YdMerchantGiftResult();
            ydMerchantGiftResult.setTitle(title);
            ydMerchantGiftResult.setMaxPrice(maxPrice);
            ydMerchantGiftResult.setMinPrice(minPrice);
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_detail", name="商户礼品详情")
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

    @ApiOperation(value = "商户礼品新增修改", httpMethod = "POST")
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_update", name="商户礼品新增修改")
    @RequestMapping(value = "saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> addMerchantGiftCart(HttpServletRequest request,YdMerchantGiftResult merchantGiftResult) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            merchantGiftResult.setMerchantId(getCurrMerchantId(request));
            merchantGiftResult.setGiftType("merchant");
            if (merchantGiftResult.getId() == null) {
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
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_update", name="商户礼品上下架")
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

    @ApiOperation(value = "商户礼品库删除", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "商户礼品id")
    })
    @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="gift_delete", name="平台礼品库删除")
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

}
