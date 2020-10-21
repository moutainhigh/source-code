package code.templates;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import code.util.DateUtils;
import code.util.JavaBeansUtil;
import code.util.TableColum;
import code.util.TableColumnEnum;
import code.util.VelocityUtil;

public abstract class Template {
	
	public void gen(String tableName, List<TableColum> colum) {
		//设置环境变量
		VelocityContext context = new VelocityContext();
		context.put("tableName", tableName);
		context.put("colums", colum);
		context.put("package", getSavePackage());
		context.put("time", DateUtils.dateToTime(new Date()));
		setContent(tableName, colum, context);
		
		//导出文件
		String content = createJavaCode(context);
		String path = this.getClass().getResource(File.separator).getPath();
		path = path.substring(0, path.indexOf("target"));
		path += "src" + File.separator + "main" + File.separator + "java" + File.separator;
		path += getSavePackage().replaceAll("\\.", "\\\\");
		//		System.out.println(path);
		//		path = "/Users/lingmao/tmp/";
		exportFile(path, getFileName(tableName), content);
		
	}
	
	public abstract void setContent(String tableName, List<TableColum> colums,
									VelocityContext context);
	
	public abstract String getFileName(String tableName);
	
	public abstract String getSavePackage();
	
	public String getTemplateName() {
		String className = getClass().getSimpleName().toLowerCase();
		className = className.substring(0, className.indexOf("template"));
		return className;
	}
	
	public String createJavaCode(VelocityContext context) {
		
		try {
			return VelocityUtil.exportContent(getTemplateName() + ".vm", context);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void exportFile(String filePath, String fileName, String source) {
		try {
			System.out.println("\t导出文件:" + filePath);
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			FileWriter writer = new FileWriter(new File(filePath + File.separator + fileName));
			writer.write(source);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException("\t导出文件失败:" + e.getMessage());
		}
	}
	
	public Map<String, String> convertJavaBean(TableColum colum) {
		
		String name = JavaBeansUtil.getCamelCaseString(colum.getName(), false);
		String simpleName = null;
		try {
			simpleName = buildClsName(TableColumnEnum.getByCode(colum.getType().toUpperCase()))
				.getSimpleName();
//			if (colum.getName().startsWith("is")) {
//				simpleName = "Boolean";
//			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(colum.getType());
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("getMethodName", JavaBeansUtil.getGetterMethodName(name, ""));
		map.put("setMethodName", JavaBeansUtil.getSetterMethodName(name));
		map.put("simpleName", simpleName);
		map.put("name", name);
		return map;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private static Class buildClsName(TableColumnEnum columnType) {
		switch (columnType) {
			case DATE:
				return Date.class;
			case DOUBLE:
				return Double.class;
			case DATETIME:
				return Date.class;
			case FLOAT:
				return Float.class;
			case INT:
				return Integer.class;
			case INT_UNSIGNED:
				return Integer.class;
			case INTEGER:
				return Integer.class;
			case TEXT:
				return String.class;
			case TIMESTAMP:
				return Date.class;
			case TINYINT:
				return Integer.class;
			case VARCHAR:
				return String.class;
			case SMALLINT_UNSIGNED:
				return Integer.class;
			case DOUBLE_UNSIGNED:
				return Integer.class;
			case DECIMAL_UNSIGNED:
				return Double.class;
			case TINYINT_UNSIGNED:
				return Integer.class;
			case SMALLINT:
				return Integer.class;
			case BIGINT:
				return Long.class;
			case DECIMAL:
				return Double.class;
			default:
				return String.class;
		}
	}
	
}
