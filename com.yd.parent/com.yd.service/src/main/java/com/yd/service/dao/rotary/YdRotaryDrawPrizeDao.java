package com.yd.service.dao.rotary;

import com.yd.service.bean.rotary.YdRotaryDrawPrize;

import java.util.List;

/**
 * @Title:优度转盘抽奖奖品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:36:56
 * @Version:1.1.0
 */
public interface YdRotaryDrawPrizeDao {

	/**
	 * 通过id得到优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawPrize getYdRotaryDrawPrizeById(Integer id) ;

	/**
	 * 得到所有优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param YdRotaryDrawPrize
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawPrize> getAll(YdRotaryDrawPrize YdRotaryDrawPrize) ;

	/**
	 * 添加优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param YdRotaryDrawPrize
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawPrize(YdRotaryDrawPrize YdRotaryDrawPrize) ;

	/**
	 * 通过id修改优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param YdRotaryDrawPrize
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawPrize(YdRotaryDrawPrize YdRotaryDrawPrize) ;

}
