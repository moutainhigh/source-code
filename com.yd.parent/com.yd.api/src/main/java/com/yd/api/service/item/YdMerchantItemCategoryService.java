package com.yd.api.service.item;

import com.yd.api.result.item.YdMerchantItemCategoryResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

import java.util.List;

/**
 * @Title:商户商品分类Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:31:16
 * @Version:1.1.0
 */
public interface YdMerchantItemCategoryService {

	/**
	 * 通过id得到商户商品分类YdShopMerchantItemCategory
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemCategoryResult getYdShopMerchantItemCategoryById(Integer id);

	/**
	 * 得到所有商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategoryResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemCategoryResult> getAll(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult);


	/**
	 * 分页查询商户商品分类YdShopMerchantItemCategory
	 * @param params
	 * @return
	 * @Description:
	 */
	public Page<YdMerchantItemCategoryResult> findCategoryListByPage(YdMerchantItemCategoryResult params, PagerInfo pagerInfo);

	/**
	 * 添加商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategoryResult
	 * @Description:
	 */
	public void insertYdShopMerchantItemCategory(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult);
	

	/**
	 * 通过id修改商户商品分类YdShopMerchantItemCategory
	 * @param ydShopMerchantItemCategoryResult
	 * @Description:
	 */
	public void updateYdShopMerchantItemCategory(YdMerchantItemCategoryResult ydShopMerchantItemCategoryResult);


	/**
	 * 删除商户商品分类
	 * @param params
	 */
	public void deleteYdShopMerchantItemCategory(YdMerchantItemCategoryResult params) throws BusinessException;

    /**
     * 分类排序
     * @param id
     * @param pid
     * @param merchantId
     * @param type
     * @throws BusinessException
     */
    void SortYdShopMerchantItemCategory(Integer id, Integer pid, Integer merchantId, String type) throws BusinessException;
}
