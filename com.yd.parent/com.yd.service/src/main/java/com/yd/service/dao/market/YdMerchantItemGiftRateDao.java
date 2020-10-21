package com.yd.service.dao.market;

import java.util.List;
import java.util.Set;

import com.yd.service.bean.market.YdMerchantItemGiftRate;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店商品礼品占比Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:59
 * @Version:1.1.0
 */
public interface YdMerchantItemGiftRateDao {

	/**
	 * 通过id得到门店商品礼品占比YdMerchantItemGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemGiftRate getYdMerchantItemGiftRateById(Integer id);

	/**
	 * 通过merchantItemId得到门店商品礼品占比YdMerchantItemGiftRate
	 * @param merchantItemId
	 * @return
	 * @Description:
	 */
	public YdMerchantItemGiftRate getGiftRateByMerchantItemId(Integer merchantItemId);
	
	/**
	 * 获取数量
	 * @param ydMerchantItemGiftRate
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantItemGiftRateCount(YdMerchantItemGiftRate ydMerchantItemGiftRate);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantItemGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemGiftRate> findYdMerchantItemGiftRateListByPage(@Param("params") YdMerchantItemGiftRate ydMerchantItemGiftRate,
                                                                             @Param("pageStart") Integer pageStart,
                                                                             @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemGiftRate> getAll(YdMerchantItemGiftRate ydMerchantItemGiftRate);

	/**
	 * 添加门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRate
	 * @Description:
	 */
	public void insertYdMerchantItemGiftRate(YdMerchantItemGiftRate ydMerchantItemGiftRate);

	/**
	 * 通过id修改门店商品礼品占比YdMerchantItemGiftRate
	 * @param ydMerchantItemGiftRate
	 * @Description:
	 */
	public void updateYdMerchantItemGiftRate(YdMerchantItemGiftRate ydMerchantItemGiftRate);

	/**
	 * 查询商品礼品占比
	 * @param merchantId
	 * @param secondCategoryId
	 * @return
	 */
	public List<YdMerchantItemGiftRate> findMerchantItemRateByPage(@Param("merchantId") Integer merchantId,
																   @Param("secondCategoryId") Integer secondCategoryId,
																   @Param("pageStart") Integer pageStart,
																   @Param("pageSize") Integer pageSize);

	public Integer getMerchantItemRateCount(@Param("merchantId") Integer merchantId,
																 @Param("secondCategoryId") Integer secondCategoryId);

	List<YdMerchantItemGiftRate> findListByMerchantIdList(@Param("merchantId") Integer merchantId,
														  @Param("list") Set<Integer> merchantItemIdList);
}
