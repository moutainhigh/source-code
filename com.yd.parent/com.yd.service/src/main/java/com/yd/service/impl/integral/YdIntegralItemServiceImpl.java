package com.yd.service.impl.integral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.integral.YdIntegralItemResult;
import com.yd.api.service.integral.YdIntegralItemService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.integral.YdIntegralConfig;
import com.yd.service.dao.integral.YdIntegralConfigDao;
import com.yd.service.impl.gift.YdMerchantGiftServiceImpl;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.integral.YdIntegralItemDao;
import com.yd.service.bean.integral.YdIntegralItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:积分商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-27 11:16:34
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdIntegralItemServiceImpl implements YdIntegralItemService {

	private static final Logger logger = LoggerFactory.getLogger(YdIntegralItemServiceImpl.class);

	@Resource
	private YdIntegralItemDao ydIntegralItemDao;

	@Resource
	private YdIntegralConfigDao ydIntegralConfigDao;

	@Override
	public YdIntegralItemResult getYdIntegralItemById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdIntegralItemResult ydIntegralItemResult = null;
		YdIntegralItem ydIntegralItem = this.ydIntegralItemDao.getYdIntegralItemById(id);
		if (ydIntegralItem != null) {
			ydIntegralItemResult = new YdIntegralItemResult();
			BeanUtilExt.copyProperties(ydIntegralItemResult, ydIntegralItem);
		}
		return ydIntegralItemResult;
	}

	@Override
	public Page<YdIntegralItemResult> findYdIntegralItemListByPage(YdIntegralItemResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdIntegralItemResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdIntegralItem ydIntegralItem = new YdIntegralItem();
		BeanUtilExt.copyProperties(ydIntegralItem, params);
		
		int amount = this.ydIntegralItemDao.getYdIntegralItemCount(ydIntegralItem);
		if (amount > 0) {
			List<YdIntegralItem> dataList = this.ydIntegralItemDao.findYdIntegralItemListByPage(
					ydIntegralItem, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdIntegralItemResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdIntegralItemResult> getAll(YdIntegralItemResult ydIntegralItemResult) {
		YdIntegralItem ydIntegralItem = null;
		if (ydIntegralItemResult != null) {
			ydIntegralItem = new YdIntegralItem();
			BeanUtilExt.copyProperties(ydIntegralItem, ydIntegralItemResult);
		}
		List<YdIntegralItem> dataList = this.ydIntegralItemDao.getAll(ydIntegralItem);
		return DTOUtils.convertList(dataList, YdIntegralItemResult.class);
	}

	@Override
	public void deleteIntegralItem(Integer itemId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(itemId == null,
				"err_empty_id", "id不可以为空");

		YdIntegralItem ydIntegralItem = this.ydIntegralItemDao.getYdIntegralItemById(itemId);
		ValidateBusinessUtils.assertFalse(ydIntegralItem == null,
				"err_empty_item", "商品不存在");
		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydIntegralItem.getIsEnable()),
				"err_enable", "上架中的商品不可以删除");
		this.ydIntegralItemDao.deleteIntegralItem(itemId);
	}

	@Override
	public void insertYdIntegralItem(YdIntegralItemResult ydIntegralItemResult) {
		checkIntegralItemParams(ydIntegralItemResult);
		ydIntegralItemResult.setCreateTime(new Date());
		YdIntegralItem ydIntegralItem = new YdIntegralItem();
		BeanUtilExt.copyProperties(ydIntegralItem, ydIntegralItemResult);
		this.ydIntegralItemDao.insertYdIntegralItem(ydIntegralItem);
	}
	
	@Override
	public void updateYdIntegralItem(YdIntegralItemResult ydIntegralItemResult) {
		ValidateBusinessUtils.assertFalse(ydIntegralItemResult.getId() == null || ydIntegralItemResult.getId() <= 0,
				"err_empty_id", "id不可以为空");
		checkIntegralItemParams(ydIntegralItemResult);

		ydIntegralItemResult.setUpdateTime(new Date());
		YdIntegralItem ydIntegralItem = new YdIntegralItem();
		BeanUtilExt.copyProperties(ydIntegralItem, ydIntegralItemResult);
		this.ydIntegralItemDao.updateYdIntegralItem(ydIntegralItem);
	}

	@Override
	public void upOrDownIntegralItem(Integer itemId, String type) throws BusinessException {
		ValidateBusinessUtils.assertFalse(itemId == null || itemId <= 0,
				"error_item_id", "错误的商品id");
		ValidateBusinessUtils.assertStringNotBlank(type,"err_empty_enable", "上下架标识不可以为空");
		ValidateBusinessUtils.assertFalse(!("up".equalsIgnoreCase(type) || "down".equalsIgnoreCase(type)),
				"error_enable", "错误的上架状态编码");

		YdIntegralItem ydIntegralItem = ydIntegralItemDao.getYdIntegralItemById(itemId);
		ValidateBusinessUtils.assertFalse(ydIntegralItem == null,
				"error_exist_item", "商品不存在");

		if (type.equalsIgnoreCase("up")) {
			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydIntegralItem.getIsEnable()),
					"err_item_handel_status", "商品已经上架，请勿重复操作");
			ydIntegralItem.setIsEnable("Y");
		} else if (type.equalsIgnoreCase("down")) {
			ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydIntegralItem.getIsEnable()),
					"err_item_handel_status", "商品已经下架，请勿重复操作");
			ydIntegralItem.setIsEnable("N");
		}
		this.ydIntegralItemDao.updateYdIntegralItem(ydIntegralItem);
	}

	// -------------------------       private 校验商品状态      ---------------------------
	private void checkIntegralItemParams(YdIntegralItemResult zdIntegralItemResult) throws BusinessException {
		ValidateBusinessUtils.assertStringNotBlank(zdIntegralItemResult.getTitle(),
				"err_empty_title", "标题不可以为空");
		ValidateBusinessUtils.assertStringNotBlank(zdIntegralItemResult.getChannelCode(),
				"err_empty_channel_code", "渠道编码不可以为空");
		ValidateBusinessUtils.assertStringNotBlank(zdIntegralItemResult.getQrCodeUrl(),
				"err_empty_qr_code_url", "二维码不可以为空");
		ValidateBusinessUtils.assertStringNotBlank(zdIntegralItemResult.getIsEnable(),
				"err_empty_enable", "上架状态不可以为空");

		ValidateBusinessUtils.assertFalse(!("Y".equalsIgnoreCase(zdIntegralItemResult.getIsEnable()) ||
				"N".equalsIgnoreCase(zdIntegralItemResult.getIsEnable())),"error_enable", "错误的上架状态编码");
		ValidateBusinessUtils.assertFalse(zdIntegralItemResult.getSalePrice() == null ||
				zdIntegralItemResult.getSalePrice() <= 0, "err_sale_price", "请输出正确的售价");
		ValidateBusinessUtils.assertFalse(zdIntegralItemResult.getIntegralCount() == null ||
				zdIntegralItemResult.getIntegralCount() <= 0, "err_integral_count", "请输出正确的积分兑换价");

		// 计算结算金额
		YdIntegralConfig ydIntegralConfig = ydIntegralConfigDao.getAll(new YdIntegralConfig()).get(0);
		ValidateBusinessUtils.assertFalse(ydIntegralConfig.getSettlementRate() == null,
				"error_settlement_rate", "请先设置结算比例");
		Double settlementPrice = new BigDecimal(zdIntegralItemResult.getSalePrice() + "")
				.multiply(new BigDecimal(ydIntegralConfig.getSettlementRate() + ""))
				.divide(new BigDecimal(100 + ""))
				.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
		zdIntegralItemResult.setSettlementPrice(settlementPrice);
	}

}

