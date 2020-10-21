package com.yd.core.utils;

import java.util.UUID;

/**
 * @author Xulg
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
