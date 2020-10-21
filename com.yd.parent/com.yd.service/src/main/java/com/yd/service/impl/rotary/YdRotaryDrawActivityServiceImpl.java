package com.yd.service.impl.rotary;

import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import com.yd.api.result.rotary.YdRotaryDrawActivityResult;
import com.yd.api.result.rotary.YdRotaryDrawPrizeResult;
import com.yd.api.result.rotary.YdRotaryDrawRecordResult;
import com.yd.api.result.user.YdUserResult;
import com.yd.api.service.rotary.YdRotaryDrawActivityService;
import com.yd.api.service.rotary.YdRotaryDrawPrizeService;
import com.yd.api.service.rotary.YdRotaryDrawUserService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.constants.AdminConstants;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DateUtils;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.service.bean.rotary.YdRotaryDrawActivity;
import com.yd.service.bean.rotary.YdRotaryDrawPrize;
import com.yd.service.bean.rotary.YdRotaryDrawRecord;
import com.yd.service.dao.rotary.YdRotaryDrawActivityDao;
import com.yd.service.dao.rotary.YdRotaryDrawPrizeDao;
import com.yd.service.dao.rotary.YdRotaryDrawRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:转盘抽奖活动Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:52:51
 * @Version:1.1.0
 */
@org.apache.dubbo.config.annotation.Service(dynamic = true)
public class YdRotaryDrawActivityServiceImpl implements YdRotaryDrawActivityService {

    private static final Logger logger = LoggerFactory.getLogger(YdRotaryDrawActivityServiceImpl.class);

	@Resource
	private YdRotaryDrawActivityDao ydRotaryDrawActivityDao;

    @Resource
    private YdRotaryDrawPrizeDao ydRotaryDrawPrizeDao;

    @Resource
    private YdUserService ydShopUserService;

    @Resource
    private YdRotaryDrawRecordDao ydRotaryDrawRecordDao;

    @Resource
    private YdRotaryDrawUserService ydRotaryDrawUserService;

    @Resource
    private YdRotaryDrawPrizeService ydRotaryDrawPrizeService;

    @Override
	public YdRotaryDrawActivityResult getYdRotaryDrawActivityById(Integer id) throws BusinessException {
		return null;
	}

    @Override
    public YdRotaryDrawActivityResult getYdRotaryDrawActivityByUuid(String uuid) {
        YdRotaryDrawActivity rotaryDrawActivity = ydRotaryDrawActivityDao.getYdRotaryDrawActivityByUuid(uuid);
        YdRotaryDrawActivityResult rotaryDrawActivityResult = null;
        if (rotaryDrawActivity != null) {
            rotaryDrawActivityResult = new YdRotaryDrawActivityResult();
            BeanUtilExt.copyProperties(rotaryDrawActivityResult, rotaryDrawActivity);
        }
        return rotaryDrawActivityResult;
    }

    @Override
	public List<YdRotaryDrawActivityResult> getAll(YdRotaryDrawActivityResult rotaryDrawActivityResult) throws BusinessException {
		return null;
	}

	@Override
	public void saveOrUpdate(YdRotaryDrawActivityResult params) throws BusinessException {
	    // 校验入参
        checkParams(params);
        // 新增修改活动
        YdRotaryDrawActivity ydRotaryDrawActivity = updateRotaryDrawActivity(params);
        // 初始化活动商品
        initDrawPrize(ydRotaryDrawActivity);
	}

