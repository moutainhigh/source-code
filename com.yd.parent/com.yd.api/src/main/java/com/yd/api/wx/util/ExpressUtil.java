package com.yd.api.wx.util;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.pay.util.HttpClientUtil;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.ValidateBusinessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class ExpressUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExpressUtil.class);

    private static final String DOMAIN = "https://api.tool.dute.me/tool/kuaidi";

    public static void main(String[] args) throws Exception{
        String expressNo = "YT4335650262245";
        expressNo = "557001208212378";
        getExpressCompany(expressNo);
    }

    public static String getExpressCompany(String expressNo) throws BusinessException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackno", expressNo);
        String json = HttpClientUtil.post(DOMAIN, parameters);
        JSONObject jsonObject = null;
        jsonObject = new JSONObject().parseObject(json);
        System.out.println("====jsonObject=" + jsonObject);
        if (jsonObject != null && jsonObject.getInteger("code") == 200) {
            String companyName = jsonObject.getJSONObject("data").getJSONObject("meta").getString("company_name");
            System.out.println(companyName);
            return companyName;
        } else {
            ValidateBusinessUtils.assertFalse(true,
                    "err_empty_express_order_id", "请输入正确的物流单号");
        }
        return null;
    }




}
