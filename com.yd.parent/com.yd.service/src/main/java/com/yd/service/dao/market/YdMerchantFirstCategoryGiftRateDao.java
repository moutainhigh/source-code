package com.yd.service.dao.market;

import java.util.List;
import java.util.Set;

import com.yd.service.bean.market.YdMerchantFirstCategoryGiftRate;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店一级分类礼品占比Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 14:09:12
 * @Version:1.1.0
 */
public interface YdMerchantFirstCategoryGiftRateDao {

	/**
	 * 通过id得到门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantFirstCategoryGiftRate getYdMerchantFirstCategoryGiftRateById(Integer id);

	/**
	 * 通过firstCategoryId得到门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param firstCategoryId
	 * @return
	 * @Description:
	 */
	public YdMerchantFirstCategoryGiftRate getGiftRateByFirstCategoryId(Integer firstCategoryId);
	
	/**
	 * 获取数量
	 * @param ydMerchantFirstCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantFirstCategoryGiftRateCount(YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantFirstCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantFirstCategoryGiftRate> findYdMerchantFirstCategoryGiftRateListByPage(@Param("params") YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate,
                                                                                               @Param("pageStart") Integer pageStart,
                                                                                               @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantFirstCategoryGiftRate> getAll(YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate);

	/**
	 * 添加门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRate
	 * @Description:
	 */
	public void insertYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate);
	
	/**
	 * 通过id修改门店一级分类礼品占比YdMerchantFirstCategoryGiftRate
	 * @param ydMerchantFirstCategoryGiftRate
	 * @Description:
	 */
	public void updateYdMerchantFirstCategoryGiftRate(YdMerchantFirstCategoryGiftRate ydMerchantFirstCategoryGiftRate);

	/**
	 * 查询一级分类设置的礼品占比
	 * @param merchantId
	 * @param firstCategoryIdList
	 * @return
	 */
	List<YdMerchantFirstCategoryGiftRate> findListByFirstCategoryIdList(@Param("merchantId") Integer merchantId,
																		@Param("list") Set<Integer> firstCategoryIdList);


	List<YdMerchantFirstCategoryGiftRate> findFirstCategoryRateListByPage(@Param("merchantId") Integer merchantId,
																		  @Param("pageStart") Integer pageStart,
																		  @Param("pageSize") Integer pageSize);

	Integer getFirstCategoryRateCount(@Param("merchantId") Integer merchantId);
}
