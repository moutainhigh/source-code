package com.yd.service.dao.item;

import java.util.List;

import com.yd.service.bean.item.YdMerchantItemCategory;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户商品分类Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:31:16
 * @Version:1.1.0
 */
public interface YdMerchantItemCategoryDao {

	/**
	 * 通过id得到商户商品分类YdShopMerchantItemCategory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemCategory getYdShopMerchantItemCategoryById(Integer id);

	/**
	 * 通过pid得到商户商品分类YdShopMerchantItemCategory
	 * @param pid
	 * @return
	 * @Description:
	 */
	public List<YdMerchantItemCategory> getYdShopMerchantItemCategoryByPid(Integer pid);

	/**
	 * 通过name得到商户商品分类YdShopMerchantItemCategory
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchantItemCategory getYdShopMerchantItemCategoryBydName(@Param("id") Integer id,
                                                                       @Param("merchantId") Integer merchantId,
                                                                       @Param("categoryName") String categoryName);

	/**
	 * 得到所有商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategory
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemCategory> getAll(YdMerchantItemCategory ydShopMerchantItemCategory);

	/**
	 * 查询分类条数
	 * @param params
	 * @return
	 */
	public int getCategoryListByCount(@Param("params") YdMerchantItemCategory params);

	/**
	 * 分页查询分类数据
	 * @param params
	 * @param pageSize
	 * @param pageStart
	 * @return
	 */
	public List<YdMerchantItemCategory> findCategoryListByPage(@Param("params") YdMerchantItemCategory params,
															   @Param("pageStart") Integer pageStart,
															   @Param("pageSize") Integer pageSize);

	/**
	 * 添加商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategory
	 * @Description:
	 */
	public void insertYdShopMerchantItemCategory(YdMerchantItemCategory ydShopMerchantItemCategory);
	

	/**
	 * 通过id修改商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategory
	 * @Description:
	 */
	public void updateYdShopMerchantItemCategory(YdMerchantItemCategory ydShopMerchantItemCategory);

	/**
	 * 通过id删除商户商品分类YdShopMerchantItemCategory
	 * @param id
	 */
	public void deleteYdShopMerchantItemCategoryById(Integer id);

	/**
	 * 查询商户最大排序号
	 * @param pid
	 * @return
	 */
    int findMerchantCategoryMaxSort(@Param("pid") Integer pid, @Param("merchantId") Integer merchantId);

	/**
	 * 升序排序
	 * @param pid
	 * @param sort
	 * @param merchantId
	 * @return
	 */
	YdMerchantItemCategory getMerchantCategoryDesc(@Param("pid") Integer pid,
                                                   @Param("sort") Integer sort,
                                                   @Param("merchantId") Integer merchantId);

	/**
	 * 降序排序
	 * @param pid
	 * @param sort
	 * @param merchantId
	 * @return
	 */
	YdMerchantItemCategory getMerchantCategoryAsc(@Param("pid") Integer pid,
                                                  @Param("sort") Integer sort,
                                                  @Param("merchantId") Integer merchantId);
}
