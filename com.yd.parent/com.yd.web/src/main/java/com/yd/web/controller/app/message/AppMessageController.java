package com.yd.web.controller.app.message;

import com.yd.api.result.message.YdMerchantMessageResult;
import com.yd.api.service.message.YdMerchantMessageService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.Page;
import com.yd.web.anotation.AppMerchantCheck;
import com.yd.web.controller.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 商户消息通知接口
 * @author wuyc
 * created 2019/12/17 18:24
 **/
@RestController
@RequestMapping("/app/merchant/message")
public class AppMessageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AppMessageController.class);

    @Reference
    private  YdMerchantMessageService ydMerchantMessageService;

    @ApiOperation(value = "商户消息列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示个数", required = true)
    })
    @AppMerchantCheck
    @RequestMapping(value = "/findList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdMerchantMessageResult>> findList(HttpServletRequest request) {
        BaseResponse<Page<YdMerchantMessageResult>> result = BaseResponse.success(null, "00", "查询成功");
        YdMerchantMessageResult params = new YdMerchantMessageResult();
        params.setMerchantId(getCurrMerchantId(request));
        params.setStartTime(null);
        params.setEndTime(null);
        logger.info("查询消息返回结果=" + ydMerchantMessageService.findYdMerchantMessageListByPage(params, getPageInfo(request)));
        result.setResult(ydMerchantMessageService.findYdMerchantMessageListByPage(params, getPageInfo(request)));
        return result;
    }

    @ApiOperation(value = "商户消息已读", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "消息id", required = true)
    })
    @AppMerchantCheck
    // @AppMerchantCheck(groupCode = EnumSiteGroup.MERCHANT, alias="merchant_message_update",name = "商户消息已读")
    @RequestMapping(value = "/updateMessage", method = {RequestMethod.POST})
    public BaseResponse<String> updateMessage(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        ydMerchantMessageService.updateMessage(getCurrMerchantId(request), id);
        return result;
    }

    @ApiOperation(value = "商户消息全部已读", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "消息id", required = true)
    })
    @AppMerchantCheck
    @RequestMapping(value = "/updateAllMessage", method = {RequestMethod.POST})
    public BaseResponse<String> updateAllMessage(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "修改成功");
        ydMerchantMessageService.updateAllMessage(getCurrMerchantId(request));
        return result;
    }

}
