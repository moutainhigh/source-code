package com.yd.api.pay.util;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class SerializeXmlUtil {

	public static XStream createXstream() {
		return new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;
					Class<?> targetClass = null;

					@Override
					public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
						super.startNode(name, clazz);
						// 业务处理，对于用XStreamCDATA标记的Field，需要加上CDATA标签
						System.out.println("11: "+clazz.getCanonicalName() + " "+isBasicClassOrList(clazz));
                        if(targetClass == null || !isBasicClassOrList(clazz)){
                            targetClass = clazz;
                        }
                        System.out.println(name+" , "+clazz.getCanonicalName()+" , "+targetClass.getCanonicalName()+" , "+name);
                        cdata = needCDATA(targetClass,name);
                        
					}

					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
	}
	
	private static boolean isBasicClassOrList(Class clazz) {
		if(clazz.isPrimitive()) {
			return true;
		}
		if(clazz==List.class) {
			return true;
		}
		if(clazz==String.class) {
			return true;
		}
		if(clazz==Integer.class) {
			return true;
		}
		if(clazz==Long.class) {
			return true;
		}
		
		return false;
	}

	private static boolean needCDATA(Class<?> targetClass, String fieldAlias) {
		boolean cdata = false;
		// first, scan self
		cdata = existsCDATA(targetClass, fieldAlias);
		if (cdata)
			return cdata;
		// if cdata is false, scan supperClass until java.lang.Object
		Class<?> superClass = targetClass.getSuperclass();
		while (!superClass.equals(Object.class)) {
			cdata = existsCDATA(superClass, fieldAlias);
			if (cdata)
				return cdata;
			superClass = superClass.getClass().getSuperclass();
		}
		return false;
	}

	private static boolean existsCDATA(Class<?> clazz, String fieldAlias) {
		if ("MediaId".equals(fieldAlias)) {
			return true; // 特例添加 morning99
		}
		// scan fields
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 1. exists XStreamCDATA
			if (field.getAnnotation(XStreamCDATA.class) != null) {
				XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
				// 2. exists XStreamAlias
				if (null != xStreamAlias) {
					if (fieldAlias.equals(xStreamAlias.value()))// matched
						return true;
				} else {// not exists XStreamAlias
					if (fieldAlias.equals(field.getName()))
						return true;
				}
			}
		}
		return false;
	}

}