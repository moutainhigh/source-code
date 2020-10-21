package com.yd.service.dao.sms;

import java.util.List;
import com.yd.service.bean.sms.YdSmsCode;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:短信验证码Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
public interface YdSmsCodeDao {

	/**
	 * 通过id得到短信验证码YdSmsCode
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YdSmsCode getYdSmsCodeById(Integer id);

	/**
	 * 得到所有短信验证码YdSmsCode
	 * @param ydSmsCode
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YdSmsCode> getAll(YdSmsCode ydSmsCode);


	YdSmsCode getLastSmsCode(@Param("mobile") String mobile,
							 @Param("smsCode") String smsCode,
							 @Param("source") String source,
							 @Param("platform") String platform);

	/**
	 * 添加短信验证码ydSmsCode
	 * @param ydSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void insertYdSmsCode(YdSmsCode ydSmsCode);
	
	/**
	 * 通过id修改短信验证码YdSmsCode
	 * @param ydSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void updateYdSmsCode(YdSmsCode ydSmsCode);


}
