package com.yd.service.dao.user;

import java.util.List;
import com.yd.service.bean.user.YdUserAuth;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户授权表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 10:59:46
 * @Version:1.1.0
 */
public interface YdUserAuthDao {

	/**
	 * 通过id得到用户授权表YdUserAuth
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserAuth getYdUserAuthById(Integer id);

	public YdUserAuth getYdUserAuthByUserId(Integer userId);

	/**
	 * 通过openId得到用户授权表YdUserAuth
	 * @param openId
	 * @return
	 * @Description:
	 */
	public YdUserAuth getYdUserAuthByOpenId(String openId);

	/**
	 * 获取数量
	 * @param YdUserAuth
	 * @return 
	 * @Description:
	 */
	public int getYdUserAuthCount(YdUserAuth YdUserAuth);
	
	/**
	 * 分页获取数据
	 * @param YdUserAuth
	 * @return 
	 * @Description:
	 */
	public List<YdUserAuth> findYdUserAuthListByPage(@Param("params") YdUserAuth YdUserAuth,
                                                     @Param("pageStart") Integer pageStart,
                                                     @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有用户授权表YdUserAuth
	 * @param YdUserAuth
	 * @return 
	 * @Description:
	 */
	public List<YdUserAuth> getAll(YdUserAuth YdUserAuth);

	/**
	 * 添加用户授权表YdUserAuth
	 * @param YdUserAuth
	 * @Description:
	 */
	public void insertYdUserAuth(YdUserAuth YdUserAuth);
	
	/**
	 * 通过id修改用户授权表YdUserAuth
	 * @param YdUserAuth
	 * @Description:
	 */
	public void updateYdUserAuth(YdUserAuth YdUserAuth);

}
