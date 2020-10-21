package com.yd.service.dao.member;

import java.util.List;
import com.yd.service.bean.member.YdMemberLevelConfig;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:优度会员配置Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:33:56
 * @Version:1.1.0
 */
public interface YdMemberLevelConfigDao {

	/**
	 * 通过id得到优度会员配置YdMemberLevelConfig
	 *
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMemberLevelConfig getYdMemberLevelConfigById(Integer id);

	public YdMemberLevelConfig getYdMemberLevelConfigByMemberLevel(Integer memberLevel);

	/**
	 * 获取数量
	 *
	 * @param ydMemberLevelConfig
	 * @return
	 * @Description:
	 */
	public int getYdMemberLevelConfigCount(YdMemberLevelConfig ydMemberLevelConfig);

	/**
	 * 分页获取数据
	 *
	 * @param ydMemberLevelConfig
	 * @return
	 * @Description:
	 */
	public List<YdMemberLevelConfig> findYdMemberLevelConfigListByPage(@Param("params") YdMemberLevelConfig ydMemberLevelConfig,
																	   @Param("pageStart") Integer pageStart,
																	   @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有优度会员配置YdMemberLevelConfig
	 *
	 * @param ydMemberLevelConfig
	 * @return
	 * @Description:
	 */
	public List<YdMemberLevelConfig> getAll(YdMemberLevelConfig ydMemberLevelConfig);


	/**
	 * 添加优度会员配置YdMemberLevelConfig
	 *
	 * @param ydMemberLevelConfig
	 * @Description:
	 */
	public void insertYdMemberLevelConfig(YdMemberLevelConfig ydMemberLevelConfig);

	/**
	 * 通过id修改优度会员配置YdMemberLevelConfig
	 *
	 * @param ydMemberLevelConfig
	 * @Description:
	 */
	public void updateYdMemberLevelConfig(YdMemberLevelConfig ydMemberLevelConfig);
}
