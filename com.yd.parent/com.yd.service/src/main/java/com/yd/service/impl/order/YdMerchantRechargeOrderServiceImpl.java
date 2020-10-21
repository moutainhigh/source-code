package com.yd.service.impl.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.order.YdMerchantRechargeOrderResult;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.order.YdMerchantRechargeOrderService;
import com.yd.core.constants.SystemPrefixConstants;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchant;
import com.yd.service.bean.merchant.YdMerchantAccount;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import com.yd.service.dao.merchant.YdMerchantAccountDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.order.YdMerchantRechargeOrderDao;
import com.yd.service.bean.order.YdMerchantRechargeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户充值订单Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-01-13 15:56:23
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantRechargeOrderServiceImpl implements YdMerchantRechargeOrderService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantRechargeOrderServiceImpl.class);

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdMerchantAccountDao ydMerchantAccountDao;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdMerchantRechargeOrderDao ydMerchantRechargeOrderDao;

	@Resource
	private YdMerchantTransService ydMerchantTransService;

	@Resource
	private YdMerchantGiftTransService ydMerchantGiftTransService;

	@Override
	public YdMerchantRechargeOrderResult getYdMerchantRechargeOrderById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult = null;
		YdMerchantRechargeOrder ydMerchantRechargeOrder = this.ydMerchantRechargeOrderDao.getYdMerchantRechargeOrderById(id);
		if (ydMerchantRechargeOrder != null) {
			ydMerchantRechargeOrderResult = new YdMerchantRechargeOrderResult();
			BeanUtilExt.copyProperties(ydMerchantRechargeOrderResult, ydMerchantRechargeOrder);
		}	
		return ydMerchantRechargeOrderResult;
	}

	@Override
	public Page<YdMerchantRechargeOrderResult> findYdMerchantRechargeOrderListByPage(YdMerchantRechargeOrderResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantRechargeOrderResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantRechargeOrder ydMerchantRechargeOrder = new YdMerchantRechargeOrder();
		BeanUtilExt.copyProperties(ydMerchantRechargeOrder, params);
		
		int amount = this.ydMerchantRechargeOrderDao.getYdMerchantRechargeOrderCount(ydMerchantRechargeOrder);
		if (amount > 0) {
			List<YdMerchantRechargeOrder> dataList = this.ydMerchantRechargeOrderDao.findYdMerchantRechargeOrderListByPage(
				ydMerchantRechargeOrder, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantRechargeOrderResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantRechargeOrderResult> getAll(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult) {
		YdMerchantRechargeOrder ydMerchantRechargeOrder = null;
		if (ydMerchantRechargeOrderResult != null) {
			ydMerchantRechargeOrder = new YdMerchantRechargeOrder();
			BeanUtilExt.copyProperties(ydMerchantRechargeOrder, ydMerchantRechargeOrderResult);
		}
		List<YdMerchantRechargeOrder> dataList = this.ydMerchantRechargeOrderDao.getAll(ydMerchantRechargeOrder);
		return DTOUtils.convertList(dataList, YdMerchantRechargeOrderResult.class);
	}

	@Override
	public YdMerchantRechargeOrderResult insertYdMerchantRechargeOrder(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult) {
		Double payPrice = ydMerchantRechargeOrderResult.getPayPrice();
		ValidateBusinessUtils.assertFalse(payPrice == null || payPrice < 0,
				"err_empty_pay_price", "请输入正确的充值金额");

		Integer merchantId = ydMerchantRechargeOrderResult.getMerchantId();

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId < 0,
				"err_empty_merchant", "商户id不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_merchant", "商户不存在");


		YdMerchantRechargeOrder ydMerchantRechargeOrder = new YdMerchantRechargeOrder();
		ydMerchantRechargeOrder.setCreateTime(new Date());
		ydMerchantRechargeOrder.setMerchantId(merchantId);
		ydMerchantRechargeOrder.setPid(ydMerchant.getPid());
		ydMerchantRechargeOrder.setIsPay("N");
		ydMerchantRechargeOrder.setPayPrice(payPrice);
		this.ydMerchantRechargeOrderDao.insertYdMerchantRechargeOrder(ydMerchantRechargeOrder);

		YdMerchantRechargeOrderResult result = new YdMerchantRechargeOrderResult();
		BeanUtilExt.copyProperties(result, ydMerchantRechargeOrder);
		return result;
	}
	
	@Override
	public void updateYdMerchantRechargeOrder(YdMerchantRechargeOrderResult ydMerchantRechargeOrderResult) {
		if (null != ydMerchantRechargeOrderResult) {
			ydMerchantRechargeOrderResult.setUpdateTime(new Date());
			YdMerchantRechargeOrder ydMerchantRechargeOrder = new YdMerchantRechargeOrder();
			BeanUtilExt.copyProperties(ydMerchantRechargeOrder, ydMerchantRechargeOrderResult);
			this.ydMerchantRechargeOrderDao.updateYdMerchantRechargeOrder(ydMerchantRechargeOrder);
		}
	}

	/**
	 * 微信回调修改充值状态
	 * @param outOrderId
	 * @param billNo
	 * @throws BusinessException
	 */
	@Override
	public void updateRechargeStatus(String outOrderId, String billNo) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(outOrderId),
				"err_empty_bill_no", "对外订单号不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(billNo),
				"err_empty_bill_no", "流水号不可以为空");

		ValidateBusinessUtils.assertFalse(!outOrderId.startsWith(SystemPrefixConstants.YD_RECHARGE_PREFIX) ,
				"err_notify_recharge_order_id", "错误格式的订单号" + outOrderId);

		String[] split = outOrderId.split(SystemPrefixConstants.YD_RECHARGE_PREFIX);
		logger.info("====充值订单回调截取后的值=" + JSON.toJSONString(split));
		logger.info("====id=" + split[split.length - 1]);
		Integer id = Integer.valueOf(split[split.length - 1]);

		YdMerchantRechargeOrder ydMerchantRechargeOrder = this.ydMerchantRechargeOrderDao.getYdMerchantRechargeOrderById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantRechargeOrder == null,
				"err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantRechargeOrder.getIsPay())
						|| StringUtils.isNotEmpty(ydMerchantRechargeOrder.getBillNo()),
				"err_recharge_order_pay_status", "充值订单已经回调成功");

		// 修改充值状态
		ydMerchantRechargeOrder.setIsPay("Y");
		ydMerchantRechargeOrder.setBillNo(billNo);
		this.ydMerchantRechargeOrderDao.updateYdMerchantRechargeOrder(ydMerchantRechargeOrder);

		// 给商户礼品账户加钱
		Integer storeId = ydMerchantRechargeOrder.getPid();

		YdMerchantGiftAccount ydMerchantGiftAccount = this.ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(storeId);

		Double giftAfterBalance = MathUtils.add(ydMerchantRechargeOrder.getPayPrice(), ydMerchantGiftAccount.getBalance(), 2);
		// 礼品账户增加余额
		this.ydMerchantGiftAccountDao.addGiftAccountBalance(storeId, ydMerchantRechargeOrder.getPayPrice());

		// 增加礼品账户流水记录
		ydMerchantGiftTransService.addMerchantGiftTrans(storeId, ydMerchantRechargeOrder.getId() + "", YdMerchantTransSourceEnum.TOP_UP.getCode(),
				"in", YdMerchantTransSourceEnum.TOP_UP.getDescription(), "SUCCESS", ydMerchantRechargeOrder.getPayPrice(),
				ydMerchantGiftAccount.getBalance(), giftAfterBalance);
	}

	/**
	 * 微信回调修改扫一扫付钱状态
	 * @param outOrderId
	 * @param billNo
	 * @throws BusinessException
	 */
	@Override
	public void updateQrCodeStatus(String outOrderId, String billNo) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(outOrderId),
				"err_empty_bill_no", "对外订单号不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(billNo),
				"err_empty_bill_no", "流水号不可以为空");

		ValidateBusinessUtils.assertFalse(!outOrderId.startsWith(SystemPrefixConstants.YD_APP_QR_CODE_PREFIX)  ,
				"err_notify_recharge_order_id", "错误格式的订单号" + outOrderId);

		String[] split = outOrderId.split(SystemPrefixConstants.YD_APP_QR_CODE_PREFIX);
		logger.info("====扫一扫支付回调截取后的值=" + JSON.toJSONString(split));
		logger.info("====id=" + split[split.length - 1]);
		Integer id = Integer.valueOf(split[split.length - 1]);

		YdMerchantRechargeOrder ydMerchantRechargeOrder = this.ydMerchantRechargeOrderDao.getYdMerchantRechargeOrderById(id);
		ValidateBusinessUtils.assertFalse(ydMerchantRechargeOrder == null,
				"err_not_exist_order", "用户订单不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydMerchantRechargeOrder.getIsPay())
						|| StringUtils.isNotEmpty(ydMerchantRechargeOrder.getBillNo()),
				"err_recharge_order_pay_status", "扫一扫订单已经回调成功");

		// 修改支付状态
		ydMerchantRechargeOrder.setIsPay("Y");
		ydMerchantRechargeOrder.setBillNo(billNo);
		this.ydMerchantRechargeOrderDao.updateYdMerchantRechargeOrder(ydMerchantRechargeOrder);

		// 给商户余额账户加钱
		Integer storeId = ydMerchantRechargeOrder.getPid();

		YdMerchantAccount ydMerchantAccount = this.ydMerchantAccountDao.getYdMerchantAccountByMerchantId(storeId);

		Double giftAfterBalance = MathUtils.add(ydMerchantRechargeOrder.getPayPrice(), ydMerchantAccount.getBalance(), 2);
		// 账户增加余额
		this.ydMerchantAccountDao.addAccountBalance(storeId, ydMerchantRechargeOrder.getPayPrice());

		// 增加账户流水记录
		ydMerchantTransService.addMerchantTransDetail(storeId, ydMerchantRechargeOrder.getId() + "", YdMerchantTransSourceEnum.YD_QR_CODE.getCode(),
				"in", YdMerchantTransSourceEnum.YD_QR_CODE.getDescription(), "SUCCESS", ydMerchantRechargeOrder.getPayPrice(),
				ydMerchantAccount.getBalance(), giftAfterBalance, 0.0);
	}

}

