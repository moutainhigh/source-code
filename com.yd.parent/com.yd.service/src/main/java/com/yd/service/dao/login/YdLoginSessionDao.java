package com.yd.service.dao.login;

import com.yd.service.bean.login.YdLoginSession;

import java.util.List;


/**
 * @Title:登录sessionDao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 09:49:07
 * @Version:1.1.0
 */
public interface YdLoginSessionDao {

	/**
	 * 通过id得到登录sessionYdLoginSession
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdLoginSession getYdLoginSessionById(Integer id);

	/**
	 * 得到所有登录sessionYdLoginSession
	 * @param ydLoginSession
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdLoginSession> getAll(YdLoginSession ydLoginSession);

	/**
	 * 添加登录sessionYdLoginSession
	 * @param ydLoginSession
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdLoginSession(YdLoginSession ydLoginSession);

	/**
	 * 通过id修改登录sessionYdLoginSession
	 * @param ydLoginSession
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdLoginSession(YdLoginSession ydLoginSession);


}
