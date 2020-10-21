package com.yd.web.controller.front.user;

import com.yd.api.result.user.YdUserAddressResult;
import com.yd.api.service.user.YdUserAddressService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.web.anotation.FrontCheck;
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
 * 用户收货地址
 * @author wuyc
 * created 2019/11/28 13:45
 **/
@RestController
@RequestMapping("/front/user/address")
public class FrontUserAddressController extends FrontBaseController {

    @Reference
    private YdUserAddressService ydUserAddressService;

    @ApiOperation(value = "查询用户收货地址列表", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.POST})
    public BaseResponse<List<YdUserAddressResult>> getUserInfo(HttpServletRequest request) {
        BaseResponse<List<YdUserAddressResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            Integer userId = getCurrUserId(request);
            YdUserAddressResult params = new YdUserAddressResult();
            params.setUserId(userId);
            result.setResult(ydUserAddressService.getAll(params));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "新增修改收货地址", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    public BaseResponse<String> saveOrUpdate(HttpServletRequest request, YdUserAddressResult params) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer userId = getCurrUserId(request);
            params.setUserId(userId);
            if (params.getId() == null || params.getId() == 0) {
                ydUserAddressService.insertYdUserAddress(params);
            } else {
                ydUserAddressService.updateYdUserAddress(params);
            }
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "删除收货地址收货地址", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "addressId", value = "收货地址id")
    })
    @FrontCheck
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public BaseResponse<String> delete(HttpServletRequest request, Integer addressId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            Integer userId = getCurrUserId(request);
            ydUserAddressService.deleteYdUserAddress(userId, addressId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

}
