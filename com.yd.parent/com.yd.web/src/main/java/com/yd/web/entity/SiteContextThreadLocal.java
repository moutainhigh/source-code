package com.yd.web.entity;

public class SiteContextThreadLocal {
	
	public static final ThreadLocal<SiteContext>	threadLocal	= new ThreadLocal<SiteContext>() {
																	protected SiteContext initialValue() {
																		return new SiteContext();
																	};
																};
	
	public static void set(SiteContext c) {
		threadLocal.set(c);
	}
	
	public static void reSet() {
		threadLocal.remove();
	}
	
	public static SiteContext get() {
		return threadLocal.get();
	}
	
}
