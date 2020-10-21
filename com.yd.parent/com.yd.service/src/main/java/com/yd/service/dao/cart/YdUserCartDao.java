package com.yd.service.dao.cart;

import java.util.List;
import com.yd.service.bean.cart.YdUserCart;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户购物车Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-08 10:08:32
 * @Version:1.1.0
 */
public interface YdUserCartDao {

	/**
	 * 通过id得到用户购物车YdUserCart
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserCart getYdUserCartById(Integer id);

	/**
	 * 通过ids得到用户购物车YdUserCart
	 * @param carIdList
	 * @return
	 * @Description:
	 */
	public List<YdUserCart> findYdUserCartByIds(List<Integer> carIdList);

	/**
	 * 得到所有用户购物车YdUserCart
	 * @param ydUserCart
	 * @return 
	 * @Description:
	 */
	public List<YdUserCart> getAll(YdUserCart ydUserCart);

	/**
	 * 添加用户购物车YdUserCart
	 * @param ydUserCart
	 * @Description:
	 */
	public void insertYdUserCart(YdUserCart ydUserCart);
	
	/**
	 * 通过id修改用户购物车YdUserCart
	 * @param ydUserCart
	 * @Description:
	 */
	public void updateYdUserCart(YdUserCart ydUserCart);

	/**
	 * 删除购物车商品
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param carIdList	 购物车ids
	 */
    void deleteCartByIdList(@Param("userId") Integer userId,
							@Param("merchantId") Integer merchantId,
							@Param("list") List<Integer> carIdList);

	/**
	 * 清空购物车
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 */
	void clearCart(@Param("userId") Integer userId, @Param("merchantId") Integer merchantId);

	/**
	 * 查询用户购物车
	 * @param userId
	 * @param merchantId
	 * @param carIdList
	 * @return
	 */
    List<YdUserCart> findCartListByIdList(@Param("userId")Integer userId,
										  @Param("merchantId") Integer merchantId,
										  @Param("list") List<Integer> carIdList);

}
