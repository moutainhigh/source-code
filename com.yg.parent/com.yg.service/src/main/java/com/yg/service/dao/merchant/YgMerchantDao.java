package com.yg.service.dao.merchant;

import java.util.List;
import com.yg.service.bean.merchant.YgMerchant;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户信息Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-25 16:44:35
 * @Version:1.1.0
 */
public interface YgMerchantDao {

	/**
	 * 通过id得到商户信息YgMerchant
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YgMerchant getYgMerchantById(Integer id);

	/**
	 * 通过mobile得到商户信息YgMerchant
	 * @param mobile
	 * @return
	 * @Description:
	 */
	YgMerchant getYdMerchantByMobile(String mobile);
	
	/**
	 * 获取数量
	 * @param ygMerchant
	 * @return 
	 * @Description:
	 */
	public int getYgMerchantCount(YgMerchant ygMerchant);
	
	/**
	 * 分页获取数据
	 * @param ygMerchant
	 * @return 
	 * @Description:
	 */
	public List<YgMerchant> findYgMerchantListByPage(@Param("params") YgMerchant ygMerchant,
                                                     @Param("pageStart") Integer pageStart,
                                                     @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户信息YgMerchant
	 * @param ygMerchant
	 * @return 
	 * @Description:
	 */
	public List<YgMerchant> getAll(YgMerchant ygMerchant);

	/**
	 * 添加商户信息YgMerchant
	 * @param ygMerchant
	 * @Description:
	 */
	public void insertYgMerchant(YgMerchant ygMerchant);
	
	/**
	 * 通过id修改商户信息YgMerchant
	 * @param ygMerchant
	 * @Description:
	 */
	public void updateYgMerchant(YgMerchant ygMerchant);

}
