package code.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import code.util.JavaBeansUtil;
import code.util.PropertiesHelp;
import code.util.TableColum;


public class SqlMapperTemplate extends Template {
	
	@Override
	public void setContent(String tableName, List<TableColum> colums, VelocityContext context) {
		context.put("className", JavaBeansUtil.getCamelCaseString(tableName, true));
		
		List<Map<String, String>> fileds = new ArrayList<Map<String, String>>();
		for (TableColum colum : colums) {
			fileds.add(_convertJavaBean(colum));
		}
		context.put("fields", fileds);
		context.put("beanPackage", PropertiesHelp.getProperty("package"));
		context.put("daoPackage", PropertiesHelp.getProperty("dao"));
	}
	
	@Override
	public String getFileName(String tableName) {
		return JavaBeansUtil.getCamelCaseString(tableName, true) + "DaoMapper.xml";
	}
	
	@Override
	public String getSavePackage() {
		return "mapper";
	}
	
	private Map<String, String> _convertJavaBean(TableColum colum) {
		String name = JavaBeansUtil.getCamelCaseString(colum.getName(), false);
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("columnName", colum.getName());
		return map;
	}
}
