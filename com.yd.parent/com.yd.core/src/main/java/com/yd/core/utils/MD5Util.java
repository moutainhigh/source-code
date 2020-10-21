package com.yd.core.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.TreeMap;


public class MD5Util {
    private static Logger LOG = Logger.getLogger("debugAppender");
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * MD5 加密
     *
     * @param str 字符串
     * @return 返回加密后的字符串
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            LOG.info("getMD5Str error", e);
        } catch (UnsupportedEncodingException e) {
            LOG.info("getMD5Str erro", e);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    /**
     * MD5 GB2312加密
     *
     * @param str 字符串
     * @return 返回加密后的字符串
     */
    public static String getMD5StrGB2312(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("GB2312"));
        } catch (NoSuchAlgorithmException e) {
            LOG.info("getMD5Str error", e);
        } catch (UnsupportedEncodingException e) {
            LOG.info("getMD5Str erro", e);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    public static String getMD5UUID(String params) {
        if (StringUtils.isEmpty(params)) {
            throw new RuntimeException("参数不能为空");
        }
        params = params.trim() + UserConstants.ENCRYPT_AUTH_KEY;
        return getMD5Str(params);
    }

    /**
     * chck password
     *
     * @param newStr    no encrypt password
     * @param oldMD5Str
     * @return
     */
    public final static boolean checkMD5(String newStr, String oldMD5Str) {
        String temp = getMD5Str(newStr);
        if (temp != null && temp.equals(oldMD5Str)) {
            return true;
        } else {
            return false;
        }
    }


    public static String KeyCreate(int KeyLength){
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" ;
        Random random = new Random();
        StringBuffer Keysb = new StringBuffer();
        for(int i = 0; i<KeyLength; i++)
        {
            int number = random.nextInt(base.length());
            Keysb.append(base.charAt(number));
        }
        return Keysb.toString();
    }


    public static void main(String[] args) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("userId", "123456");
        treeMap.put("actId", "123");
        treeMap.put("testCount", 2);
        treeMap.put("score", 80);
        System.out.println(JSON.toJSONString(treeMap));
        getMD5Str(JSON.toJSONString(treeMap));
        System.out.println(getMD5Str(JSON.toJSONString(treeMap)));
    }
}
