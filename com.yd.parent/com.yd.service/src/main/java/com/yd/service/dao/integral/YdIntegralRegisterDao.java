package com.yd.service.dao.integral;

import java.util.List;
import com.yd.service.bean.integral.YdIntegralRegister;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:积分登记Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:42:46
 * @Version:1.1.0
 */
public interface YdIntegralRegisterDao {

	/**
	 * 通过id得到积分登记YdIntegralRegister
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralRegister getYdIntegralRegisterById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydIntegralRegister
	 * @return 
	 * @Description:
	 */
	public int getYdIntegralRegisterCount(YdIntegralRegister ydIntegralRegister);
	
	/**
	 * 分页获取数据
	 * @param ydIntegralRegister
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralRegister> findYdIntegralRegisterListByPage(@Param("params") YdIntegralRegister ydIntegralRegister,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有积分登记YdIntegralRegister
	 * @param ydIntegralRegister
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralRegister> getAll(YdIntegralRegister ydIntegralRegister);

	/**
	 * 添加积分登记YdIntegralRegister
	 * @param ydIntegralRegister
	 * @Description:
	 */
	public void insertYdIntegralRegister(YdIntegralRegister ydIntegralRegister);
	
	/**
	 * 通过id修改积分登记YdIntegralRegister
	 * @param ydIntegralRegister
	 * @Description:
	 */
	public void updateYdIntegralRegister(YdIntegralRegister ydIntegralRegister);
	
}
