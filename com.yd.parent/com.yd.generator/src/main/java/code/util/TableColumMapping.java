package code.util;

import java.io.Serializable;
import java.util.List;

/**
 *                       
 * @Filename TableColumMapping.java
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
public class TableColumMapping implements Serializable {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -6236026819206091723L;
	
	private String				tableName;
	
	private String				tableNameRemak;
	
	private String				methodName;
	
	private List<TableColum>	searchColums;
	
	private List<TableColum>	listColums;
	
	private List<TableColum>	addColums;
	
	private List<TableColum>	updateColums;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableNameRemak() {
		return tableNameRemak;
	}
	
	public void setTableNameRemak(String tableNameRemak) {
		this.tableNameRemak = tableNameRemak;
	}
	
	public List<TableColum> getSearchColums() {
		return searchColums;
	}
	
	public void setSearchColums(List<TableColum> searchColums) {
		this.searchColums = searchColums;
	}
	
	public List<TableColum> getListColums() {
		return listColums;
	}
	
	public void setListColums(List<TableColum> listColums) {
		this.listColums = listColums;
	}
	
	public List<TableColum> getAddColums() {
		return addColums;
	}
	
	public void setAddColums(List<TableColum> addColums) {
		this.addColums = addColums;
	}
	
	public List<TableColum> getUpdateColums() {
		return updateColums;
	}
	
	public void setUpdateColums(List<TableColum> updateColums) {
		this.updateColums = updateColums;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	@Override
	public String toString() {
		return String
			.format(
				"TableColumMapping [tableName=%s, tableNameRemak=%s, methodName=%s, searchColums=%s, listColums=%s, addColums=%s, updateColums=%s]",
				tableName, tableNameRemak, methodName, searchColums, listColums, addColums,
				updateColums);
	}
	
}
