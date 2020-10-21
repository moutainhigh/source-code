package com.yd.service.dao.gift;

import java.util.List;

import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGiftCart;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户礼品购物车Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-01 14:29:29
 * @Version:1.1.0
 */
public interface YdMerchantGiftCartDao {

	/**
	 * 通过id得到商户礼品购物车YdMerchantGiftCart
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGiftCart getYdMerchantGiftCartById(Integer id);

	/**
	 * 通过idList 查询购物车
	 * @param carIdList
	 * @Description:
	 */
	public List<YdMerchantGiftCart> getYdMerchantGiftCartByIdList(@Param("list") List<Integer> carIdList);

	/**
	 * 通过merchantId得到商户礼品购物车YdMerchantGiftCart
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	List<YdMerchantGiftCart> getYdMerchantGiftCartByMerchantId(Integer merchantId);
	
	/**
	 * 获取数量
	 * @param ydMerchantGiftCart
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantGiftCartCount(YdMerchantGiftCart ydMerchantGiftCart);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantGiftCart
	 * @return
	 * @Description:
	 */
	public List<YdMerchantGiftCart> findYdMerchantGiftCartListByPage(@Param("params") YdMerchantGiftCart ydMerchantGiftCart,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);


	/**
	 * 得到所有商户礼品购物车YdMerchantGiftCart
	 * @param ydMerchantGiftCart
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGiftCart> getAll(YdMerchantGiftCart ydMerchantGiftCart);


	/**
	 * 添加商户礼品购物车YdMerchantGiftCart
	 * @param ydMerchantGiftCart
	 * @Description:
	 */
	public void insertYdMerchantGiftCart(YdMerchantGiftCart ydMerchantGiftCart);
	

	/**
	 * 通过id修改商户礼品购物车YdMerchantGiftCart
	 * @param ydMerchantGiftCart
	 * @Description:
	 */
	public void updateYdMerchantGiftCart(YdMerchantGiftCart ydMerchantGiftCart);

	/**
	 * 清空商户礼品购物车
	 * @param merchantId
	 */
	public void deleteMerchantGiftCart(Integer merchantId);

	/**
	 * 根据id删除礼品购物车礼品
	 * @param merchantId
	 * @param idList
	 */
	public void deleteMerchantGiftCartByIdList(@Param("merchantId") Integer merchantId,
										@Param("list") List<Integer> idList);
}
