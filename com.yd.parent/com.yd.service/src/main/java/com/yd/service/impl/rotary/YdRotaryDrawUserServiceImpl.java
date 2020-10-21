package com.yd.service.impl.rotary;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.yd.api.result.rotary.YdRotaryDrawUserResult;
import com.yd.api.service.rotary.YdRotaryDrawUserService;
import com.yd.core.constants.RotaryDrawConstants;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DateUtils;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.service.bean.rotary.YdRotaryDrawActivity;
import com.yd.service.bean.rotary.YdRotaryDrawUser;
import com.yd.service.dao.rotary.YdRotaryDrawActivityDao;
import com.yd.service.dao.rotary.YdRotaryDrawUserDao;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:用户Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-18 11:46:36
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdRotaryDrawUserServiceImpl implements YdRotaryDrawUserService {

	@Resource
	private YdRotaryDrawUserDao ydRotaryDrawUserDao;

	@Resource
	private YdRotaryDrawActivityDao ydRotaryDrawActivityDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer getUserCanUseDrawCount(Integer userId, String uuid) throws BusinessException{

		// 校验活动信息
		YdRotaryDrawActivity ydRotaryDrawActivity = ydRotaryDrawActivityDao.getYdRotaryDrawActivityByUuid(uuid);

		ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity == null,
				"error_no_activity", "活动不存在");
		ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity.getIsEnable().equalsIgnoreCase("N"),
				"error_activity_no_enable", "活动禁用中，暂不可用");
		ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity.getStartTime().getTime() > new Date().getTime(),
				"error_no_activity", "活动尚未开始");
		ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity.getEndTime().getTime() < new Date().getTime(),
				"error_no_activity", "活动已经结束");

		Date expireDate = getExpireTime(ydRotaryDrawActivity);
		Integer merchantId = ydRotaryDrawActivity.getMerchantId();
		String drawCountType = RotaryDrawConstants.All;
		String outOrderId = DateUtils.getNowDateStr() + "-" +  RotaryDrawConstants.All + "-" + ydRotaryDrawActivity.getId();
		if (ydRotaryDrawActivity.getDrawCountType().equals("DAY")) {
			drawCountType = RotaryDrawConstants.DAY;
			outOrderId = DateUtils.getNowDateStr() + "-" +  RotaryDrawConstants.DAY + "-" + ydRotaryDrawActivity.getId();
		}

		// 查询用户抽奖次数前初始化用户可用抽奖记录
		initUserDrawCount(userId, merchantId, ydRotaryDrawActivity.getId(), ydRotaryDrawActivity.getDrawTotalCount(), drawCountType, outOrderId, expireDate);

		// 根据过期时间查询剩余抽奖次数
		// List<YdRotaryDrawUser> list = ydRotaryDrawUserDao.findUserCanUseDrawList(userId, ydRotaryDrawActivity.getId());

		int count = ydRotaryDrawUserDao.getUserCanUseDrawCount(userId, ydRotaryDrawActivity.getId());
		return count;
	}

	@Override
	public Integer reduceUserDrawCount(Integer userId, String uuid) throws BusinessException {
		YdRotaryDrawActivity ydRotaryDrawActivity = ydRotaryDrawActivityDao.getYdRotaryDrawActivityByUuid(uuid);
		// 查询可以抽奖的次数， 按过期时间排序
		List<YdRotaryDrawUser> list = ydRotaryDrawUserDao.findUserCanUseDrawList(userId, ydRotaryDrawActivity.getId());
		YdRotaryDrawUser ydRotaryDrawUser = list.get(0);
		return ydRotaryDrawUserDao.reduceUserDrawCount(ydRotaryDrawUser.getId(), 1);
	}


	@Override
	public YdRotaryDrawUserResult getYdRotaryDrawUserById(Integer id) {
		return null;
	}

	@Override
	public List<YdRotaryDrawUserResult> getAll(YdRotaryDrawUserResult ydRotaryDrawUser) {
		return null;
	}

	@Override
	public void insertYdRotaryDrawUser(YdRotaryDrawUserResult ydRotaryDrawUser) {

	}

	@Override
	public void updateYdRotaryDrawUser(YdRotaryDrawUserResult ydRotaryDrawUser) {

	}

	/**
	 *
	 * @return
	 */
	private Date getExpireTime(YdRotaryDrawActivity ydRotaryDrawActivity) {
		// 获取明日凌晨日期
		Date expireDate =  ydRotaryDrawActivity.getEndTime();
		Date todayEndDate = DateUtils.getDayStartDate(DateUtils.addDays(new Date(), 1));
		if (ydRotaryDrawActivity.getDrawCountType().equals("DAY") &&
				ydRotaryDrawActivity.getEndTime().getTime() < todayEndDate.getTime()) {
			expireDate = todayEndDate;
		}
		return expireDate;
	}

	/**
	 * 查询用户抽奖次数前初始化用户可用抽奖记录
	 * @param userId
	 * @param merchantId
	 * @param useDrawCount
	 * @param drawCountType
	 * @param outOrderId
	 * @param expireDate
	 */
	private void initUserDrawCount(Integer userId, Integer merchantId, Integer activityId, Integer useDrawCount, String drawCountType, String outOrderId, Date expireDate) {
		Date nowDate = new Date();
		// 查询是否初始化过次数
		YdRotaryDrawUser ydRotaryDrawUser = ydRotaryDrawUserDao.getYdRotaryDrawUserByOutOrderId(outOrderId);
		if (ydRotaryDrawUser == null) {
			ydRotaryDrawUser = new YdRotaryDrawUser();
			ydRotaryDrawUser.setCreateTime(nowDate);
			ydRotaryDrawUser.setUpdateTime(nowDate);
			ydRotaryDrawUser.setUserId(userId);
			ydRotaryDrawUser.setMerchantId(merchantId);
			ydRotaryDrawUser.setActivityId(activityId);
			ydRotaryDrawUser.setOutOrderId(outOrderId);
			ydRotaryDrawUser.setUserDrawCount(useDrawCount);
			ydRotaryDrawUser.setDrawCountType(drawCountType);
			ydRotaryDrawUser.setValidTime(nowDate);
			ydRotaryDrawUser.setExpireTime(expireDate);
			ydRotaryDrawUserDao.insertYdRotaryDrawUser(ydRotaryDrawUser);
		}
	}



}

