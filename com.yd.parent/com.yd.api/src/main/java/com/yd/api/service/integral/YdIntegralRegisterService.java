package com.yd.api.service.integral;

import java.util.List;

import com.yd.api.result.integral.YdIntegralRegisterResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:积分登记Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:42:46
 * @Version:1.1.0
 */
public interface YdIntegralRegisterService {

	/**
	 * 通过id得到积分登记YdIntegralRegister
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdIntegralRegisterResult getYdIntegralRegisterById(Integer id);

	/**
	 * 分页查询积分登记YdIntegralRegister
	 * @param ydIntegralRegisterResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdIntegralRegisterResult> findYdIntegralRegisterListByPage(YdIntegralRegisterResult ydIntegralRegisterResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有积分登记YdIntegralRegister
	 * @param ydIntegralRegisterResult
	 * @return 
	 * @Description:
	 */
	public List<YdIntegralRegisterResult> getAll(YdIntegralRegisterResult ydIntegralRegisterResult);

	/**
	 * 添加积分登记YdIntegralRegister
	 * @param ydIntegralRegisterResult
	 * @Description:
	 */
	public void insertYdIntegralRegister(YdIntegralRegisterResult ydIntegralRegisterResult) throws BusinessException;

	/**
	 * 通过id修改积分登记YdIntegralRegister throws BusinessException;
	 * @param ydIntegralRegisterResult
	 * @Description:
	 */
	public void updateYdIntegralRegister(YdIntegralRegisterResult ydIntegralRegisterResult)throws BusinessException;

}
