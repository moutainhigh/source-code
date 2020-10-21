package com.yd.web.controller.admin.market;

/**
 * 门店礼品营销
 * @author wuyc
 * created 2019/11/06 10:33
 **/

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.market.YdMerchantGiftRateResult;
import com.yd.api.service.market.YdMerchantGiftRateService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
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
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_market",name = "营销管理")
@RequestMapping("/admin/merchant/gift/market")
public class AdminMerchantGiftMarketController extends BaseController {

    @Reference
    private YdMerchantGiftRateService ydMerchantGiftRateService;

    @ApiOperation(value = "商户礼品营销列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "firstCategoryId", value = "一级分类id", required = false),
            @ApiImplicitParam(paramType = "query", name = "secondCategoryId", value = "二级分类id", required = false)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_market_data",name = "商户礼品营销列表")
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantGiftRateResult>> findList(HttpServletRequest request, Integer firstCategoryId,
                                                                 Integer secondCategoryId) {
        BaseResponse<List<YdMerchantGiftRateResult>> result = BaseResponse.success(null, "00", "查询成功");

        YdMerchantGiftRateResult params = new YdMerchantGiftRateResult();
        params.setMerchantId(getCurrMerchantId(request));
        params.setFirstCategoryId(firstCategoryId);
        params.setSecondCategoryId(secondCategoryId);
        result.setResult(ydMerchantGiftRateService.getAll(params));
        return result;
    }

    @ApiOperation(value = "商户礼品营销修改比率", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "type", value = "store | first | second | item ", required = true),
            @ApiImplicitParam(paramType = "query", name = "firstCategoryId", value = "一级分类id", required = false),
            @ApiImplicitParam(paramType = "query", name = "secondCategoryId", value = "二级分类id", required = false),
            @ApiImplicitParam(paramType = "query", name = "merchantItemId", value = "门店商品id", required = false),
            @ApiImplicitParam(paramType = "query", name = "rate", value = "礼品占比", required = true)
    })
    @MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_gift_market_update_rate",name = "商户礼品营销修改比率")
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public BaseResponse<String> update(HttpServletRequest request, Integer firstCategoryId, Integer secondCategoryId, Integer merchantItemId,
                                       @NotEmpty(message = "类型不可以为空") String type, @NotEmpty(message = "礼品占比不能为空") Integer rate) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");

        try {
            YdMerchantGiftRateResult params = new YdMerchantGiftRateResult();
            params.setMerchantId(getCurrMerchantId(request));
            params.setFirstCategoryId(firstCategoryId);
            params.setSecondCategoryId(secondCategoryId);
            ydMerchantGiftRateService.updateGiftRate(type, getCurrMerchantId(request), rate, firstCategoryId, secondCategoryId, merchantItemId);

        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
         return result;
    }

}
