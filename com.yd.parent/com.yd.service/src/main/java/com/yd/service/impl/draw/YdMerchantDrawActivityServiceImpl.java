package com.yd.service.impl.draw;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import com.yd.api.req.MerchantDrawReq;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.api.result.draw.YdMerchantDrawPrizeResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.user.YdUserResult;
import com.yd.api.service.draw.YdMerchantDrawActivityService;
import com.yd.api.service.draw.YdMerchantDrawPrizeService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.coupon.YdMerchantCoupon;
import com.yd.service.bean.coupon.YdUserCoupon;
import com.yd.service.bean.draw.YdMerchantDrawPrize;
import com.yd.service.bean.draw.YdUserDrawRecord;
import com.yd.service.dao.coupon.YdMerchantCouponDao;
import com.yd.service.dao.coupon.YdUserCouponDao;
import com.yd.service.dao.draw.YdMerchantDrawActivityDao;
import com.yd.service.dao.draw.YdMerchantDrawPrizeDao;
import com.yd.service.dao.draw.YdUserDrawRecordDao;
import com.yd.service.dao.order.YdUserOrderDao;
import com.yd.service.impl.coupon.YdUserCouponServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.bean.draw.YdMerchantDrawActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户抽奖活动Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:33:33
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantDrawActivityServiceImpl implements YdMerchantDrawActivityService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantDrawActivityServiceImpl.class);

	@Resource
	private YdUserOrderDao ydUserOrderDao;

	@Resource
	private YdUserService ydUserService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdUserCouponDao ydUserCouponDao;

    @Resource
    private YdMerchantCouponDao ydMerchantCouponDao;

	@Resource
	private YdUserDrawRecordDao ydUserDrawRecordDao;

	@Resource
	private YdMerchantDrawPrizeDao ydMerchantDrawPrizeDao;

	@Resource
	private YdMerchantDrawActivityDao ydMerchantDrawActivityDao;

	@Resource
	private YdMerchantDrawPrizeService ydMerchantDrawPrizeService;


	@Override
	public YdMerchantDrawActivityResult getYdMerchantDrawActivityById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantDrawActivityResult ydMerchantDrawActivityResult = null;
		YdMerchantDrawActivity ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityById(id);
		if (ydMerchantDrawActivity != null) {
			ydMerchantDrawActivityResult = new YdMerchantDrawActivityResult();
			BeanUtilExt.copyProperties(ydMerchantDrawActivityResult, ydMerchantDrawActivity);
		}
		return ydMerchantDrawActivityResult;
	}

	@Override
	public YdMerchantDrawActivityResult getYdMerchantDrawActivityByUdid(String uuid) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(uuid),
				"err_empty_uuid", "uuid不可以为空");

		YdMerchantDrawActivityResult ydMerchantDrawActivityResult = null;
		YdMerchantDrawActivity ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityByUuid(uuid);
		if (ydMerchantDrawActivity != null) {
			ydMerchantDrawActivityResult = new YdMerchantDrawActivityResult();
			BeanUtilExt.copyProperties(ydMerchantDrawActivityResult, ydMerchantDrawActivity);
		}
		return ydMerchantDrawActivityResult;
	}

	@Override
	public Page<YdMerchantDrawActivityResult> findYdMerchantDrawActivityListByPage(YdMerchantDrawActivityResult params, PagerInfo pagerInfo) throws BusinessException {
		// 校验商户信息
		YdMerchantResult ydStoreInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(ydStoreInfo.getId());

		Page<YdMerchantDrawActivityResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantDrawActivity ydMerchantDrawActivity = new YdMerchantDrawActivity();
		BeanUtilExt.copyProperties(ydMerchantDrawActivity, params);
		
		int amount = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityCount(ydMerchantDrawActivity);
		if (amount > 0) {
			List<YdMerchantDrawActivity> dataList = this.ydMerchantDrawActivityDao.findYdMerchantDrawActivityListByPage(
					ydMerchantDrawActivity, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdMerchantDrawActivityResult> resultList = DTOUtils.convertList(dataList, YdMerchantDrawActivityResult.class);
			resultList.forEach(data -> {
				YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
				ydUserDrawRecord.setActivityId(data.getId());
				ydUserDrawRecord.setMerchantId(data.getMerchantId());

				// 查询活动抽奖人数
				data.setDrawUserCount(ydUserDrawRecordDao.getActivityDrawCount(ydUserDrawRecord));

				// 查询活动中奖人数
				ydUserDrawRecord.setPrizeType("YHQ");
				data.setWinUserCount(ydUserDrawRecordDao.getActivityDrawCount(ydUserDrawRecord));
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantDrawActivityResult> getAll(YdMerchantDrawActivityResult ydMerchantDrawActivityResult) {
		YdMerchantDrawActivity ydMerchantDrawActivity = null;
		if (ydMerchantDrawActivityResult != null) {
			ydMerchantDrawActivity = new YdMerchantDrawActivity();
			BeanUtilExt.copyProperties(ydMerchantDrawActivity, ydMerchantDrawActivityResult);
		}
		List<YdMerchantDrawActivity> dataList = this.ydMerchantDrawActivityDao.getAll(ydMerchantDrawActivity);
		List<YdMerchantDrawActivityResult> resultList = DTOUtils.convertList(dataList, YdMerchantDrawActivityResult.class);
		return resultList;
	}

	@Override
	public void insertYdMerchantDrawActivity(YdMerchantDrawActivityResult ydMerchantDrawActivityResult) {
		if (null != ydMerchantDrawActivityResult) {
			ydMerchantDrawActivityResult.setCreateTime(new Date());
			ydMerchantDrawActivityResult.setUpdateTime(new Date());
			YdMerchantDrawActivity ydMerchantDrawActivity = new YdMerchantDrawActivity();
			BeanUtilExt.copyProperties(ydMerchantDrawActivity, ydMerchantDrawActivityResult);
			this.ydMerchantDrawActivityDao.insertYdMerchantDrawActivity(ydMerchantDrawActivity);
		}
	}
	
	@Override
	public void updateYdMerchantDrawActivity(YdMerchantDrawActivityResult ydMerchantDrawActivityResult) {
		if (null != ydMerchantDrawActivityResult) {
			ydMerchantDrawActivityResult.setUpdateTime(new Date());
			YdMerchantDrawActivity ydMerchantDrawActivity = new YdMerchantDrawActivity();
			BeanUtilExt.copyProperties(ydMerchantDrawActivity, ydMerchantDrawActivityResult);
			this.ydMerchantDrawActivityDao.updateYdMerchantDrawActivity(ydMerchantDrawActivity);
		}
	}

	/**
	 * 获取抽奖活动详情
	 * @param id 礼品id
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public YdMerchantDrawActivityResult getActivityDetail(Integer id) throws BusinessException{
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "id不可以为空");

		YdMerchantDrawActivity ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
				"err_not_exist", "活动不存在");
		YdMerchantDrawActivityResult ydMerchantDrawActivityResult = new YdMerchantDrawActivityResult();
		BeanUtilExt.copyProperties(ydMerchantDrawActivityResult, ydMerchantDrawActivity);

		YdMerchantDrawPrize ydMerchantDrawPrize = new YdMerchantDrawPrize();
		ydMerchantDrawPrize.setActivityId(id);
		List<YdMerchantDrawPrize> prizeList = ydMerchantDrawPrizeDao.getAll(ydMerchantDrawPrize);
        List<YdMerchantDrawPrizeResult> dataList = DTOUtils.convertList(prizeList, YdMerchantDrawPrizeResult.class);
        dataList.forEach(drawPrize -> {
            if ("YHQ".equalsIgnoreCase(drawPrize.getPrizeType()) && drawPrize.getCouponId() != null) {
                YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(drawPrize.getCouponId());
                if (merchantCoupon != null) {
                    drawPrize.setCouponTitle(merchantCoupon.getCouponTitle());
                }
            }
        });
		ydMerchantDrawActivityResult.setPrizeList(dataList);
		return ydMerchantDrawActivityResult;
	}

	/**
	 * 删除抽奖活动
	 * @param currMerchantId
	 * @param id
	 * @throws BusinessException
	 */
	@Override
	public void deleteYdMerchantDrawActivity(Integer currMerchantId, Integer id) throws BusinessException {
		// 校验商户信息
		YdMerchantResult ydStoreInfo = ydMerchantService.getStoreInfo(currMerchantId);
		currMerchantId = ydStoreInfo.getId();

		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "id不可以为空");

		YdMerchantDrawActivity ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
				"err_not_exist", "活动不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantDrawActivity.getIsEnable()),
				"err_not_delete", "上架中的活动不可以删除");

        ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantDrawActivity.getIsFlag()),
                "err_flag", "活动已经删除，不可以再次删除");

		// 删除抽奖活动, 逻辑删除
		ydMerchantDrawActivity.setIsFlag("Y");
		this.ydMerchantDrawActivityDao.updateYdMerchantDrawActivity(ydMerchantDrawActivity);
	}

	/**
	 * 活动新增修改
	 * @param currMerchantId
	 * @param req
	 * @throws BusinessException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Integer currMerchantId, MerchantDrawReq req) throws BusinessException {
		// 校验商户信息
		YdMerchantResult ydStoreInfo = ydMerchantService.getStoreInfo(currMerchantId);
		currMerchantId = ydStoreInfo.getId();

		YdMerchantDrawActivityResult ydMerchantDrawActivityResult = req.getDrawActivity();
		ydMerchantDrawActivityResult.setMerchantId(currMerchantId);
		List<YdMerchantDrawPrizeResult> prizeList = req.getPrizeList();

		// 校验活动， 校验奖品入参
		checkDrawActivityParams(ydMerchantDrawActivityResult);
		checkDrawDrawPrizeParams(ydMerchantDrawActivityResult.getActivityType(), prizeList);

		// 判断新增修改
		Date nowDate = new Date();
		YdMerchantDrawActivity ydMerchantDrawActivity = null;

		if (ydMerchantDrawActivityResult.getId() == null || ydMerchantDrawActivityResult.getId() <= 0) {
			ydMerchantDrawActivity = new YdMerchantDrawActivity();
			BeanUtilExt.copyProperties(ydMerchantDrawActivity, ydMerchantDrawActivityResult);
			// 未删除
			ydMerchantDrawActivity.setIsFlag("N");
			// 未上架
			ydMerchantDrawActivity.setIsEnable("N");
			ydMerchantDrawActivity.setUuid(UUIDUtil.getUUID());
			ydMerchantDrawActivity.setCreateTime(nowDate);
			this.ydMerchantDrawActivityDao.insertYdMerchantDrawActivity(ydMerchantDrawActivity);

            // 保存奖品
            Integer activityId = ydMerchantDrawActivity.getId();
            saveActivityPrize(activityId, prizeList);
		} else {
			ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityById(
					ydMerchantDrawActivityResult.getId());
			ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
					"err_not_exist", "活动不存在");

			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantDrawActivity.getIsEnable()),
					"err_not_update", "上架中的活动不可以编辑");

			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantDrawActivity.getIsFlag()),
					"err_not_update", "删除的活动不可以编辑");

			ydMerchantDrawActivity = new YdMerchantDrawActivity();
			BeanUtilExt.copyProperties(ydMerchantDrawActivity, ydMerchantDrawActivityResult);
			this.ydMerchantDrawActivityDao.updateYdMerchantDrawActivity(ydMerchantDrawActivity);

			// 删除奖品
			this.ydMerchantDrawPrizeDao.deleteYdMerchantDrawPrize(currMerchantId, ydMerchantDrawActivityResult.getId());

			// 保存奖品
			Integer activityId = ydMerchantDrawActivity.getId();
			saveActivityPrize(activityId, prizeList);
		}
	}

	/**
	 * 上下架活动
	 * @param currMerchantId	商户id
	 * @param id	活动id
	 * @param isEnable	Y | N
	 * @throws BusinessException
	 */
	@Override
	public void upOrDownActivity(Integer currMerchantId, Integer id, String isEnable) throws BusinessException {
		// 校验商户信息
		YdMerchantResult ydStoreInfo = ydMerchantService.getStoreInfo(currMerchantId);
		currMerchantId = ydStoreInfo.getId();

		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(isEnable),
				"err_empty_is_enable", "上下架状态不可以为空");

		ValidateBusinessUtils.assertFalse(!("Y".equalsIgnoreCase(isEnable) || "N".equalsIgnoreCase(isEnable)),
				"err_is_enable", "上下架状态不正确");

		YdMerchantDrawActivity ydMerchantDrawActivity = this.ydMerchantDrawActivityDao.getYdMerchantDrawActivityById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
				"err_not_exist", "活动不存在");

		if ("Y".equalsIgnoreCase(isEnable)) {
			ValidateBusinessUtils.assertFalse(isEnable.equalsIgnoreCase(ydMerchantDrawActivity.getIsEnable()),
					"err_is_enable", "活动已经上架");

			// 查询是否有上架的
			YdMerchantDrawActivity drawActivity = new YdMerchantDrawActivity();
			drawActivity.setIsFlag("N");
			drawActivity.setIsEnable("Y");
			drawActivity.setMerchantId(currMerchantId);
			drawActivity.setActivityType(ydMerchantDrawActivity.getActivityType());
			List<YdMerchantDrawActivity> activityList = this.ydMerchantDrawActivityDao.getAll(drawActivity);
			ValidateBusinessUtils.assertFalse(activityList.size() > 0,
					"err_is_enable", "已经有上架的活动");
		} else {
			ValidateBusinessUtils.assertFalse(isEnable.equalsIgnoreCase(ydMerchantDrawActivity.getIsEnable()),
					"err_is_enable", "活动已经下架");
		}

		// 上下架抽奖活动
		ydMerchantDrawActivity.setIsEnable(isEnable);
		this.ydMerchantDrawActivityDao.updateYdMerchantDrawActivity(ydMerchantDrawActivity);
	}


	/**
	 * 用户点击抽奖
	 * @param userId	 用户id
	 * @param uuid		 活动uuid
	 * @throws BusinessException
	 */
	@Override
	public Integer clickDraw(Integer userId, String uuid) throws BusinessException {
		// 校验用户信息
		YdUserResult ydUserResult = ydUserService.checkUserInfo(userId);

		// 用户抽奖，保存抽奖记录
		YdMerchantDrawActivity ydMerchantDrawActivity = checkDrawActivityInfo(uuid);

		Integer merchantId = ydMerchantDrawActivity.getMerchantId();

		// 校验抽奖次数
		Integer canUseCount = getUserDrawCount(userId, uuid);
		ValidateBusinessUtils.assertFalse(canUseCount <= 0,
				"err_not_draw_count", "您没有抽奖次数");

		return userStartDraw(merchantId, ydUserResult, ydMerchantDrawActivity.getId());
	}

	/**
	 * 获取用户可抽奖次数
	 * @param userId	 用户id
	 * @param uuid		 活动uuid
	 * @return integer
	 * @throws BusinessException
	 */
	@Override
	public Integer getUserDrawCount(Integer userId, String uuid) throws BusinessException {
		// 校验用户信息
		ydUserService.checkUserInfo(userId);

		// 校验抽奖信息
		YdMerchantDrawActivity ydMerchantDrawActivity = checkDrawActivityInfo(uuid);

		Integer merchantId = ydMerchantDrawActivity.getMerchantId();
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(uuid),
				"err_user_type", "您是平台的老用户，暂时不可以抽奖");

		// 判断用户抽奖类型, ALL 所有用户, NEW (新用户，未下单过的用户)
		if ("NEW".equalsIgnoreCase(ydMerchantDrawActivity.getDrawUserType())) {
			Integer orderCompleteCount = ydUserOrderDao.getCompleteOrderUserCount(userId, merchantId,
					YdUserOrderStatusEnum.SUCCESS.getCode(), null, null);
			ValidateBusinessUtils.assertFalse(orderCompleteCount > 0,
					"err_user_type", "您是平台的老用户，暂时不可以抽奖");
		}

		// 判断抽奖次数类型,  ALL(整个活动一共抽奖) DAY(每日抽奖次数)
		String startTime = null, endTime = null;
		Integer drawCount = ydMerchantDrawActivity.getDrawCount();
		if ("DAY".equalsIgnoreCase(ydMerchantDrawActivity.getDrawCountType())) {
			// 查询用户今日抽奖次数
			startTime = DateUtils.datetime(new Date(), DateUtils.DEFAULT_DATE_FORMAT);
			endTime = DateUtils.datetime(DateUtils.addDays(new Date(), 1), DateUtils.DEFAULT_DATE_FORMAT);
			ydUserDrawRecordDao.getYdUserDrawCount(userId, merchantId, ydMerchantDrawActivity.getId(), null, null);
		}
		Integer drawUseCount = ydUserDrawRecordDao.getYdUserDrawCount(userId, merchantId, ydMerchantDrawActivity.getId(), startTime, endTime);
		Integer canUseCount = drawCount - drawUseCount;
		return canUseCount > 0 ? canUseCount : 0;
	}

	// ---------------------------------   私有方法    ---------------------------

    /**
     * 保存奖品
     * @param activityId
     * @param prizeList
     */
    private void saveActivityPrize(Integer activityId, List<YdMerchantDrawPrizeResult> prizeList) {
        Date nowDate = new Date();
        for (int i = 0; i < prizeList.size(); i++) {
            YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult = prizeList.get(i);
            YdMerchantDrawPrize ydMerchantDrawPrize = new YdMerchantDrawPrize();
            BeanUtilExt.copyProperties(ydMerchantDrawPrize, ydMerchantDrawPrizeResult);
            ydMerchantDrawPrize.setCreateTime(nowDate);
            ydMerchantDrawPrize.setActivityId(activityId);
            ydMerchantDrawPrize.setSort(i + 1);
            this.ydMerchantDrawPrizeDao.insertYdMerchantDrawPrize(ydMerchantDrawPrize);
        }
    }

	/**
	 * 校验活动奖品入参
	 * @param activityType
	 * @param prizeList
	 * @throws BusinessException
	 */
	private void checkDrawDrawPrizeParams(String activityType, List<YdMerchantDrawPrizeResult> prizeList) throws BusinessException {

		if ("DZP".equalsIgnoreCase(activityType)) {
			ValidateBusinessUtils.assertFalse(prizeList.size() != 8,
					"err_prize_size", "请传入正确的奖品数量");
		} else if ("JJG".equalsIgnoreCase(activityType)) {
			ValidateBusinessUtils.assertFalse(prizeList.size() != 8,
					"err_prize_size", "请传入正确的奖品数量");
		} else if ("CHB".equalsIgnoreCase(activityType)) {
			ValidateBusinessUtils.assertFalse(prizeList.size() != 5,
					"err_prize_size", "请传入正确的奖品数量");
		}

		prizeList.forEach(ydMerchantDrawPrizeResult -> {
			String prizeType = ydMerchantDrawPrizeResult.getPrizeType();
			Integer couponId = ydMerchantDrawPrizeResult.getCouponId();
			Double winRate = ydMerchantDrawPrizeResult.getWinRate();

			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(prizeType),
					"err_empty_prize_type", "奖品类型不可以为空");

			ValidateBusinessUtils.assertFalse(!("WZJ".equalsIgnoreCase(prizeType) || "YHQ".equalsIgnoreCase(prizeType)),
					"err_prize_type", "奖品类型不正确");

			if ("YHQ".equalsIgnoreCase(prizeType)) {
				ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
						"err_empty_coupon_id", "优惠券id不可以为空");

                YdMerchantCoupon ydMerchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(couponId);
                ValidateBusinessUtils.assertFalse(ydMerchantCoupon == null,
                        "err_not_exist_coupon", "优惠券不存在");

            }

			ValidateBusinessUtils.assertFalse(winRate == null,
					"err_empty_win_rate", "中奖概率不可以为空");

			ValidateBusinessUtils.assertFalse(winRate > 100 || winRate < 0,
					"err_win_rate", "中奖概率不能小于0 并且不能大于100");

			// 判断中奖概率是否小于 0.01
			if (winRate != 0) {
				ValidateBusinessUtils.assertFalse(winRate * 100 < 1,
						"err_win_rate", "中奖概率不能小于0");
			}

		});
		Double winRate = prizeList.stream().mapToDouble(YdMerchantDrawPrizeResult :: getWinRate).sum();
		ValidateBusinessUtils.assertFalse(winRate != 100,
				"err_empty_win_rate", "中奖概率总和必须等于100");
	}

	/**
	 * 校验活动入参
	 * @param params
	 * @throws BusinessException
	 */
	private void checkDrawActivityParams(YdMerchantDrawActivityResult params) throws BusinessException{
		ValidateBusinessUtils.assertFalse(params.getMerchantId() == null || params.getMerchantId() <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getBannerUrl()),
				"err_empty_banner_url", "活动bannerUrl不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getStartTimeStr()),
				"err_empty_start_time", "开始时间不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getEndTimeStr()),
				"err_empty_end_time", "结束时间不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getDrawUserType()),
				"err_empty_draw_user_type", "用户抽奖身份可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getDrawCountType()),
				"err_empty_draw_count_type", "用户抽奖次数类型不可以为空");

		ValidateBusinessUtils.assertFalse(params.getDrawCount() == null,
				"err_empty_draw_count", "用户抽奖次数不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getRules()),
				"err_empty_rules", "抽奖规则不可以为空");

		params.setStartTime(DateUtils.getDateFromStr(params.getStartTimeStr()));
		params.setEndTime(DateUtils.getDateFromStr(params.getEndTimeStr()));
	}

	/**
	 * 校验抽奖信息
	 * @param uuid
	 */
	private YdMerchantDrawActivity checkDrawActivityInfo(String uuid) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(uuid),
				"err_empty_uuid", "活动uuid不可以为空");

		// 校验活动信息
		YdMerchantDrawActivity ydMerchantDrawActivity = ydMerchantDrawActivityDao.getYdMerchantDrawActivityByUuid(uuid);
		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity == null,
				"err_not_exist_activity", "活动不存在");

		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydMerchantDrawActivity.getIsEnable()),
				"error_is_enable", "活动未上架，暂不可用");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantDrawActivity.getIsFlag()),
				"error_is_flag", "活动不存在，暂不可用");

		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity.getStartTime().getTime() > new Date().getTime(),
				"error_no_activity", "活动尚未开始");

		ValidateBusinessUtils.assertFalse(ydMerchantDrawActivity.getEndTime().getTime() < new Date().getTime(),
				"error_no_activity", "活动已经结束");
		return ydMerchantDrawActivity;
	}


	/**
	 * 用户开始抽奖
	 * @param merchantId	商户id
	 * @param ydUserResult	用户信息
	 * @param activityId	活动id
	 * @return
	 */
	private Integer userStartDraw(Integer merchantId, YdUserResult ydUserResult, Integer activityId) {
		// 查询活动所有的奖品
		YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult = new YdMerchantDrawPrizeResult();
		ydMerchantDrawPrizeResult.setActivityId(activityId);
		List<YdMerchantDrawPrizeResult> prizeList = ydMerchantDrawPrizeService.getAll(ydMerchantDrawPrizeResult);

		// 查询奖品是未中奖的奖品
		ydMerchantDrawPrizeResult.setPrizeType("WZJ");
		List<YdMerchantDrawPrizeResult> noPrizeList = ydMerchantDrawPrizeService.getAll(ydMerchantDrawPrizeResult);

		// 根据权重算法计算中奖几率, 比率如示例; 10.01%,乘100肯定为正整数
		int sumRate = 0;
		for (YdMerchantDrawPrizeResult data : prizeList) {
			sumRate += data.getWinRate() * 100;
		}

		int number = 0;
		YdUserCouponResult ydUserCouponResult = new YdUserCouponResult();
		Integer random = new Random().nextInt(sumRate);
		Integer prizeId = 0;
		for (YdMerchantDrawPrizeResult data : prizeList) {
			if (number <= random && random < number + data.getWinRate() * 100) {
				// 中奖后判断是否是优惠券， 是优惠券券的话校验是否可以给用户发放, 发放成功的话将优惠券发放id存到中奖纪录里面
				Date nowDate = new Date();
				YdUserDrawRecord ydUserDrawRecord = new YdUserDrawRecord();
				ydUserDrawRecord.setCreateTime(nowDate);
				ydUserDrawRecord.setMerchantId(merchantId);
				ydUserDrawRecord.setUserId(ydUserResult.getId());

				ydUserDrawRecord.setActivityId(activityId);
				ydUserDrawRecord.setPrizeId(data.getId());
				ydUserDrawRecord.setPrizeType(data.getPrizeType());
				ydUserDrawRecord.setNickname(ydUserResult.getNickname());
				ydUserDrawRecord.setMobile(ydUserResult.getMobile());

				YdUserCoupon ydUserCoupon = new YdUserCoupon();
				if ("YHQ".equalsIgnoreCase(data.getPrizeType())) {
					YdMerchantCoupon ydMerchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(data.getCouponId());
					String validType = ydMerchantCoupon.getValidType();
					// 判断是否下架，未上架设置未中奖
					if ("N".equalsIgnoreCase(ydMerchantCoupon.getIsShelf())) {
						prizeId = getRandomNoPrizeId(noPrizeList);
						ydUserDrawRecord.setPrizeType("WZJ");
						ydUserDrawRecord.setPrizeId(prizeId);
						ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
						break;
					}

					// 判断优惠券是否过期,过期了设置未中奖
					if ("DATE_RANGE".equalsIgnoreCase(validType)) {
						if (ydMerchantCoupon.getValidStartTime().getTime() > nowDate.getTime() ||
								ydMerchantCoupon.getValidEndTime().getTime() < nowDate.getTime()) {
							prizeId = getRandomNoPrizeId(noPrizeList);
							ydUserDrawRecord.setPrizeType("WZJ");
							ydUserDrawRecord.setPrizeId(prizeId);
							ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
							break;
						}
					}

					// 判断优惠券发放数量
					if (ydMerchantCoupon.getCouponAmount() > 0) {
						YdUserCoupon couponParams = new YdUserCoupon();
						couponParams.setCouponId(ydMerchantCoupon.getId());
						int sendCount = ydUserCouponDao.getYdUserCouponCount(couponParams);
						if (ydMerchantCoupon.getCouponAmount()  <= sendCount) {
							prizeId = getRandomNoPrizeId(noPrizeList);
							ydUserDrawRecord.setPrizeType("WZJ");
							ydUserDrawRecord.setPrizeId(prizeId);
							ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
							break;
						}
					}

					// 判断用户领取数量, 不可以领取了设置未中奖
					int limitCount = ydMerchantCoupon.getLimitAmount();
					YdUserCoupon couponParams = new YdUserCoupon();
					couponParams.setUserId(ydUserResult.getId());
					couponParams.setCouponId(ydMerchantCoupon.getId());
					int userCouponCount = ydUserCouponDao.getYdUserCouponCount(couponParams);
					if (limitCount  <= userCouponCount) {
						prizeId = getRandomNoPrizeId(noPrizeList);
						ydUserDrawRecord.setPrizeType("WZJ");
						ydUserDrawRecord.setPrizeId(prizeId);
						ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
						break;
					}

					ydUserCoupon.setCreateTime(nowDate);
					ydUserCoupon.setMerchantId(merchantId);
					ydUserCoupon.setUserId(ydUserResult.getId());
					ydUserCoupon.setNickname(ydUserResult.getNickname());
					ydUserCoupon.setMobile(ydUserResult.getMobile());

					ydUserCoupon.setCouponId(ydMerchantCoupon.getId());
					ydUserCoupon.setCouponTitle(ydMerchantCoupon.getCouponTitle());
					ydUserCoupon.setCouponType(ydMerchantCoupon.getCouponType());
					ydUserCoupon.setCouponPrice(ydMerchantCoupon.getCouponPrice());
					ydUserCoupon.setCouponDesc(ydMerchantCoupon.getCouponDesc());
					ydUserCoupon.setUseConditionPrice(ydMerchantCoupon.getUseConditionPrice());
					ydUserCoupon.setUseRangeType(ydMerchantCoupon.getUseRangeType());
					ydUserCoupon.setCanUseItemIds(ydMerchantCoupon.getCanUseItemIds());
					ydUserCoupon.setUseStatus("N");

					// 设置优惠券有效时间

					logger.info("====validType=" + validType);
					if ("DATE_RANGE".equalsIgnoreCase(validType)) {
						logger.info("====DATE_RANGE");
						ydUserCoupon.setValidStartTime(ydMerchantCoupon.getValidStartTime());
						ydUserCoupon.setValidEndTime(ydMerchantCoupon.getValidEndTime());
					} else if ("TODAY".equalsIgnoreCase(validType)) {
						// 今日有效，结束时间设置为第二天凌晨 00:00:00
						logger.info("====TODAY");
						ydUserCoupon.setValidStartTime(nowDate);
						ydUserCoupon.setValidEndTime(DateUtils.getDayStartDate(DateUtils.addDays(nowDate, 1)));
					} else if ("FIX_DAY".equalsIgnoreCase(validType)) {
						// 领取后几日有效
						logger.info("====validType");
						ydUserCoupon.setValidStartTime(new Date());
						ydUserCoupon.setValidEndTime(DateUtils.addDays(nowDate, ydMerchantCoupon.getValidDay()));
					}

					ydUserCouponDao.insertYdUserCoupon(ydUserCoupon);
				}
				ydUserDrawRecord.setUserCouponId(ydUserCoupon.getId());
				ydUserDrawRecordDao.insertYdUserDrawRecord(ydUserDrawRecord);
				prizeId = data.getId();
				break;
			}
			number += data.getWinRate() * 100;
		}
		return prizeId;
	}

	private Integer getRandomNoPrizeId(List<YdMerchantDrawPrizeResult> noPrizeList) {
		if (CollectionUtils.isEmpty(noPrizeList)) return 0;

		int size = noPrizeList.size();
		int random = new Random().nextInt(size);
		return noPrizeList.get(random).getId();
	}

}

