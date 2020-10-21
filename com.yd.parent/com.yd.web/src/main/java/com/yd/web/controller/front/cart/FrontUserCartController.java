package com.yd.web.controller.front.cart;

import com.yd.api.req.SettlementDetailReq;
import com.yd.api.result.cart.SettlementDetailResult;
import com.yd.api.result.cart.YdUserCartDetailResult;
import com.yd.api.service.cart.YdUserCartService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户购物车
 * @author wuyc
 * created 2019/11/8 10:19
 **/
@RestController
@FrontCheck
@RequestMapping("/front/user/cart")
public class FrontUserCartController extends FrontBaseController {

    @Reference
    private YdUserCartService ydUserCartService;

    @ApiOperation(value = "添加商品到购物车", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "num", value = "商品sku数量"),
            @ApiImplicitParam(paramType = "query", name = "skuId", value = "skuId"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "shop(页面加入购物车) cart(购物车继续添加修改)")
    })
    @FrontCheck
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public BaseResponse<String> addMerchantGiftCart(HttpServletRequest request, String type, Integer num, Integer skuId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "添加成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            ydUserCartService.addCart(userId, merchantId, skuId, num, type);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "购物车商品更换规格", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "cartId", value = "购物车id"),
            @ApiImplicitParam(paramType = "query", name = "num", value = "商品sku数量"),
            @ApiImplicitParam(paramType = "query", name = "skuId", value = "skuId")
    })
    @FrontCheck
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public BaseResponse<String> addMerchantGiftCart(HttpServletRequest request, Integer cartId, Integer num, Integer skuId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "编辑成功成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            ydUserCartService.updateCart(userId, merchantId, skuId, num, cartId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "删除购物车商品", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @FrontCheck
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, String carIds) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "删除成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ",")).map(Integer::valueOf).collect(Collectors.toList());
            ydUserCartService.deleteCart(userId, merchantId, carIdList);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "清空购物车", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/clear", method = {RequestMethod.POST})
    public BaseResponse<String> clearMerchantGiftCart(HttpServletRequest request) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            ydUserCartService.clearCart(userId, merchantId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "购物车列表", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/findCartList", method = {RequestMethod.POST})
    public BaseResponse<Map<String, List<YdUserCartDetailResult>>> findCartList(HttpServletRequest request) {
        BaseResponse<Map<String, List<YdUserCartDetailResult>>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            result.setResult(ydUserCartService.findCartList(userId, merchantId));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "购物车选中计算优惠券明细", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "carIds", value = "购物车ids")
    })
    @FrontCheck
    @RequestMapping(value = "/calcCartCheckedMonty", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> calcCartCheckedMonty(HttpServletRequest request, String carIds) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
                    .map(Integer::valueOf).collect(Collectors.toList());
            result.setResult(ydUserCartService.calcCartCheckedMonty(userId, merchantId, carIdList));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "订单结算详情页面数据", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/settlementDetail", method = {RequestMethod.POST})
    public BaseResponse<SettlementDetailResult> settlementDetail(HttpServletRequest request, @RequestBody SettlementDetailReq req) {
        BaseResponse<SettlementDetailResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = req.getMerchantId();
            result.setResult(ydUserCartService.settlementDetail(userId, merchantId, req.getSkuId(),
                    req.getNum(), req.getType(), req.getCarIds(), req.getGiftList()));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
