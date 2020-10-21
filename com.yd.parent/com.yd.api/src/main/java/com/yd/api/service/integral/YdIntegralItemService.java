package com.yd.api.service.integral;

import java.util.List;

import com.yd.api.result.integral.YdIntegralItemResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:积分商品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:16:34
 * @Version:1.1.0
 */
public interface YdIntegralItemService {

	/**
	 * 通过id得到积分商品YdIntegralItem
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralItemResult getYdIntegralItemById(Integer id);

	/**
	 * 分页查询积分商品YdIntegralItem
	 * @param ydIntegralItemResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdIntegralItemResult> findYdIntegralItemListByPage(YdIntegralItemResult ydIntegralItemResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有积分商品YdIntegralItem
	 * @param ydIntegralItemResult
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralItemResult> getAll(YdIntegralItemResult ydIntegralItemResult);

	/**
	 * 积分商品
	 * @param itemId
	 * @throws BusinessException
	 */
	void deleteIntegralItem(Integer itemId) throws BusinessException;

	/**
	 * 添加积分商品YdIntegralItem
	 * @param ydIntegralItemResult
	 * @Description:
	 */
	public void insertYdIntegralItem(YdIntegralItemResult ydIntegralItemResult) throws BusinessException;
	
	/**
	 * 通过id修改积分商品YdIntegralItem throws BusinessException;
	 * @param ydIntegralItemResult
	 * @Description:
	 */
	public void updateYdIntegralItem(YdIntegralItemResult ydIntegralItemResult)throws BusinessException;

	/**
	 * 上下架积分商品
	 * @param itemId
	 * @param type
	 */
	public void upOrDownIntegralItem(Integer itemId, String type) throws BusinessException;
	
}
