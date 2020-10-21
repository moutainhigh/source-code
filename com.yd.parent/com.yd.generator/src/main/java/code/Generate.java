package code;

import java.util.List;

import code.templates.BeanTemplate;
import code.templates.ConvertTemplate;
import code.templates.DaoTemplate;
import code.templates.ReqTemplate;
import code.templates.ResultTemplate;
import code.templates.SqlMapperTemplate;
import code.templates.Template;
import code.util.DBConn;
import code.util.TableColum;


public class Generate {
	
	private static Template[]	templates	= new Template[] { new ResultTemplate(),
			new BeanTemplate(), new DaoTemplate(), new SqlMapperTemplate(), new ConvertTemplate(),new ReqTemplate() };
	
	public static void main(String[] args) {
		List<String> names = DBConn.getTableNamesByDBName();
//		for (String tableName : names) {
//			if(tableName.contains("es_walk")&&!"es_walk_count".equals(tableName) && !"es_walk_".equals(tableName)) {
//				//System.out.println(tableName);
//				
//				String sql="insert into tt  select sum(dayWalk),sum(dayLength) from "+tableName+" ;";
//				System.out.println(sql);
//			}
//		}
		
		String tablename="test";
		List<TableColum> colum = DBConn.getColumnsNamesByTableName(tablename);
		System.out.println("处理【" + tablename + "】");
		for (Template tmp : templates) {//生成文件
			tmp.gen(tablename, colum);
		}
		
				
		
	}
	
}
