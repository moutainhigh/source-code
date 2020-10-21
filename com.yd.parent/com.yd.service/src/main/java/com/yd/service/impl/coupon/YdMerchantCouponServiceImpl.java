package com.yd.service.impl.coupon;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.item.YdMerchantItemResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.coupon.YdMerchantCouponService;
import com.yd.api.service.coupon.YdUserCouponService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.coupon.YdUserCoupon;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.dao.coupon.YdUserCouponDao;
import com.yd.service.dao.item.YdMerchantItemDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.coupon.YdMerchantCouponDao;
import com.yd.service.bean.coupon.YdMerchantCoupon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户优惠券Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-26 11:40:33
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantCouponServiceImpl implements YdMerchantCouponService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantCouponServiceImpl.class);

	@Resource
	private YdUserCouponDao ydUserCouponDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdMerchantCouponDao ydMerchantCouponDao;

	@Resource
	private YdUserCouponService ydUserCouponService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantCouponResult getYdMerchantCouponById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantCouponResult ydMerchantCouponResult = null;
		YdMerchantCoupon ydMerchantCoupon = this.ydMerchantCouponDao.getYdMerchantCouponById(id);
		if (ydMerchantCoupon != null) {
			ydMerchantCouponResult = new YdMerchantCouponResult();
			BeanUtilExt.copyProperties(ydMerchantCouponResult, ydMerchantCoupon);

			// 查询已使用数量
			YdUserCoupon ydUserCoupon = new YdUserCoupon();
			ydUserCoupon.setCouponId(ydMerchantCouponResult.getId());
			ydUserCoupon.setUseStatus("Y");
			int useCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
			ydUserCoupon.setUseStatus("N");
			int noUseCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
			ydMerchantCouponResult.setUseCount(useCount);
			ydMerchantCouponResult.setNoUseCount(noUseCount);
			ydMerchantCouponResult.setReceiveCount(useCount + noUseCount);

			// 将可使用商品ids转换成list
			if (StringUtils.isNotEmpty(ydMerchantCouponResult.getCanUseItemIds())) {
				List<Integer> canUseIdList = Arrays.stream(StringUtils.split(ydMerchantCouponResult.getCanUseItemIds(), ","))
						.map(Integer::valueOf).collect(Collectors.toList());
				List<YdMerchantItem> itemList = ydMerchantItemDao.findYdMerchantItemListByIds(canUseIdList);
				ydMerchantCouponResult.setItemList(DTOUtils.convertList(itemList, YdMerchantItemResult.class));
			}
		}
		return ydMerchantCouponResult;
	}

	@Override
	public Page<YdMerchantCouponResult> findYdMerchantCouponListByPage(YdMerchantCouponResult params, PagerInfo pagerInfo) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		Page<YdMerchantCouponResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantCoupon ydMerchantCoupon = new YdMerchantCoupon();
		BeanUtilExt.copyProperties(ydMerchantCoupon, params);
		
		int amount = this.ydMerchantCouponDao.getYdMerchantCouponCount(ydMerchantCoupon);
		if (amount > 0) {
			List<YdMerchantCoupon> dataList = this.ydMerchantCouponDao.findYdMerchantCouponListByPage(ydMerchantCoupon,
					pagerInfo.getStart(), pagerInfo.getPageSize());

			List<YdMerchantCouponResult> resultList = DTOUtils.convertList(dataList, YdMerchantCouponResult.class);
			// 查询已使用数量
			resultList.forEach(couponResult -> {
				// 查询已使用的优惠券
				YdUserCoupon ydUserCoupon = new YdUserCoupon();
				ydUserCoupon.setCouponId(couponResult.getId());
				ydUserCoupon.setUseStatus("Y");
				int useCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);

				// 查询未使用的优惠券
				ydUserCoupon.setUseStatus("N");
				int noUseCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);

				// 查询使用中的优惠券
				ydUserCoupon.setUseStatus("IN");
				int inUseCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);

				couponResult.setUseCount(useCount);
				couponResult.setNoUseCount(noUseCount);
				couponResult.setInUseCount(inUseCount);
				couponResult.setReceiveCount(useCount + noUseCount + inUseCount);
			});
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantCouponResult> getAll(YdMerchantCouponResult ydMerchantCouponResult) {
		YdMerchantCoupon ydMerchantCoupon = null;
		if (ydMerchantCouponResult != null) {
			ydMerchantCoupon = new YdMerchantCoupon();
			BeanUtilExt.copyProperties(ydMerchantCoupon, ydMerchantCouponResult);
		}
		List<YdMerchantCoupon> dataList = this.ydMerchantCouponDao.getAll(ydMerchantCoupon);
		List<YdMerchantCouponResult> resultList = DTOUtils.convertList(dataList, YdMerchantCouponResult.class);
		return resultList;
	}

    /**
     * 商户添加优惠券
     * @param ydMerchantCouponResult
     * @throws BusinessException
     */
    @Override
    public void insertYdMerchantCoupon(YdMerchantCouponResult ydMerchantCouponResult) throws BusinessException {
        checkCouponParams(ydMerchantCouponResult);
        ydMerchantCouponResult.setCreateTime(new Date());
        YdMerchantCoupon ydMerchantCoupon = new YdMerchantCoupon();
        BeanUtilExt.copyProperties(ydMerchantCoupon, ydMerchantCouponResult);
        this.ydMerchantCouponDao.insertYdMerchantCoupon(ydMerchantCoupon);
    }

    /**
     * 商户修改优惠券
     * @param ydMerchantCouponResult
     * @throws BusinessException
     */
    @Override
	public void updateYdMerchantCoupon(YdMerchantCouponResult ydMerchantCouponResult) throws BusinessException {
		YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(ydMerchantCouponResult.getId());
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");
		checkCouponParams(ydMerchantCouponResult);

		merchantCoupon.setUpdateTime(new Date());
		YdMerchantCoupon ydMerchantCoupon = new YdMerchantCoupon();
		BeanUtilExt.copyProperties(ydMerchantCoupon, ydMerchantCouponResult);
		this.ydMerchantCouponDao.updateYdMerchantCoupon(ydMerchantCoupon);
	}

	/**
	 * 商户优惠券增加发行量
	 * @param couponId		优惠券id
	 * @param num			增加的优惠券数量
	 * @throws BusinessException
	 */
	@Override
	public void addCouponAmount(Integer merchantId, Integer couponId, Integer num) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		ValidateBusinessUtils.assertFalse(num == null || num <= 0,
				"err_add_num", "请输入正确的数量");

		YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");


		ValidateBusinessUtils.assertFalse(merchantCoupon.getCouponAmount() == 0,
				"err_coupon_amount", "优惠券类型是无限量，不用增加对应数量");

		this.ydMerchantCouponDao.addCouponAmount(couponId, num);
	}

	/**
	 * 查询商户可用优惠券列表
	 * @param itemId		商品id
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @return
	 */
	@Override
	public List<YdMerchantCouponResult> findFrontMerchantCouponList(Integer itemId, Integer userId, Integer merchantId) {
		// 查询可用优惠券
		List<YdMerchantCoupon> couponList = ydMerchantCouponDao.findFrontMerchantCouponList(merchantId, itemId);
		List<YdMerchantCouponResult> resultList = DTOUtils.convertList(couponList, YdMerchantCouponResult.class);

		// 查询用户领取张数
		resultList.forEach(ydMerchantCoupon -> {
			YdUserCoupon ydUserCoupon = new YdUserCoupon();
			ydUserCoupon.setUserId(userId);
			ydUserCoupon.setCouponId(ydMerchantCoupon.getId());
			ydMerchantCoupon.setReceiveCount(ydUserCouponDao.getYdUserCouponCount(ydUserCoupon));
		});
		return resultList;
	}

	/**
	 * 用户领取优惠券
	 * @param couponId		优惠券id
	 * @param userId		用户id
	 * @param merchantId	商户id
	 * @return
	 */
	@Override
	public void receiveCoupon(Integer couponId, Integer userId, Integer merchantId) throws BusinessException {
		// 判断优惠券是否存在
		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(merchantCoupon.getIsShelf()),
				"err_is_shelf", "优惠券未上架");

		ValidateBusinessUtils.assertFalse("ACTIVITY".equalsIgnoreCase(merchantCoupon.getCouponResource()),
				"err_coupon_resource", "优惠券不可领取");

		if ("DATE_RANGE".equalsIgnoreCase(merchantCoupon.getValidType())) {
			long startTime = merchantCoupon.getValidStartTime().getTime();
			long endTime = merchantCoupon.getValidEndTime().getTime();
			long nowTime = new Date().getTime();
			ValidateBusinessUtils.assertFalse(startTime > nowTime || endTime < nowTime,
					"err_date_range", "优惠券不可领取");
		}

		// 校验发行量
		YdUserCoupon ydUserCoupon = new YdUserCoupon();
		ydUserCoupon.setCouponId(couponId);
		ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
		int sendCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
		if (merchantCoupon.getCouponAmount() != 0) {
			ValidateBusinessUtils.assertFalse(merchantCoupon.getCouponAmount() <= sendCount,
					"err_more_limit", "非常抱歉，优惠券已经被领完，您不可以领取");
		}

		// 校验用户是否超领
		ydUserCoupon.setUserId(userId);
		int receiveCount = ydUserCouponDao.getYdUserCouponCount(ydUserCoupon);
		ValidateBusinessUtils.assertFalse(merchantCoupon.getLimitAmount() <= receiveCount,
				"err_more_limit", "优惠券领取达到上限");

		// 保存领取记录
		YdMerchantCouponResult ydMerchantCouponResult = new YdMerchantCouponResult();
		BeanUtilExt.copyProperties(ydMerchantCouponResult, merchantCoupon);
		this.ydUserCouponService.insertYdUserCoupon(userId, ydMerchantCouponResult);
	}

	/**
	 * 商户删除优惠券
	 * @param merchantId 商户id
	 * @param couponId	 优惠券id
	 * @throws BusinessException
	 */
	@Override
	public void deleteMerchantCoupon(Integer merchantId, Integer couponId) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		ydMerchantCouponDao.deleteMerchantCoupon(couponId);
	}

	/**
	 * 商户上下架优惠券
	 * @param merchantId 商户id
	 * @param couponId	 优惠券id
	 * @param isShelf	 Y: 上架, N: 下架
	 * @throws BusinessException
	 */
	@Override
	public void upOrDownMerchantCoupon(Integer merchantId, Integer couponId, String isShelf) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(couponId == null || couponId <= 0,
				"err_coupon_id", "优惠券id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(isShelf),
				"err_is_enable_empty", "上下架状态不可以为空");

		YdMerchantCoupon merchantCoupon = ydMerchantCouponDao.getYdMerchantCouponById(couponId);
		ValidateBusinessUtils.assertFalse(merchantCoupon == null,
				"err_empty_coupon_title", "优惠券不存在");

		merchantCoupon.setIsShelf(isShelf);
		ydMerchantCouponDao.updateYdMerchantCoupon(merchantCoupon);
	}

	//----------------------------   private method   ----------------------------
	private void checkCouponParams(YdMerchantCouponResult params) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(params.getMerchantId());
		params.setMerchantId(storeInfo.getId());

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCouponTitle()),
				"err_empty_coupon_title", "优惠券标题不可以为空");

		ValidateBusinessUtils.assertFalse(params.getLimitAmount() == null || params.getLimitAmount() <= 0,
				"err_empty_limit_amount", "每人限领张数不可以为空");

		ValidateBusinessUtils.assertFalse(params.getCouponAmount() == null || params.getCouponAmount() < 0,
				"err_empty_coupon_amount", "发行量不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCouponType()),
				"err_empty_coupon_type", "优惠券类型不可以为空");

		ValidateBusinessUtils.assertFalse(params.getCouponPrice() == null || params.getCouponPrice() <= 0,
				"err_empty_coupon_price", "优惠券面值不可以为空");

		ValidateBusinessUtils.assertFalse(params.getUseConditionPrice() == null,
				"err_empty_coupon_use_condition", "优惠券使用条件不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getValidType()),
				"err_empty_valid_type", "有效期限不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getUseRangeType()),
				"err_empty_range_type", "优惠券适用商品不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getIsShelf()),
				"err_empty_is_shelf", "是否上架不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCouponResource()),
				"err_empty_source", "适用场景不可以为空");

		// DATE_RANGE(日期范围), TODAY(领取当天), FIX_DAY(固定天数)
		String validType = params.getValidType();
		logger.info("====validType=" + validType);
		if ("DATE_RANGE".equalsIgnoreCase(validType)) {
			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getStartTime())
					|| StringUtils.isEmpty(params.getEndTime()),"err_empty_date_range", "日期范围不可以为空");

			params.setValidStartTime(DateUtils.getDateFromStr(params.getStartTime()));
			params.setValidEndTime(DateUtils.getDateFromStr(params.getEndTime()));
			params.setValidDay(null);
		}else if ("FIX_DAY".equalsIgnoreCase(validType)) {
			ValidateBusinessUtils.assertFalse(params.getValidDay() == null || params.getValidDay() <= 0,
					"err_empty_valid_day", "领取后有效日期不可以为空");
			params.setValidStartTime(null);
			params.setValidEndTime(null);
		} else {
			params.setValidStartTime(null);
			params.setValidEndTime(null);
			params.setValidDay(null);
		}

		String useRangeType = params.getUseRangeType();
		if ("ALL".equalsIgnoreCase(useRangeType)) {
			params.setCanUseItemIds(null);
		} else if ("ITEM".equalsIgnoreCase(useRangeType)) {
			ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getCanUseItemIds()),
					"err_empty_use_items", "请选择可使用的商品");
		}
	}

}

