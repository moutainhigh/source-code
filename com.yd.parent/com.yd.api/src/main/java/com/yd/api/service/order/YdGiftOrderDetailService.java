package com.yd.api.service.order;

import java.util.List;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:礼品订单明细表Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 16:54:16
 * @Version:1.1.0
 */
public interface YdGiftOrderDetailService {

	/**
	 * 通过id得到礼品订单明细表YdGiftOrderDetail
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGiftOrderDetailResult getYdGiftOrderDetailById(Integer id);


	/**
	 * 查询供应商礼品订单详情
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdGiftOrderDetailResult getMerchantGiftOrderDetail(Integer id);


	/**
	 * 查询供应商礼品订单详情
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdGiftOrderDetailResult getSupplierGiftOrderDetail(Integer id);

	/**
	 * 得到所有礼品订单明细表YdGiftOrderDetail
	 * @param ydGiftOrderDetailResult
	 * @return 
	 * @Description:
	 */
	public List<YdGiftOrderDetailResult> getAll(YdGiftOrderDetailResult ydGiftOrderDetailResult);

	/**
	 * 创建礼品订单详情
	 * @param giftOrderId	  	礼品订单id
	 * @param supplierId	  	供应商id
	 * @param giftId		  	商户礼品id
	 * @param merchantGiftId  	平台礼品id
	 * @param giftNum		  	礼品数量
	 * @param marketPrice	  	市场价
	 * @param salePrice		  	礼品真实售价(如果为阶梯价的话显示阶梯价)
	 * @param purchasePrice	  	采购供应商的价格
	 */
	void createMerchantGiftOrderDetail(Integer giftOrderId, Integer supplierId, Integer merchantGiftId, Integer giftId,
									   Integer giftNum, Double marketPrice, Double salePrice, Double purchasePrice);


	/**
	 * 查询平台，供应商礼品订单
	 * @param giftOrderDetailResult	礼品订单详情信息
	 * @param pageInfo	分页信息
	 * @return
	 */
	Page<YdGiftOrderDetailResult> findGiftOrderDetailByPage(YdGiftOrderDetailResult giftOrderDetailResult, PagerInfo pageInfo);

	/**
	 * 商户礼品订单发货
	 * @param merchantId	商id
	 * @param detailOrderId	礼品订单详情id
	 * @param expressOrderId	订单编号
	 * @throws BusinessException
	 */
    void updateGiftOrderExpress(Integer merchantId, Integer detailOrderId, String expressOrderId) throws BusinessException;

	/**
	 * 礼品确认收货
	 * @param detailOrderId 礼品订单详情id
	 */
	void confirmGoods(Integer detailOrderId)  throws BusinessException;

	/**
	 * 礼品确认收货，根据物流号
	 * @param giftOrderId
	 * @param expressOrderId
	 * @throws BusinessException
	 */
	void appGiftOrderConfirmGoods(Integer giftOrderId, String expressOrderId) throws BusinessException;

	/**
	 * 商户确认礼品收货,根据礼品订单详情
	 * @param giftOrderDetailId
	 * @throws BusinessException
	 */
	void appConfirmGiftOrderDetail(Integer giftOrderDetailId) throws BusinessException;

	/**
	 * 查询供应商礼品订单
	 * @param ydGiftOrderDetailResult
	 * @param pageInfo
	 * @return
	 */
    Page<YdGiftOrderDetailResult> findSupplierGiftOrderDetailListByPage(YdGiftOrderDetailResult ydGiftOrderDetailResult, PagerInfo pageInfo);

}
