package com.yd.service.dao.user;

import java.util.List;
import com.yd.service.bean.user.YdUserTransStatistic;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户交易数据统计Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-07 17:05:25
 * @Version:1.1.0
 */
public interface YdUserTransStatisticDao {

	/**
	 * 通过id得到用户交易数据统计YdUserTransStatistic
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserTransStatistic getYdUserTransStatisticById(Integer id);

	int getUserTransStatisticCount(@Param("merchantId") Integer merchantId,
								   @Param("mobile") String mobile,
								   @Param("nickname") String nickname,
								   @Param("startTime") String startTime,
								   @Param("endTime") String endTime,
								   @Param("minBuyNum") Integer minBuyNum,
								   @Param("maxBuyNum") Integer maxBuyNum,
								   @Param("minTransAmount") Double minTransAmount,
								   @Param("maxTransAmount") Double maxTransAmount);

	List<YdUserTransStatistic> findUserTransStatisticListByPage(@Param("merchantId") Integer merchantId,
																@Param("mobile") String mobile,
																@Param("nickname") String nickname,
																@Param("startTime") String startTime,
																@Param("endTime") String endTime,
																@Param("minBuyNum") Integer minBuyNum,
																@Param("maxBuyNum") Integer maxBuyNum,
																@Param("minTransAmount") Double minTransAmount,
																@Param("maxTransAmount") Double maxTransAmount,
																@Param("pageStart") Integer pageStart,
																@Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有用户交易数据统计YdUserTransStatistic
	 * @param ydUserTransStatistic
	 * @return 
	 * @Description:
	 */
	public List<YdUserTransStatistic> getAll(YdUserTransStatistic ydUserTransStatistic);

	/**
	 * 添加用户交易数据统计YdUserTransStatistic
	 * @param ydUserTransStatistic
	 * @Description:
	 */
	public void insertYdUserTransStatistic(YdUserTransStatistic ydUserTransStatistic);
	

	/**
	 * 通过id修改用户交易数据统计YdUserTransStatistic
	 * @param ydUserTransStatistic
	 * @Description:
	 */
	public void updateYdUserTransStatistic(YdUserTransStatistic ydUserTransStatistic);

}
