package com.yd.service.impl.common;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;

import com.yd.api.pay.EnumPayMethod;
import com.yd.api.pay.req.PayReq;
import com.yd.api.pay.result.PayParam;
import com.yd.api.result.common.WbAlipayAccountResult;
import com.yd.api.result.common.WbPayOrderResult;
import com.yd.api.result.common.WbPaySecretResult;
import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.service.common.PayService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.service.bean.common.WbAlipayAccount;
import com.yd.service.bean.common.WbPayOrder;
import com.yd.service.bean.common.WbPaySecret;
import com.yd.service.bean.common.WbWeixinAccount;
import com.yd.service.dao.common.WbAlipayAccountDao;
import com.yd.service.dao.common.WbPayOrderDao;
import com.yd.service.dao.common.WbPaySecretDao;
import com.yd.service.dao.common.WbWeixinAccountDao;

@Service(dynamic = true)
public class PayServiceImpl implements PayService {
	@Resource
	private WbAlipayAccountDao		wbAlipayAccountDao;
	@Resource
	private WbWeixinAccountDao		wbWeixinAccountDao;
	@Resource
	private WbPaySecretDao		wbPaySecretDao;
	@Resource
	private WbPayOrderDao	wbPayOrderDao;

	@Override
	public WbAlipayAccountResult findAlipayAccountById(int id) {
		WbAlipayAccount item=wbAlipayAccountDao.findWbAlipayAccountById(id);
		if(item==null) {
			return null;
		}
		WbAlipayAccountResult result=new WbAlipayAccountResult();
		BeanUtilExt.copyProperties(result, item);
		
		return result;
	}
	
	@Override
	public WbWeixinAccountResult findWexinAccountById(int id) {
		WbWeixinAccount item=wbWeixinAccountDao.findWbWeixinAccountById(id);
		if(item==null) {
			return null;
		}
		WbWeixinAccountResult result=new WbWeixinAccountResult();
		BeanUtilExt.copyProperties(result, item);
		
		return result;
	}


	@Override
	public WbPaySecretResult findPaySecretById(int id) {
		WbPaySecret item=wbPaySecretDao.findWbPaySecretById(id);
		if(item==null) {
			return null;
		}
		WbPaySecretResult result=new WbPaySecretResult();
		BeanUtilExt.copyProperties(result, item);
		
		return result;
	}

	@Override
	public WbPayOrderResult payNotifyWithPayAccountId(Integer payOrderId, EnumPayMethod enumPayMethod, String totalAmount,String billNo, Date payTime, Integer accountId) {
        double amount = new BigDecimal("" + totalAmount).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        String payMethod = enumPayMethod.getCode();
        wbPayOrderDao.payNotifyWithPayAccountId(payOrderId, amount, billNo, payTime, payMethod, accountId);
        WbPayOrder order = wbPayOrderDao.findWbPayOrderById(payOrderId);
        if (order == null) {
            return null;
        }
        WbPayOrderResult result = new WbPayOrderResult();
        BeanUtilExt.copyProperties(result, order);
        return result;
	}

	@Override
	public PayParam createOrderAndPay(PayReq req) throws BusinessException{
        WbPayOrder order = wbPayOrderDao.findQyPayOrderByOutOrderId(req.getOutOrderId());
        Date date = new Date();
        if (order == null) {
            order = new WbPayOrder();
            order.setUserId(req.getUserId());
            order.setMerchantId(req.getMerchantId());
            order.setTitle(req.getSubject());
            order.setReturnUrl(req.getReturnUrl());
            order.setNotifyUrl(req.getNotifyUrl());
            order.setMoney(req.getAmount());
            order.setCreateTime(date);
            order.setOutOrderId(req.getOutOrderId());
            order.setPayMethod(req.getPayMethod().getCode());
            order.setIsPay("N");
            order.setIsNotify("N");
            order.setPrepareId(req.getPrepareId());
            wbPayOrderDao.insert(order);
        }
        if ("Y".equals(order.getIsPay())) {
            throw new BusinessException("error_is_pay", "订单已经支付");
        }
        PayParam payParam = new PayParam();
        payParam.setAmount(req.getAmount());
        payParam.setOutOrderId(req.getOutOrderId());
        payParam.setSubject(req.getSubject());
        payParam.setPrepareId(req.getPrepareId());

        payParam.setPayMethod(req.getPayMethod().getCode());
        if (EnumPayMethod.ALIPAY_WAP == req.getPayMethod()) {
            // 支付宝默认账户
            payParam.setGoUrl("/goToAlipay?outOrderId=payOrder-" + order.getId());
        } else if (EnumPayMethod.WECHAT_MP == req.getPayMethod()) {
            payParam.setGoUrl("/goToWxPay?outOrderId=payOrder-" + order.getId());
        } else {
            throw new BusinessException("err_payMethod", "支付方式错误");
        }
        return payParam;
	}

	@Override
	public WbPayOrderResult findPayOrderById(Integer id) {
		WbPayOrder item=wbPayOrderDao.findWbPayOrderById(id);
		if(item==null) {
			return null;
		}
		WbPayOrderResult result=new WbPayOrderResult();
		BeanUtilExt.copyProperties(result, item);
		return result;
	}

	@Override
	public WbAlipayAccountResult findDefaultAlipayAccount() {
		WbAlipayAccount item=wbAlipayAccountDao.findDefaultAlipayAccount();
		if(item==null) {
			return null;
		}
		WbAlipayAccountResult result=new WbAlipayAccountResult();
		BeanUtilExt.copyProperties(result, item);
		return result;
	}

	@Override
	public WbWeixinAccountResult findDefaultWexinAccount() {
		WbWeixinAccount item=wbWeixinAccountDao.findDefaultWexinAccount();
		if(item==null) {
			return null;
		}
		WbWeixinAccountResult result=new WbWeixinAccountResult();
		BeanUtilExt.copyProperties(result, item);
		
		return result;
	}


}
