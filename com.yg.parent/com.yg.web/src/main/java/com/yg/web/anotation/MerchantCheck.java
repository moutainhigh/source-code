package com.yg.web.anotation;

import com.yg.api.enums.YgSiteGroupEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD ,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MerchantCheck {

    String alias() default "";

    String parent() default "";

    String name() default "";//权限名称

    YgSiteGroupEnum groupCode() default YgSiteGroupEnum.UNKNOW ;
    
}