package com.yg.web.controller.front.order;

import com.alibaba.fastjson.JSON;
import com.yg.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyc
 * created 2019/12/2 17:14
 **/
@RestController
@RequestMapping("/front/order")
public class FrontOrderController extends FrontBaseController {

    private static final Logger logger = LoggerFactory.getLogger(FrontOrderController.class);

    // @Reference

    @ApiOperation(value = "跳转支付模板页面", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id")
    })
    @RequestMapping("/jumpPayIndex")
    public ModelAndView authLogin(HttpServletRequest request, Integer orderId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("front/payIndex.html");
        modelAndView.addObject("orderId", orderId);
        return modelAndView;
    }


}
