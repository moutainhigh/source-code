package com.yd.service.impl.merchant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.merchant.YdMerchantWithdrawResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.merchant.YdMerchantWithdrawService;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.YdMerchantAccount;
import com.yd.service.dao.merchant.YdMerchantAccountDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.merchant.YdMerchantWithdrawDao;
import com.yd.service.bean.merchant.YdMerchantWithdraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户提现记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 16:39:54
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantWithdrawServiceImpl implements YdMerchantWithdrawService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantWithdrawServiceImpl.class);

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdMerchantTransService ydMerchantTransService;

	@Resource
	private YdMerchantAccountDao ydMerchantAccountDao;

	@Resource
	private YdMerchantWithdrawDao ydMerchantWithdrawDao;

	// 微信提现费率
	private final Double withdrawRate = 0.006;

	@Override
	public YdMerchantWithdrawResult getYdMerchantWithdrawById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantWithdrawResult ydMerchantWithdrawResult = null;
		YdMerchantWithdraw ydMerchantWithdraw = this.ydMerchantWithdrawDao.getYdMerchantWithdrawById(id);
		if (ydMerchantWithdraw != null) {
			ydMerchantWithdrawResult = new YdMerchantWithdrawResult();
			BeanUtilExt.copyProperties(ydMerchantWithdrawResult, ydMerchantWithdraw);
		}
		return ydMerchantWithdrawResult;
	}

	@Override
	public Page<YdMerchantWithdrawResult> findYdMerchantWithdrawListByPage(YdMerchantWithdrawResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantWithdrawResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantWithdraw ydMerchantWithdraw = new YdMerchantWithdraw();
		BeanUtilExt.copyProperties(ydMerchantWithdraw, params);
		
		int amount = this.ydMerchantWithdrawDao.getYdMerchantWithdrawCount(ydMerchantWithdraw);
		if (amount > 0) {
			List<YdMerchantWithdraw> dataList = this.ydMerchantWithdrawDao.findYdMerchantWithdrawListByPage(ydMerchantWithdraw,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantWithdrawResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantWithdrawResult> getAll(YdMerchantWithdrawResult ydMerchantWithdrawResult) {
		YdMerchantWithdraw ydMerchantWithdraw = null;
		if (ydMerchantWithdrawResult != null) {
			ydMerchantWithdraw = new YdMerchantWithdraw();
			BeanUtilExt.copyProperties(ydMerchantWithdraw, ydMerchantWithdrawResult);
		}
		List<YdMerchantWithdraw> dataList = this.ydMerchantWithdrawDao.getAll(ydMerchantWithdraw);
		return DTOUtils.convertList(dataList, YdMerchantWithdrawResult.class);
	}

	@Override
	public void insertYdMerchantWithdraw(YdMerchantWithdrawResult ydMerchantWithdrawResult) {
		ydMerchantWithdrawResult.setCreateTime(new Date());
		ydMerchantWithdrawResult.setUpdateTime(new Date());
		YdMerchantWithdraw ydMerchantWithdraw = new YdMerchantWithdraw();
		BeanUtilExt.copyProperties(ydMerchantWithdraw, ydMerchantWithdrawResult);
		this.ydMerchantWithdrawDao.insertYdMerchantWithdraw(ydMerchantWithdraw);
	}
	
	@Override
	public void updateYdMerchantWithdraw(YdMerchantWithdrawResult ydMerchantWithdrawResult) {
		ydMerchantWithdrawResult.setUpdateTime(new Date());
		YdMerchantWithdraw ydMerchantWithdraw = new YdMerchantWithdraw();
		BeanUtilExt.copyProperties(ydMerchantWithdraw, ydMerchantWithdrawResult);
		this.ydMerchantWithdrawDao.updateYdMerchantWithdraw(ydMerchantWithdraw);
	}

	/**
	 * 商户余额提现
	 * @param merchantId	商户id
	 * @param withdrawAmount 提现金额
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void merchantWithdraw(Integer merchantId, Double withdrawAmount) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);

		ValidateBusinessUtils.assertFalse(withdrawAmount == null || withdrawAmount < 10 || withdrawAmount > 20000,
				"err_empty_withdraw_amount", "请输入正确的提现金额，单次提现金额为10-20000元");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(storeInfo.getWxOpenId()),
				"err_empty_open_id", "请先绑定门店微信");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(storeInfo.getCard()),
				"err_empty_open_id", "请先绑定身份证号码");

		Integer storeId = storeInfo.getId();
		YdMerchantAccount storeAccount = ydMerchantAccountDao.getYdMerchantAccountByMerchantId(storeId);
		ValidateBusinessUtils.assertFalse(storeAccount == null || storeAccount.getBalance() < withdrawAmount,
				"err_no_balance", "账户余额不足,提现失败");

		// 扣减账户余额
		int result = this.ydMerchantAccountDao.reduceAccountBalance(storeId, withdrawAmount);
		ValidateBusinessUtils.assertFalse(result <= 0,
				"err_balance", "账户扣钱失败，可能余额不足");

		// 计算费率,实际打款金额 实际打款金额 提现金额 - 费率
		Double rateAmount = MathUtils.multiply(withdrawAmount, withdrawRate, 2);
		Double payAmount = MathUtils.subtract(withdrawAmount, rateAmount, 2);

		// 增加提现记录
		YdMerchantWithdraw ydMerchantWithdraw = new YdMerchantWithdraw();
		ydMerchantWithdraw.setCreateTime(new Date());
		ydMerchantWithdraw.setStoreId(storeId);
		ydMerchantWithdraw.setMerchantId(merchantId);
		ydMerchantWithdraw.setWithdrawAmount(withdrawAmount);
		ydMerchantWithdraw.setRateAmount(rateAmount);
		ydMerchantWithdraw.setWithdrawOpenId(storeInfo.getWxOpenId());
		ydMerchantWithdraw.setIdCard(storeInfo.getCard());
		ydMerchantWithdraw.setRealname(storeInfo.getContact());
		// 审核状态(WAIT|FAIL|SUCCESS),现在全部SUCCESS)
		ydMerchantWithdraw.setStatus("SUCCESS");
		ydMerchantWithdraw.setMoneyStatus("WAIT");
		ydMerchantWithdraw.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
		this.ydMerchantWithdrawDao.insertYdMerchantWithdraw(ydMerchantWithdraw);

		boolean withdrawResult = true;
		try {
			// 调用微信提现接口
			WeiXinTransferUtils.WeiXinTransferResponse weiXinTransferResponse = WeiXinTransferUtils.weiXinTransfer(YdMerchantTransSourceEnum.WITHDRAWAL.getCode() + ydMerchantWithdraw.getId(),
					ydMerchantWithdraw.getWithdrawOpenId(), ydMerchantWithdraw.getRealname(), payAmount, "优度提现", "127.0.0.1");
			System.out.println("====优度微信提现返回的值=" + JSON.toJSONString(weiXinTransferResponse));
			logger.info("====优度微信提现返回的值=" + JSON.toJSONString(weiXinTransferResponse));
			if (weiXinTransferResponse.isSuccess()) {
				// 提现记录修改为成功
				ydMerchantWithdraw.setMoneyStatus("SUCCESS");
				this.ydMerchantWithdrawDao.updateYdMerchantWithdraw(ydMerchantWithdraw);

				Double afterBalance = MathUtils.subtract(storeAccount.getBalance(), withdrawAmount, 2);
				// 增加交易流水
				String transDesc = YdMerchantTransSourceEnum.WITHDRAWAL.getDescription() + ",提现金额:" +
						withdrawAmount + ",手续费:" + rateAmount + ",实际到账:" + payAmount;
				ydMerchantTransService.addMerchantTransDetail(storeId, "yd_merchant_withdraw_" + ydMerchantWithdraw.getId(),
						YdMerchantTransSourceEnum.WITHDRAWAL.getCode(), "OUT", transDesc,
						"SUCCESS", withdrawAmount, storeAccount.getBalance(), afterBalance, rateAmount);
			} else {
				// 失败的话给用户账户余额退钱
				this.ydMerchantAccountDao.addAccountBalance(merchantId, withdrawAmount);

				// 提现记录修改为失败
				ydMerchantWithdraw.setMoneyStatus("FAIL");
				this.ydMerchantWithdrawDao.updateYdMerchantWithdraw(ydMerchantWithdraw);
				withdrawResult = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			withdrawResult = false;
			// 失败的话给用户账户余额退钱
			this.ydMerchantAccountDao.addAccountBalance(merchantId, withdrawAmount);

			// 提现记录修改为失败
			ydMerchantWithdraw.setMoneyStatus("FAIL");
			this.ydMerchantWithdrawDao.updateYdMerchantWithdraw(ydMerchantWithdraw);
		}
		ValidateBusinessUtils.assertFalse(!withdrawResult,
				"err_withdraw", "系统异常，提现失败，请联系技术人员协助排查");
	}
}

