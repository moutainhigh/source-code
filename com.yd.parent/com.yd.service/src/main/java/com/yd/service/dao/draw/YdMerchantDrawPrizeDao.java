package com.yd.service.dao.draw;

import java.util.List;
import com.yd.service.bean.draw.YdMerchantDrawPrize;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户抽奖活动奖品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:34:22
 * @Version:1.1.0
 */
public interface YdMerchantDrawPrizeDao {

	/**
	 * 通过id得到商户抽奖活动奖品YdMerchantDrawPrize
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantDrawPrize getYdMerchantDrawPrizeById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantDrawPrize
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantDrawPrizeCount(YdMerchantDrawPrize ydMerchantDrawPrize);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantDrawPrize
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawPrize> findYdMerchantDrawPrizeListByPage(@Param("params") YdMerchantDrawPrize ydMerchantDrawPrize,
                                                                       @Param("pageStart") Integer pageStart,
                                                                       @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrize
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawPrize> getAll(YdMerchantDrawPrize ydMerchantDrawPrize);


	/**
	 * 添加商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrize
	 * @Description:
	 */
	public void insertYdMerchantDrawPrize(YdMerchantDrawPrize ydMerchantDrawPrize);

	/**
	 * 通过id修改商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrize
	 * @Description:
	 */
	public void updateYdMerchantDrawPrize(YdMerchantDrawPrize ydMerchantDrawPrize);


    void deleteYdMerchantDrawPrize(@Param("merchantId") Integer merchantId, @Param("activityId") Integer activityId);
}
