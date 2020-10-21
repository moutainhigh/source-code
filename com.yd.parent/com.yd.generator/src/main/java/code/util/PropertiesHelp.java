package code.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PropertiesHelp {
	protected static final Logger LOG = LogManager.getLogger(PropertiesHelp.class);
	private static PropertiesHelp instance = new PropertiesHelp();

	/**
	 * 配置文件列表，格式为 (相对于classpath的文件路径, 上次使用的文件修改时间)
	 */
	private Map<String, Long> propFiles = new HashMap<String, Long>();

	private Map<Object, Object> map = null;

	{
		propFiles.put("/properties/appconfig.properties", 0l);
	}

	private long getLastModify(String resourceName) {
		URL url = PropertiesHelp.class.getResource(resourceName);
		if (url == null) return 0;
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
			LOG.warn(e1);
		}
		
		if (LOG.isDebugEnabled()) LOG.debug(uri);
		File file;
		file = new File(uri);
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
			long lm = getLastModify(it.getKey());
			if (LOG.isDebugEnabled()) LOG.debug("up" + updateTime);
			if (LOG.isDebugEnabled()) LOG.debug("lm:" + lm);
			if (updateTime < lm) {
				result = true;
				break;
			}
		}
		
		if (result) LOG.info("配置文件日期改变，需重新加载...");
		if (LOG.isDebugEnabled()) LOG.debug("----------需要重载? " + result);
		return result;
	}
	
	private void reload() {
		InputStream in = null;

		try {
			Map<Object, Object> tm = new HashMap<Object, Object>();
			for (Map.Entry<String, Long> it : propFiles.entrySet()) {
				if (LOG.isDebugEnabled()) LOG.debug("it.getKey: " + it.getKey());

				in = PropertiesHelp.class.getResourceAsStream(it.getKey());
				if (in == null) {
					LOG.error("读配置文件出错，跳过配置更新 : " + it.getKey());
					tm = null;
					break;
				}
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
			if (tm != null) map = tm;
			if (LOG.isDebugEnabled()) {
				LOG.debug(propFiles);
				LOG.debug(map);
//				System.out.println(propFiles);
//				System.out.println(map);
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
		if (instance.needReload()) instance.reload();

		return (String)instance.map.get(key);
	}
	
	  public static void main(String[] args) {
		  
//		  public static void main(String[] args) {
//		System.out.println("sheet.reCalcTime is " + PropertiesHelp.getProperty("sheet.reCalcTime"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("5秒后再看 sheet.reCalcTime is " + PropertiesHelp.getProperty("sheet.reCalcTime"));
//		System.out.println("prop.env.production is " + PropertiesHelp.getProperty("prop.env.production"));
	}
}
