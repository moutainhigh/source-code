package com.yg.web.anotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD ,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginStatus {
	
	
}