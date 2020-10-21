package com.yg.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;



public class DTOUtils {

	private static final Logger logger = Logger.getLogger(DTOUtils.class);
	private DTOUtils() {}

	public static <T, K> List<K> convertList(List<T> origList, Class<K> clazz) {
		List<K> destList = new ArrayList<K>();
		if (origList == null || origList.size() < 1) {
			return destList;
		}
		logger.debug("Converting list in size of " + origList.size());
		
		try {
			for (T original : origList) {
				K result = clazz.newInstance();
				BeanUtilExt.copyProperties(result, original);
//				BeanUtilExt.copyProperties(original, result);
				destList.add(result);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return destList;
	}
	

}
