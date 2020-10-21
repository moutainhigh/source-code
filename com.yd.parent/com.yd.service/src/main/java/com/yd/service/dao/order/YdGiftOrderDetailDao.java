package com.yd.service.dao.order;

import java.util.List;
import com.yd.service.bean.order.YdGiftOrderDetail;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:礼品订单明细表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:54:16
 * @Version:1.1.0
 */
public interface YdGiftOrderDetailDao {

	/**
	 * 通过id得到礼品订单明细表YdGiftOrderDetail
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftOrderDetail getYdGiftOrderDetailById(Integer id);

	/**
	 * 通过giftOrderId得到礼品订单明细表YdGiftOrderDetail
	 * @param giftOrderId
	 * @return
	 * @Description:
	 */
	public List<YdGiftOrderDetail> getYdGiftOrderDetailByGiftOrderId(Integer giftOrderId);
	
	/**
	 * 获取数量
	 * @param ydGiftOrderDetail
	 * @return
	 * @Description:
	 */
	public int getYdGiftOrderDetailCount(YdGiftOrderDetail ydGiftOrderDetail);

	/**
	 * 分页获取数据
	 * @param ydGiftOrderDetail
	 * @return
	 * @Description:
	 */
	public List<YdGiftOrderDetail> findYdGiftOrderDetailListByPage(@Param("params") YdGiftOrderDetail ydGiftOrderDetail,
                                                                   @Param("pageStart") Integer pageStart,
                                                                   @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有礼品订单明细表YdGiftOrderDetail
	 * @param ydGiftOrderDetail
	 * @return 
	 * @Description:
	 */
	public List<YdGiftOrderDetail> getAll(YdGiftOrderDetail ydGiftOrderDetail);

	/**
	 * 添加礼品订单明细表YdGiftOrderDetail
	 * @param ydGiftOrderDetail
	 * @Description:
	 */
	public void insertYdGiftOrderDetail(YdGiftOrderDetail ydGiftOrderDetail);

	/**
	 * 通过id修改礼品订单明细表YdGiftOrderDetail
	 * @param ydGiftOrderDetail
	 * @Description:
	 */
	public void updateYdGiftOrderDetail(YdGiftOrderDetail ydGiftOrderDetail);

	/**
	 * 通过id修改礼品订单明细表YdGiftOrderDetail
	 * @param ydGiftOrderDetail
	 * @Description:
	 */
	public void updateYdGiftOrderDetailByExpressOrderId(YdGiftOrderDetail ydGiftOrderDetail);

	/**
	 * 查询供应商礼品订单数量
	 * @param ydGiftOrderDetail
	 * @return
	 */
	int getSupplierGiftOrderDetailCount(YdGiftOrderDetail ydGiftOrderDetail);

	List<YdGiftOrderDetail> findSupplierGiftOrderDetailListByPage(@Param("params") YdGiftOrderDetail ydGiftOrderDetail,
																  @Param("pageStart") Integer pageStart,
																  @Param("pageSize") Integer pageSize);
}
