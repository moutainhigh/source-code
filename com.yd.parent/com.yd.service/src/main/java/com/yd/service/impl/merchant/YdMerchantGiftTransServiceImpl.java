package com.yd.service.impl.merchant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.merchant.YdMerchantGiftTransResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.enums.YdLoginUserSourceEnums;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.*;
import com.yd.service.bean.merchant.*;
import com.yd.service.dao.merchant.YdMerchantAccountDao;
import com.yd.service.dao.merchant.YdMerchantDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.merchant.YdMerchantGiftTransDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户礼品账户流水Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 15:27:31
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantGiftTransServiceImpl implements YdMerchantGiftTransService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantGiftTransServiceImpl.class);

	@Resource
	private YdMerchantDao ydMerchantDao;

	@Resource
	private YdSmsCodeService ydSmsCodeService;

    @Resource
    private YdMerchantAccountDao ydMerchantAccountDao;

    @Resource
    private YdMerchantGiftTransDao ydMerchantGiftTransDao;

    @Resource
    private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdMerchantTransService ydMerchantTransService;

	@Resource
	private YdMerchantGiftTransService ydMerchantGiftTransService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantGiftTransResult getYdMerchantGiftTransById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantGiftTransResult ydMerchantGiftTransResult = null;
		YdMerchantGiftTrans ydMerchantGiftTrans = this.ydMerchantGiftTransDao.getYdMerchantGiftTransById(id);
		if (ydMerchantGiftTrans != null) {
			ydMerchantGiftTransResult = new YdMerchantGiftTransResult();
			BeanUtilExt.copyProperties(ydMerchantGiftTransResult, ydMerchantGiftTrans);

			YdMerchantTransSourceEnum transSourceEnum = YdMerchantTransSourceEnum.getByCode(ydMerchantGiftTransResult.getTransSource());
			if (transSourceEnum != null) {
				ydMerchantGiftTransResult.setIcon(transSourceEnum.getIcon());
			}
		}
		return ydMerchantGiftTransResult;
	}

	/**
	 * 分页查询商户礼品账户流水
	 * @param merchantId	商户id
	 * @param giftOrderId	礼品订单id
	 * @param transType		交易类型
	 * @param startTime		交易开始时间
	 * @param endTime		交易结束时间
	 * @param pagerInfo		分页信息
	 * @return Page<YdMerchantGiftTransResult>
	 */
	@Override
	public Page<YdMerchantGiftTransResult> getMerchantTransListByPage(Integer merchantId, String giftOrderId, String transType,
																	  String startTime, String endTime, PagerInfo pagerInfo) {
		Page<YdMerchantGiftTransResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = ydMerchantGiftTransDao.getMerchantGiftTransListCount(merchantId, giftOrderId, transType, startTime, endTime);
		if (amount > 0) {
			// 查询交易列表
			List<YdMerchantGiftTrans> dataList = ydMerchantGiftTransDao.getMerchantGiftTransListByPage(merchantId, giftOrderId, transType,
					startTime, endTime,(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());

			List<YdMerchantGiftTransResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftTransResult.class);

			resultList.forEach(ydMerchantGiftTransResult -> {
				YdMerchantTransSourceEnum transSourceEnum = YdMerchantTransSourceEnum.getByCode(ydMerchantGiftTransResult.getTransSource());
				if (transSourceEnum != null) {
					ydMerchantGiftTransResult.setIcon(transSourceEnum.getIcon());
				}
			});
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public List<YdMerchantGiftTransResult> getAll(YdMerchantGiftTransResult ydMerchantGiftTransResult) {
		YdMerchantGiftTrans ydMerchantGiftTrans = null;
		if (ydMerchantGiftTransResult != null) {
			ydMerchantGiftTrans = new YdMerchantGiftTrans();
			BeanUtilExt.copyProperties(ydMerchantGiftTrans, ydMerchantGiftTransResult);
		}
		List<YdMerchantGiftTrans> dataList = this.ydMerchantGiftTransDao.getAll(ydMerchantGiftTrans);
		List<YdMerchantGiftTransResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftTransResult.class);
		return resultList;
	}

	/**
	 * 新增礼品账户交易流水
	 * @param merchantId	 	 商户ID
	 * @param outOrderId	 	 外部订单ID
	 * @param transSource	  	交易流水来源(MerchantTransSourceEnum对应code)
	 * @param transType		  	交易类型(IN|OUT)
	 * @param transDesc		  	交易流水描述信息
	 * @param outOrderStatus  	外部订单状态
	 * @param transAmount		交易金额(单位元)
	 * @param transBeforeAmount	交易前金额
	 * @param transAfterAmount	交易后金额
	 */
	@Override
	public YdMerchantGiftTransResult addMerchantGiftTrans(Integer merchantId, String outOrderId, String transSource,
									 String transType, String transDesc, String outOrderStatus,
									 Double transAmount, Double transBeforeAmount, Double transAfterAmount) {
		Date nowDate = new Date();
		YdMerchantGiftTrans ydMerchantGiftTrans = new YdMerchantGiftTrans();
		ydMerchantGiftTrans.setCreateTime(nowDate);
		ydMerchantGiftTrans.setUpdateTime(nowDate);
		ydMerchantGiftTrans.setMerchantId(merchantId);
		ydMerchantGiftTrans.setOutOrderId(outOrderId);
		ydMerchantGiftTrans.setTransSource(transSource);
		ydMerchantGiftTrans.setTransType(transType);
		ydMerchantGiftTrans.setTransDesc(transDesc);
		ydMerchantGiftTrans.setTransAmount(transAmount);
		ydMerchantGiftTrans.setOutOrderStatus(outOrderStatus);
		ydMerchantGiftTrans.setTransBeforeAmount(transBeforeAmount);
		ydMerchantGiftTrans.setTransAfterAmount(transAfterAmount);
		logger.info("====记录礼品交易流水的入参=" + JSON.toJSONString(ydMerchantGiftTrans, true));
		this.ydMerchantGiftTransDao.insertYdMerchantGiftTrans(ydMerchantGiftTrans);

		YdMerchantGiftTransResult ydMerchantGiftTransResult = new YdMerchantGiftTransResult();
		BeanUtilExt.copyProperties(ydMerchantGiftTransResult, ydMerchantGiftTrans);
		return ydMerchantGiftTransResult;
	}

	/**
	 * 商户设置支付密码发送短信
	 * @param merchantId 商户id
	 * @throws BusinessException
	 */
	@Override
	public void sendPayPasswordSms(Integer merchantId) throws BusinessException {
		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");
		String mobile = null;
		if (ydMerchant.getPid() >= 0) {
			YdMerchant store = ydMerchantDao.getYdMerchantById(merchantId);
			mobile = store.getMobile();
		} else {
			mobile = ydMerchant.getMobile();
		}
		ydSmsCodeService.sendSmsCode(mobile, YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
				YdSmsResourceEnum.MERCHANT_SET_PAY_PASSWORD);
	}

	/**
	 * 商户设置支付密码
	 * @param merchantId 商户id
	 * @param smsCode	短信验证码
	 * @param password	第一次输入密码
	 * @param password2	第二次输入的密码
	 * @throws BusinessException
	 */
	@Override
	public void setPayPassword(Integer merchantId, String smsCode, String password, String password2) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(smsCode),
				"err_empty_code", "验证码不可以为空");

		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(password) ||
						StringUtil.isEmpty(password2), "err_empty_password", "密码不可以为空");

		ValidateBusinessUtils.assertFalse(!password.equalsIgnoreCase(password2),
				"err_password", "两次密码输入不一致");


		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();
		if (!ydSmsCodeService.getLastSmsCode(storeInfo.getMobile(), smsCode, YdLoginUserSourceEnums.YD_ADMIN_MERCHANT.getCode(),
				YdSmsResourceEnum.MERCHANT_SET_PAY_PASSWORD.getCode())) {
			ValidateBusinessUtils.assertFalse(true, "err_smsCode", "验证码错误");
		}
		ydMerchantDao.updatePayPassword(merchantId, PasswordUtil.encryptPassword(password), null);


