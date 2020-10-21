package com.yd.service.dao.visitor;

import java.util.List;
import com.yd.service.bean.visitor.YdVisitorLog;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:接口访问记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-09 19:17:07
 * @Version:1.1.0
 */
public interface YdVisitorLogDao {

	/**
	 * 通过id得到接口访问记录YdVisitorLog
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdVisitorLog getYdVisitorLogById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydVisitorLog
	 * @return 
	 * @Description:
	 */
	public int getYdVisitorLogCount(YdVisitorLog ydVisitorLog);

	/**
	 * 获取时间段内访问用户数量
	 * @param ydVisitorLog
	 * @return
	 * @Description:
	 */
	public int getUserVisitorCount(YdVisitorLog ydVisitorLog);

	/**
	 * 获取商品浏览数量
	 * @param ydVisitorLog
	 * @return
	 * @Description:
	 */
	public int getItemVisitorCount(YdVisitorLog ydVisitorLog);
	
	/**
	 * 分页获取数据
	 * @param ydVisitorLog
	 * @return 
	 * @Description:
	 */
	public List<YdVisitorLog> findYdVisitorLogListByPage(@Param("params") YdVisitorLog ydVisitorLog,
                                                         @Param("pageStart") Integer pageStart,
                                                         @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有接口访问记录YdVisitorLog
	 * @param ydVisitorLog
	 * @return 
	 * @Description:
	 */
	public List<YdVisitorLog> getAll(YdVisitorLog ydVisitorLog);

	/**
	 * 添加接口访问记录YdVisitorLog
	 * @param ydVisitorLog
	 * @Description:
	 */
	public void insertYdVisitorLog(YdVisitorLog ydVisitorLog);
	
	/**
	 * 通过id修改接口访问记录YdVisitorLog
	 * @param ydVisitorLog
	 * @Description:
	 */
	public void updateYdVisitorLog(YdVisitorLog ydVisitorLog);

}
