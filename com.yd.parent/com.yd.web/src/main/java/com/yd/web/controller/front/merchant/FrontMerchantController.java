package com.yd.web.controller.front.merchant;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.RandomHelper;
import com.yd.api.pay.util.SHA1;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.common.WeixinService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.user.YdUserMerchantService;
import com.yd.core.res.BaseResponse;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import com.yd.web.controller.FrontBaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author wuyc
 * created 2019/12/3 18:05
 **/
@Controller
@RequestMapping("/front/merchant")
public class FrontMerchantController extends FrontBaseController {

    @Reference
    private WeixinService weixinService;

    @Reference
    private YdMerchantService ydMerchantService;

    @Reference
    private YdUserMerchantService ydUserMerchantService;

    @ApiOperation(value = "查询商户信息", httpMethod = "POST")
    @ResponseBody
    @RequestMapping(value = "/getMerchantInfo", method = {RequestMethod.POST})
    public BaseResponse<YdMerchantResult> getMerchantInfo(HttpServletRequest request) {
        BaseResponse<YdMerchantResult> result = BaseResponse.success(null, "00", "查询成功");
        try {
            YdMerchantResult ydMerchantResult = ydMerchantService.getYdMerchantById(getCurrMerchantId(request));
            result.setResult(ydMerchantResult);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "公众号商户信息", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "distinctId", value = "区id"),
            @ApiImplicitParam(paramType = "query", name = "merchantName", value = "商户名")
    })
    @ResponseBody
    @RequestMapping(value = "/findMerchantList", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<Page<YdMerchantResult>> findMerchantList(HttpServletRequest request, Integer distinctId, String merchantName) {
        BaseResponse<Page<YdMerchantResult>> result = BaseResponse.success(null, "00", "查询成功");
        try {
            PagerInfo pageInfo = getPageInfo(request, 1, 5);
            YdMerchantResult req = new YdMerchantResult();
            req.setDistrictId(distinctId);
            req.setMerchantName(merchantName);
            req.setIsFlag("N");
            req.setMemberStatus("valid");
            result.setResult(ydMerchantService.findStoreListByPage(req, pageInfo));
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "用户商户绑定", httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户id")
    })
    @ResponseBody
    @RequestMapping(value = "/bindUserMerchant", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<String> bindUserMerchant(HttpServletRequest request, Integer merchantId) {
        BaseResponse<String> result = BaseResponse.success(null, "00", "绑定成功");
        try {
            System.out.println("====进入绑定商户用户接口，入参merchantId=" + merchantId + ",userId=" + getCurrUserId(request));
            ydUserMerchantService.saveOrUpdate(getCurrUserId(request), merchantId);
        } catch (BusinessException businessException) {
            result = BaseResponse.fail(businessException.getCode(), businessException.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "微信分享链接配置", httpMethod = "POST")
    @ResponseBody
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "url", value = "分享的url？"),
            @ApiImplicitParam(paramType = "query", name = "callback", value = "返回地址")
    })
    @RequestMapping(value = "/user/share/jsonp/post", method = {RequestMethod.POST, RequestMethod.GET})
    public void weixinShareJsonP(HttpServletRequest request, HttpServletResponse response, String url, String callback) {
        BaseResponse<Map<String, String>> result = new BaseResponse<>();
        if (StringUtils.isEmpty(url)) {
            result.setCode("01");
            result.setMessage("url不能为空");
        }else{
            WbWeixinAccountResult accessToken = weixinService.getByWeixinAccountByType("91kuaiqiang");
            String timestamp = "" + (new Date().getTime() / 1000);
            String noncestr = RandomHelper.getNonceStr();
            SortedMap<String, String> model = new TreeMap<String, String>();
            String ticket = accessToken.getJsapiTicket();
            model.put("noncestr", noncestr);
            model.put("jsapi_ticket", ticket);
            model.put("timestamp", timestamp);
            model.put("url", url.split("#")[0]);
            StringBuffer sb = new StringBuffer();
            Set<Map.Entry<String, String>> es = model.entrySet();// 所有参与传参的参数按照accsii排序（升序）
            Iterator<Map.Entry<String, String>> it = es.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String k = (String) entry.getKey();
                String v = entry.getValue();
                sb.append(k + "=" + v + "&");
            }
            String p = sb.toString();
            if (StringUtils.isNotEmpty(p) && p.endsWith("&")) {
                p = p.substring(0, p.length() - 1);
            }
            String signature = SHA1.encode(p);

            Map<String, String> map = new HashMap<String, String>();
            map.put("url", url);
            map.put("appId", accessToken.getAppId());
            map.put("timestamp", timestamp);
            map.put("nonceStr", noncestr);
            map.put("signature", signature);
            result.setResult(map);
            result.setValue("00", "获取成功");
        }
        try {
            PrintWriter out = response.getWriter();
            out.print(callback+"("+JSONObject.toJSONString(result)+")");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
