package com.yd.service.impl.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.wx.util.ExpressUtil;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.order.YdGiftOrder;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.order.YdGiftOrderDao;
import com.yd.service.impl.merchant.YdMerchantServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.order.YdGiftOrderDetailDao;
import com.yd.service.bean.order.YdGiftOrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:礼品订单明细表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:54:16
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdGiftOrderDetailServiceImpl implements YdGiftOrderDetailService {

	private static final Logger logger = LoggerFactory.getLogger(YdGiftOrderDetailServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdGiftOrderDao ydGiftOrderDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdGiftOrderDetailDao ydGiftOrderDetailDao;

	@Override
	public YdGiftOrderDetailResult getYdGiftOrderDetailById(Integer id) {
		if (id == null || id <= 0) return null;
		YdGiftOrderDetailResult ydGiftOrderDetailResult = null;
		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(id);
		if (ydGiftOrderDetail != null) {
			ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
			BeanUtilExt.copyProperties(ydGiftOrderDetailResult, ydGiftOrderDetail);

			// 查询礼品信息

			// 查询礼品订单信息
			YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderById(ydGiftOrderDetailResult.getId());
			ydGiftOrderDetailResult.setRealname(ydGiftOrder.getRealname());
			ydGiftOrderDetailResult.setMobile(ydGiftOrder.getMobile());
			ydGiftOrderDetailResult.setProvince(ydGiftOrder.getProvince());
			ydGiftOrderDetailResult.setCity(ydGiftOrder.getCity());
			ydGiftOrderDetailResult.setDistrict(ydGiftOrder.getDistrict());
			ydGiftOrderDetailResult.setAddress(ydGiftOrder.getAddress());
		}
		return ydGiftOrderDetailResult;
	}

	@Override
	public YdGiftOrderDetailResult getMerchantGiftOrderDetail(Integer id) {
		if (id == null || id <= 0) return null;
		YdGiftOrderDetailResult ydGiftOrderDetailResult = null;
		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(id);
		if (ydGiftOrderDetail != null) {
			ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
			BeanUtilExt.copyProperties(ydGiftOrderDetailResult, ydGiftOrderDetail);

			// 查询礼品信息,  如果平台礼品id为空， 获取商户礼品信息
			if (ydGiftOrderDetailResult.getGiftId() == null) {
				YdMerchantGift ydMerchantGift = ydMerchantGiftDao.getYdMerchantGiftById(ydGiftOrderDetailResult.getMerchantGiftId());
				if (ydMerchantGift.getGiftType().equalsIgnoreCase("platform")) {
					YdGift ydGift = ydGiftDao.getYdGiftById(ydMerchantGift.getGiftId());
					ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
					ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
					ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
				} else {
					ydGiftOrderDetailResult.setTitle(ydMerchantGift.getTitle());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getGiftDesc());
					ydGiftOrderDetailResult.setSubTitle(ydMerchantGift.getSubTitle());
					ydGiftOrderDetailResult.setImageUrl(ydMerchantGift.getImageUrl());
				}
			} else {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
			}

			// 查询礼品订单信息
			YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderById(ydGiftOrderDetailResult.getId());
			ydGiftOrderDetailResult.setRealname(ydGiftOrder.getRealname());
			ydGiftOrderDetailResult.setMobile(ydGiftOrder.getMobile());
			ydGiftOrderDetailResult.setProvince(ydGiftOrder.getProvince());
			ydGiftOrderDetailResult.setCity(ydGiftOrder.getCity());
			ydGiftOrderDetailResult.setDistrict(ydGiftOrder.getDistrict());
			ydGiftOrderDetailResult.setAddress(ydGiftOrder.getAddress());

			// 查询商户名称
			if (ydGiftOrder.getMerchantId() != null) {
				YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydGiftOrder.getMerchantId());
				if (ydMerchant != null) {
					ydGiftOrderDetailResult.setMerchantName(ydMerchant.getMerchantName());
				}
			}

			// 查询供应商名称
			YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydGiftOrderDetailResult.getSupplierId());
			if (ydMerchant != null) {
				ydGiftOrderDetailResult.setSupplierName(ydMerchant.getMerchantName());
			}
		}
		return ydGiftOrderDetailResult;
	}

	@Override
	public YdGiftOrderDetailResult getSupplierGiftOrderDetail(Integer id) {
		if (id == null || id <= 0) return null;
		YdGiftOrderDetailResult ydGiftOrderDetailResult = null;
		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(id);
		if (ydGiftOrderDetail != null) {
			ydGiftOrderDetailResult = new YdGiftOrderDetailResult();
			BeanUtilExt.copyProperties(ydGiftOrderDetailResult, ydGiftOrderDetail);

			// 查询礼品信息
			if (ydGiftOrderDetailResult.getGiftId() != null) {
				YdGift ydGift = ydGiftDao.getYdGiftById(ydGiftOrderDetailResult.getGiftId());
				ydGiftOrderDetailResult.setTitle(ydGift.getTitle());
				ydGiftOrderDetailResult.setSubTitle(ydGift.getSubTitle());
				ydGiftOrderDetailResult.setImageUrl(ydGift.getImageUrl());
				ydGiftOrderDetailResult.setGiftDesc(ydGift.getGiftDesc());
			}

			// 查询礼品订单信息
			YdGiftOrder ydGiftOrder = ydGiftOrderDao.getYdGiftOrderById(ydGiftOrderDetailResult.getId());
			ydGiftOrderDetailResult.setRealname(ydGiftOrder.getRealname());
			ydGiftOrderDetailResult.setMobile(ydGiftOrder.getMobile());
			ydGiftOrderDetailResult.setProvince(ydGiftOrder.getProvince());
			ydGiftOrderDetailResult.setCity(ydGiftOrder.getCity());
			ydGiftOrderDetailResult.setDistrict(ydGiftOrder.getDistrict());
			ydGiftOrderDetailResult.setAddress(ydGiftOrder.getAddress());

			// 查询商户名称
			if (ydGiftOrder.getMerchantId() != null) {
				YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydGiftOrder.getMerchantId());
				if (ydMerchant != null) {
					ydGiftOrderDetailResult.setMerchantName(ydMerchant.getMerchantName());
				}
			}

			// 查询供应商名称
			YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(ydGiftOrderDetailResult.getSupplierId());
			if (ydMerchant != null) {
				ydGiftOrderDetailResult.setSupplierName(ydMerchant.getMerchantName());
			}
		}
		return ydGiftOrderDetailResult;
	}

	@Override
	public List<YdGiftOrderDetailResult> getAll(YdGiftOrderDetailResult ydGiftOrderDetailResult) {
		YdGiftOrderDetail ydGiftOrderDetail = null;
		if (ydGiftOrderDetailResult != null) {
			ydGiftOrderDetail = new YdGiftOrderDetail();
			BeanUtilExt.copyProperties(ydGiftOrderDetail, ydGiftOrderDetailResult);
		}
		List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.getAll(ydGiftOrderDetail);
		return DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);
	}

	/**
	 * 创建礼品订单详情
	 * @param giftOrderId	  礼品订单id
	 * @param supplierId	  供应商id
	 * @param giftId		  礼品id
	 * @param merchantGiftId  礼品id
	 * @param giftNum		  礼品数量
	 * @param marketPrice	  市场价
	 * @param salePrice		  礼品真实售价(如果为阶梯价的话显示阶梯价)
	 * @param purchasePrice	  采购供应商的价格
	 */
	@Override
	public void createMerchantGiftOrderDetail(Integer giftOrderId, Integer supplierId, Integer merchantGiftId, Integer giftId,
											  Integer giftNum, Double marketPrice, Double salePrice, Double purchasePrice) {
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setCreateTime(new Date());
		ydGiftOrderDetail.setNum(giftNum);
		ydGiftOrderDetail.setGiftId(giftId);
		ydGiftOrderDetail.setGiftOrderId(giftOrderId);
		ydGiftOrderDetail.setSupplierId(supplierId);
		ydGiftOrderDetail.setMarketPrice(marketPrice);
		ydGiftOrderDetail.setSalePrice(salePrice);
		ydGiftOrderDetail.setPurchasePrice(purchasePrice);
		ydGiftOrderDetail.setMerchantGiftId(merchantGiftId);
		ydGiftOrderDetail.setSettlementStatus("WAIT");

		Double totalSalePrice = new BigDecimal(salePrice + "")
				.multiply(new BigDecimal(giftNum + ""))
				.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		ydGiftOrderDetail.setSalePriceTotal(totalSalePrice);
		ydGiftOrderDetail.setOrderStatus("WAIT_DELIVER");
		this.ydGiftOrderDetailDao.insertYdGiftOrderDetail(ydGiftOrderDetail);
	}

	@Override
	public Page<YdGiftOrderDetailResult> findGiftOrderDetailByPage(YdGiftOrderDetailResult giftOrderDetailResult, PagerInfo pageInfo) {
		Page<YdGiftOrderDetailResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
		YdGiftOrderDetail giftOrderDetail = new YdGiftOrderDetail();
		BeanUtilExt.copyProperties(giftOrderDetail, giftOrderDetailResult);
		int amount = this.ydGiftOrderDetailDao.getYdGiftOrderDetailCount(giftOrderDetail);
		if (amount > 0) {
			List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.findYdGiftOrderDetailListByPage(giftOrderDetail,
					pageInfo.getStart(), pageInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	/**
	 * 供应商更新物流信息
	 * @param merchantId	  供应商id
	 * @param detailOrderId	  供应商订单详情id
	 * @param expressOrderId  物流单号
	 * @throws BusinessException
	 */
	@Override
	public void updateGiftOrderExpress(Integer merchantId, Integer detailOrderId, String expressOrderId) throws BusinessException {

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_merchant_id", "非法的商户id");

		ValidateBusinessUtils.assertFalse(detailOrderId == null || detailOrderId <= 0,
				"err_detail_order_id", "非法的订单详情id");

		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(detailOrderId);

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail == null,
				"err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(expressOrderId),
				"err_express_order_id", "快递单号不可以为空");

		// String companyName = ExpressUtil.getExpressCompany(expressOrderId);
		String companyName = "请根据物流单号自己去网上查询";

		ydGiftOrderDetail.setDeliveryTime(new Date());
		ydGiftOrderDetail.setExpressOrderId(expressOrderId);
		ydGiftOrderDetail.setExpressCompany(companyName);
		ydGiftOrderDetail.setOrderStatus("WAIT_GOODS");
		this.ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
	}

	/**
	 * 礼品确认收货
	 * @param detailOrderId 礼品订单详情id
	 * @throws BusinessException
	 */
	@Override
	public void confirmGoods(Integer detailOrderId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(detailOrderId == null || detailOrderId <= 0,
				"err_detail_order_id", "非法的订单详情id");

		YdGiftOrderDetail ydGiftOrderDetail = this.ydGiftOrderDetailDao.getYdGiftOrderDetailById(detailOrderId);

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail == null,
				"err_not_exist_order", "订单不存在");

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail.getOrderStatus().equalsIgnoreCase("SUCCESS"),
				"err_gift_order_detail_status", "礼品订单详情已经发过货了，不可以重复发货");

		ydGiftOrderDetail.setConfirmGoodsTime(new Date());
		ydGiftOrderDetail.setOrderStatus("SUCCESS");
		this.ydGiftOrderDetailDao.updateYdGiftOrderDetail(ydGiftOrderDetail);
	}

	/**
	 * 礼品确认收货，根据物流号
	 * @param giftOrderId
	 * @param expressOrderId
	 * @throws BusinessException
	 */
	@Override
	public void appGiftOrderConfirmGoods(Integer giftOrderId, String expressOrderId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(giftOrderId == null || giftOrderId <= 0,
				"err_gift_order_id", "非法的订单id");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(expressOrderId),
				"err_express_orderId_isnull", "物流单号不可以为空");

		YdGiftOrder ydGiftOrder = this.ydGiftOrderDao.getYdGiftOrderById(giftOrderId);

		ValidateBusinessUtils.assertFalse(ydGiftOrder == null,
				"err_not_exist_gift_order", "礼品订单不存在");

		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		ydGiftOrderDetail.setConfirmGoodsTime(new Date());
		ydGiftOrderDetail.setOrderStatus("SUCCESS");
		ydGiftOrderDetail.setExpressOrderId(expressOrderId);
		ydGiftOrderDetail.setGiftOrderId(giftOrderId);
		this.ydGiftOrderDetailDao.updateYdGiftOrderDetailByExpressOrderId(ydGiftOrderDetail);
	}

	/**
	 * 商户确认礼品收货,根据礼品订单详情
	 * @param giftOrderDetailId
	 * @throws BusinessException
	 */
	@Override
	public void appConfirmGiftOrderDetail(Integer giftOrderDetailId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(giftOrderDetailId == null || giftOrderDetailId <= 0,
				"err_gift_detail_order_id", "非法的礼品订单详情id");


		YdGiftOrderDetail ydGiftOrderDetail = ydGiftOrderDetailDao.getYdGiftOrderDetailById(giftOrderDetailId);

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail == null,
				"err_not_exist_gift_order_detail", "礼品订单详情不存在");

		ValidateBusinessUtils.assertFalse(ydGiftOrderDetail.getOrderStatus().equalsIgnoreCase("SUCCESS"),
				"err_gift_order_detail_status", "礼品订单详情已经发过货了，不可以重复发货");

		ydGiftOrderDetail.setConfirmGoodsTime(new Date());
		ydGiftOrderDetail.setOrderStatus("SUCCESS");
		this.ydGiftOrderDetailDao.updateYdGiftOrderDetailByExpressOrderId(ydGiftOrderDetail);
	}

	/**
	 * 查询供应商礼品订单列表 供应商transType传 online(线上), 平台不传，查全部
	 * @param params
	 * @param pageInfo
	 * @return
	 */
	@Override
	public Page<YdGiftOrderDetailResult> findSupplierGiftOrderDetailListByPage(YdGiftOrderDetailResult params, PagerInfo pageInfo) {
		Page<YdGiftOrderDetailResult> pageData = new Page<>(pageInfo.getPageIndex(), pageInfo.getPageSize());
		YdGiftOrderDetail ydGiftOrderDetail = new YdGiftOrderDetail();
		BeanUtilExt.copyProperties(ydGiftOrderDetail, params);
		int amount = this.ydGiftOrderDetailDao.getSupplierGiftOrderDetailCount(ydGiftOrderDetail);
		if (amount > 0) {
			List<YdGiftOrderDetail> dataList = this.ydGiftOrderDetailDao.findSupplierGiftOrderDetailListByPage(
					ydGiftOrderDetail, pageInfo.getStart(), pageInfo.getPageSize());
			List<YdGiftOrderDetailResult> resultList = DTOUtils.convertList(dataList, YdGiftOrderDetailResult.class);
			pageData.setData(resultList);
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

}

