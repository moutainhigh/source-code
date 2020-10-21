package com.yd.core.utils;

import com.yd.core.enums.YdOrderNoTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 订单号，流水号工具类
 * @author wuyc
 * created 2019/12/4 9:53
 **/
public class OrderNoUtils {

    private static final String PRODUCT_NO = "1";

    private static final int RANDOM_LENGTH = 4;

    private static final String getNowNumberStr = "yyyyMMddHHmmss";

    /**
     * 产品号 + 订单类型 + 年月日时分秒 + 随机4为数字
     * @return
     */
    public static String getOrderNo(YdOrderNoTypeEnum ydOrderNoTypeEnum) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(getNowNumberStr);
        String nowNumberStr = formatter.format(cal.getTime());
        System.out.println(nowNumberStr);
        return PRODUCT_NO + ydOrderNoTypeEnum.getCode() + nowNumberStr + RandomUtils.randNumber(4);
    }

    public static void main(String[] args) {
        // getOrderNo();
    }
}
