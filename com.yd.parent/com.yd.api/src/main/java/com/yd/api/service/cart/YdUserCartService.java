package com.yd.api.service.cart;

import java.util.List;
import java.util.Map;

import com.yd.api.req.MerchantGiftReq;
import com.yd.api.result.cart.SettlementDetailResult;
import com.yd.api.result.cart.YdUserCartDetailResult;
import com.yd.api.result.cart.YdUserCartResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:用户购物车Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-08 10:08:32
 * @Version:1.1.0
 */
public interface YdUserCartService {

	/**
	 * 通过id得到用户购物车YdUserCart
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserCartResult getYdUserCartById(Integer id);

	/**
	 * 根据购物车id获取购物车信息
	 * @param carIds
	 * @return
	 * @Description:
	 */
	public List<YdUserCartResult> findYdUserCartByIds(List<Integer> carIds);

	/**
	 * 得到所有用户购物车YdUserCart
	 * @param ydUserCartResult
	 * @return 
	 * @Description:
	 */
	public List<YdUserCartResult> getAll(YdUserCartResult ydUserCartResult);

	/**
	 * 添加商品到购物车
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param merchantItemSkuId 商户商品skuId
	 * @param num		添加的数量
	 * @param type		shop(页面加入购物车) cart(购物车继续添加修改)
	 */
    void addCart(Integer userId, Integer merchantId, Integer merchantItemSkuId, Integer num, String type) throws BusinessException;

	/**
	 * 购物车商品更换规格
	 * @param userId
	 * @param merchantId
	 * @param skuId
	 * @param num
	 * @param cartId
	 */
	void updateCart(Integer userId, Integer merchantId, Integer skuId, Integer num, Integer cartId);

	/**
	 * 删除购物车商品
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param carIdList	 购物车ids
	 */
	void deleteCart(Integer userId, Integer merchantId, List<Integer> carIdList) throws BusinessException;

	/**
	 * 清空购物车
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 */
	void clearCart(Integer userId, Integer merchantId) throws BusinessException;

	/**
	 * 查询购物车列表
	 * @param userId
	 * @param merchantId
	 * @return
	 */
	Map<String, List<YdUserCartDetailResult>> findCartList(Integer userId, Integer merchantId) throws BusinessException;

	/**
	 * 购物车选中计算优惠券明细
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param carIdList	 购物车idList
	 * @return
	 * 		totalMonty		总金额
	 * 		discountMoney	优惠券金额
	 * 		payMoney		优惠后金额
	 * 		giftMoney		可选礼品金额
	 */
    Map<String,Object> calcCartCheckedMonty(Integer userId, Integer merchantId, List<Integer> carIdList) throws BusinessException;

	/**
	 * 订单结算详情页面数据
	 * @param userId	   用户id
	 * @param merchantId   商户id
	 * @param skuId		   商户商品skuId
	 * @param num		   商品购买数量
	 * @param type		   car | item
	 * @param carIds	   购物车carIds
	 * @param giftList	   礼品idList id and num
	 * @return
	 */
	SettlementDetailResult settlementDetail(Integer userId, Integer merchantId, Integer skuId, Integer num,
											String type, String carIds, List<MerchantGiftReq> giftList) throws BusinessException;

}
