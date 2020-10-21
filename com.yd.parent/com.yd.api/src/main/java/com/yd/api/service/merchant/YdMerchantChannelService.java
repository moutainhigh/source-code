package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.result.merchant.YdMerchantChannelResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户渠道Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-30 13:02:38
 * @Version:1.1.0
 */
public interface YdMerchantChannelService {

	/**
	 * 通过id得到商户渠道YdMerchantChannel
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantChannelResult getYdMerchantChannelById(Integer id);

	/**
	 * 通过merchantId得到商户渠道YdMerchantChannel
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantChannelResult getYdMerchantChannelByMerchantId(Integer merchantId);

	/**
	 * 分页查询商户渠道YdMerchantChannel
	 * @param ydMerchantChannelResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantChannelResult> findYdMerchantChannelListByPage(YdMerchantChannelResult ydMerchantChannelResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户渠道YdMerchantChannel
	 * @param ydMerchantChannelResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantChannelResult> getAll(YdMerchantChannelResult ydMerchantChannelResult);

	/**
	 * 添加商户渠道YdMerchantChannel
	 * @param ydMerchantChannelResult
	 * @Description:
	 */
	public Integer insertYdMerchantChannel(YdMerchantChannelResult ydMerchantChannelResult) throws BusinessException;
	
	/**
	 * 通过id修改商户渠道YdMerchantChannel throws BusinessException;
	 * @param ydMerchantChannelResult
	 * @Description:
	 */
	public Integer updateYdMerchantChannel(YdMerchantChannelResult ydMerchantChannelResult)throws BusinessException;

}
