package com.yd.api.service.member;

import com.yd.api.result.member.YdMemberLevelConfigResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import java.util.List;

/**
 * @Title:优度会员配置Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:33:56
 * @Version:1.1.0
 */
public interface YdMemberLevelConfigService {

	/**
	 * 通过id得到优度会员配置YdMemberLevelConfig
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMemberLevelConfigResult getYdMemberLevelConfigById(Integer id) throws BusinessException;

	/**
	 * 分页查询优度会员配置YdMemberLevelConfig
	 * @param ydMemberLevelConfigResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMemberLevelConfigResult> findYdMemberLevelConfigListByPage(YdMemberLevelConfigResult ydMemberLevelConfigResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有优度会员配置YdMemberLevelConfig
	 * @param ydMemberLevelConfigResult
	 * @return 
	 * @Description:
	 */
	public List<YdMemberLevelConfigResult> getAll(YdMemberLevelConfigResult ydMemberLevelConfigResult) throws BusinessException;

	/**
	 * 添加优度会员配置YdMemberLevelConfig
	 * @param ydMemberLevelConfigResult
	 * @Description:
	 */
	public void insertYdMemberLevelConfig(YdMemberLevelConfigResult ydMemberLevelConfigResult) throws BusinessException;
	
	/**
	 * 通过id修改优度会员配置YdMemberLevelConfig throws BusinessException;
	 * @param ydMemberLevelConfigResult
	 * @Description:
	 */
	public void updateYdMemberLevelConfig(YdMemberLevelConfigResult ydMemberLevelConfigResult)throws BusinessException;
	
}
