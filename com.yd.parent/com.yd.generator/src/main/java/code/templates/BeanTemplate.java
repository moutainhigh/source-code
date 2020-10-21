
package code.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import code.util.JavaBeansUtil;
import code.util.PropertiesHelp;
import code.util.TableColum;


public class BeanTemplate extends Template {
	
	@Override
	public void setContent(String tableName, List<TableColum> colums, VelocityContext context) {
		context.put("className", JavaBeansUtil.getCamelCaseString(tableName, true));
		
		List<Map<String, String>> fileds = new ArrayList<Map<String, String>>();
		for (TableColum colum : colums) {
			fileds.add(convertJavaBean(colum));
		}
		context.put("fields", fileds);
		
	}
	
	@Override
	public String getSavePackage() {
		return PropertiesHelp.getProperty("package");
	}
	
	@Override
	public String getFileName(String tableName) {
		return JavaBeansUtil.getCamelCaseString(tableName, true) + ".java";
		
	}
}
