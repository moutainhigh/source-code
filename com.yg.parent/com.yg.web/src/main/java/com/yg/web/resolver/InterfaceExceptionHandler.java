package com.yg.web.resolver;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InterfaceExceptionHandler {
 
 
	/**
	 * 拦截所有运行时的全局异常   
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public String runtimeException(RuntimeException e) {
		// 返回 JOSN
		e.printStackTrace();
		return null;
	}
 
	/**
	 * 系统异常捕获处理
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String exception(Exception e) {
		e.printStackTrace();
		return null;
	}
}