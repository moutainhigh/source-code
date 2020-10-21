package com.yg.core.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author jacky
 */
public class SmsHelper {

    private static final Logger LOG = Logger.getLogger(SmsHelper.class);

    //通知账号
    private static String PASSWORD = GetMD5Str("hzjjb0315");
    private static String USERNAME = "hzjjb";

    //营销账号
    private static String MARKETPASSWORD = GetMD5Str("jjbyx0316");
    private static String MARKETUSERNAME = "jjbyx";


    static String Urlsend = "http://www.yescloudtree.cn:28001";
    static String Urlquery = "http://www.yescloudtree.cn:28001";
    static String UserName = "jjbyx";// (必填)

    // static String IsP2p = "0";//(选填)0为群发，1为点对点发送
    static String MsgID = "";// (选填)无此参数则系统返回唯一码ID
    static String ExtCode = "";// (选填)扩展码
    static String Timing = "";// (选填)定时时间，格式：yyyy-MM-dd HH:mm,值为空则立即发送
    // 2017-10-10 16:00
    static String Level = "0";// (选填)1-5优先级从低到高，无此参数或为空或小于1则默认为系统设置的优先级，验证类短信建议设置
    static Date date = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 完整的时间
    static String time = sdf.format(date);

    public static void main(String[] args) {
        // SendSmsMult();//短信群发
    	sendSmsByHttpClient("【U+会员】您的验证码是111111，有效期十分钟。如非本人操作，请忽略本短信", "15858110963");//点对点发送
//        RemainSms();// 接收余额
//        ReportSms();// 接收状态报告
//        MoSms();// 接收上行
    }

    public static String sendSms(String content, String mobile) {
        String result = SendSmsP2P(content, mobile);
        return result;
    }
    public static String sendSmsByHttpClient(String content, String mobile) {
        String result = SendSmsP2PByHttpClient(content, mobile);
        return result;
    }

    public static String sendMarketSms(String content, String mobile) {
        String result = SendSmsMult(content, mobile);
        return result;
    }

    /**
     * 群发
     *
     * @param message
     * @param mobile
     */
    public static String SendSmsMult(String message, String mobile) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Action", "SendSms");
            params.put("UserName", MARKETUSERNAME);
            params.put("Password", MARKETPASSWORD);
            params.put("Mobile", mobile);
            params.put("Message", message);
            params.put("IsP2p", "0");
            params.put("MsgID", MsgID);
            params.put("ExtCode", ExtCode);
            params.put("Timing", Timing);
            params.put("Level", Level);
            String result = doPost(Urlsend, params, "utf-8");
            String[] strsResult = result.split(":");
            System.out.println(time + ">>短信群发"
                    + Result(Integer.parseInt(strsResult[0])) + result);
            return time + ">>短信群发" + Result(Integer.parseInt(strsResult[0])) + result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 点对点发送
     *
     * @param message
     * @param mobile
     */
    public static String SendSmsP2P(String message, String mobile) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Action", "SendSms");
            params.put("UserName", USERNAME);
            params.put("Password", PASSWORD);
            params.put("Mobile", mobile);
            params.put("Message", message);
            params.put("IsP2p", "1");
            params.put("MsgID", MsgID);
            params.put("ExtCode", ExtCode);
            params.put("Timing", Timing);
            params.put("Level", Level);
            System.out.println("params============" + params);
            String result = doPost(Urlsend, params, "utf-8");
            String[] strsResult = result.split(":");
            System.out.println(time + ">>点对点短信提交" + Result(Integer.parseInt(strsResult[0])) + result);
            LOG.info(time + ">>点对点短信提交" + Result(Integer.parseInt(strsResult[0])) + result);
            return time + ">>短信群发" + Result(Integer.parseInt(strsResult[0])) + result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 点对点发送
     *
     * @param message
     * @param mobile
     */
    public static String SendSmsP2PByHttpClient(String message, String mobile) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Action", "SendSms");
            params.put("UserName", USERNAME);
            params.put("Password", PASSWORD);
            params.put("Mobile", mobile);
            params.put("Message", message);
            params.put("IsP2p", "1");
            params.put("MsgID", MsgID);
            params.put("ExtCode", ExtCode);
            params.put("Timing", Timing);
            params.put("Level", Level);
            System.out.println("params============" + params);
            String result = postForm(Urlsend, params);
            String[] strsResult = result.split(":");
            System.out.println(time + ">>点对点短信提交" + Result(Integer.parseInt(strsResult[0])) + result);
            LOG.info(time + ">>点对点短信提交" + Result(Integer.parseInt(strsResult[0])) + result);
            return time + ">>短信群发" + Result(Integer.parseInt(strsResult[0])) + result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static void MoSms() {// 接收上行
        Map<String, String> params = new HashMap<String, String>();
        params.put("Action", "MoSms");
        params.put("UserName", MARKETUSERNAME);
        params.put("Password", MARKETPASSWORD);
        String result = doPost(Urlquery, params, "utf-8");
        splitMo(result);
    }

