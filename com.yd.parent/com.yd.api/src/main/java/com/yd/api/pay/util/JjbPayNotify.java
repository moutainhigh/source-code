package com.yd.api.pay.util;

import java.util.Map;


public class JjbPayNotify {
    
    
    public static boolean verify(Map<String, String> params,String jjbPublicKey) {
	    String sign =  params.get("sign");
	    if(sign == null) {
	    	return false;
	    }
	  //过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = JjbPayCore.paraFilter(params);
        //获取待签名字符串
        String preSignStr = JjbPayCore.createLinkString(sParaNew);
        //获得签名验证结果
        return RSA.verify(preSignStr, sign, jjbPublicKey, JjbPayConfig.input_charset);
    }
}
