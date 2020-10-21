package com.yd.service;

import com.yd.api.pay.util.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * 畅游积分工具类
 * @author wuyc
 * created 2020/3/18 14:42
 **/
public class ChangyouTest {

    /** 渠道号 */
    public static String CHANNEL_SOURCE = "02004362";

    /** 商户号 */
    public static String PARTNER_ID = "S1000131";

    /** 专区编号 */
    public static String STORE_ID = "best_store1";

    /** 签名密钥 */
    public static String SIGN_KEY = "123456789";


    private static String domain = "https://test-m-stg.ppppoints.com/finance/apply/output/index.html";

    /** 请求地址参数 */
    public static String URL_PARAMS_STR = "?channelSource=%s&partnerId=%s&storeId=%s&callbackUrl=%s&outTokenId=%s&mobile=%s&merSign=%s";

    public static void main(String[] args) {
        String callbackUrl = "";
        String mobile = "15858110963";
        String outTokenId = mobile + "out";
        String sign = getSign(outTokenId, mobile);
        String paramsStr = String.format(URL_PARAMS_STR, CHANNEL_SOURCE, PARTNER_ID, STORE_ID, callbackUrl, outTokenId, mobile, sign);
        System.out.println("====paramsStr=" + paramsStr);
        String url = domain + paramsStr;
        String getUrl = HttpClientUtil.get(url, null);
        System.out.println(getUrl);

    }

    public static Map<String, String> getParamMap(String outTokenId, String mobile, String callbackUrl) {
        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put("outTokenId", outTokenId);
        paramMap.put("mobile", mobile);
        paramMap.put("storeId", STORE_ID);
        paramMap.put("partnerId", PARTNER_ID);
        paramMap.put("channelSource", CHANNEL_SOURCE);
        return paramMap;
    }

    public static String getSign(String outTokenId, String mobile) {
        Map<String, String> signMap = new TreeMap<>();
        signMap.put(mobile, mobile);
        signMap.put(outTokenId, outTokenId);
        signMap.put(STORE_ID, STORE_ID);
        signMap.put(SIGN_KEY, SIGN_KEY);
        signMap.put(PARTNER_ID, PARTNER_ID);
        signMap.put(CHANNEL_SOURCE, CHANNEL_SOURCE);

        StringBuilder signData = new StringBuilder();
        for(Map.Entry<String,String> entry : signMap.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value)) {
                signData.append(value);
            }
        }
        System.out.println("====加密前的数据=" + signData.toString());
        String sign = DigestUtils.sha1Hex(signData.toString());
        System.out.println("====加密后的数据=" + sign);
        return sign;
    }

}
