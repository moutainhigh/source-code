package com.yd.service.dao.market;

import java.util.List;
import com.yd.service.bean.market.YdMerchantGiftRate;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店礼品占比Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-06 13:51:09
 * @Version:1.1.0
 */
public interface YdMerchantGiftRateDao {

	/**
	 * 通过id得到门店礼品占比YdMerchantGiftRate
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftRate getYdMerchantGiftRateById(Integer id);

	/**
	 * 通过merchantId得到门店礼品占比YdMerchantGiftRate
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantGiftRate getYdMerchantGiftRateByMerchantId(Integer merchantId);
	
	/**
	 * 获取数量
	 * @param ydMerchantGiftRate
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantGiftRateCount(YdMerchantGiftRate ydMerchantGiftRate);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftRate> findYdMerchantGiftRateListByPage(@Param("params") YdMerchantGiftRate ydMerchantGiftRate,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店礼品占比YdMerchantGiftRate
	 * @param ydMerchantGiftRate
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftRate> getAll(YdMerchantGiftRate ydMerchantGiftRate);

	/**
	 * 添加门店礼品占比YdMerchantGiftRate
	 * @param ydMerchantGiftRate
	 * @Description:
	 */
	public void insertYdMerchantGiftRate(YdMerchantGiftRate ydMerchantGiftRate);
	
	/**
	 * 通过id修改门店礼品占比YdMerchantGiftRate
	 * @param ydMerchantGiftRate
	 * @Description:
	 */
	public void updateYdMerchantGiftRate(YdMerchantGiftRate ydMerchantGiftRate);
	
}
