package com.yd.api.service.decoration;

import java.util.List;

import com.yd.api.result.decoration.YdVrManagerResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:店铺vr设置Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:36:46
 * @Version:1.1.0
 */
public interface YdVrManagerService {

	/**
	 * 通过id得到店铺vr设置YdVrManager
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdVrManagerResult getYdVrManagerById(Integer id) throws BusinessException;

	/**
	 * 通过merchantId得到店铺vr设置YdVrManager
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdVrManagerResult getYdVrManagerByMerchantId(Integer merchantId) throws BusinessException;

	/**
	 * 通过id删除店铺vr设置YdVrManager
	 * @param id
	 * @return
	 * @Description:
	 */
	public void deleteYdVrManager(Integer id) throws BusinessException;

	/**
	 * 分页查询店铺vr设置YdVrManager
	 * @param ydVrManagerResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdVrManagerResult> findYdVrManagerListByPage(YdVrManagerResult ydVrManagerResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有店铺vr设置YdVrManager
	 * @param ydVrManagerResult
	 * @return 
	 * @Description:
	 */
	public List<YdVrManagerResult> getAll(YdVrManagerResult ydVrManagerResult) throws BusinessException;

	/**
	 * 添加店铺vr设置YdVrManager
	 * @param ydVrManagerResult
	 * @Description:
	 */
	public void insertYdVrManager(YdVrManagerResult ydVrManagerResult) throws BusinessException;
	
	/**
	 * 通过id修改店铺vr设置YdVrManager throws BusinessException;
	 * @param ydVrManagerResult
	 * @Description:
	 */
	public void updateYdVrManager(YdVrManagerResult ydVrManagerResult) throws BusinessException;

}
