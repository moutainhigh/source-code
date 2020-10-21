package com.yd.web.controller.front.order;

import com.alibaba.fastjson.JSON;
import com.yd.api.req.SubmitOrderReq;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdUserOrderResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.message.YdMerchantMessageService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PropertiesHelp;
import com.yd.core.utils.WechatUtil;
import com.yd.web.anotation.FrontCheck;
import com.yd.web.controller.BaseController;
import com.yd.web.controller.FrontBaseController;
import com.yd.web.util.ControllerUtil;
import com.yd.web.util.QrCodeUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdGiftOrderService ydGiftOrderService;

    @Reference
    private YdUserOrderService ydUserOrderService;

    @Reference
    private YdMerchantMessageService ydMerchantMessageService;

    @ApiOperation(value = "跳转支付模板页面", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "type = 1, 走订单支付； type=register，走商户注册支付"),
            @ApiImplicitParam(paramType = "query", name = "memberLevel", value = "会员等级"),
            @ApiImplicitParam(paramType = "query", name = "memberPayId", value = "开通会员支付的id")
    })
    @RequestMapping("/jumpPayIndex")
    public ModelAndView authLogin(HttpServletRequest request, HttpServletResponse response, String type, Integer orderId,
                                  Integer memberPayId, Integer memberLevel) {
        ModelAndView modelAndView = new ModelAndView();
        String sendURL = request.getParameter("sendURL");
        logger.info("====进入支付页面type=" + type + ",sendURL=" + sendURL);
        if (StringUtils.isNotEmpty(sendURL) || ("register".equalsIgnoreCase(type))){
            // 判断是否有openId, 没有openId授权获取openId
            String openId = ControllerUtil.getCookieValue(request, ControllerUtil.KQ_OPEN_ID);
            if (StringUtils.isNotEmpty(openId)) {
                modelAndView.setViewName("front/payIndex.html");
                return modelAndView;
            } else {
                modelAndView = getUserOpenId(request, response, modelAndView, type, memberPayId, memberLevel);
                return modelAndView;
            }
        } else {
            modelAndView.addObject("orderId", orderId);
            modelAndView.setViewName("front/payIndex.html");
            return modelAndView;
        }
    }

    private ModelAndView getUserOpenId(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, String type, Integer memberPayId, Integer memberLevel) {
        String sendURL = request.getParameter("sendURL");
        if (StringUtils.isEmpty(sendURL)) {
            sendURL = "/front/order/jumpPayIndex?type=" + type + "&memberPayId=" + memberPayId + "&memberLevel=" + memberLevel;
        }

        String code = request.getParameter("code");
        logger.info("====商户注册获取openId的senUrl====" + sendURL + ",code=" + code);

        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String appId = weixinAccountResult.getAppId();
        String appSecret = weixinAccountResult.getAppSecret();

        String openId = null;
        if (StringUtils.isNotEmpty(code)) {
            Map<String, String> accessTokenMap = WechatUtil.getAccessToken(appId, appSecret, code);
            logger.info("accessTokenMap===" + JSON.toJSONString(accessTokenMap));
            openId = accessTokenMap.get("openId");
            ControllerUtil.addCookie(response, ControllerUtil.KQ_OPEN_ID,  openId, 1 * 12 * 60 * 60);
        } else {
            String autoLoginUrl = PropertiesHelp.getProperty("webDomain") + "/front/order/jumpPayIndex";
            String redirectUrl = WechatUtil.getWeixinOpenIdUrl(autoLoginUrl, sendURL, appId);
            modelAndView.setViewName("redirect:" + redirectUrl);
            return modelAndView;
        }
        if (sendURL.contains("?")) {
            sendURL += "&openId=" + openId;
        } else {
            sendURL += "?openId=" + openId;
        }
        modelAndView.setViewName("redirect:" + sendURL);
        logger.info("======getOpenId的sendURL=" + sendURL);
        return modelAndView;
    }

    @ApiOperation(value = "提交订单", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/submitOrder", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Object>> submitOrder(HttpServletRequest request, @RequestBody SubmitOrderReq req) {
        BaseResponse<Map<String, Object>> result = BaseResponse.success(null, "00", "提交成功");
        try {
            logger.info("========前台提交订单的入参=" + JSON.toJSONString(req));
            Integer userId = getCurrUserId(request);
            Integer merchantId = req.getMerchantId();
            Map<String, Object> resultData = ydUserOrderService.submitOrder(userId, merchantId, req.getCouponId(), req.getAddressId(),
                    req.getSkuId(),req.getNum(), req.getIsCheckIntegralReduce(), req.getIsCheckOldMachineReduce(),
                    req.getType(), req.getReceivingWay(), req.getCarIds(), req.getGiftList());

            // 生成更新用户订单二维码
            String imageUrl = updateUserOrderQrcode(userId, merchantId, Integer.parseInt(resultData.get("id").toString()));
            try {
                ydMerchantMessageService.insertOrderMessage("user_submit_order", Integer.parseInt(resultData.get("id").toString()), merchantId);
            } catch (Exception e) {
                logger.error("用户提交订单发送消息失败, 失败原因:" + e.getStackTrace());
            }
            resultData.put("qrCodeUrl", imageUrl);
            result.setResult(resultData);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询用户订单数量", httpMethod = "POST")
    @FrontCheck
    @RequestMapping(value = "/getOrderNum", method = {RequestMethod.POST})
    public BaseResponse<Map<String, Integer>> getOrderNum(HttpServletRequest request) {
        BaseResponse<Map<String, Integer>> result = BaseResponse.success(null, "00", "提交成功");
        try {
            Integer userId = getCurrUserId(request);
            Integer merchantId = getCurrMerchantId(request);
            Map<String, Integer> hashMap = new HashMap<>();
            hashMap.put("WAIT_PAY", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_PAY.getCode()));
            hashMap.put("WAIT_DELIVER", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_DELIVER.getCode()));
            hashMap.put("WAIT_GOODS", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_GOODS.getCode()));
            hashMap.put("WAIT_HANDLE", ydUserOrderService.getUserOrderNumByOrderStatus(userId, merchantId, YdUserOrderStatusEnum.WAIT_HANDLE.getCode()));
            result.setResult(hashMap);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询用户订单列表", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderStatus", value = "不传为全部订单, WAIT_PAY(待付款), CANCEL(订单取消), WAIT_DELIVER(待发货), WAIT_HANDLE(待处理), WAIT_GOODS(待收货), SUCCESS(已完成)")
    })
    @FrontCheck
    @RequestMapping(value = "/findOrderList", method = {RequestMethod.POST})
    public BaseResponse<Page<YdUserOrderResult>> findOrderList(HttpServletRequest request, String orderStatus) {
        BaseResponse<Page<YdUserOrderResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdUserOrderResult params = new YdUserOrderResult();
            params.setOrderStatus(orderStatus);
            params.setUserId(getCurrUserId(request));
            params.setMerchantId(getCurrMerchantId(request));
            result.setResult(ydUserOrderService.findFrontOrderListByPage(params, getPageInfo(request)));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据订单id修改收货地址", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id"),
            @ApiImplicitParam(paramType = "query", name = "addressId", value = "用户地址id"),
    })
    @FrontCheck
    @RequestMapping(value = "/updateReceiveAddress", method = {RequestMethod.POST})
    public BaseResponse<String> updateReceiveAddress(HttpServletRequest request, Integer id, Integer addressId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "查询成功");
        try {
            ydUserOrderService.updateReceiveAddress(getCurrUserId(request), id, addressId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据订单id查询订单详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @FrontCheck
    @RequestMapping(value = "/getOrderDetail", method = {RequestMethod.POST})
    public BaseResponse<YdUserOrderResult> getOrderDetail(HttpServletRequest request, Integer id) {
        BaseResponse<YdUserOrderResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydUserOrderService.getFrontOrderDetail(getCurrUserId(request), id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "根据订单id查询礼品详情", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @FrontCheck
    @RequestMapping(value = "/getGiftOrderDetail", method = {RequestMethod.POST})
    public BaseResponse<List<YdGiftOrderDetailResult>> findOrderList(HttpServletRequest request, Integer id) {
        BaseResponse<List<YdGiftOrderDetailResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            result.setResult(ydGiftOrderService.getFrontGiftOrderDetailAndGroupByExpress(getCurrUserId(request), id));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户取消待付款订单", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @FrontCheck
    @RequestMapping(value = "/cancelOrder", method = {RequestMethod.POST})
    public BaseResponse<String> cancelOrder(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydUserOrderService.cancelOrder(getCurrUserId(request), id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户确认收货商品订单", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "id", value = "订单id")
    })
    @FrontCheck
    @RequestMapping(value = "/confirmOrder", method = {RequestMethod.POST})
    public BaseResponse<String> confirmOrder(HttpServletRequest request, Integer id) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydUserOrderService.userConfirmOrder(getCurrUserId(request), id);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户确认收货礼品订单", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "expressOrderId", value = "快递单号")
    })
    @FrontCheck
    @RequestMapping(value = "/confirmGiftOrder", method = {RequestMethod.POST})
    public BaseResponse<String> confirmGiftOrder(HttpServletRequest request, String expressOrderId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "操作成功");
        try {
            ydUserOrderService.userConfirmGiftOrder(getCurrUserId(request), expressOrderId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户唤起微信支付", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单id")
    })
    @RequestMapping(value = "/toPay", method = {RequestMethod.POST})
    public BaseResponse<Map<String, String>> toPay(HttpServletRequest request, Integer orderId) {
        BaseResponse<Map<String, String>> result = BaseResponse.success(null, "00", "操作成功");
        try {
            String domain = PropertiesHelp.getProperty("webDomain");
            result.setResult(ydUserOrderService.toPay(getCurrUserId(request), orderId, domain));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    /**
     * 订单生成二维码，并且更新
     * @param userId
     * @param merchantId
     * @param orderId
     */
    private String updateUserOrderQrcode(Integer userId, Integer merchantId, Integer orderId) {
        String content = userId + "," + merchantId + "," + orderId;
        try {
            String imageUrl = QrCodeUtil.makeQrCode(content, 200, 200);
            ydUserOrderService.updateYdUserOrderQrCode(userId, merchantId, orderId, imageUrl);
            return imageUrl;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

}
