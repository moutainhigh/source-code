package com.yd.api.service.gift;

import java.util.List;
import java.util.Map;

import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.gift.YdMerchantGiftCartResult;
import com.yd.core.utils.BusinessException;

/**
 * @Title:商户礼品购物车Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-01 14:29:29
 * @Version:1.1.0
 */
public interface YdMerchantGiftCartService {

	/**
	 * 通过id得到商户礼品购物车YdMerchantGiftCart
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftCartResult getYdMerchantGiftCartById(Integer id);

	/**
	 * 得到所有商户礼品购物车YdMerchantGiftCart
	 * @param ydMerchantGiftCartResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftCartResult> getAll(YdMerchantGiftCartResult ydMerchantGiftCartResult);

	/**
	 * 添加礼品到购物车
	 * @param merchantId 商户ID
	 * @param giftId	礼品id
	 * @param num		数量
	 * @param type shop(页面加入购物车) cart(购物车继续添加修改)
	 * @throws BusinessException
	 */
	public void addMerchantGiftCart(Integer merchantId, Integer giftId, Integer num, String type) throws BusinessException;

	/**
	 * 通过id修改商户礼品购物车YdMerchantGiftCart throws BusinessException;
	 * @param ydMerchantGiftCartResult
	 * @Description:
	 */
	public void updateYdMerchantGiftCart(YdMerchantGiftCartResult ydMerchantGiftCartResult) throws BusinessException;

	/**
	 * 查询商户礼品购物车数量
	 * @param currMerchantId
	 * @return
	 */
	Integer getMerchantGiftCartCount(Integer currMerchantId) throws BusinessException;

	/**
	 * 商户清空礼品购物车
	 * @param merchantId
	 * @throws BusinessException
	 */
	void clearMerchantGiftCart(Integer merchantId) throws BusinessException;

	/**
	 * 查询商户购物车数据
	 * @param merchantId
	 * @return
	 */
	List<YdGiftResult> getMerchantGiftCart(Integer merchantId);

	/**
	 * 商户礼品购物车提交订单
	 * @param merchantId	商户id
	 * @param carIds		购物车ids
	 * @return	balance payPrice
	 */
	Map<String, Double> submitMerchantGiftCart(Integer merchantId, String carIds) throws BusinessException;

	/**
	 * 商户付款购物车礼品
	 * @param merchantId	商户id
	 * @param password		礼品支付密码
	 * @param carIds		购物车ids
	 * @return
	 * @throws BusinessException
	 */
    Boolean toPay(Integer merchantId, String password, String carIds) throws BusinessException;

	/**
	 * 删除商户购物车礼品
	 * @param currMerchantId
	 * @param carIds
	 * @throws BusinessException
	 */
	void deleteMerchantGiftCart(Integer currMerchantId, String carIds) throws BusinessException;

	/**
	 * 获取礼品商城购物车选中价格
	 * @param merchantId
	 * @param carIds
	 */
	Double getGiftCartTotalPrice(Integer merchantId, String carIds) throws BusinessException;

}
