package com.yd.web.controller.front.sys;

import com.yd.api.result.sys.SysRegionResult;
import com.yd.api.service.sys.SysRegionService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 用户收货地址
 * @author wuyc
 * created 2019/11/28 13:45
 **/
@RestController
@RequestMapping("/front/region")
public class FrontRegionController extends FrontBaseController {

    @Reference
    private SysRegionService sysRegionService;

    @ApiOperation(value = "查询省市区列表", httpMethod = "POST")
    @RequestMapping(value = "/getRegionList", method = {RequestMethod.POST})
    public BaseResponse<List<SysRegionResult>> getRegionList() {
        BaseResponse<List<SysRegionResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(sysRegionService.getChildList());
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
