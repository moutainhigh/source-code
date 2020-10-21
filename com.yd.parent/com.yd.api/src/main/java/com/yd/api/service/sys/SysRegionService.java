package com.yd.api.service.sys;

import com.yd.api.result.sys.SysRegionResult;

import java.util.List;

/**
 * @Title:地区表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-03 16:21:47
 * @Version:1.1.0
 */
public interface SysRegionService {

	/**
	 * 通过id得到地区表SysRegion
	 * @param id
	 * @return 
	 * @Description:
	 */
	public SysRegionResult getSysRegionById(Integer id);

	/**
	 * 得到所有地区表SysRegion
	 * @param sysRegionResult
	 * @return 
	 * @Description:
	 */
	public List<SysRegionResult> getAll(SysRegionResult sysRegionResult);

	/**
	 * 获取所有省市区，分组
	 * @return
	 */
	public List<SysRegionResult> getChildList();

}
