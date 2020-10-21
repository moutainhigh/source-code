package com.yd.web.controller;

import com.alibaba.fastjson.JSON;
import com.yd.api.pay.util.MessageUtil;
import com.yd.api.pay.util.PayStatus;
import com.yd.api.pay.util.WeixinSignHelper;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.member.YdMerchantMemberPayRecordService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdMerchantRechargeOrderService;
import com.yd.api.service.order.YdUserOrderService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DateUtil;
import com.yd.core.utils.PropertiesHelp;
import com.yd.web.util.QrCodeUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 优度消息通知
 * @author wuyc
 * created 2019/12/25 11:15
 **/
@Controller
@RequestMapping("/yd/callback")
public class YdCallbackNotifyController extends FrontBaseController {

    private static final Logger logger = LoggerFactory.getLogger(YdCallbackNotifyController.class);

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdUserOrderService ydUserOrderService;

    @Reference
    private YdMerchantRechargeOrderService ydMerchantRechargeOrderService;

    @Reference
    private YdMerchantMemberPayRecordService ydMerchantMemberPayRecordService;

    /**
     * 微信支付成功通知-订单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixin/orderPay/notify", method = {RequestMethod.GET, RequestMethod.POST})
    public void orderPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("====进入优度订单支付微信异步回调方法");

        Map<String, String> resultMap = MessageUtil.parseXml(request);
        String billNo = resultMap.get("transaction_id");
        logger.info("微信回调日期:" +  DateUtil.date(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")  + "，billNo="+ billNo);
        logger.info("微信回调所有参数===" + JSON.toJSONString(resultMap));

        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String mySign = WeixinSignHelper.createSign("UTF-8", new TreeMap<>(resultMap), weixinAccountResult.getSignKey());
        if (!mySign.equals(resultMap.get("sign"))) {
            logger.error("======优度订单支付微信回调签名错误");
            return;
        }

        if (!"SUCCESS".equals(resultMap.get("return_code")) || !"SUCCESS".equals(resultMap.get("result_code"))) {
            logger.error("======优度订单支付微信回调支付失败,return_code=" + resultMap.get("return_code"));
            return;
        }

        try {
            // 业务代码，修改订单支付状态
            String outOrderId = resultMap.get("out_trade_no");
            ydUserOrderService.updateUserOrderPayStatus(outOrderId, billNo);

            PayStatus payStatus = new PayStatus("SUCCESS", "OK");
            String xml = MessageUtil.payStatusToXml(payStatus);
            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            out.print(xml);
            out.flush();
            out.close();
            return;
        } catch (BusinessException e) {
            logger.error("======优度订单支付微信回调报错: " + e.getCode() + e.getMessage());
        }
    }

    /**
     * 微信支付成功通知-
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixin/rechargePay/notify", method = {RequestMethod.GET, RequestMethod.POST})
    public void rechargePayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("====进入优度充值微信异步回调方法");
        Map<String, String> resultMap = MessageUtil.parseXml(request);
        String billNo = resultMap.get("transaction_id");
        logger.info("====优度充值微信回调所有参数===" + JSON.toJSONString(resultMap));
        logger.info("====优度充值微信回调日期:" +  DateUtil.date(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")  + "，billNo="+ billNo);

        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String mySign = WeixinSignHelper.createSign("UTF-8", new TreeMap<>(resultMap), weixinAccountResult.getSignKey());
        if (!mySign.equals(resultMap.get("sign"))) {
            logger.error("======优度充值微信回调签名错误");
            return;
        }

        if (!"SUCCESS".equals(resultMap.get("return_code")) || !"SUCCESS".equals(resultMap.get("result_code"))) {
            logger.error("======优度充值微信回调支付失败,return_code=" + resultMap.get("return_code"));
            return;
        }

        try {
            // 业务代码，修改充值状态
            String outOrderId = resultMap.get("out_trade_no");
            ydMerchantRechargeOrderService.updateRechargeStatus(outOrderId, billNo);
            PayStatus payStatus = new PayStatus("SUCCESS", "OK");
            String xml = MessageUtil.payStatusToXml(payStatus);
            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            out.print(xml);
            out.flush();
            out.close();
            return;
        } catch (BusinessException e) {
            logger.error("======优度充值微信回调报错: " + e.getCode() + e.getMessage());
        }
    }

    /**
     * 微信扫一扫支付成功通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixin/qrCode/notify", method = {RequestMethod.GET, RequestMethod.POST})
    public void qrCodePayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("====进入优度扫一扫支付微信异步回调方法");
        Map<String, String> resultMap = MessageUtil.parseXml(request);
        String billNo = resultMap.get("transaction_id");
        logger.info("====优度扫一扫支付微信回调所有参数===" + JSON.toJSONString(resultMap));
        logger.info("====优度扫一扫支付微信回调日期:" +  DateUtil.date(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")  + "，billNo="+ billNo);

        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String mySign = WeixinSignHelper.createSign("UTF-8", new TreeMap<>(resultMap), weixinAccountResult.getSignKey());
        if (!mySign.equals(resultMap.get("sign"))) {
            logger.error("======优度扫一扫支付微信回调签名错误");
            return;
        }

        if (!"SUCCESS".equals(resultMap.get("return_code")) || !"SUCCESS".equals(resultMap.get("result_code"))) {
            logger.error("======优度扫一扫支付微信回调支付失败,return_code=" + resultMap.get("return_code"));
            return;
        }

        try {
            // 业务代码，修改充值状态
            String outOrderId = resultMap.get("out_trade_no");
            ydMerchantRechargeOrderService.updateQrCodeStatus(outOrderId, billNo);
            PayStatus payStatus = new PayStatus("SUCCESS", "OK");
            String xml = MessageUtil.payStatusToXml(payStatus);
            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            out.print(xml);
            out.flush();
            out.close();
            return;
        } catch (BusinessException e) {
            logger.error("======优度扫一扫支付微信回调报错: " + e.getCode() + e.getMessage());
        }
    }

    /**
     * 商户开通会员支付成功
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/weixin/merchantMemberPay/notify", method = {RequestMethod.GET, RequestMethod.POST})
    public void memberPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("====进入优度会员支付微信异步回调方法");

        Map<String, String> resultMap = MessageUtil.parseXml(request);
        String billNo = resultMap.get("transaction_id");
        logger.info("====会员支付微信回调日期:" +  DateUtil.date(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")  + "，billNo="+ billNo);
        logger.info("====会员支付微信回调所有参数===" + JSON.toJSONString(resultMap));

        WbWeixinAccountResult weixinAccountResult = weixinService.getByWeixinAccountByType("91kuaiqiang");
        String mySign = WeixinSignHelper.createSign("UTF-8", new TreeMap<>(resultMap), weixinAccountResult.getSignKey());
        if (!mySign.equals(resultMap.get("sign"))) {
            logger.error("======优度订单支付微信回调签名错误");
            return;
        }

        if (!"SUCCESS".equals(resultMap.get("return_code")) || !"SUCCESS".equals(resultMap.get("result_code"))) {
            logger.error("======优度会员支付微信回调支付失败,return_code=" + resultMap.get("return_code"));
            return;
        }

        try {
            // 业务代码，修改订单支付状态
            String outOrderId = resultMap.get("out_trade_no");
            Integer merchantId = ydMerchantMemberPayRecordService.paySuccessNotify(outOrderId, billNo);

            PayStatus payStatus = new PayStatus("SUCCESS", "OK");
            String xml = MessageUtil.payStatusToXml(payStatus);
            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            out.print(xml);
            out.flush();
            out.close();
            return;
        } catch (BusinessException e) {
            logger.error("======优度会员支付微信回调报错: " + e.getCode() + e.getMessage());
        }
    }

}
