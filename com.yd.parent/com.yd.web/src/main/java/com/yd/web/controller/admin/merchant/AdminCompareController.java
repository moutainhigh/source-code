package com.yd.web.controller.admin.merchant;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.service.merchant.YdMerchantService;
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

/**
 * @author wuyc
 * created 2019/12/10 9:58
 **/
@RestController
@MerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="mod_merchant_store",name = "店铺管理")
@RequestMapping("/admin/merchant")
public class AdminCompareController extends BaseController {

    @Reference
    private YdMerchantService ydMerchantService;

    @ApiOperation(value = "商户设置比价功能", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "type", value = "Y(开通) | N(关闭)")
    })
    @MerchantCheck(alias="merchant_set_compare", name="商户设置比价功能")
    @RequestMapping(value = "/compare/setComparePrice", method = {RequestMethod.POST})
    public BaseResponse<String> openPay(HttpServletRequest request, String type) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.setComparePrice(getCurrMerchantId(request), type);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "商户开启关闭旧机抵扣", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "type", value = "Y(开通) | N(关闭)")
    })
    @MerchantCheck(alias="merchant_set_old_machine_reduce", name="商户开启关闭旧机抵扣")
    @RequestMapping(value = "/oldMachine/isCanUse", method = {RequestMethod.POST})
    public BaseResponse<String> setOldMachineReduce(HttpServletRequest request, String type) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydMerchantService.setOldMachineReduce(getCurrMerchantId(request), type);
        } catch (BusinessException e) {
            result = BaseResponse.fail(e.getCode(), e.getMessage());
        }
        return result;
    }

}
