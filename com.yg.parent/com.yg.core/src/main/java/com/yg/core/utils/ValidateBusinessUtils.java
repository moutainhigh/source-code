package com.yg.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Xulg
 * Created in 2019-07-29 9:09
 */
public class ValidateBusinessUtils {

    private ValidateBusinessUtils() {
    }

    /**
     * 断言为true
     *
     * @param expression 表达式
     * @param code       异常错误码
     * @param message    异常信息
     */
    public static void assertTrue(boolean expression, String code, String message) {
        if (!expression) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言为false
     *
     * @param expression 表达式
     * @param code       异常错误码
     * @param message    异常信息
     */
    public static void assertFalse(boolean expression, String code, String message) {
        if (expression) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言为空
     *
     * @param target  目标对象
     * @param code    异常错误码
     * @param message 异常信息
     */
    public static void assertNull(Object target, String code, String message) {
        if (target != null) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言非空
     *
     * @param target  目标对象
     * @param code    异常错误码
     * @param message 异常信息
     */
    public static void assertNonNull(Object target, String code, String message) {
        if (target == null) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言字符串非空(null,""," ")
     *
     * @param sequence the string sequence
     * @param code     异常错误码
     * @param message  异常信息
     */
    public static void assertStringNotBlank(CharSequence sequence, String code, String message) {
        if (StringUtils.isBlank(sequence)) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言字符串为空(null,""," ")
     *
     * @param sequence the string sequence
     * @param code     异常错误码
     * @param message  异常信息
     */
    public static void assertStringBlank(CharSequence sequence, String code, String message) {
        if (StringUtils.isNotBlank(sequence)) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言两对象相等
     *
     * @param a       the object a
     * @param b       the object b
     * @param code    异常错误码
     * @param message 异常信息
     */
    public static void assertEquals(Object a, Object b, String code, String message) {
        if (!Objects.equals(a, b)) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言两对象不相等
     *
     * @param a       the object a
     * @param b       the object b
     * @param code    异常错误码
     * @param message 异常信息
     */
    public static void assertNotEquals(Object a, Object b, String code, String message) {
        if (Objects.equals(a, b)) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言集合不能为空
     *
     * @param collection 集合
     * @param code       异常错误码
     * @param message    异常信息
     */
    public static void assertCollectionNotEmpty(Collection collection, String code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw BusinessException.build(code, message);
        }
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param code       异常错误码
     * @param message    异常信息
     */
    public static void assertCollectionEmpty(Collection collection, String code, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw BusinessException.build(code, message);
        }
    }
}
