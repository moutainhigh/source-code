package com.yd.api.service.common;

import java.util.Date;

import com.yd.api.pay.EnumPayMethod;
import com.yd.api.pay.req.PayReq;
import com.yd.api.pay.result.PayParam;
import com.yd.api.result.common.WbAlipayAccountResult;
import com.yd.api.result.common.WbPayOrderResult;
import com.yd.api.result.common.WbPaySecretResult;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.core.utils.BusinessException;

public interface PayService {
	WbAlipayAccountResult findAlipayAccountById(int id);

	WbWeixinAccountResult findWexinAccountById(int id);

	WbPaySecretResult findPaySecretById(int id);

	WbPayOrderResult payNotifyWithPayAccountId(Integer payOrderId, EnumPayMethod alipayWap, String totalFee,String tradeNo, Date date, Integer id);

	PayParam createOrderAndPay(PayReq req)  throws BusinessException;

	WbPayOrderResult findPayOrderById(Integer id);

	WbAlipayAccountResult findDefaultAlipayAccount();

	WbWeixinAccountResult findDefaultWexinAccount();


}
