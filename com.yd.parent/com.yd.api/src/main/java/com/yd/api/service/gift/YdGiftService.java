package com.yd.api.service.gift;

import java.util.List;

import com.yd.api.result.gift.YdGiftResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;


/**
 * @Title:平台礼品Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 20:01:07
 * @Version:1.1.0
 */
public interface YdGiftService {

	/**
	 * 通过id得到平台礼品YdGift
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftResult getYdGiftById(Integer id);

	/**
	 * 分页查询平台礼品YdGift
	 * @param ydGiftResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdGiftResult> findYdGiftListByPage(YdGiftResult ydGiftResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有平台礼品YdGift
	 * @param ydGiftResult
	 * @return 
	 * @Description:
	 */
	public List<YdGiftResult> getAll(YdGiftResult ydGiftResult);

	/**
	 * 添加平台礼品YdGift
	 * @param ydGiftResult
	 * @Description:
	 */
	public void insertYdGift(YdGiftResult ydGiftResult) throws BusinessException;
	

	/**
	 * 通过id修改平台礼品YdGift throws BusinessException;
	 * @param ydGiftResult
	 * @Description:
	 */
	public void updateYdGift(YdGiftResult ydGiftResult) throws BusinessException;

	/**
	 * 上下架礼品库礼品
	 * @param id
	 * @param isEnable	Y | N
	 * @throws BusinessException
	 */
	public void upOrDownYdGift(Integer id, String isEnable) throws BusinessException;

	/**
	 * 删除礼品库礼品
	 * @param id
	 * @throws BusinessException
	 */
	public void deleteYdGift(Integer id) throws BusinessException;

	/**
	 * 修改打款状态为成功
	 * @param giftOrderDetailIds
	 * @throws BusinessException
	 */
	public void updateSettlementStatus(String giftOrderDetailIds) throws BusinessException;

	/**
	 * 平台礼品库修改线下采购价,数量
	 * @param giftOrderDetailId		平台礼品订单详情ID
	 * @param num					num 线下采购数量
	 * @param purchasePrice			采购单价
	 * @throws BusinessException
	 */
	public void updatePurchaseInfo(Integer giftOrderDetailId, Integer num, Double purchasePrice) throws BusinessException;

	/**
	 * 平台礼品库新增线下采购价,数量
	 * @param giftId	礼品id
	 * @param supplierId	供应商id
	 * @param num		线下采购数量
	 * @param purchasePrice 采购单价
	 */
	void insertPurchaseInfo(Integer giftId, Integer supplierId, Integer num, Double purchasePrice) throws BusinessException;

}
