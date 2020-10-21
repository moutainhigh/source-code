package com.yg.core.constants;

import com.alibaba.fastjson.JSON;

public class SystemPrefixConstants {

    /** 用户下单支付 */
    public static final String YD_ORDER_PREFIX = "ydOrder-";

    /** 商户后台充值 */
    public static final String YD_RECHARGE_PREFIX = "ydRecharge-";

    /** 用户在app扫码支付 */
    public static final String YD_APP_QR_CODE_PREFIX = "ydAppQrCode-";


    public static void main(String[] args) {
        String outOrderId = "ydAppQrCode-141";
        // String[] split = outOrderId.split(SystemPrefixConstants.YD_ORDER_PREFIX);
        String[] split = outOrderId.split(SystemPrefixConstants.YD_APP_QR_CODE_PREFIX);
        System.out.println("====充值订单回调截取后的值=" + JSON.toJSONString(split));
        System.out.println("====id=" + split[split.length - 1]);
    }

}
