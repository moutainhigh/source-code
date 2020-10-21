package com.yd.service.dao.decoration;

import java.util.List;
import com.yd.service.bean.decoration.YdVrManager;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:店铺vr设置Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:36:46
 * @Version:1.1.0
 */
public interface YdVrManagerDao {

	/**
	 * 通过id得到店铺vr设置YdVrManager
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdVrManager getYdVrManagerById(Integer id);

	/**
	 * 通过merchantId得到店铺vr设置YdVrManager
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdVrManager getYdVrManagerByMerchantId(Integer merchantId);

	/**
	 * 获取数量
	 * @param ydVrManager
	 * @return 
	 * @Description:
	 */
	public int getYdVrManagerCount(YdVrManager ydVrManager);
	
	/**
	 * 分页获取数据
	 * @param ydVrManager
	 * @return 
	 * @Description:
	 */
	public List<YdVrManager> findYdVrManagerListByPage(@Param("params") YdVrManager ydVrManager,
                                                       @Param("pageStart") Integer pageStart,
                                                       @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有店铺vr设置YdVrManager
	 * @param ydVrManager
	 * @return 
	 * @Description:
	 */
	public List<YdVrManager> getAll(YdVrManager ydVrManager);

	/**
	 * 添加店铺vr设置YdVrManager
	 * @param ydVrManager
	 * @Description:
	 */
	public void insertYdVrManager(YdVrManager ydVrManager);

	/**
	 * 通过id修改店铺vr设置YdVrManager
	 * @param ydVrManager
	 * @Description:
	 */
	public void updateYdVrManager(YdVrManager ydVrManager);

	/**
	 * 通过id删除店铺vr设置YdVrManager
	 * @param id
	 * @Description:
	 */
	public void deleteYdVrManagerById(Integer id);
}
