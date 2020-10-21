package com.yd.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.result.BijiaColumn;
import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.api.result.ActIqiyiResult;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.service.ActIqiyiService;
import com.yd.api.service.item.YdItemService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.RequestUtils;
import com.yd.core.utils.SmsHelper;
import com.yd.web.util.AiQiYiUtil;
import com.yd.web.util.WebUtil;

@Controller
public class BijiaTestController {
    private static final Logger LOG = LoggerFactory.getLogger(BijiaTestController.class);

    private static final String orderPrev = "iqiyi2Act";

    @Reference
    private YdItemService ydItemService;
    @Reference
    private CrawerService crawerService;
    @Reference
    private ActIqiyiService actIqiyiService;

    @ResponseBody
    @RequestMapping("/api/iqiyiAct/send")
    public BaseResponse<List<BijiaColumn>> iqiyiSend(HttpServletRequest request, HttpServletResponse response) {
        int limit = Integer.parseInt(WebUtil.getParameter(request, "limit", "3"));
        List<ActIqiyiResult> list = actIqiyiService.getWaitList(limit);
        LOG.info("=================/api/iqiyiAct/send==========");
        if (list != null && list.size() > 0) {
            LOG.info("=============需要发送视频：" + list.size() + "条");
            for (ActIqiyiResult item : list) {
                actIqiyiService.updateStatus(item.getId(), "SUCCESS");
                String itemNo = "17014";
                String price = "19.8";
                String notifyUrl = "https://prev-saas.guijitech.com/api/iqiyiAct/notifyUrl";
                Map<String, String> retMap = AiQiYiUtil.submitOrder(item.getMobile(), orderPrev, item.getId(), itemNo, item.getQuantity() + "", price, notifyUrl, "127.0.0.1");
                LOG.info("====item.id=" + item.getId() + ",mobile=" + item.getMobile() + ",retMap=" + JSONObject.toJSONString(retMap));
            }
        }

        return BaseResponse.success(null, "发送成功");
    }

    public static void main(String[] args) throws ClientProtocolException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        //"CoopId":"103","OrderNo":"2020010317085818488103","OrderStatus":"SUCCESS","OrderSuccessTime":"20200103172435","Sign":"64ec89f0ccc531f98a22874d72020a48","TranId":"iqiyi2Act1"
        params.put("CoopId", "103");
        params.put("OrderNo", "2020010317085818488103");
        params.put("OrderStatus", "SUCCESS");
        params.put("OrderSuccessTime", "20200103172435");
        params.put("Sign", "64ec89f0ccc531f98a22874d72020a48");
        params.put("TranId", "iqiyi2Act1");


