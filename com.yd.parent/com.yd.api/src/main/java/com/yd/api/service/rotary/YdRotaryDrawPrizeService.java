package com.yd.api.service.rotary;

import com.yd.api.result.rotary.YdRotaryDrawPrizeResult;

import java.util.List;



/**
 * @Title:优度转盘抽奖奖品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:36:56
 * @Version:1.1.0
 */
public interface YdRotaryDrawPrizeService {

	/**
	 * 通过id得到优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawPrizeResult getYdRotaryDrawPrizeById(Integer id) ;

	/**
	 * 得到所有优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param rotaryDrawPrizeResult
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawPrizeResult> getAll(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) ;

	/**
	 * 添加优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param rotaryDrawPrizeResult
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawPrize(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) ;
	
	/**
	 * 通过id修改优度转盘抽奖奖品YdRotaryDrawPrize
	 * @param rotaryDrawPrizeResult
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawPrize(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) ;

}
