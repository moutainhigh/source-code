package code.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *                       
 * @Filename DBConn.java
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

public class DBConn {
	
	private static final Logger		LOG	= Logger.getLogger(DBConn.class);
	
	public static final Properties	p	= new Properties();
	
	static {
		try {
			p.load(DBConn.class.getResourceAsStream("/properties/appconfig.properties"));
			Class.forName(p.getProperty("jdbc.wrt.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws IOException, SQLException {
		return DriverManager.getConnection(p.getProperty("jdbc.wrt.url"),
			p.getProperty("jdbc.wrt.username"), p.getProperty("jdbc.wrt.password"));
	}
	
	/**
	 * 获取数据库的所有表名称
	 * 
	 */
	public static List<String> getTableNamesByDBName() {
		List<String> list = new ArrayList<String>();
		try {
			Statement stame = (Statement) DBConn.getConnection().createStatement();
			ResultSet rs = stame.executeQuery("show tables;");
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return list;
	}
	
	/**
	 * 根据表名称获取表的所有字段名称
	 * @throws
	 */
	public static List<TableColum> getColumnsNamesByTableName(String tName) {
		List<TableColum> list = new ArrayList<TableColum>();
		try {
			DatabaseMetaData metaData = DBConn.getConnection().getMetaData();
			ResultSet columnSet = metaData.getColumns(null, "%", tName, "%");
			while (columnSet.next()) {
				String columnName = columnSet.getString("COLUMN_NAME");
				String columnComment = columnSet.getString("REMARKS");
				String type = columnSet.getString("TYPE_NAME");
				type = type.replaceAll("\\([^)]+\\)", "");
				
				TableColum colum = new TableColum();
				colum.setName(columnName);
				colum.setDesc(columnComment);
				colum.setType(type.toLowerCase());
				colum.setMethodName(JavaBeansUtil.getCamelCaseString(colum.getName(), false));
				list.add(colum);
			}
			
			return list;
		} catch (Exception e) {
			LOG.error("", e);
		}
		return list;
	}
}
