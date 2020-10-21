package com.yd.api.service;

import java.util.List;

import com.yd.api.result.ActIqiyiResult;

public	interface ActIqiyiService {

	List<ActIqiyiResult> getWaitList(int limit);

	void notifyIqiyiOrder(int id, String orderStatus, String failedCode, String failedReason,String billNo);

	void updateStatus(Integer id,String status);

	List<ActIqiyiResult> getWaitSendSms();

	int updateSmsStatus(Integer id);

	List<ActIqiyiResult> getNeedQueryConfirmStatusList();

}
