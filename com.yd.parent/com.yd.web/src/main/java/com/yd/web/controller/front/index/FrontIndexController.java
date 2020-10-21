package com.yd.web.controller.front.index;

import com.yd.api.result.decoration.YdMerchantBannerResult;
import com.yd.api.result.decoration.YdMerchantBrandResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdMerchantBannerService;
import com.yd.api.service.decoration.YdMerchantBrandService;
import com.yd.api.service.decoration.YdMerchantHotItemService;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wuyc
 * created 2019/11/7 19:55
 **/
@RestController
@RequestMapping("/front/banner")
public class FrontIndexController extends FrontBaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdMerchantItemService ydMerchantItemService;

    @Reference
    private YdMerchantBannerService ydMerchantBannerService;

    @Reference
    private YdMerchantBrandService ydMerchantBrandService;

    @Reference
    private YdMerchantHotItemService ydMerchantHotItemService;

    @ApiOperation(value = "查询首页banner列表", httpMethod = "GET")
    @RequestMapping(value = "/findBannerList", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse<List<YdMerchantBannerResult>> findBannerList(HttpServletRequest request) {
        BaseResponse<List<YdMerchantBannerResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantBannerResult ydMerchantBannerResult = new YdMerchantBannerResult();
            ydMerchantBannerResult.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydMerchantBannerService.getAll(ydMerchantBannerResult));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户首页品牌列表", httpMethod = "POST")
    @RequestMapping(value = "/findBrandList", method = {RequestMethod.POST})
    public BaseResponse<List<YdMerchantBrandResult>> findBrandList(HttpServletRequest request) {
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

    @ApiOperation(value = "查询商户名称和商品数量", httpMethod = "POST")
    @RequestMapping(value = "/findItemCount", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> findItemCount(HttpServletRequest request) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Map<String, String> resultMap = new HashMap<>();
            YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(getCurrMerchantId(request));
            YdMerchantItemResult ydMerchantItemResult = new YdMerchantItemResult();
            ydMerchantItemResult.setIsEnable("Y");
            ydMerchantItemResult.setMerchantId(storeInfo.getId());
            List<YdMerchantItemResult> itemList = ydMerchantItemService.getAll(ydMerchantItemResult);
            if (CollectionUtils.isEmpty(itemList)) {
                resultMap.put("itemCount", "0");
            } else {
                resultMap.put("itemCount", itemList.size() + "");
            }
            resultMap.put("merchantName", storeInfo.getMerchantName());
            result.setResult(resultMap);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }
}
