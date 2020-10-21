package com.yd.core.utils;



import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class AliDaYuSmsSdkClient {
	private static final String appkey = "23318638";
	private static final String url = "http://gw.api.taobao.com/router/rest";
	private static final String secret = "049af141673e965eb1639177275298a5";

	public static String sendSms(String extend,String sms_free_sign_name,JSONObject sms_param,String rec_num,String sms_template_code) throws ApiException{
		Assert.notNull(sms_free_sign_name);
		Assert.notNull(sms_param);
		Assert.notNull(rec_num);
		Assert.notNull(sms_template_code);
		if(StringUtils.isEmpty(extend)){
			extend ="";
		}
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend(extend);
		req.setSmsType("normal");
		req.setSmsFreeSignName(sms_free_sign_name);
		req.setSmsParamString(sms_param.toString());
		req.setRecNum(rec_num);
		req.setSmsTemplateCode(sms_template_code);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		return rsp.getBody();
	}
	
	
	public static void main(String[] args) throws ApiException {
		JSONObject json=new JSONObject();
		json.put("code", "111111");
		sendSms("", "快抢商城", json, "15166795821", "SMS_186942914");
	}
}
