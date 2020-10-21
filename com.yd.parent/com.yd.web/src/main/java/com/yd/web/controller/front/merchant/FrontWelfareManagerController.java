package com.yd.web.controller.front.merchant;

import com.yd.api.result.market.YdWelfareManagerResult;
import com.yd.api.service.market.YdWelfareManagerService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wuyc
 * created 2020/5/22 16:33
 **/
@RestController
@RequestMapping("/front/platform/welfare/manager")
public class FrontWelfareManagerController extends FrontBaseController {

    @Reference
    private YdWelfareManagerService ydWelfareManagerService;

    @ApiOperation(value = "平台福利列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "merchantId")
    })
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<List<YdWelfareManagerResult>> findList(HttpServletRequest request, Integer merchantId) {
        BaseResponse<List<YdWelfareManagerResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydWelfareManagerService.getAll(new YdWelfareManagerResult()));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "平台福利详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "id")
    })
    @RequestMapping(value = "/getDetail", method = {RequestMethod.POST})
    public BaseResponse<YdWelfareManagerResult> getDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdWelfareManagerResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydWelfareManagerService.getYdWelfareManagerById(id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
