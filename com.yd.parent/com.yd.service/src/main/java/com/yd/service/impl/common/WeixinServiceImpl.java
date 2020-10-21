package com.yd.service.impl.common;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;

import com.yd.api.result.common.WbWeixinAccountResult;
import com.yd.api.service.common.WeixinService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.service.bean.common.WbWeixinAccount;
import com.yd.service.dao.common.WbWeixinAccountDao;

@Service(dynamic = true)
public class WeixinServiceImpl implements WeixinService {
	@Resource
	private WbWeixinAccountDao	wbWeixinAccountDao;

	@Override
	public WbWeixinAccountResult getByWeixinAccountByType(String type) {
		WbWeixinAccount account=wbWeixinAccountDao.getByWeixinAccountByType(type);
		if(account==null) {
			return null;
		}
		
		WbWeixinAccountResult result=new WbWeixinAccountResult();
		BeanUtilExt.copyProperties(result, account);
		
		return result;
	}

	@Override
	public void updateAccessTokenWithTicket(WbWeixinAccountResult result) {
		WbWeixinAccount account=new WbWeixinAccount();
		BeanUtilExt.copyProperties(account, result);
		wbWeixinAccountDao.updateAccessTokenWithTicket(account);
	}

}
