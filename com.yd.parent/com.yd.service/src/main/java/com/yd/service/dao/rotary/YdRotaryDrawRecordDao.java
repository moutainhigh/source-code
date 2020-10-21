package com.yd.service.dao.rotary;

import java.util.List;

import com.yd.service.bean.rotary.YdRotaryDrawRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:转盘抽奖记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:47:52
 * @Version:1.1.0
 */
public interface YdRotaryDrawRecordDao {

	/**
	 * 通过id得到转盘抽奖记录YdRotaryDrawRecord
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdRotaryDrawRecord getYdRotaryDrawRecordById(Integer id);

	/**
	 * 得到所有转盘抽奖记录YdRotaryDrawRecord
	 * @param YdRotaryDrawRecord
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdRotaryDrawRecord> getAll(YdRotaryDrawRecord YdRotaryDrawRecord);

	/**
	 * 添加转盘抽奖记录YdRotaryDrawRecord
	 * @param YdRotaryDrawRecord
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdRotaryDrawRecord(YdRotaryDrawRecord YdRotaryDrawRecord);

	/**
	 * 通过id修改转盘抽奖记录YdRotaryDrawRecord
	 * @param YdRotaryDrawRecord
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdRotaryDrawRecord(YdRotaryDrawRecord YdRotaryDrawRecord);

	List<YdRotaryDrawRecord> findUserDrawRecordList(@Param("userId") Integer userId,
											   @Param("activityId") Integer activityId,
											   @Param("startTime") String startTime,
											   @Param("endTime") String endTime);
}
