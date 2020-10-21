package com.yd.service.dao.decoration;

import java.util.List;
import com.yd.service.bean.decoration.YdMerchantBrand;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店品牌管理Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:08:16
 * @Version:1.1.0
 */
public interface YdMerchantBrandDao {

	/**
	 * 通过id得到门店品牌管理YdMerchantBrand
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBrand getYdMerchantBrandById(Integer id);

	/**
	 * 通过merchantId得到门店品牌管理YdMerchantBrand
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public List<YdMerchantBrand> getYdMerchantBrandByMerchantId(Integer merchantId);

	/**
	 * 通过id得到门店品牌管理YdMerchantBrand
	 * @param merchantId	商户ID
	 * @param brandAlias	品牌唯一标识
	 * @return
	 */
	public YdMerchantBrand getYdMerchantBrandByMerchantIdAndAlias(@Param("merchantId") Integer merchantId,
																  @Param("brandAlias") String brandAlias);
	
	/**
	 * 获取数量
	 * @param ydMerchantBrand
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantBrandCount(YdMerchantBrand ydMerchantBrand);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantBrand
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBrand> findYdMerchantBrandListByPage(@Param("params") YdMerchantBrand ydMerchantBrand,
                                                               @Param("pageStart") Integer pageStart,
                                                               @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrand
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBrand> getAll(YdMerchantBrand ydMerchantBrand);


	/**
	 * 添加门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrand
	 * @Description:
	 */
	public void insertYdMerchantBrand(YdMerchantBrand ydMerchantBrand);
	

	/**
	 * 通过id修改门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrand
	 * @Description:
	 */
	public void updateYdMerchantBrand(YdMerchantBrand ydMerchantBrand);

	/**
	 * 通过id删除门店品牌管理
	 * @param id
	 * @Description:
	 */
	public void deleteYdMerchantBrand(Integer id);
}