    public static void ReportSms() {// 接收状态报告
        Map<String, String> params = new HashMap<String, String>();
        params.put("Action", "ReportSms");
        params.put("UserName", MARKETUSERNAME);
        params.put("Password", MARKETPASSWORD);
        String result = doPost(Urlquery, params, "utf-8");
        String[] strsResult = result.split(":");
        System.out.println(time + ">>接收状态报告"
                + Result(Integer.parseInt(strsResult[0])) + result);
    }

    public static void RemainSms() {// 接收余额
        Map<String, String> params = new HashMap<String, String>();
        params.put("Action", "RemainSms");
        params.put("UserName", MARKETUSERNAME);
        params.put("Password", MARKETPASSWORD);
        String result = doPost(Urlquery, params, "utf-8");
        String[] strsResult = result.split(":");
        System.out.println(time + ">>接收余额"
                + Result(Integer.parseInt(strsResult[0])) + result);
    }

    public static String doPost(String url, Map<String, String> params,
                                String charset) {// 执行POST
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        method.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=" + charset);
        if (params != null) {
            Set<String> keySet = params.keySet();
            NameValuePair[] param = new NameValuePair[keySet.size()];
            int i = 0;
            for (String key : keySet) {
                param[i] = new NameValuePair(key, params.get(key));
                i++;
            }
            System.out.println(JSONObject.toJSONString(param));
            method.setRequestBody(param);
        }
        InputStream responseBodyStream = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                responseBodyStream = method.getResponseBodyAsStream();
                streamReader = new InputStreamReader(responseBodyStream,
                        charset);
                reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e);
        } finally {
            try {
                responseBodyStream.close();
                streamReader.close();
                reader.close();
            } catch (IOException e) {
                System.out.println("执行HTTP Post请求" + url + "时，发生异常，关闭流异常！" + e);
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        // System.out.println(response.toString());
        return response.toString();
    }

    private static String postForm(String url, Map<String, String> params) {
        String ret = null;
        HttpPost httpost = new HttpPost(url);
        // httpost.setHeader(HTTP.CHARSET_PARAM, "UTF-8");
        // httpost.setHeader("Content-Type",
        // " application/x-www-form-urlencoded");

        List<BasicNameValuePair> nvps = new ArrayList<>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps,
                    "UTF-8");
            // entity.setContentType("text/xml;　charset=utf-8");
            entity.setContentType("application/x-www-form-urlencoded");

            httpost.setEntity(entity);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpost);
            ret = EntityUtils.toString(response.getEntity(), "utf-8");

            // httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public final static String GetMD5Str(String s) {// MD5加密
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void splitMo(String data) { // 解析上行
        data = data.toLowerCase();
        data = data.replaceAll("\r\n", "");
        if (data.indexOf("</mopack>") != -1) {
            Pattern pattern1 = Pattern.compile("<mopack>(.*?)</mopack>");
            Matcher matcher1 = pattern1.matcher(data);
            String[] strsResult = data.split(":");
            while (matcher1.find()) {

                int i = 1;
                String datamopack = matcher1.group(i);
                String mobile = getParameter(datamopack, "mobile");
                String message = getParameter(datamopack, "message");
                message = URLDecoder.decode(message);
                String motime = getParameter(datamopack, "motime");
                System.out.println(time + ">>接收上行"
                        + Result(Integer.parseInt(strsResult[0])) + ",回复号码:"
                        + mobile + ",回复内容:" + message + ",回复时间:" + motime);
                i++;
            }
        } else {
            String[] strsResult = data.split(":");
            System.out.println(time + ">>接收上行"
                    + Result(Integer.parseInt(strsResult[0])) + data);
        }
    }

    public static String getParameter(String data, String para) {
        String result = "";
        StringBuffer reStr = new StringBuffer();
        reStr.append("<");
        reStr.append(para);
        reStr.append(">");
        reStr.append("(.*?)");
        reStr.append("</");
        reStr.append(para);
        reStr.append(">");
        Pattern pattern = Pattern.compile(reStr.toString());
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public static String Result(int i) {// 错误码说明
        String r = "";
        if (i == 0) {
            r = "成功";
        } else if (i == 1) {
            r = "失败，原因：目的号码太长（最多100个),错误码：";
        } else if (i == 2) {
            r = "失败，原因：超过今天的最大发送量,错误码：";
        } else if (i == 3) {
            r = "失败，原因：所剩于的发送总量低于您现在的发送量,错误码：";
        } else if (i == 4) {
            r = "失败，原因：信息提交失败,错误码：";
        } else if (i == 5) {
            r = "失败，原因：出现未知情况，请联系管理员,错误码：";
        } else if (i == 6) {
            r = "失败，原因：点对点手机号码与短信内容数量不匹配,错误码：";
        } else if (i == 7) {
            r = "失败，原因：Action参数错误,错误码：";
        } else if (i == 8) {
            r = "失败，原因：系统故障,错误码：";
        } else if (i == 9) {
            r = "失败，原因：用户名密码不正确,错误码：";
        } else if (i == 10) {
            r = "失败，原因：定时时间格式错误,错误码：";
        } else if (i == 11) {
            r = "失败，原因：定时时间小于当前时间,需大于当前时间30分钟,错误码：";
        } else if (i == 99) {
            r = "失败，原因：超出许可连接数,错误码：";
        } else {
            r = "失败，未知错误,错误码：";
        }
        return r;
    }


}
