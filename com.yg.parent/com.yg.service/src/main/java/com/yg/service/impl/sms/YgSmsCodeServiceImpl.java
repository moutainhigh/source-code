package com.yg.service.impl.sms;

import com.yg.api.result.sms.YgSmsCodeResult;
import com.yg.core.enums.YgSmsResourceEnum;
import com.yg.core.utils.BeanUtilExt;
import com.yg.core.utils.DTOUtils;
import com.yg.core.utils.DateUtils;
import com.yg.core.utils.StringUtil;
import com.yg.api.service.sms.YgSmsCodeService;
import com.yg.service.bean.sms.YgSmsCode;
import com.yg.service.dao.sms.YgSmsCodeDao;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Title:短信验证码Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YgSmsCodeServiceImpl implements YgSmsCodeService {

	@Resource
	private YgSmsCodeDao ygSmsCodeDao;

	@Override
	public YgSmsCodeResult getYgSmsCodeById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YgSmsCodeResult ygSmsCodeResult = null;
		YgSmsCode ygSmsCode = this.ygSmsCodeDao.getYgSmsCodeById(id);
		if (ygSmsCode != null) {
			ygSmsCodeResult = new YgSmsCodeResult();
			BeanUtilExt.copyProperties(ygSmsCodeResult, ygSmsCode);
		}
		return ygSmsCodeResult;
	}

	@Override
	public List<YgSmsCodeResult> getAll(YgSmsCodeResult params) {
		YgSmsCode YgSmsCode = null;
		if (params != null) {
			YgSmsCode = new YgSmsCode();
			BeanUtilExt.copyProperties(YgSmsCode, params);
		}
		List<YgSmsCode> dataList = this.ygSmsCodeDao.getAll(YgSmsCode);
		List<YgSmsCodeResult> resultList = DTOUtils.convertList(dataList, YgSmsCodeResult.class);
		return resultList;
	}

	@Override
	public void insertYgSmsCode (YgSmsCodeResult ygSmsCodeResult) {
		ygSmsCodeResult.setCreateTime(new Date());
		ygSmsCodeResult.setUpdateTime(new Date());
		YgSmsCode ygSmsCode = new YgSmsCode();
		BeanUtilExt.copyProperties(ygSmsCode, ygSmsCodeResult);
		this.ygSmsCodeDao.insertYgSmsCode(ygSmsCode);
	}

	@Override
	public void updateYgSmsCode (YgSmsCodeResult ygSmsCodeResult) {
		ygSmsCodeResult.setUpdateTime(new Date());
		YgSmsCode ygSmsCode = new YgSmsCode();
		BeanUtilExt.copyProperties(ygSmsCode, ygSmsCodeResult);
		this.ygSmsCodeDao.updateYgSmsCode(ygSmsCode);
	}

	/**
	 * 发送验证码
	 * @param mobile	手机号
	 * @param platform	平台		admin | shop
	 * @param smsResourceEnum	来源枚举
	 */
	@Override
	public void sendSmsCode(String mobile, String platform, YgSmsResourceEnum smsResourceEnum) {
		Date nowDate = new Date();
		String smsCode = StringUtil.getRandomDigit(6);
		// todo 暂时全部123456
		smsCode = "123456";

		// 保存验证码
		YgSmsCode YgSmsCode = new YgSmsCode();
		YgSmsCode.setMobile(mobile);
		YgSmsCode.setSmsCode(smsCode);
		YgSmsCode.setPlatform(platform);
		YgSmsCode.setCreateTime(nowDate);
		YgSmsCode.setUpdateTime(nowDate);
		YgSmsCode.setSource(smsResourceEnum.getCode());
		YgSmsCode.setValidTime(DateUtils.addMinutes(nowDate, smsResourceEnum.getValidTime()));

		this.ygSmsCodeDao.insertYgSmsCode(YgSmsCode);
		// todo 发送短信

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
		YgSmsCode YgSmsCode = ygSmsCodeDao.getLastSmsCode(mobile, smsCode, resource, platform);
		if (YgSmsCode == null) {
			return false;
		} else {
			return true;
		}
	}

}

