package com.yg.core.enums;

import java.util.*;

/**
 * 商户品牌枚举
 * @author wuyc
 * created 2019/10/31 15:29
 **/
public enum YdPlatformBrandEnum {

    PLATFORM_BRAND_VIVO("1", "vivo", "www.baidu.com"),
    PLATFORM_BRAND_OPPO("2", "oppo", "www.baidu.com"),
    PLATFORM_BRAND_APPLE("3", "苹果", "www.baidu.com"),
    PLATFORM_BRAND_SAMSUNG("4", "三星", "www.baidu.com"),
    PLATFORM_BRAND_GLORY("5", "荣耀", "www.baidu.com"),
    PLATFORM_BRAND_MILLET("6", "小米", "www.baidu.com"),
    PLATFORM_BRAND_MEIZU("7", "魅族", "www.baidu.com"),
    PLATFORM_BRAND_LENOVO("8", "联想", "www.baidu.com"),
    PLATFORM_BRAND_NOKIA("9", "诺基亚", "www.baidu.com"),
    PLATFORM_BRAND_SUONI("10", "索尼", "www.baidu.com"),
    PLATFORM_BRAND_MEITU("11", "美图", "www.baidu.com");

    private String	code;

    private String	name;

    private String	imageUrl;

    private YdPlatformBrandEnum(String code, String name, String imageUrl) {
        this.code = code;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static YdPlatformBrandEnum getByCode(String code) {
        for (YdPlatformBrandEnum afe : values()) {
            if (afe.getCode().equals(code)) {
                return afe;
            }
        }
        return null;
    }

    public static Map<String, YdPlatformBrandEnum> getAllPlatformBrandEnum() {
        Map<String, YdPlatformBrandEnum> hashMap = new HashMap<>();
        for (YdPlatformBrandEnum platformBrandEnum : values()) {
            hashMap.put(platformBrandEnum.code, platformBrandEnum);
        }
        return hashMap;
    }

    public static Set<String> getAllPlatformBrandCode() {
        Set<String> codeList = new HashSet<>();
        for (YdPlatformBrandEnum platformBrandEnum : values()) {
            codeList.add(platformBrandEnum.code);
        }
        return codeList;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static void main(String[] args) {
        getAllPlatformBrandEnum();
    }

}
