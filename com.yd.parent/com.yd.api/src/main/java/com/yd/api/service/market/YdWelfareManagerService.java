package com.yd.api.service.market;

import java.util.List;

import com.yd.api.result.market.YdWelfareManagerResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:福利管理Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:20:37
 * @Version:1.1.0
 */
public interface YdWelfareManagerService {

	/**
	 * 通过id得到福利管理YdWelfareManager
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdWelfareManagerResult getYdWelfareManagerById(Integer id) throws BusinessException;

	/**
	 * 分页查询福利管理YdWelfareManager
	 * @param ydWelfareManagerResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdWelfareManagerResult> findYdWelfareManagerListByPage(YdWelfareManagerResult ydWelfareManagerResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有福利管理YdWelfareManager
	 * @param ydWelfareManagerResult
	 * @return 
	 * @Description:
	 */
	public List<YdWelfareManagerResult> getAll(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException;

	/**
	 * 添加福利管理YdWelfareManager
	 * @param ydWelfareManagerResult
	 * @Description:
	 */
	public void insertYdWelfareManager(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException;
	
	/**
	 * 通过id修改福利管理YdWelfareManager throws BusinessException;
	 * @param ydWelfareManagerResult
	 * @Description:
	 */
	public void updateYdWelfareManager(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException;

	/**
	 * 通过id删除福利管理YdWelfareManager throws BusinessException;
	 * @param id
	 * @Description:
	 */
	public void deleteYdWelfareManager(Integer id) throws BusinessException;

	/**
	 * 通过id修改上下架状态
	 * @param id
	 * @Description:
	 */
	void updateYdWelfareManagerStatus(Integer id, String isEnable) throws BusinessException;


    void listSort(List<YdWelfareManagerResult> dataList) throws BusinessException;
}
