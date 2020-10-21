package com.yd.api.service.draw;

import java.util.List;

import com.yd.api.result.draw.YdMerchantDrawPrizeResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户抽奖活动奖品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:34:22
 * @Version:1.1.0
 */
public interface YdMerchantDrawPrizeService {

	/**
	 * 通过id得到商户抽奖活动奖品YdMerchantDrawPrize
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantDrawPrizeResult getYdMerchantDrawPrizeById(Integer id);

	/**
	 * 分页查询商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrizeResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantDrawPrizeResult> findYdMerchantDrawPrizeListByPage(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrizeResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawPrizeResult> getAll(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult);

	/**
	 * 添加商户抽奖活动奖品YdMerchantDrawPrize
	 * @param ydMerchantDrawPrizeResult
	 * @Description:
	 */
	public void insertYdMerchantDrawPrize(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult) throws BusinessException;
	
	/**
	 * 通过id修改商户抽奖活动奖品YdMerchantDrawPrize throws BusinessException;
	 * @param ydMerchantDrawPrizeResult
	 * @Description:
	 */
	public void updateYdMerchantDrawPrize(YdMerchantDrawPrizeResult ydMerchantDrawPrizeResult)throws BusinessException;

}