//		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
//		ValidateBusinessUtils.assertFalse(ydMerchant == null,
//				"err_empty_id", "商户不存在");
//
//		String mobile = null;
//		if (ydMerchant.getPid() >= 0) {
//			YdMerchant store = ydMerchantDao.getYdMerchantById(merchantId);
//			mobile = store.getMobile();
//			merchantId = store.getId();
//		} else {
//			mobile = ydMerchant.getMobile();
//		}
	}

	/**
	 * 校验商户的密码
	 * @param merchantId	商户id
	 * @param oldPassword	商户的密码
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public boolean checkPassword(Integer merchantId, String oldPassword) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(oldPassword),
				"err_empty_password", "密码不可以为空");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");

		String payPassword = null;
		if (ydMerchant.getPid() >= 0) {
			YdMerchant store = ydMerchantDao.getYdMerchantById(merchantId);
			payPassword = store.getPayPassword();
		} else {
			payPassword = ydMerchant.getPayPassword();
		}
		if (!oldPassword.equalsIgnoreCase(payPassword)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 商户修改支付密码
	 * @param merchantId 商户id
	 * @param password	第一次输入密码
	 * @param password2	第二次输入的密码
	 * @throws BusinessException
	 */
	@Override
	public void updatePayPassword(Integer merchantId, String password, String password2) throws BusinessException {
		ValidateBusinessUtils.assertFalse(merchantId == null || merchantId <= 0,
				"err_empty_merchant_id", "商户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(password) ||
				StringUtil.isEmpty(password2), "err_empty_password", "密码不可以为空");

		ValidateBusinessUtils.assertFalse(!password.equalsIgnoreCase(password2),
				"err_password", "两次密码输入不一致");

		YdMerchant ydMerchant = ydMerchantDao.getYdMerchantById(merchantId);
		ValidateBusinessUtils.assertFalse(ydMerchant == null,
				"err_empty_id", "商户不存在");

		String mobile = null;
		String oldPassword = null;
		if (ydMerchant.getPid() >= 0) {
			YdMerchant store = ydMerchantDao.getYdMerchantById(merchantId);
			merchantId = store.getId();
			oldPassword = store.getPayPassword();
		} else {
			oldPassword = ydMerchant.getPayPassword();
		}
		ydMerchantDao.updatePayPassword(merchantId, PasswordUtil.encryptPassword(password), oldPassword);
	}

	/**
	 * 商户礼品余额转入账户中心
	 * @param merchantId 商户id
	 * @param transPrice 转移的金额
	 */
	@Override
	public void transIntoAccount(Integer merchantId, Double transPrice) throws BusinessException {
        ValidateBusinessUtils.assertFalse(transPrice == null || transPrice <= 0,
                "err_into_price", "请输入正确的转入金额");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

        YdMerchantAccount ydMerchantAccount = this.ydMerchantAccountDao.getYdMerchantAccountByMerchantId(merchantId);
        YdMerchantGiftAccount ydMerchantGiftAccount = this.ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(merchantId);
        if (ydMerchantGiftAccount.getBalance() >= transPrice) {
			// 礼品账户扣余额
        	int result = this.ydMerchantGiftAccountDao.reduceGiftAccountBalance(merchantId, transPrice);
            ValidateBusinessUtils.assertFalse(result <= 0,
                    "err_balance", "礼品账户余额不足");

            // 账户余额增加
            this.ydMerchantAccountDao.addAccountBalance(merchantId, transPrice);

            // 账户余额流水
			Double afterBalance = MathUtils.add(ydMerchantAccount.getBalance(), transPrice, 2);
            ydMerchantTransService.addMerchantTransDetail(merchantId, null, YdMerchantTransSourceEnum.GIFT_ACCOUNT_TRANS.getCode(),
                    "out", YdMerchantTransSourceEnum.GIFT_ACCOUNT_TRANS.getDescription(), "SUCCESS", transPrice,
                    ydMerchantAccount.getBalance(), afterBalance, 0.00);

            // 礼品账户余额流水
			Double giftAfterBalance = MathUtils.subtract(ydMerchantGiftAccount.getBalance(), transPrice, 2);
            ydMerchantGiftTransService.addMerchantGiftTrans(merchantId, null, YdMerchantTransSourceEnum.TRANS_ACCOUNT.getCode(),
                    "in", YdMerchantTransSourceEnum.TRANS_ACCOUNT.getDescription(), "SUCCESS", transPrice,
                    ydMerchantGiftAccount.getBalance(), giftAfterBalance);
        } else {
            throw new BusinessException("err_balance", "礼品账户余额不足");
        }
	}

}

