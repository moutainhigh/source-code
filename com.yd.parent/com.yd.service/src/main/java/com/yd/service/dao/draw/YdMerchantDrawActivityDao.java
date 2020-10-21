package com.yd.service.dao.draw;

import java.util.List;
import com.yd.service.bean.draw.YdMerchantDrawActivity;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户抽奖活动Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:33:33
 * @Version:1.1.0
 */
public interface YdMerchantDrawActivityDao {

	/**
	 * 通过id得到商户抽奖活动YdMerchantDrawActivity
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantDrawActivity getYdMerchantDrawActivityById(Integer id);

	/**
	 * 通过uuid得到商户抽奖活动YdMerchantDrawActivity
	 * @param uuid
	 * @return
	 * @Description:
	 */
	public YdMerchantDrawActivity getYdMerchantDrawActivityByUuid(String uuid);
	
	/**
	 * 获取数量
	 * @param ydMerchantDrawActivity
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantDrawActivityCount(YdMerchantDrawActivity ydMerchantDrawActivity);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantDrawActivity
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawActivity> findYdMerchantDrawActivityListByPage(@Param("params") YdMerchantDrawActivity ydMerchantDrawActivity,
                                                                             @Param("pageStart") Integer pageStart,
                                                                             @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivity
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawActivity> getAll(YdMerchantDrawActivity ydMerchantDrawActivity);


	/**
	 * 添加商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivity
	 * @Description:
	 */
	public void insertYdMerchantDrawActivity(YdMerchantDrawActivity ydMerchantDrawActivity);
	
	/**
	 * 通过id修改商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivity
	 * @Description:
	 */
	public void updateYdMerchantDrawActivity(YdMerchantDrawActivity ydMerchantDrawActivity);
	
}
