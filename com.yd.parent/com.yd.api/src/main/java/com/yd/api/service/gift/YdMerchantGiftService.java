package com.yd.api.service.gift;

import java.util.List;

import com.yd.api.req.MerchantItmSkuReq;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户礼品库Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 09:33:43
 * @Version:1.1.0
 */
public interface YdMerchantGiftService {

	/**
	 * 通过id得到商户礼品库YdMerchantGift
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftResult getYdMerchantGiftById(Integer id) throws BusinessException;

	public YdMerchantGiftResult getYdMerchantGiftDetail(Integer id) throws BusinessException;

	/**
	 * 通过idList得到商户礼品库YdMerchantGift
	 * @param merchantId
	 * @param idList
	 * @return
	 * @Description:
	 */
	public List<YdMerchantGiftResult> findYdMerchantGiftByIdList(Integer merchantId, List<Integer> idList) throws BusinessException;

	/**
	 * 分页查询商户礼品库YdMerchantGift
	 * @param ydMerchantGiftResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantGiftResult> findYdMerchantGiftListByPage(YdMerchantGiftResult ydMerchantGiftResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户礼品库YdMerchantGift
	 * @param ydMerchantGiftResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftResult> getAll(YdMerchantGiftResult ydMerchantGiftResult);

	/**
	 * 添加商户礼品库YdMerchantGift
	 * @param ydMerchantGiftResult
	 * @Description:
	 */
	public void insertYdMerchantGift(YdMerchantGiftResult ydMerchantGiftResult) throws BusinessException;

	/**
	 * 通过id修改商户礼品库YdMerchantGift throws BusinessException;
	 * @param ydMerchantGiftResult
	 * @Description:
	 */
	public void updateYdMerchantGift(YdMerchantGiftResult ydMerchantGiftResult) throws BusinessException;

	/**
	 * 商户礼品上下架
	 * @param merchantId 	商户id
	 * @param id			商户礼品id
	 * @param isEnable		Y | N
	 */
	void upOrDownYdMerchantGift(Integer merchantId, Integer id, String isEnable) throws BusinessException;

	/**
	 * 删除商户礼品
	 * @param id	商户礼品id
	 */
	void deleteYdMerchantGift(Integer id) throws BusinessException;

	/**
	 * 导入商户礼品库
	 * @param merchantId 商户id
	 * @param carIdList	 购物车idList
	 */
	void exportMerchantGift(Integer merchantId, List<Integer> carIdList) throws BusinessException;

	/**
	 * 根据商品查询礼品列表
	 * @param merchantId	商户id
	 * @param skuList	商户商品skuId + num
	 * @return
	 */
    List<YdMerchantGiftResult> findGiftListBySkuList(Integer merchantId, List<MerchantItmSkuReq> skuList) throws BusinessException;

	/**
	 * 根据商品查询礼品占比总金额
	 * @param merchantId	商户id
	 * @param skuList		商户商品规格id + 数量
	 * @return
	 */
	public double getGiftSumPrice(Integer merchantId, List<MerchantItmSkuReq> skuList) throws BusinessException;

	/**
	 * 获取下单时订单商品礼品占比总金额
	 * @param merchantId 商户id
	 * @param carIds	 购物车ids
	 * @param skuId		 规格id
	 * @param num		 购买数量
	 * @param num		 car 代表购物车
	 * @return 礼品占比总金额
	 * @throws BusinessException
	 */
	public double getOrderGiftPrice(Integer merchantId, String carIds, Integer skuId, Integer num, String type) throws BusinessException;

}
