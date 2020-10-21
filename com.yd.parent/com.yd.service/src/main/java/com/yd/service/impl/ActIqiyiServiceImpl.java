package com.yd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;

import com.yd.api.result.ActIqiyiResult;
import com.yd.api.service.ActIqiyiService;
import com.yd.core.utils.DTOUtils;
import com.yd.service.bean.ActIqiyi;
import com.yd.service.dao.ActIqiyiDao;

@Service(dynamic = true)
public class ActIqiyiServiceImpl implements ActIqiyiService {
	@Resource
	private ActIqiyiDao		actIqiyiDao;

	@Override
	public List<ActIqiyiResult> getWaitList(int limit) {
		List<ActIqiyi> list=actIqiyiDao.getWaitList(limit);
		return DTOUtils.convertList(list, ActIqiyiResult.class);
	}

	@Override
	public void notifyIqiyiOrder(int id, String orderStatus, String failedCode, String failedReason,String billNo) {
		actIqiyiDao.notifyIqiyiOrder(id,orderStatus,failedCode,failedReason,billNo);
	}

	@Override
	public void updateStatus(Integer id,String status) {
		actIqiyiDao.updateStatus(id,status);
	}

	@Override
	public List<ActIqiyiResult> getWaitSendSms() {
		List<ActIqiyi> list=actIqiyiDao.getWaitSendSms();
		return DTOUtils.convertList(list, ActIqiyiResult.class);
	}

	@Override
	public int updateSmsStatus(Integer id) {
		return actIqiyiDao.updateSmsStatus(id);
	}

	@Override
	public List<ActIqiyiResult> getNeedQueryConfirmStatusList() {
		List<ActIqiyi> list=actIqiyiDao.getNeedQueryConfirmStatusList();
		return DTOUtils.convertList(list, ActIqiyiResult.class);
	}

}
