package com.yg.service.dao.login;

import com.yg.service.bean.login.YgLoginSession;
import java.util.List;

/**
 * @Title:登录sessionDao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 09:49:07
 * @Version:1.1.0
 */
public interface YgLoginSessionDao {

	/**
	 * 通过id得到登录sessionYdLoginSession
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YgLoginSession getYgLoginSessionById(Integer id);

	/**
	 * 得到所有登录sessionYdLoginSession
	 * @param ygLoginSession
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YgLoginSession> getAll(YgLoginSession ygLoginSession);

	/**
	 * 添加登录sessionYdLoginSession
	 * @param ygLoginSession
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdLoginSession(YgLoginSession ygLoginSession);

	/**
	 * 通过id修改登录sessionYdLoginSession
	 * @param ygLoginSession
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdLoginSession(YgLoginSession ygLoginSession);


}
