package com.yd.service.dao.rotary;

import java.util.List;

import com.yd.service.bean.rotary.YdRotaryDrawActivity;

/**
 * @Title:转盘抽奖活动Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:52:51
 * @Version:1.1.0
 */
public interface YdRotaryDrawActivityDao {

	/**
	 * 通过id得到转盘抽奖活动YdRotaryDrawActivity
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawActivity getYdRotaryDrawActivityById(Integer id);

	/**
	 * 通过uuid得到转盘抽奖活动YdRotaryDrawActivity
	 * @param uuid
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawActivity getYdRotaryDrawActivityByUuid(String uuid);

	/**
	 * 得到所有转盘抽奖活动YdRotaryDrawActivity
	 * @param ydRotaryDrawActivity
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawActivity> getAll(YdRotaryDrawActivity ydRotaryDrawActivity);

	/**
	 * 添加转盘抽奖活动YdRotaryDrawActivity
	 * @param ydRotaryDrawActivity
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawActivity(YdRotaryDrawActivity ydRotaryDrawActivity);
	

	/**
	 * 通过id修改转盘抽奖活动YdRotaryDrawActivity
	 * @param ydRotaryDrawActivity
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawActivity(YdRotaryDrawActivity ydRotaryDrawActivity);

}
