package com.yd.service.impl.gift;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.service.gift.YdGiftService;
import com.yd.api.service.message.YdMerchantMessageService;
import com.yd.core.enums.YdOrderNoTypeEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.order.YdGiftOrder;
import com.yd.service.bean.order.YdGiftOrderDetail;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.order.YdGiftOrderDao;
import com.yd.service.dao.order.YdGiftOrderDetailDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.bean.gift.YdGift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:平台礼品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 20:15:54
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdGiftServiceImpl implements YdGiftService {

	private static final Logger logger = LoggerFactory.getLogger(YdGiftServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdGiftOrderDao ydGiftOrderDao;

	@Resource
	private YdGiftOrderDetailDao ydGiftOrderDetailDao;

	@Resource
	private YdMerchantMessageService ydMerchantMessageService;

	@Override
	public YdGiftResult getYdGiftById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdGiftResult ydGiftResult = null;
		YdGift ydGift = this.ydGiftDao.getYdGiftById(id);
		if (ydGift != null) {
			ydGiftResult = new YdGiftResult();
			BeanUtilExt.copyProperties(ydGiftResult, ydGift);
		}
		return ydGiftResult;
	}

	@Override
	public Page<YdGiftResult> findYdGiftListByPage(YdGiftResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdGiftResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdGift ydGift = new YdGift();
		BeanUtilExt.copyProperties(ydGift, params);

		ydGift.setIsFlag("N");
		int amount = this.ydGiftDao.getYdGiftCount(ydGift);
		if (amount > 0) {
			List<YdGift> dataList = this.ydGiftDao.findYdGiftListByPage(ydGift, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdGiftResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdGiftResult> getAll(YdGiftResult ydGiftResult) {
		YdGift ydGift = null;
		if (ydGiftResult != null) {
			ydGift = new YdGift();
			BeanUtilExt.copyProperties(ydGift, ydGiftResult);
		}
		List<YdGift> dataList = this.ydGiftDao.getAll(ydGift);
		List<YdGiftResult> resultList = DTOUtils.convertList(dataList, YdGiftResult.class);
		return resultList;
	}

	/**
	 * 新增礼品库礼品
	 * @param ydGiftResult
	 * @return
	 */
	@Override
	public void insertYdGift(YdGiftResult ydGiftResult) {
		// 校验必填字段信息
		checkGiftParams(ydGiftResult);
		ydGiftResult.setCreateTime(new Date());
		ydGiftResult.setUpdateTime(new Date());
		YdGift ydGift = new YdGift();
		BeanUtilExt.copyProperties(ydGift, ydGiftResult);
		this.ydGiftDao.insertYdGift(ydGift);

		try {
			if ("Y".equalsIgnoreCase(ydGiftResult.getIsEnable())) {
				ydMerchantMessageService.insertGiftMessage("platform_gift_up", ydGift.getId());
			}
		} catch (Exception e) {
			logger.error("上架礼品发送消息失败, 失败原因:" + e.getLocalizedMessage());
		}

	}

	/**
	 * 修改礼品库礼品
	 * @param ydGiftResult
	 */
	@Override
	public void updateYdGift(YdGiftResult ydGiftResult) {
		// 校验必填字段
		checkGiftParams(ydGiftResult);
		ydGiftResult.setUpdateTime(new Date());
		YdGift ydGift = new YdGift();
		BeanUtilExt.copyProperties(ydGift, ydGiftResult);
		this.ydGiftDao.updateYdGift(ydGift);

		// 将商户礼品价钱修更新掉
		// this.ydMerchantGiftDao.updateYdMerchantGiftPrice(ydGift);
	}

	/**
	 * 上下架礼品
	 * @param id
	 * @param isEnable	Y | N
	 * @throws BusinessException
	 */
	@Override
	public void upOrDownYdGift(Integer id, String isEnable) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_gift_id", "礼品id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(isEnable),
				"err_empty_is_enable", "上下架状态不可以为空");

		YdGift ydGift = this.ydGiftDao.getYdGiftById(id);
		ValidateBusinessUtils.assertFalse(ydGift == null,
				"err_exist_gift", "礼品不存在");
		ValidateBusinessUtils.assertFalse(ydGift.getIsEnable().equalsIgnoreCase(isEnable),
				"err_exist_gift", "请勿重复操作");
		ydGift.setIsEnable(isEnable);
		this.ydGiftDao.updateYdGift(ydGift);

		try {
			if ("Y".equalsIgnoreCase(ydGift.getIsEnable())) {
				ydMerchantMessageService.insertGiftMessage("platform_gift_up", ydGift.getId());
			}
		} catch (Exception e) {
			logger.error("上架礼品发送消息失败, 失败原因:" + e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteYdGift(Integer id) throws BusinessException {
		YdGift ydGift = this.ydGiftDao.getYdGiftById(id);
		ValidateBusinessUtils.assertFalse(ydGift == null,
				"err_exist_gift", "礼品不存在");
		ydGift.setIsFlag("Y");
		this.ydGiftDao.updateYdGift(ydGift);
	}


	@Override
	public void updateSettlementStatus(String giftOrderDetailIds) throws BusinessException {
		List<Integer> idList = Arrays.stream(StringUtils.split(giftOrderDetailIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(idList),
				"err_empty_ids", "订单详情id不可以为空");

		idList.forEach(giftOrderDetailId -> {
			YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(giftOrderDetailId);
			if (ydGiftOrderDetail != null && "WAIT".equalsIgnoreCase(ydGiftOrderDetail.getSettlementStatus())) {
				ydGiftOrderDetail.setSettlementStatus("SUCCESS");
				ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
			}
		});
	}

	/**
	 * 平台礼品库修改线下采购价,数量
	 * @param giftOrderDetailId		平台礼品订单详情ID
	 * @param num					num 线下采购数量
	 * @param purchasePrice			采购单价
	 * @throws BusinessException
	 */
	@Override
	public void updatePurchaseInfo(Integer giftOrderDetailId, Integer num, Double purchasePrice) throws BusinessException {
		ValidateBusinessUtils.assertFalse(num == null || num <= 0,
				"err_purchase_num", "采购数量不正确");

		ValidateBusinessUtils.assertFalse(purchasePrice == null || purchasePrice <= 0,
				"err_purchase_price", "采购单价不正确");

		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(giftOrderDetailId);
		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail == null,
				"err_exist_gift_order_detail", "礼品订单详情不存在");

		YdGiftOrder ydGiftOrder = this.ydGiftOrderDao.getYdGiftOrderById(ydGiftOrderDetail.getGiftOrderId());
		ValidateBusinessUtils.assertFalse(ydGiftOrder == null,
				"err_exist_gift_order", "礼品订单不存在");

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail == null,
				"err_exist_gift_order_detail", "礼品订单详情不存在");

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail.getGiftId() == null,
				"err_exist_gift_id", "错误的礼品id");

		YdGift ydGift = this.ydGiftDao.getYdGiftById(ydGiftOrderDetail.getGiftId());
		ValidateBusinessUtils.assertFalse(ydGift == null,
				"err_exist_gift", "礼品不存在");

		// 修改采购单价
		ydGift.setPurchasePrice(purchasePrice);
		this.ydGiftDao.updateYdGift(ydGift);

		// 修改礼品主订单采购数量
		ydGiftOrder.setTotalGiftCount(num);
		ydGiftOrder.setTotalDetailCount(1);
		this.ydGiftOrderDao.updateYdGiftOrder(ydGiftOrder);

		// 修改礼品子订单采购数量
		ydGiftOrderDetail.setNum(num);
		ydGiftOrderDetail.setPurchasePrice(purchasePrice);
		this.ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
	}

	/**
	 * 平台礼品库新增线下采购价,数量
	 * @param giftId	礼品id
	 * @param supplierId	供应商id
	 * @param num		线下采购数量
	 * @param purchasePrice 采购单价
	 */
	@Override
	public void insertPurchaseInfo(Integer giftId, Integer supplierId, Integer num, Double purchasePrice) throws BusinessException {
		ValidateBusinessUtils.assertFalse(giftId == null || giftId <= 0,
				"err_gift_id", "礼品id不可以为空");

		ValidateBusinessUtils.assertFalse(supplierId == null || supplierId <= 0,
				"err_supplier_id", "供应商id不可以为空");

		ValidateBusinessUtils.assertFalse(num == null || num <= 0,
				"err_purchase_num", "采购数量不正确");

		ValidateBusinessUtils.assertFalse(purchasePrice == null || purchasePrice <= 0,
				"err_purchase_price", "采购单价不正确");

		YdGift ydGift = this.ydGiftDao.getYdGiftById(giftId);
		ValidateBusinessUtils.assertFalse(ydGift == null,
				"err_exist_gift", "礼品不存在");

		ValidateBusinessUtils.assertFalse(!ydGift.getSupplierId().equals(supplierId),
				"err_supplier_id", "礼品对应供应商id与选择供应商id不一样的");

		// 修改采购单价
		ydGift.setPurchasePrice(purchasePrice);
		this.ydGiftDao.updateYdGift(ydGift);

		// 新增礼品主订单
		YdGiftOrder ydGiftOrder = new YdGiftOrder();
		ydGiftOrder.setCreateTime(new Date());
		ydGiftOrder.setType("out");
		ydGiftOrder.setTransType("offline");
		ydGiftOrder.setOrderStatus("SUCCESS");
		ydGiftOrder.setPayStatus("SUCCESS");
		ydGiftOrder.setPayTime(new Date());
		ydGiftOrder.setTotalDetailCount(1);
		ydGiftOrder.setTotalGiftCount(num);
		ydGiftOrder.setTotalSalePrice(ydGift.getSalePrice() * num);
		ydGiftOrder.setTotalMarketPrice(ydGift.getMarketPrice() * num);
		ydGiftOrder.setGiftOrderNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.GIFT_OUT));
		ydGiftOrderDao.insertYdGiftOrder(ydGiftOrder);

		// 新增礼品子订单
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setCreateTime(new Date());
		ydGiftOrderDetail.setGiftOrderId(ydGiftOrder.getId());
		ydGiftOrderDetail.setSupplierId(supplierId);
		ydGiftOrderDetail.setGiftId(giftId);
		ydGiftOrderDetail.setSalePrice(ydGift.getSalePrice());
		ydGiftOrderDetail.setSalePriceTotal(ydGift.getSalePrice() * num);
		ydGiftOrderDetail.setMarketPrice(ydGift.getMarketPrice());
		ydGiftOrderDetail.setPurchasePrice(purchasePrice);
		ydGiftOrderDetail.setOrderStatus("SUCCESS");
		ydGiftOrderDetail.setSettlementStatus("SUCCESS");
		ydGiftOrderDetail.setNum(num);
        ydGiftOrderDetail.setGiftOrderDetailNo(OrderNoUtils.getOrderNo(YdOrderNoTypeEnum.GIFT_OUT));

		ydGiftOrderDetail.setSalePriceTotal(ydGift.getSalePrice() * num);
		ydGiftOrderDetailDao.insertYdGiftOrderDetail(ydGiftOrderDetail);
	}

	/**
	 * 校验保存修改参数
	 * @param ydGiftResult
	 */
	private void checkGiftParams(YdGiftResult ydGiftResult) {
		ValidateBusinessUtils.assertFalse(ydGiftResult.getSupplierId() == null,
				"err_empty_supplier_id", "供应商id不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydGiftResult.getTitle()),
				"err_empty_gift_title", "礼品名称不可以为空");
		ValidateBusinessUtils.assertFalse(ydGiftResult.getCategoryId() == null,
				"err_empty_gift_category", "礼品分类不可以为空");
		ValidateBusinessUtils.assertFalse(ydGiftResult.getSalePrice() == null,
				"err_empty_sale_price", "售价不可以为空");
		ValidateBusinessUtils.assertFalse(ydGiftResult.getMarketPrice() == null,
				"err_empty_marketing_price", "划线价不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydGiftResult.getIsEnable()),
				"err_empty_is_enable", "上架状态不可以为空");
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydGiftResult.getImageUrl()),
				"err_empty_image", "礼品图片不可以为空");
		// 原型要求描述为空默认的描述
		if (StringUtil.isEmpty(ydGiftResult.getGiftDesc())) {
			ydGiftResult.setGiftDesc("礼品款式随机发");
		}
		ydGiftResult.setIsFlag("N");
	}

}

