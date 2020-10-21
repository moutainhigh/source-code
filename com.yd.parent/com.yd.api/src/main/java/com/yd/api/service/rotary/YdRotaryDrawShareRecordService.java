package com.yd.api.service.rotary;

import com.yd.api.result.rotary.YdRotaryDrawShareRecordResult;

import java.util.List;

/**
 * @Title:转盘抽奖分享记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:53:47
 * @Version:1.1.0
 */
public interface YdRotaryDrawShareRecordService {

	/**
	 * 通过id得到转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawShareRecordResult getYdRotaryDrawShareRecordById(Integer id);

	/**
	 * 得到所有转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param rotaryDrawShareRecordResult
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawShareRecordResult> getAll(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult);

	/**
	 * 添加转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param rotaryDrawShareRecordResult
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawShareRecord(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult);
	
	/**
	 * 通过id修改转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param rotaryDrawShareRecordResult
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawShareRecord(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult);

	
}
