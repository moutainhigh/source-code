package com.yd.service.dao.draw;

import java.util.List;
import com.yd.service.bean.draw.YdUserDrawRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户抽奖记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:35:07
 * @Version:1.1.0
 */
public interface YdUserDrawRecordDao {

	/**
	 * 通过id得到用户抽奖记录YdUserDrawRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserDrawRecord getYdUserDrawRecordById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydUserDrawRecord
	 * @return 
	 * @Description:
	 */
	public int getYdUserDrawRecordCount(YdUserDrawRecord ydUserDrawRecord);
	
	/**
	 * 分页获取数据
	 * @param ydUserDrawRecord
	 * @return 
	 * @Description:
	 */
	public List<YdUserDrawRecord> findYdUserDrawRecordListByPage(@Param("params") YdUserDrawRecord ydUserDrawRecord,
                                                                 @Param("pageStart") Integer pageStart,
                                                                 @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecord
	 * @return 
	 * @Description:
	 */
	public List<YdUserDrawRecord> getAll(YdUserDrawRecord ydUserDrawRecord);

	/**
	 * 添加用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecord
	 * @Description:
	 */
	public void insertYdUserDrawRecord(YdUserDrawRecord ydUserDrawRecord);

	/**
	 * 通过id修改用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecord
	 * @Description:
	 */
	public void updateYdUserDrawRecord(YdUserDrawRecord ydUserDrawRecord);

	public int getYdUserDrawCount(@Param("userId") Integer userId,
								  @Param("merchantId") Integer merchantId,
								  @Param("activityId") Integer activityId,
								  @Param("startTime") String startTime,
								  @Param("endTime") String endTime);

	/**
	 * 查询活动抽奖人数，中奖人数
	 * @param ydUserDrawRecord
	 * @return
	 */
	int getActivityDrawCount(YdUserDrawRecord ydUserDrawRecord);
}
