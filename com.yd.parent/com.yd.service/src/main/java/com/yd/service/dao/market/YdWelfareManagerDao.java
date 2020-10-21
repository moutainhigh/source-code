package com.yd.service.dao.market;

import java.util.List;
import com.yd.service.bean.market.YdWelfareManager;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:福利管理Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:20:37
 * @Version:1.1.0
 */
public interface YdWelfareManagerDao {

	/**
	 * 通过id得到福利管理YdWelfareManager
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdWelfareManager getYdWelfareManagerById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydWelfareManager
	 * @return 
	 * @Description:
	 */
	public int getYdWelfareManagerCount(YdWelfareManager ydWelfareManager);
	
	/**
	 * 分页获取数据
	 * @param ydWelfareManager
	 * @return 
	 * @Description:
	 */
	public List<YdWelfareManager> findYdWelfareManagerListByPage(@Param("params") YdWelfareManager ydWelfareManager,
                                                                 @Param("pageStart") Integer pageStart,
                                                                 @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有福利管理YdWelfareManager
	 * @param ydWelfareManager
	 * @return 
	 * @Description:
	 */
	public List<YdWelfareManager> getAll(YdWelfareManager ydWelfareManager);

	/**
	 * 添加福利管理YdWelfareManager
	 * @param ydWelfareManager
	 * @Description:
	 */
	public void insertYdWelfareManager(YdWelfareManager ydWelfareManager);
	
	/**
	 * 通过id修改福利管理YdWelfareManager
	 * @param ydWelfareManager
	 * @Description:
	 */
	public void updateYdWelfareManager(YdWelfareManager ydWelfareManager);

	/**
	 * 通过id修改福利管理YdWelfareManager
	 * @param id
	 * @Description:
	 */
	public void deleteYdWelfareManagerById(Integer id);

    void deleteYdWelfareManager();
}
