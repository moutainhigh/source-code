package com.yd.core.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 优度积分中控系统对接工具类
 * @author wuyc
 * created 2019/12/20 13:52
 **/
public class YunxiaokejiUtil {

    private static final Logger logger = LoggerFactory.getLogger(YunxiaokejiUtil.class);

    /* 域名 **/
    private static final String API_DOMAIN = "http://www.yunxiaokeji.cn/sbc/api";

    /* 获取token完成url **/
    private static final String API_GET_TOKEN = API_DOMAIN + "/getToken";

    /* 核销完成url **/
    private static final String API_CHECK_MOBILE = API_DOMAIN + "/check/mobile";

    private static final String API_MOBILT_REGISTER = API_DOMAIN + "/register/";

    private static final String API_ID = "7895857800984854528";

    private static final String API_SECRET = "6334adce788869accb2b3163c2f95f9c";

    private static final String API_ACCOUNT_USERNAME = "julezhifu";

    private static final String API_ACCOUNT_PWD = "jlzf@123456";


    private static String getToken() {
        Map<String, Object> params = new TreeMap<>();
        params.put("username", API_ACCOUNT_USERNAME);
        params.put("password", API_ACCOUNT_PWD);
        params.put("apiId", API_ID);
        params.put("sign", getSign(params));
        String result = HttpRequest.post(API_GET_TOKEN).body(JSON.toJSONString(params)).timeout(20000).execute().body();
        logger.info("获取token返回的结果===" + JSON.toJSONString(result));
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject != null && jsonObject.getInteger("code") == 200) {
            return jsonObject.getJSONObject("result").getString("token");
        }
        return null;
    }

    public static JSONObject checkMobile(String mobile, String productId) {
        // 获取token
        String token = getToken();
        Map<String, Object> params = new TreeMap<>();
        params.put("mobile", mobile);
        params.put("productId", productId);
        params.put("apiId", API_ID);
        params.put("sign", getSign(params));
        params.put("token", token);

        String result = HttpRequest.post(API_CHECK_MOBILE).header("X-Access-Token", token).body(JSON.toJSONString(params)).timeout(20000).execute().body();
        logger.info("核销返回的结果===" + JSON.toJSONString(result));
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }


    public static JSONObject register(String mobile, String productId) {
        // 获取token
        String token = getToken();

        Map<String, Object> params = new TreeMap<>();
        params.put("mobile", mobile);
        params.put("productId", productId);
        params.put("apiId", API_ID);
        params.put("sign", getSign(params));
        params.put("token", token);

        String result = HttpRequest.post(API_MOBILT_REGISTER).header("X-Access-Token", token).body(JSON.toJSONString(params)).timeout(20000).execute().body();
        logger.info("中域登记返回的结果===" + JSON.toJSONString(result));
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static String getSign(Map<String, Object> params) {
        params.remove("sign");
        try {
            logger.info("签名前的params===" + JSON.toJSONString(params));
            BASE64Encoder encoder = new BASE64Encoder();
            String beforeText = encoder.encode(JSON.toJSONString(params).getBytes("UTF-8"))
                    .replace("\n","").replace("\r","").trim() + API_SECRET;
            logger.info("签名前的数据===" + beforeText);
            String sign = MD5Util.getMD5Str(beforeText);
            logger.info("签名后的值===" + sign);
            return sign;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        // getToken();
        // checkMobile("15166795821", "10");
        // register("15166795821", "13");

//        String url = "https://test-m-stg.ppppoints.com/partner-gateway/api/changyouMarket/couponResult";
//        String data = "{\"data\":\"xBaGoJkCwahQV/O7E6rNABVDUWvBRKMOYshrGwtFgnr99LFtiw4ngSU78vvGXPVHoewanJMcaiyo\\n/jYmPvx+P0tNdYL8x1toqN+aQ0dqETM+CAxppvOqGwY3+yFGp1BcX+6xezPQPZayUz3U6natUOy5\\nfJlwTwIAjVYMFomD8VrBQ4n/qPL47cAiP4/hpnDJIGVnIFtiHPL3acBSwd3JPwthblFvJ4HnRKds\\nidkp/Q0=\",\"partnerId\":\"S9990037\",\"requestId\":\"1591083732219\",\"sign\":\"59702eaa26bdd6911d4ef20729b50dfa2fcff6dc\",\"storeId\":\"S9990037_best_store1\",\"timestamp\":1591083732219}\n";
//        String result = HttpRequest.post(url).body(data).timeout(20000).execute().body();
//
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("data", "xBaGoJkCwahQV/O7E6rNABVDUWvBRKMOYshrGwtFgnr99LFtiw4ngSU78vvGXPVHoewanJMcaiyo\\n/jYmPvx+P0tNdYL8x1toqN+aQ0dqETM+CAxppvOqGwY3+yFGp1BcX+6xezPQPZayUz3U6natUOy5\\nfJlwTwIAjVYMFomD8VrBQ4n/qPL47cAiP4/hpnDJIGVnIFtiHPL3acBSwd3JPwthblFvJ4HnRKds\\nidkp/Q0=");
//        params.put("partnerId", "S9990037");
//        params.put("requestId", "1591083732219");
//        params.put("sign", "59702eaa26bdd6911d4ef20729b50dfa2fcff6dc");
//        params.put("storeId", "S9990037_best_store1");
//        params.put("timestamp", 1591083732219L);
//        String result2 = HttpRequest.post(url).body(JSON.toJSONString(params)).timeout(20000).execute().body();
//        System.out.println("result=" + result);


    }



}
