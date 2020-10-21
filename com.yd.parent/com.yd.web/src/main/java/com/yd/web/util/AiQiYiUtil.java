package com.yd.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.api.pay.util.MD5Util;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爱奇艺下单工具类
 *
 * @author wuyc
 * created 2019/8/5 18:59
 **/
public class AiQiYiUtil {

    private static final String COOPID = "103";
    private static final String KEY = "28b27a4ebf567b43cf20845d177c45f6";

    /**
     * 查询订单是否已经发货
     * @param orderId
     * @return Map<String, String>
     *
     */
    public static Map<String, String> searchOrder(String orderPrev,Integer orderId) {
        String url = "http://121.199.38.244:30021/Service/GetOrder.aspx";
        TreeMap<String, String> map = new TreeMap<>();
        map.put("coopid", COOPID);
        map.put("tranid", orderPrev + orderId);
        map.put("sign", sign(map));
        System.out.println("searchOrder=========param=============="+JSONObject.toJSONString(map));
        String ret = HttpClientUtil.post(url, map);
        System.out.println("searchOrder===========ret=============="+ret);
        Map<String, String> result = parseXml2Map(ret);
        System.out.println("查询点点订单结果返回值 result = " + JSON.toJSONString(result));
        return result;
    }

    /**
     * 调用点点接口终止爱奇艺订单
     * @param account       账号
     * @param orderId       订单id
     * @param itemNo        商品编号
     * @param quantity      充值数量
     * @param price         单价
     * @param notifyUrl     回调地址
     * @param userIp        用户ip
     * @return
     */
    public static Map<String, String> submitOrder(String account,String orderPrev, Integer orderId, String itemNo, String quantity, String price, String notifyUrl, String userIp) {
        String url="http://121.199.38.244:30021/Service/PostOrder.aspx";
        TreeMap<String,String> map=new TreeMap<String,String>();
        // 对外订单id
        map.put("tranid", orderPrev + orderId);
        map.put("proid", itemNo);
        map.put("coopid", COOPID);

        map.put("quantity", quantity);
        map.put("price", price);
        map.put("account", account);
        map.put("asyncurl", notifyUrl);
        map.put("ipaddr", userIp);
        map.put("sign", sign(map));
        System.out.println("submitOrder=====param========="+JSONObject.toJSONString(map));
        String ret = HttpClientUtil.post(url, map);
        System.out.println("submitOrder=======ret=============="+ret);
        Map<String, String> result = parseXml2Map(ret);
        System.out.println("result = " + JSON.toJSONString(result));
        return result;
    }


    private static String sign(TreeMap<String,String> map) {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,String> entry : map.entrySet()) {
            String keyName = entry.getKey();
            String value = entry.getValue();
            if(StringUtils.isNotEmpty(value)) {
                sb.append(keyName+value);
            }
        }
        sb.append(KEY);
        System.out.println("mkd.str=="+sb.toString());
        return MD5Util.MD5Encode(sb.toString(), "GBK");
    }

    /**
     * 根据hashMap获取签名
     * @param map
     * @return sign
     */
    public static String getSign(Map<String, String> map) {
        TreeMap<String,String> treeMap = new TreeMap<>();
        for(Map.Entry<String,String> entry : map.entrySet()) {
            String keyName = entry.getKey();
            if (!"Sign".equalsIgnoreCase(keyName)) {
                String value = entry.getValue();
                treeMap.put(keyName, value);
            }
        }
        return sign(treeMap);
    }


    /**
     * 将爱奇艺返回结果转化为map
     * @param xmlStr
     * @return Map<String, String>
     */
    public static Map<String, String> parseXml2Map(String xmlStr) {
        if (xmlStr != null && !"".equals(xmlStr.trim())) {
            Map<String, String> paramMap = new HashMap();
            Pattern pattern = Pattern.compile(String.format("(?<=<%s>)[\\s\\S]*(?=</%s>)", "response", "response"));
            Matcher matcher = pattern.matcher(xmlStr);
            String messageContent = null;
            if (!matcher.find()) {
                return paramMap;
            } else {
                messageContent = matcher.group();
                if ("".equals(messageContent.trim())) {
                    return paramMap;
                } else {
                    pattern = Pattern.compile("(?<=<)[^/].*?(?=>)");
                    matcher = pattern.matcher(messageContent);
                    ArrayList nodeNameList = new ArrayList();

                    while(matcher.find()) {
                        nodeNameList.add(matcher.group());
                    }

                    Iterator iterator = nodeNameList.iterator();

                    while(iterator.hasNext()) {
                        String nodeName = (String)iterator.next();
                        pattern = Pattern.compile(String.format("(?<=<%s>)[\\s\\S]*(?=</%s>)", nodeName, nodeName));
                        matcher = pattern.matcher(messageContent);
                        if (matcher.find()) {
                            paramMap.put(nodeName, matcher.group());
                        }
                    }

                    return paramMap;
                }
            }
        } else {
            throw new IllegalArgumentException("xmlStr is null or blank!");
        }
    }


}
