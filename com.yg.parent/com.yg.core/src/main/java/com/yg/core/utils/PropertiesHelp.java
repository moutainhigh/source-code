package com.yg.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesHelp {
	private final static Logger LOG = LoggerFactory.getLogger(PropertiesHelp.class);
	
	private static PropertiesHelp instance = new PropertiesHelp();

	
	/**
	 * 配置文件列表，格式为 (相对于classpath的文件路径, 上次使用的文件修改时间)
	 */
	private Map<String, Long> propFiles = new HashMap<String, Long>();

	private Map<Object, Object> map = new HashMap<Object,Object>();

	{
		propFiles.put("/home/www/web-deploy/deploy/config/ydConfig.properties", 0l);
	}

	private long getLastModify(String filePath) {
		File file = new File(filePath);
		if (LOG.isDebugEnabled()) LOG.debug("lm date:" + new Date(file.lastModified()));
		
		return file.lastModified();
	}
	/**
	 * 检查属性文件上次加载后是否有修改
	 */
	public boolean needReload() {
		boolean result = false;
		
		for (Map.Entry<String, Long> it : propFiles.entrySet()) {
			long updateTime = it.getValue();
			if(updateTime==0) {
				result = true;
				break;
			}
			long lm = getLastModify(it.getKey());
			if (updateTime < lm) {
				result = true;
				break;
			}
		}
		
		if (result) {
			LOG.info("配置文件日期改变，需重新加载...");
		}
		return result;
	}
	
	private void reload() {
		InputStream in = null;
		try {
			Map<Object, Object> tm = new HashMap<Object, Object>();
			for (Map.Entry<String, Long> it : propFiles.entrySet()) {
				File file=new File(it.getKey());
				if(file.exists()) {
					in = new FileInputStream(file);
					Properties props = new Properties();
					props.load(in);
					it.setValue(getLastModify(it.getKey()));
					Set<Object> keys = props.keySet();
					for (Object o : keys) {
						Object t = props.get(o);
						tm.put(o, t);
					}
					in.close();
				}
			}
			if (tm != null) {
				map = tm;
			}
		} catch (IOException e) {
			LOG.error("读取properties失败！" + e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				LOG.error("关闭properties失败！");
			}
		}
	}

	public static String getProperty(String key) {
		// 每次检查是否需要重新加载
		if (instance.needReload()) {
			instance.reload();
		}

		return (String)instance.map.get(key);
	}

	public static String getProperty(String key, String defaultValue) {
		// 每次检查是否需要重新加载
		if (instance.needReload()) {
			instance.reload();
		}
		String value = (String)instance.map.get(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
	
	  public static void main(String[] args) throws InterruptedException {
		  for(int i=0;i<100;i++) {
			  System.out.println(PropertiesHelp.getProperty("cookie.domain"));
			  Thread.sleep(1000);
		  }
	}
}
