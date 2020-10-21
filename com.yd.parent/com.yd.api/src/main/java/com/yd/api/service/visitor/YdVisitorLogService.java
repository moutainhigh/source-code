package com.yd.api.service.visitor;

import java.util.List;

import com.yd.api.result.visitor.YdVisitorLogResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:接口访问记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-09 19:17:07
 * @Version:1.1.0
 */
public interface YdVisitorLogService {

	/**
	 * 通过id得到接口访问记录YdVisitorLog
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdVisitorLogResult getYdVisitorLogById(Integer id);

	/**
	 * 分页查询接口访问记录YdVisitorLog
	 * @param ydVisitorLogResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdVisitorLogResult> findYdVisitorLogListByPage(YdVisitorLogResult ydVisitorLogResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有接口访问记录YdVisitorLog
	 * @param ydVisitorLogResult
	 * @return 
	 * @Description:
	 */
	public List<YdVisitorLogResult> getAll(YdVisitorLogResult ydVisitorLogResult);

	/**
	 * 添加接口访问记录YdVisitorLog
	 * @param ydVisitorLogResult
	 * @Description:
	 */
	public void insertYdVisitorLog(YdVisitorLogResult ydVisitorLogResult) throws BusinessException;
	
	/**
	 * 通过id修改接口访问记录YdVisitorLog throws BusinessException;
	 * @param ydVisitorLogResult
	 * @Description:
	 */
	public void updateYdVisitorLog(YdVisitorLogResult ydVisitorLogResult)throws BusinessException;

}
