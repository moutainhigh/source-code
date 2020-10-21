package com.yg.core.utils;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
    private final static Logger LOG = LoggerFactory.getLogger(RequestUtils.class);

    public static TreeMap<String, String> requestToMap(HttpServletRequest request) {
        TreeMap<String, String> verifyParams = new TreeMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            verifyParams.put(name, valueStr.replaceAll("ZZZZ", "+"));
        }

        return verifyParams;
    }

    public static String getStr(TreeMap<String, String> treeMap) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : treeMap.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value) && !"sign".equals(entry.getKey())) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String retStr = sb.toString();
        return retStr.substring(0, retStr.length() - 1);
    }

    public static String getStrHaveNull(TreeMap<String, String> treeMap) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : treeMap.entrySet()) {
            String value = entry.getValue();
            if (!"sign".equals(entry.getKey())) {
                if (StringUtils.isEmpty(value)) {
                    sb.append(entry.getKey()).append("=").append("&");
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }

            }
        }
        String retStr = sb.toString();
        return retStr.substring(0, retStr.length() - 1);
    }

    public static String getSign(TreeMap<String, String> treeMap, String md5Key) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : treeMap.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(value) && !"sign".equals(entry.getKey())) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        sb.append("key=" + md5Key);
        String retStr = sb.toString();
        System.out.println("retStr:" + retStr);
        return MD5Util.getMD5Str(retStr);
    }

    public static boolean validSign(TreeMap<String, String> treeMap, String md5Key, String sign) {
        String retSign = getSign(treeMap, md5Key);
        System.out.println("===retSign:" + retSign);
        System.out.println("===sign:" + sign);
        return retSign.equals(sign);
    }

    public static void main(String[] args) {
        String merchantId = "1";
        String userToken = "15726940633";
//        String mobile = "15726940631";
//        String nickname = "15726940631";
//        String cardExpireTime = "2019-07-24 18:00:00";
        TreeMap<String, String> map = new TreeMap<>();
        map.put("merchantId", merchantId);
        map.put("userToken", userToken);
//        map.put("mobile", mobile);
//        map.put("nickname", nickname);
//        map.put("cardExpireTime", cardExpireTime);
        String aaaaaa = getSign(map, "aaaaaa");
        System.out.println(aaaaaa);

        System.out.println(DateUtil.getDateForSdf("2019-07-24 18:00:00", "yyyy-MM-dd HH:mm:SS"));

    }
}
