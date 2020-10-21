package com.yd.api.pay.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;



public class PaySignHelper {
    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	public static boolean valid(Map<String, String> params, String publicKey) {
		String sign=params.get("sign");
		if(StringUtils.isEmpty(sign)){
			return false;
		}
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = SignCore.paraFilter(params);
        //获取待签名字符串
        String preSignStr = SignCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = RSA.verify(preSignStr, sign, publicKey, SignConfig.input_charset);
        return isSign;
    }
	
	public static Map<String, String> sign(Map<String, String> sParaTemp,String privateKey) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = SignCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara,privateKey);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", SignConfig.sign_type);

        return sPara;
    }
	
	
	private static String buildRequestMysign(Map<String, String> sPara,String privateKey) {
    	String prestr = SignCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = RSA.sign(prestr, privateKey, SignConfig.input_charset);
        return mysign;
    }
}