    /**
     * 用户抽奖
     * @param uuid	活动uuid
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public YdRotaryDrawRecordResult userDraw(String uuid, Integer userId) {
        // 校验用户
        YdUserResult ydShopUserResult = ydShopUserService.getYdShopUserById(userId);
        ValidateBusinessUtils.assertFalse(ydShopUserResult == null,
                "error_user_no_exit", "用户不存在");

        // 查询活动
        YdRotaryDrawActivity ydRotaryDrawActivity = ydRotaryDrawActivityDao.getYdRotaryDrawActivityByUuid(uuid);
        ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity == null,
                "error_activity_no_exit", "活动不存在");
        ValidateBusinessUtils.assertFalse(ydRotaryDrawActivity.getIsEnable().equalsIgnoreCase("N"),
                "error_activity_no_enable", "活动禁用中，暂不可用");

        // 查询剩余抽奖次数
        int canUseDrawCount = ydRotaryDrawUserService.getUserCanUseDrawCount(userId, uuid);
        if (canUseDrawCount <= 0) {
            throw new BusinessException("error_no_draw_count", "暂无可用抽奖次数");
        }

        // 扣除用户抽奖次数
        if (ydRotaryDrawUserService.reduceUserDrawCount(userId, uuid) <= 0) {
            throw new BusinessException("system_error", "系统异常，请稍后重试");
        }
        // 用户抽奖
        return userStartDraw(userId, ydRotaryDrawActivity.getId(), ydRotaryDrawActivity.getMerchantId());
    }

    @Override
    public Integer findUserDrawCount(Integer userId, String uuid) {
        YdRotaryDrawActivity ydRotaryDrawActivity = ydRotaryDrawActivityDao.getYdRotaryDrawActivityByUuid(uuid);
        if (ydRotaryDrawActivity != null) {
            return getUserDrawCount(userId, ydRotaryDrawActivity);
        } else {
            return new Integer(0);
        }
    }

    /**
     * 查询用户剩余抽奖次数
     * @param userId
     * @param ydRotaryDrawActivity
     * @return
     */
    private int getUserDrawCount(Integer userId, YdRotaryDrawActivity ydRotaryDrawActivity) {
        // 查询抽奖记录
        String startTime = null;
        String endTime = null;
        // 判断是否为每日几次活动
        if (ydRotaryDrawActivity.getDrawCountType().equals("DAY")) {
            startTime = DateUtils.datetime(new Date(), DateUtils.DEFAULT_DATE_FORMAT);
            endTime = DateUtils.datetime(DateUtils.addDays(new Date(), 1), DateUtils.DEFAULT_DATE_FORMAT);
        }
        logger.info("----startTime = " + startTime + " , ----endTime = " + endTime);
        List<YdRotaryDrawRecord> drawRecordList = ydRotaryDrawRecordDao.findUserDrawRecordList(userId, ydRotaryDrawActivity.getId(), startTime, endTime);
        // 已经使用的次数
        int useDrawCount = CollectionUtils.isEmpty(drawRecordList) ? 0 : drawRecordList.size();

        // 活动配置的抽奖次数
        int activityDrawCount = ydRotaryDrawActivity.getDrawTotalCount();
        logger.info("---- useDrawCount = " + useDrawCount + " , activityDrawCount = " + activityDrawCount);
        logger.info("----用户剩余抽奖次数 = " + (activityDrawCount - useDrawCount));
        return activityDrawCount - useDrawCount;
    }

    /**
     * 用户开始抽奖
     * @param userId
     * @param activityId
     */
    private YdRotaryDrawRecordResult userStartDraw(Integer userId, Integer activityId, int merchantId) {
        // 查询活动所有的奖品
        YdRotaryDrawPrizeResult ydRotaryDrawPrizeResult = new YdRotaryDrawPrizeResult();
        ydRotaryDrawPrizeResult.setActivityId(activityId);
        List<YdRotaryDrawPrizeResult> prizeList = ydRotaryDrawPrizeService.getAll(ydRotaryDrawPrizeResult);

        // 根据权重算法计算中奖几率
        int sumRate = 0;
        for (YdRotaryDrawPrizeResult data : prizeList) {
            sumRate += data.getWinRate();
        }

        Integer random = new Random().nextInt(sumRate);
        Integer number = 0;
        YdRotaryDrawRecordResult rotaryDrawRecordResult = new YdRotaryDrawRecordResult();
        for (YdRotaryDrawPrizeResult data : prizeList) {
            if (number <= random && random < number + data.getWinRate()) {
                // 抽中奖品, 保存抽奖记录
                Date nowDate = new Date();
                YdRotaryDrawRecord drawRecord = new YdRotaryDrawRecord();
                drawRecord.setCreateTime(nowDate);
                drawRecord.setUpdateTime(nowDate);
                drawRecord.setMerchantId(merchantId);
                drawRecord.setUserId(userId);
                drawRecord.setActivityId(activityId);
                drawRecord.setPrizeId(data.getId());
                drawRecord.setPrizeName(data.getName());
                drawRecord.setType(data.getType());
                ydRotaryDrawRecordDao.insertYdRotaryDrawRecord(drawRecord);
                BeanUtilExt.copyProperties(rotaryDrawRecordResult, drawRecord);
                break;
            }
            number += data.getWinRate();
        }
        return rotaryDrawRecordResult;
    }

