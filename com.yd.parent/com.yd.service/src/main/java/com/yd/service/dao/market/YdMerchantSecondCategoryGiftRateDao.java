package com.yd.service.dao.market;

import java.util.List;
import java.util.Set;

import com.yd.service.bean.market.YdMerchantSecondCategoryGiftRate;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店二级分类礼品占比Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:52:52
 * @Version:1.1.0
 */
public interface YdMerchantSecondCategoryGiftRateDao {

	/**
	 * 通过id得到门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantSecondCategoryGiftRate getYdMerchantSecondCategoryGiftRateById(Integer id);

	/**
	 * 通过secondCategoryId得到门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param secondCategoryId
	 * @return
	 * @Description:
	 */
	public YdMerchantSecondCategoryGiftRate getGiftRateBySecondCategoryId(Integer secondCategoryId);
	
	/**
	 * 获取数量
	 * @param ydMerchantSecondCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantSecondCategoryGiftRateCount(YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantSecondCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantSecondCategoryGiftRate> findYdMerchantSecondCategoryGiftRateListByPage(@Param("params") YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate,
                                                                                                 @Param("pageStart") Integer pageStart,
                                                                                                 @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantSecondCategoryGiftRate> getAll(YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate);

	/**
	 * 添加门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRate
	 * @Description:
	 */
	public void insertYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate);
	
	/**
	 * 通过id修改门店二级分类礼品占比YdMerchantSecondCategoryGiftRate
	 * @param ydMerchantSecondCategoryGiftRate
	 * @Description:
	 */
	public void updateYdMerchantSecondCategoryGiftRate(YdMerchantSecondCategoryGiftRate ydMerchantSecondCategoryGiftRate);


	/**
	 * 查询二级分类占比
	 */
	public List<YdMerchantSecondCategoryGiftRate> findSecondCategoryRateByPage(@Param("merchantId") Integer merchantId,
																			   @Param("firstCategoryId") Integer firstCategoryId,
																			   @Param("pageStart") Integer pageStart,
																			   @Param("pageSize") Integer pageSize);

	public Integer getSecondCategoryRateCount(@Param("merchantId") Integer merchantId,
											  @Param("firstCategoryId") Integer firstCategoryId);


	/**
	 * 查询二级分类占比
	 * @param merchantId
	 * @param secondCategoryIdSet
	 * @return
	 */
	List<YdMerchantSecondCategoryGiftRate> findListBySecondCategoryIdList(@Param("merchantId") Integer merchantId,
																		  @Param("list") Set<Integer> secondCategoryIdSet);
}
