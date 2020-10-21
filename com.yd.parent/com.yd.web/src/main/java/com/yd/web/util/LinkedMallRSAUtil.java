package com.yd.web.util;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
public class LinkedMallRSAUtil {

    /** 签名算法 **/
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /** 加密算法 **/
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 公钥验签
     *
     * @param text      原字符串
     * @param sign      签名结果
     * @param publicKey 公钥
     * @return 验签结果
     */
    public static boolean verify(String text, String sign, String publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            PublicKey key = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
            signature.initVerify(key);
            signature.update(text.getBytes());
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            System.out.println("验签失败");
        }
        return false;
    }

    /**
     * 私钥加签
     *
     * @param text       需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     * @return 签名结果(BASE64编码)
     */
    public static String sign(String text, String privateKey) {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(text.getBytes());
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            System.out.println("签名失败");
        }
        return null;
    }


    public static void main(String[] args) {
//        /** 演示用数据 **/
//        Map<String, Object> data = new HashMap<String, Object>();
//        /** 时间戳为 Long, ms **/
//        data.put("timestamp", 1551752768213L);
//        data.put("action", "testAction");
//        /** 复杂对象需要以 String 形式传入，以固定内部属性的顺序 **/
//        data.put("model", "{\"name\":\"test\", \"age\":20}");
//
//        /** 按照key字典序排序，也可自行拼凑 **/
//        String dataToBeSigned = "";
//        List arrayList = new ArrayList(data.entrySet());
//        Collections.sort(arrayList, new Comparator()
//        {
//            public int compare(Object arg1, Object arg2)
//            {
//                Map.Entry obj1 = (Map.Entry) arg1;
//                Map.Entry obj2 = (Map.Entry) arg2;
//                return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
//            }
//        });
//
//        for (Iterator iter = arrayList.iterator(); iter.hasNext();)
//        {
//            Map.Entry entry = (Map.Entry)iter.next();
//            dataToBeSigned = dataToBeSigned + (dataToBeSigned.equals("") ? "" : "&")
//                + entry.getKey() + "=" + entry.getValue();
//        }
//
//        /** 字典序排序，复杂结构转为String以固定内部排序 **/
//        assert dataToBeSigned.equals("action=testAction&model={\"name\":\"test\", \"age\":20}&timestamp=1551752768213");
//
//        /** 加签 **/
//        String sign = sign(dataToBeSigned, DEMO_PRIVATE_KEY);
//        /** 验签 **/
//        boolean verify = verify(dataToBeSigned, sign, DEMO_PUBLIC_KEY);
//        assert verify;
    }
}
