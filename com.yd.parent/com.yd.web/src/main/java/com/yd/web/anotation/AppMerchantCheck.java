package com.yd.web.anotation;

import com.yd.api.enums.EnumSiteGroup;

import java.lang.annotation.*;

@Target({ElementType.METHOD ,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppMerchantCheck {

    String alias() default "";

    String parent() default "";

    String name() default "";//权限名称
    
    EnumSiteGroup groupCode() default EnumSiteGroup.UNKNOW ;
	
}