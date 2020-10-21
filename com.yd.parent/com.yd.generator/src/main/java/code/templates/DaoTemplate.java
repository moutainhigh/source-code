package code.templates;

import java.util.List;

import org.apache.velocity.VelocityContext;

import code.util.JavaBeansUtil;
import code.util.PropertiesHelp;
import code.util.TableColum;


public class DaoTemplate extends Template {
	
	@Override
	public void setContent(String tableName, List<TableColum> colums, VelocityContext context) {
		context.put("className", JavaBeansUtil.getCamelCaseString(tableName, true));
		context.put("beanPackage", PropertiesHelp.getProperty("package"));
		context.put("reqPackage", PropertiesHelp.getProperty("req"));
	}
	
	@Override
	public String getFileName(String tableName) {
		return JavaBeansUtil.getCamelCaseString(tableName, true) + "Dao.java";
	}
	
	@Override
	public String getSavePackage() {
		return PropertiesHelp.getProperty("dao");
	}

	
}
