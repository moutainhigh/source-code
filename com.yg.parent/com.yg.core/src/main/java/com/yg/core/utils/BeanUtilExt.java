package com.yg.core.utils;

import org.apache.log4j.Logger;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtilExt {

	private static final Logger logger = Logger.getLogger(BeanUtilExt.class);
	/**
	 * copy属性,这个方法只是在同类型对象之间的信息copy. <br>
	 * 
	 * <pre>
	 * &lt;font size=&quot;3&quot; color=&quot;bb&quot;&gt;
	 * 空值处理：
	 *     当dest为空null时，抛出IllegalArgumentException异常
	 *     当source为空null时，抛出IllegalArgumentException异常
	 * 使用示例：
	 *     Foo fo = new Foo();
	 *     fo.setUserName(&quot;Tom&quot;);
	 *     fo.setUserID(&quot;T1001&quot;);
	 *     Foo f1 = new Foo();
	 *     Assert.copyProperties(f1,fo);
	 *     输出：f1的相关属性的值变为&quot;Tom&quot;,&quot;T1001&quot;   
	 * &lt;/font&gt;
	 * </pre>
	 * 
	 * @param dest
	 *            得到属性赋值得对象
	 * @param source
	 *            拷贝得原对象
	 * @throws IllegalArgumentException,
	 *             当dest为空null时
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * 
	 * 该接口可以实现Map对象，和普通Bean之间的相互拷贝
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyProperties(Object dest, Object source) {
		AssertUtil.notNull(dest);
		AssertUtil.notNull(source);
				
		if (source instanceof Map) {
			//如果是Map对象拷贝到bean去
			try {
				org.apache.commons.beanutils.BeanUtils.populate(dest, (Map) source);
			} catch (IllegalAccessException e) {
				logger.error("copy error", e);
			} catch (InvocationTargetException e) {
				logger.error("copy error", e);
			}
		} else {
			Class sourceClazz = source.getClass();
			
			Class destClazz = dest.getClass();
			//使用cglib的拷贝，目前只有针对相同对象，下的相同类型属性
			BeanCopier copier = BeanCopier.create(sourceClazz, destClazz, false);
			
			copier.copy(source, dest, null);
		}
	}

	/**
	 * 将对象的属性值拷贝到Map对象中. <br>
	 * 
	 * <pre>
	 * &lt;font size=&quot;3&quot; color=&quot;bb&quot;&gt;
	 * 空值处理：
	 *     当map为空null时，抛出IllegalArgumentException异常
	 *     当source为空null时，抛出IllegalArgumentException异常
	 * 使用示例：
	 *     Foo fo = new Foo();
	 *     fo.setUserName(&quot;Tom&quot;);
	 *     fo.setUserID(&quot;T1001&quot;); 
	 *     Map map = new HashMap();
	 *     Assert.copyProperties(map,fo); 
	 *     输出：key      － value
	 *     		userName － “Tom”
	 *          userID   － “T1001”   
	 * /font&gt;
	 * </pre>
	 * 
	 * @param map
	 *            拷贝的Map
	 * @param source
	 *            原对象
	 * @throws Exception 
	 * @throws IllegalArgumentException,
	 *             当map为空null时或当source为空null时,如果有重复的数据直接覆盖
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyProperties(Map map, Object source) throws Exception {

		if (source instanceof Map) {
			//如果是Map对象拷贝到bean去
			try {
				org.apache.commons.beanutils.BeanUtils.populate(map, (Map) source);
			} catch (IllegalAccessException e) {
				logger.error("copy error", e);
				throw new Exception("属性拷贝异常!");
			} catch (InvocationTargetException e) {
				logger.error("copy error", e);
				throw new Exception("属性拷贝异常!");
			}
		}
		else{
			BeanMap beanMap = BeanMap.create(source);
			map.putAll(beanMap);
		}


	}


    public static void copyPropertiesNoExp(Map map, Object source){

        if (source instanceof Map) {
            //如果是Map对象拷贝到bean去
            try {
                org.apache.commons.beanutils.BeanUtils.populate(map, (Map) source);
            } catch (IllegalAccessException e) {
                logger.error("copy error", e);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                logger.error("copy error", e);
                e.printStackTrace();
            }
        }
        else{
            BeanMap beanMap = BeanMap.create(source);
            map.putAll(beanMap);
        }

    }
}
