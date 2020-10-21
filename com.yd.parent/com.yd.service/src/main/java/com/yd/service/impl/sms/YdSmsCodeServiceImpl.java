package com.yd.service.impl.sms;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.result.sms.YdSmsCodeResult;
import com.yd.api.service.sms.YdSmsCodeService;
import com.yd.core.enums.YdSmsResourceEnum;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.sms.YdSmsCodeDao;
import com.yd.service.bean.sms.YdSmsCode;

/**
 * @Title:短信验证码Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdSmsCodeServiceImpl implements YdSmsCodeService {

	@Resource
	private YdSmsCodeDao ydSmsCodeDao;

	@Override
	public YdSmsCodeResult getYdSmsCodeById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdSmsCodeResult ydSmsCodeResult = null;
		YdSmsCode ydSmsCode = this.ydSmsCodeDao.getYdSmsCodeById(id);
		if (ydSmsCode != null) {
			ydSmsCodeResult = new YdSmsCodeResult();
			BeanUtilExt.copyProperties(ydSmsCodeResult, ydSmsCode);
		}
		return ydSmsCodeResult;
	}

	@Override
	public List<YdSmsCodeResult> getAll(YdSmsCodeResult params) {
		YdSmsCode ydSmsCode = null;
		if (params != null) {
			ydSmsCode = new YdSmsCode();
			BeanUtilExt.copyProperties(ydSmsCode, params);
		}
		List<YdSmsCode> dataList = this.ydSmsCodeDao.getAll(ydSmsCode);
		List<YdSmsCodeResult> resultList = DTOUtils.convertList(dataList, YdSmsCodeResult.class);
		return resultList;
	}

	@Override
	public void insertYdSmsCode (YdSmsCodeResult ydSmsCodeResult) {
		if (null != ydSmsCodeResult) {
			ydSmsCodeResult.setCreateTime(new Date());
			ydSmsCodeResult.setUpdateTime(new Date());
			YdSmsCode ydSmsCode = new YdSmsCode();
			BeanUtilExt.copyProperties(ydSmsCode, ydSmsCodeResult);
			this.ydSmsCodeDao.insertYdSmsCode(ydSmsCode);
		}
	}

	@Override
	public void updateYdSmsCode (YdSmsCodeResult ydSmsCodeResult) {
		if (null != ydSmsCodeResult) {
			ydSmsCodeResult.setUpdateTime(new Date());
			YdSmsCode ydSmsCode = new YdSmsCode();
			BeanUtilExt.copyProperties(ydSmsCode, ydSmsCodeResult);
			this.ydSmsCodeDao.updateYdSmsCode(ydSmsCode);
		}
	}

	/**
	 * 发送验证码
	 * @param mobile	手机号
	 * @param platform	平台		admin | shop
	 * @param smsResourceEnum	来源枚举
	 */
	@Override
	public void sendSmsCode(String mobile, String platform, YdSmsResourceEnum smsResourceEnum) {
		Date nowDate = new Date();
		String smsCode = StringUtil.getRandomDigit(6);
		// 暂时全部123456
		// smsCode = "123456";

		// 保存验证码
		YdSmsCode ydSmsCode = new YdSmsCode();
		ydSmsCode.setMobile(mobile);
		ydSmsCode.setSmsCode(smsCode);
		ydSmsCode.setPlatform(platform);
		ydSmsCode.setCreateTime(nowDate);
		ydSmsCode.setUpdateTime(nowDate);
		ydSmsCode.setSource(smsResourceEnum.getCode());
		ydSmsCode.setValidTime(DateUtils.addMinutes(nowDate, smsResourceEnum.getValidTime()));

		this.ydSmsCodeDao.insertYdSmsCode(ydSmsCode);

		JSONObject json = new JSONObject();
		json.put("code", smsCode);
		try {
			AliDaYuSmsSdkClient.sendSms("0", YdSmsResourceEnum.YD_SMS_FREE_SIGN_NAME.getCode(), json,
					mobile, YdSmsResourceEnum.YD_SMS_CODE_TEMPLATE.getCode());
		} catch (Exception e) {

		}
	}

	/**
	 * 校验短信验证码是否正确
	 * @param mobile	手机号
	 * @param smsCode	验证码
	 * @param platform	平台		admin | shop
	 * @param resource	来源
	 * @return
	 */
	@Override
	public boolean getLastSmsCode(String mobile, String smsCode, String platform, String resource) {
		YdSmsCode ydSmsCode = ydSmsCodeDao.getLastSmsCode(mobile, smsCode, resource, platform);
		if (ydSmsCode == null) {
			return false;
		} else {
			return true;
		}
	}

}