        String ret = HttpClientUtil.post("http://prev-saas.guijitech.com/api/iqiyiAct/notifyUrl", params);
        System.out.println(ret);
    }

    @ResponseBody
    @RequestMapping("/api/iqiyiAct/sms")
    public BaseResponse<List<BijiaColumn>> iqiyiSms(HttpServletRequest request, HttpServletResponse response) {
        List<ActIqiyiResult> list = actIqiyiService.getWaitSendSms();
        LOG.info("=================/api/iqiyiAct/send==========");
        if (list != null && list.size() > 0) {
            LOG.info("=============需要发送短信：" + list.size() + "条");
            for (ActIqiyiResult item : list) {
                int count = actIqiyiService.updateSmsStatus(item.getId());
                if (count == 1) {
                    String smsContent = "【轨迹科技】尊敬的嘉兴移动用户您好！两个月的爱奇艺会员已直充到您的手机号账户中，请通过该手机号登录爱奇艺客户端使用，有效期至2020年3月2日！";
                    String smsRet = SmsHelper.SendSmsMult(smsContent, item.getMobile());
                    LOG.info("=============id：" + item.getId() + " , mobile=" + item.getMobile() + ", smsRet=" + smsRet);
                }
            }
        }

        return BaseResponse.success(null, "发送成功");
    }


    @ResponseBody
    @RequestMapping("/api/iqiyiAct/query")
    public BaseResponse<List<BijiaColumn>> iqiyiQuery(HttpServletRequest request, HttpServletResponse response) {
        List<ActIqiyiResult> list = actIqiyiService.getNeedQueryConfirmStatusList();
        LOG.info("=================/api/iqiyiAct/query==========");
        if (list != null && list.size() > 0) {
            LOG.info("=============需要查询的订单数量：" + list.size() + "条");
            for (ActIqiyiResult item : list) {
                Map<String, String> paramMap = AiQiYiUtil.searchOrder(orderPrev, item.getId());
                LOG.info("==/api/iqiyiAct/query==item.id=" + item.getId() + ",mobile=" + item.getMobile() + ",retMap=" + JSONObject.toJSONString(paramMap));

                String tranId = paramMap.get("tranId");
                String billNo = paramMap.get("orderNo");//点点订单号
                String orderStatus = paramMap.get("orderStatus");
                String failedCode = paramMap.get("failedCode");
                String failedReason = paramMap.get("failedReason");
                LOG.info("---------->> outOrderId = " + tranId + " ,orderStatus = " + orderStatus + ",failedCode = " + failedCode + ",failedReason=" + failedReason);
                String id = tranId.replace(orderPrev, "");
                actIqiyiService.notifyIqiyiOrder(Integer.parseInt(id), orderStatus, failedCode, failedReason, billNo);
            }
        }

        return BaseResponse.success(null, "发送成功");
    }

    @RequestMapping("/api/iqiyiAct/notifyUrl")
    public void iqiyiNotify(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("=================/api/iqiyiAct/notifyUrl=======================");
        String bodyStr = getBodyStr(request);
        LOG.info("=====/api/iqiyiAct/notifyUrl==req.body===" + bodyStr);

        TreeMap<String, String> paramMap = RequestUtils.requestToMap(request);
        LOG.info("=====/api/iqiyiAct/notifyUrl==req.param===" + JSONObject.toJSONString(paramMap));
        // 传入进来的签名
        String oldSign = paramMap.get("Sign");
        String sign = AiQiYiUtil.getSign(paramMap);
        LOG.info("=======/api/iqiyiAct/notifyUrl=======>> 点点异步回调 sign = " + sign);
        if (!oldSign.equalsIgnoreCase(sign)) {
            LOG.info("=======/api/iqiyiAct/notifyUrl====签名错误：req.param===" + JSONObject.toJSONString(paramMap));
            return;
        }

        String tranId = paramMap.get("TranId");
        String billNo = paramMap.get("OrderNo");//点点订单号
        String orderStatus = paramMap.get("OrderStatus");
        String failedCode = paramMap.get("FailedCode");
        String failedReason = paramMap.get("FailedReason");
        LOG.info("---------->> outOrderId = " + tranId + " ,orderStatus = " + orderStatus + ",failedCode = " + failedCode + ",failedReason=" + failedReason);
        String id = tranId.replace(orderPrev, "");
        actIqiyiService.notifyIqiyiOrder(Integer.parseInt(id), orderStatus, failedCode, failedReason, billNo);
        try {
            response.getWriter().print(resultString("T", null, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 封装拼接点点回调报文
     *
     * @param orderSuccess
     * @param faileCode
     * @param faileReason
     * @return
     */
    private String resultString(String orderSuccess, Integer faileCode, String faileReason) {
        StringBuilder stringBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        stringBuilder.append("<response>")
                .append("<orderSuccess>" + orderSuccess + "</orderSuccess>")
                .append("<failedCode>" + faileCode + "</failedCode>")
                .append("<failedReason>" + faileReason + "</failedReason>")
                .append("</response>");
        return stringBuilder.toString();
    }

    @ResponseBody
    @RequestMapping("/api/bijia")
    public BaseResponse<List<BijiaColumn>> bijia(HttpServletRequest request, HttpServletResponse response) {
        String merchantSkuId = request.getParameter("skuId");
        if (StringUtils.isEmpty(merchantSkuId)) {
            return BaseResponse.fail("err_skuId", "skuid不能为空");
        }
        List<BijiaColumn> bijiaList = crawerService.getBijia(Integer.parseInt(merchantSkuId));
        return BaseResponse.success(bijiaList);
    }

    @ResponseBody
    @RequestMapping("/api/bijia/chooseItem")
    public BaseResponse<String> chooseItem(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json) {
        Integer skuId = json.getInteger("skuId");
        Integer itemId = json.getInteger("itemId");
        String currSite = json.getString("currSite");

        if (skuId == null || itemId == null || StringUtils.isEmpty(currSite)) {
            return BaseResponse.fail("err_param", "参数异常");
        }
        crawerService.chooseItem(skuId, currSite, itemId);
        return BaseResponse.success(null, "提交成功");
    }


    @ResponseBody
    @RequestMapping("/api/search/index")
    public BaseResponse<String> searchIndex(HttpServletRequest request, HttpServletResponse response) {
        crawerService.createSearchIndex();
        return BaseResponse.success(null, "索引建立完成");
    }


    @RequestMapping("/bijia/pipei")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("bijia/pipei");

        return mav;
    }

    @RequestMapping("/bijia/op")
    public ModelAndView op(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("bijia/op");

        String itemId = request.getParameter("itemId");
        if (StringUtils.isEmpty(itemId)) {
            mav.setViewName("redirect:/bijia/pipei");
        } else {
            mav.addObject("itemId", itemId);
        }

        return mav;
    }

    @ResponseBody
    @RequestMapping("/bijia/getItemDetail")
    public BaseResponse<YdItemResult> getItemDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json) {
        Integer itemId = json.getInteger("itemId");
        YdItemResult result = ydItemService.getItemDetail(itemId);

        return BaseResponse.success(result);
    }

    @ResponseBody
    @RequestMapping("/bijia/search")
    public BaseResponse<Map<String, List<CrawerSiteSkuResult>>> search(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject json) {
        Integer skuId = json.getInteger("skuId");
        Map<String, List<CrawerSiteSkuResult>> searchMap = crawerService.search(skuId);
        return BaseResponse.success(searchMap);
    }

    @ResponseBody
    @RequestMapping("/api/bijia/bindItem")
    public BaseResponse<String> search(HttpServletRequest request) {
        crawerService.bindOtherPlatformItem();
        return BaseResponse.success("绑定成功");
    }

    private String getBodyStr(HttpServletRequest request) {
        // 从request中取得输入流
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                int bytesRead = -1;
                char[] charBuffer = new char[128];
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            LOG.error("解析点点回调流出错， {}", ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    LOG.error("关闭流出错,{}", ex);
                }
            }
        }
        return stringBuilder.toString();
    }
}
