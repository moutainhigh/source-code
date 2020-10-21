package com.yd.service.dao.user;

import java.util.List;

import com.yd.service.bean.user.YdUser;

/**
 * @Title:用户Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:54:35
 * @Version:1.1.0
 */
public interface YdUserDao {

	/**
	 * 通过id得到用户YdShopUser
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdUser getYdUserById(Integer id) ;

	/**
	 * 通过mobile得到用户YdUser
	 * @param mobile
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public YdUser getYdUserByMobile(String mobile) ;

	/**
	 * 得到所有用户ydUser
	 * @param ydUser
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdUser> getAll(YdUser ydUser) ;

	/**
	 * 添加用户ydUser
	 * @param ydUser
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdUser(YdUser ydUser) ;
	
	/**
	 * 通过id修改用户ydUser
	 * @param ydUser
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdUser(YdUser ydUser) ;

}
