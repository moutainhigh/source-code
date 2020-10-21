package code.util;

import java.io.Serializable;

/**
 *                       
 * @Filename TableColum.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author fred
 *
 * @Email kuci@mbaobao.com
 *       
 * @History
 *<li>Author: fred</li>
 *<li>Date: 2014-1-20</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class TableColum implements Serializable {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -4931787771785246895L;
	
	private String				type;
	
	private String				name;
	
	private String				desc;
	
	private String				methodName;
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return String.format("TableColum [type=%s, name=%s, desc=%s, methodName=%s]", type, name,
			desc, methodName);
	}
	
}
