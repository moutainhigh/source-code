package com.yd.api.service.rotary;

import com.yd.api.result.rotary.YdRotaryDrawRecordResult;

import java.util.List;



/**
 * @Title:转盘抽奖记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:47:52
 * @Version:1.1.0
 */
public interface YdRotaryDrawRecordService {

	public List<YdRotaryDrawRecordResult> findUserDrawRecordList(Integer userId, Integer activityId, String startTime, String endTime);

	/**
	 * 通过id得到转盘抽奖记录YdRotaryDrawRecord
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawRecordResult getYdRotaryDrawRecordById(Integer id) throws Exception;

	/**
	 * 得到所有转盘抽奖记录YdRotaryDrawRecord
	 * @param rotaryDrawRecordResult
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawRecordResult> getAll(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception;


	/**
	 * 添加转盘抽奖记录YdRotaryDrawRecord
	 * @param rotaryDrawRecordResult
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawRecord(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception;
	
	/**
	 * 通过id修改转盘抽奖记录YdRotaryDrawRecord
	 * @param rotaryDrawRecordResult
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawRecord(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception;


}
