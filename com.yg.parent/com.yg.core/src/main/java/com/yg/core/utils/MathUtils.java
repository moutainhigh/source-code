package com.yg.core.utils;

import java.math.BigDecimal;

/**
 * math utils
 *
 * @author xulg 2017年1月26日
 */
@SuppressWarnings("WeakerAccess")
public class MathUtils {

    private static final BigDecimal ONE = BigDecimal.ONE;

    /**
     * 精确的四舍五入
     *
     * @param value 被四舍五入的值
     * @param scale 保留的小数位数
     * @return 四舍五入后的结果
     */
    public static double round(double value, int scale) {
        /*
         * Math.round()不能实现精确的四舍五入，
         * BigDecimal的除法运算可以精确计算，且
         * 任何数除以1都等于它本身：num/1 = num
         */
        return BigDecimal.valueOf(value)
                .divide(ONE, scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的四舍五入除法
     *
     * @param a     除数
     * @param b     被除数
     * @param scale 保留的小数位数
     * @return 商
     */
    public static double divide(double a, double b, int scale) {
        return BigDecimal.valueOf(a)
                .divide(BigDecimal.valueOf(b), scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的四舍五入除法
     *
     * @param a     除数
     * @param b     被除数
     * @param scale 保留的小数位数
     * @return 商
     */
    public static double divide(String a, String b, int scale) {
        return new BigDecimal(a)
                .divide(new BigDecimal(b), scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的乘法
     *
     * @param a     乘数
     * @param b     乘数
     * @param scale 保留的小数位数
     * @return 乘积
     */
    public static Double multiply(double a, double b, int scale) {
        return BigDecimal.valueOf(a)
                .multiply(BigDecimal.valueOf(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的乘法
     *
     * @param a     乘数
     * @param b     乘数
     * @param scale 保留的小数位数
     * @return 乘积
     */
    public static double multiply(String a, String b, int scale) {
        return new BigDecimal(a)
                .multiply(new BigDecimal(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的乘法
     *
     * @param a     乘数
     * @param b     乘数
     * @param scale 保留的小数位数
     * @return the big decimal result
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b, int scale) {
        return a.multiply(b).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确的减法
     *
     * @param a     减数
     * @param b     被减数
     * @param scale 保留小数
     * @return result
     */
    public static double subtract(double a, double b, int scale) {
        return BigDecimal.valueOf(a)
                .subtract(BigDecimal.valueOf(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的减法
     *
     * @param a     减数
     * @param b     被减数
     * @param scale 保留小数
     * @return result
     */
    public static double subtract(String a, String b, int scale) {
        return new BigDecimal(a)
                .subtract(new BigDecimal(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的加法
     *
     * @param a     加数
     * @param b     加数
     * @param scale 保留小数
     * @return result
     */
    public static double add(double a, double b, int scale) {
        return BigDecimal.valueOf(a)
                .add(BigDecimal.valueOf(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的加法
     *
     * @param a     加数
     * @param b     加数
     * @param scale 保留小数
     * @return result
     */
    public static double add(String a, String b, int scale) {
        return new BigDecimal(a)
                .add(new BigDecimal(b))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 精确的加法
     *
     * @param a     加数
     * @param b     加数
     * @param scale 保留小数
     * @return the big decimal result
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b, int scale) {
        return a.add(b).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(Math.round(1.237323));
        double v = MathUtils.round(1.237323, 2);
        System.out.println(v);
        double divide = MathUtils.divide(2, 3, 4);
        System.err.println(divide);
        double value = MathUtils.multiply(1.321, 4.657, 3);
        System.out.println(1.321 * 4.657);
        System.out.println(value);
    }

}
