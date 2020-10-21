package com.yd.api.service.rotary;

import com.yd.api.result.rotary.YdRotaryDrawActivityResult;
import com.yd.api.result.rotary.YdRotaryDrawRecordResult;
import com.yd.core.utils.BusinessException;

import java.util.List;

/**
 * @Title:转盘抽奖活动Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:52:51
 * @Version:1.1.0
 */
public interface YdRotaryDrawActivityService {

	/**
	 * 通过id得到转盘抽奖活动YdRotaryDrawActivity
	 * @param id
	 * @return 
	 * @throws BusinessException
	 * @Description:
	 */
	public YdRotaryDrawActivityResult getYdRotaryDrawActivityById(Integer id) throws BusinessException;

	/**
	 * 通过id得到转盘抽奖活动YdRotaryDrawActivity
	 * @param uuid
	 * @return
	 * @throws BusinessException
	 * @Description:
	 */
	public YdRotaryDrawActivityResult getYdRotaryDrawActivityByUuid(String uuid);

	/**
	 * 得到所有转盘抽奖活动YdRotaryDrawActivity
	 * @param params
	 * @return 
	 * @throws BusinessException
	 * @Description:
	 */
	public List<YdRotaryDrawActivityResult> getAll(YdRotaryDrawActivityResult params) throws BusinessException;

	/**
	 * 添加修改转盘抽奖活动YdRotaryDrawActivity
	 * @param params
	 * @throws BusinessException
	 * @Description:
	 */
	public void saveOrUpdate(YdRotaryDrawActivityResult params) throws BusinessException;

	/**
	 * 用户抽奖
	 * @param uuid	活动uuid
	 * @param userId
	 */
	YdRotaryDrawRecordResult userDraw(String uuid, Integer userId);

	/**
	 * 查询用户剩余抽奖次数
	 * @param userId
	 * @param uuid
	 * @return
	 */
	Integer findUserDrawCount(Integer userId, String uuid);
}
