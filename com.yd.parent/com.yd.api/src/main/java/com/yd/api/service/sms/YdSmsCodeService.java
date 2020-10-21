package com.yd.api.service.sms;

import java.util.List;

import com.yd.api.result.sms.YdSmsCodeResult;
import com.yd.core.enums.YdSmsResourceEnum;

/**
 * @Title:短信验证码Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
public interface YdSmsCodeService {

	/**
	 * 通过id得到短信验证码YdSmsCode
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdSmsCodeResult getYdSmsCodeById(Integer id);

	/**
	 * 得到所有短信验证码YdSmsCode
	 * @param ydSmsCode
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdSmsCodeResult> getAll(YdSmsCodeResult ydSmsCode);


	/**
	 * 添加短信验证码YdSmsCode
	 * @param ydSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdSmsCode(YdSmsCodeResult ydSmsCode);
	

	/**
	 * 通过id修改短信验证码YdSmsCode
	 * @param ydSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdSmsCode(YdSmsCodeResult ydSmsCode);

	/**
	 * 发送短信验证码
	 * @param mobile	手机号
	 * @param platform	平台		admin | shop (LoginUserSourceEnums)
	 * @param smsResourceEnum	来源枚举
	 */
	void sendSmsCode(String mobile, String platform, YdSmsResourceEnum smsResourceEnum);

	/**
	 * 查询短信验证码是否存在
	 * @param mobile	手机号
	 * @param smsCode	验证码
	 * @param platform	平台		admin | shop
	 * @param resource	来源
	 */
	public boolean getLastSmsCode(String mobile, String smsCode, String platform, String resource);

}
