package com.yd.service.dao.sys;

import java.util.List;
import com.yd.api.result.sys.SysRegionResult;
import com.yd.service.bean.sys.SysRegion;

/**
 * @Title:地区表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-03 16:21:47
 * @Version:1.1.0
 */
public interface SysRegionDao {

	/**
	 * 通过id得到地区表SysRegion
	 * @param id
	 * @return 
	 * @Description:
	 */
	public SysRegion getSysRegionById(Integer id);

	/**
	 * 通过code得到地区表SysRegion
	 * @param code
	 * @return
	 * @Description:
	 */
	public SysRegion getSysRegionByCode(Integer code);

	/**
	 * 得到所有地区表SysRegion
	 * @param sysRegion
	 * @return 
	 * @Description:
	 */
	public List<SysRegion> getAll(SysRegion sysRegion);

}
