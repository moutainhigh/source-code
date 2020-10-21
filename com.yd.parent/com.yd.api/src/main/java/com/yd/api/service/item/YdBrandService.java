package com.yd.api.service.item;

import java.util.List;

import com.yd.api.result.item.YdBrandResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商品品牌Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-19 14:09:38
 * @Version:1.1.0
 */
public interface YdBrandService {

	/**
	 * 通过id得到商品品牌YdBrand
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdBrandResult getYdBrandById(Integer id);

	/**
	 * 分页查询商品品牌YdBrand
	 * @param ydBrandResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdBrandResult> findYdBrandListByPage(YdBrandResult ydBrandResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商品品牌YdBrand
	 * @param ydBrandResult
	 * @return 
	 * @Description:
	 */
	public List<YdBrandResult> getAll(YdBrandResult ydBrandResult);

	/**
	 * 添加商品品牌YdBrand
	 * @param ydBrandResult
	 * @Description:
	 */
	public void insertYdBrand(YdBrandResult ydBrandResult) throws BusinessException;
	
	/**
	 * 通过id修改商品品牌YdBrand throws BusinessException;
	 * @param ydBrandResult
	 * @Description:
	 */
	public void updateYdBrand(YdBrandResult ydBrandResult)throws BusinessException;
	
}
