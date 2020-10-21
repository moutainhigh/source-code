package com.yd.api.service.draw;

import java.util.List;

import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.draw.YdUserDrawRecordResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户抽奖记录Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:35:07
 * @Version:1.1.0
 */
public interface YdUserDrawRecordService {

	/**
	 * 通过id得到用户抽奖记录YdUserDrawRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserDrawRecordResult getYdUserDrawRecordById(Integer id);

	/**
	 * 分页查询用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecordResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdUserDrawRecordResult> findYdUserDrawRecordListByPage(YdUserDrawRecordResult ydUserDrawRecordResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecordResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserDrawRecordResult> getAll(YdUserDrawRecordResult ydUserDrawRecordResult);

	/**
	 * 添加用户抽奖记录YdUserDrawRecord
	 * @param ydUserDrawRecordResult
	 * @Description:
	 */
	public void insertYdUserDrawRecord(YdUserDrawRecordResult ydUserDrawRecordResult) throws BusinessException;
	
	/**
	 * 通过id修改用户抽奖记录YdUserDrawRecord throws BusinessException;
	 * @param ydUserDrawRecordResult
	 * @Description:
	 */
	public void updateYdUserDrawRecord(YdUserDrawRecordResult ydUserDrawRecordResult) throws BusinessException;

	List<YdUserCouponResult> getUserDrawRecord(Integer currUserId, String uuid) throws BusinessException;
}
