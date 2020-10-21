package com.yd.service.dao.rotary;

import com.yd.service.bean.rotary.YdRotaryDrawUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Title:用户Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-18 11:46:36
 * @Version:1.1.0
 */
public interface YdRotaryDrawUserDao {

	/**
	 * 通过id得到用户YdRotaryDrawUser
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawUser getYdRotaryDrawUserById(Integer id);

	/**
	 * 通过id得到用户YdRotaryDrawUser
	 * @param outOrderId
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawUser getYdRotaryDrawUserByOutOrderId(String outOrderId);


	/**
	 * 得到所有用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawUser> getAll(YdRotaryDrawUser ydRotaryDrawUser);

	/**
	 * 添加用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawUser(YdRotaryDrawUser ydRotaryDrawUser);
	
	/**
	 * 通过id修改用户YdRotaryDrawUser
	 * @param ydRotaryDrawUser
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawUser(YdRotaryDrawUser ydRotaryDrawUser);

	List<YdRotaryDrawUser> findUserCanUseDrawList(@Param("userId")Integer userId, @Param("activityId") Integer activityId);

	int getUserCanUseDrawCount(@Param("userId")Integer userId, @Param("activityId") Integer activityId);

	int reduceUserDrawCount(@Param("id")Integer id, @Param("reduceDrawCount" )Integer reduceDrawCount);
}
