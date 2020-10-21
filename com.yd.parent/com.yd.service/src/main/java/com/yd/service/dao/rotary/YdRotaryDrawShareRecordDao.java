package com.yd.service.dao.rotary;

import com.yd.service.bean.rotary.YdRotaryDrawShareRecord;

import java.util.List;

/**
 * @Title:转盘抽奖分享记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:53:47
 * @Version:1.1.0
 */
public interface YdRotaryDrawShareRecordDao {

	/**
	 * 通过id得到转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawShareRecord getYdRotaryDrawShareRecordById(Integer id) throws Exception;

	/**
	 * 得到所有转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param ydRotaryDrawShareRecord
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawShareRecord> getAll(YdRotaryDrawShareRecord ydRotaryDrawShareRecord) throws Exception;

	/**
	 * 添加转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param ydRotaryDrawShareRecord
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawShareRecord(YdRotaryDrawShareRecord ydRotaryDrawShareRecord) throws Exception;
	
	/**
	 * 通过id修改转盘抽奖分享记录YdRotaryDrawShareRecord
	 * @param ydRotaryDrawShareRecord
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawShareRecord(YdRotaryDrawShareRecord ydRotaryDrawShareRecord) throws Exception;

}
