package com.yd.web.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yd.api.enums.EnumSiteGroup;

@Target({ElementType.METHOD ,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MerchantCheck {

    String alias() default "";

    String parent() default "";

    String name() default "";//权限名称
    
    EnumSiteGroup groupCode() default EnumSiteGroup.UNKNOW ;
	
}