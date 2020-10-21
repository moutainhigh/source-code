package com.yd.api.service.decoration;

import java.util.List;

import com.yd.api.result.decoration.PlatBrandResult;
import com.yd.api.result.decoration.YdMerchantBrandResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:门店品牌管理Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-31 15:08:16
 * @Version:1.1.0
 */
public interface YdMerchantBrandService {

	/**
	 * 通过id得到门店品牌管理YdMerchantBrand
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantBrandResult getYdMerchantBrandById(Integer id);

	/**
	 * 分页查询门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrandResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantBrandResult> findYdMerchantBrandListByPage(YdMerchantBrandResult ydMerchantBrandResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrandResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantBrandResult> getAll(YdMerchantBrandResult ydMerchantBrandResult);

	/**
	 * 添加门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrandResult
	 * @Description:
	 */
	public void insertYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) throws BusinessException;
	

	/**
	 * 通过id修改门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrandResult
	 * @Description:
	 */
	public void updateYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) throws BusinessException;

	/**
	 * 通过id删除门店品牌管理YdMerchantBrand
	 * @param ydMerchantBrandResult
	 * @Description:
	 */
	public void deleteYdMerchantBrand(YdMerchantBrandResult ydMerchantBrandResult) throws BusinessException;

	/**
	 * 排序商户品牌
	 * @param brandResultList
	 * @throws BusinessException
	 */
	public void sortYdMerchantBrand(List<YdMerchantBrandResult> brandResultList)  throws BusinessException;

//	/**
//	 * 获取平台品牌列表
//	 * @return
//	 * @throws BusinessException
//	 */
//	public List<PlatBrandResult> findPlatformBrandList(Integer merchantId)  throws BusinessException;
	
}
