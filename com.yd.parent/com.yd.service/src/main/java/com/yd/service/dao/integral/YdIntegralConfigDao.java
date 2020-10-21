package com.yd.service.dao.integral;

import java.util.List;
import com.yd.service.bean.integral.YdIntegralConfig;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:积分配置Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:17:05
 * @Version:1.1.0
 */
public interface YdIntegralConfigDao {

	/**
	 * 通过id得到积分配置YdIntegralConfig
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralConfig getYdIntegralConfigById(Integer id);

	/**
	 * 得到所有积分配置YdIntegralConfig
	 * @param ydIntegralConfig
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralConfig> getAll(YdIntegralConfig ydIntegralConfig);

	/**
	 * 添加积分配置YdIntegralConfig
	 * @param ydIntegralConfig
	 * @Description:
	 */
	public void insertYdIntegralConfig(YdIntegralConfig ydIntegralConfig);

	/**
	 * 通过id修改积分配置YdIntegralConfig
	 * @param ydIntegralConfig
	 * @Description:
	 */
	public void updateYdIntegralConfig(YdIntegralConfig ydIntegralConfig);

}
