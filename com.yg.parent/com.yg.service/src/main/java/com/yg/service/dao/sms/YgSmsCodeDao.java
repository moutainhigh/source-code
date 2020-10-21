package com.yg.service.dao.sms;

import com.yg.service.bean.sms.YgSmsCode;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Title:短信验证码Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 09:58:15
 * @Version:1.1.0
 */
public interface YgSmsCodeDao {

	/**
	 * 通过id得到短信验证码YgSmsCode
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public YgSmsCode getYgSmsCodeById(Integer id);

	/**
	 * 得到所有短信验证码YgSmsCode
	 * @param YgSmsCode
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<YgSmsCode> getAll(YgSmsCode YgSmsCode);


	public YgSmsCode getLastSmsCode(@Param("mobile") String mobile,
                             @Param("smsCode") String smsCode,
                             @Param("source") String source,
                             @Param("platform") String platform);

	/**
	 * 添加短信验证码YgSmsCode
	 * @param YgSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void insertYgSmsCode(YgSmsCode YgSmsCode);
	
	/**
	 * 通过id修改短信验证码YgSmsCode
	 * @param YgSmsCode
	 * @throws Exception
	 * @Description:
	 */
	public void updateYgSmsCode(YgSmsCode YgSmsCode);

}