    /**
     * 新增修改活动
     * @param params
     * @return
     */
	private YdRotaryDrawActivity updateRotaryDrawActivity(YdRotaryDrawActivityResult params) {
        YdRotaryDrawActivity ydRotaryDrawActivity = new YdRotaryDrawActivity();
        BeanUtilExt.copyProperties(ydRotaryDrawActivity, params);
        Date nowDate = new Date();
        ydRotaryDrawActivity.setUpdateTime(nowDate);
        if (params.getId() == null) {
            ydRotaryDrawActivity.setCreateTime(nowDate);
            ydRotaryDrawActivityDao.insertYdRotaryDrawActivity(ydRotaryDrawActivity);
        } else {
            ydRotaryDrawActivityDao.updateYdRotaryDrawActivity(ydRotaryDrawActivity);
        }
        return ydRotaryDrawActivity;
    }

    /**
     * 初始化奖品
     * @param ydRotaryDrawActivity
     */
    private void initDrawPrize(YdRotaryDrawActivity ydRotaryDrawActivity) {
        int itemCount = AdminConstants.PRIZE_DEFAULT_COUNT;
        if (ydRotaryDrawActivity.getType().equals("JGG")) {
            itemCount = 8;
        }
        Date nowDate = new Date();
        for (int index = 0; index < itemCount; index++) {
            YdRotaryDrawPrize ydRotaryDrawPrize = new YdRotaryDrawPrize();
            ydRotaryDrawPrize.setCreateTime(nowDate);
            ydRotaryDrawPrize.setUpdateTime(nowDate);
            ydRotaryDrawPrize.setActivityId(ydRotaryDrawActivity.getId());
            // todo
            ydRotaryDrawPrize.setType("MYZJ");
            ydRotaryDrawPrize.setIcon(AdminConstants.PRIZE_DEFAULT_ICON);
            ydRotaryDrawPrize.setName(AdminConstants.IPRIZE_DEFAULT_NAME);
            ydRotaryDrawPrize.setSort(AdminConstants.PRIZE_DEFAULT_SORT);
            ydRotaryDrawPrize.setWinRate(AdminConstants.PRIZE_DEFAULT_RATE);
            ydRotaryDrawPrizeDao.insertYdRotaryDrawPrize(ydRotaryDrawPrize);
        }
    }

    private void checkParams(YdRotaryDrawActivityResult params) {
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getTitle()),
                "error_no_title", "活动标题不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getTitle()),
                "error_no_rule", "活动规则不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getStartDateTime()),
                "error_no_start_time", "开始时间不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getEndDateTime()),
                "error_no_end_time", "结束时间不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getType()),
                "error_no_type", "活动类型不可以为空");
        ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getDrawCountType()),
                "error_no_draw_count_type", "抽奖次数类型不可以为空");

        params.setStartTime(DateUtils.getDateTime(params.getStartDateTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
        params.setEndTime(DateUtils.getDateTime(params.getEndDateTime(), DateUtils.DEFAULT_DATETIME_FORMAT));

    }


}

