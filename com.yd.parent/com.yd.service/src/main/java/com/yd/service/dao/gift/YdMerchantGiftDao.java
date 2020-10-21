package com.yd.service.dao.gift;

import java.util.List;
import com.yd.service.bean.gift.YdMerchantGift;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户礼品库Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-04 09:33:43
 * @Version:1.1.0
 */
public interface YdMerchantGiftDao {

	/**
	 * 通过id得到商户礼品库YdMerchantGift
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantGift getYdMerchantGiftById(Integer id);

	/**
	 * 通过giftId得到商户礼品库YdMerchantGift
	 * @param giftId
	 * @return
	 * @Description:
	 */
	public YdMerchantGift getYdMerchantGiftByGiftId(@Param("giftId") Integer giftId,
													@Param("merchantId") Integer merchantId);

	List<YdMerchantGift> findYdMerchantGiftByIdList(@Param("merchantId") Integer merchantId,
													@Param("list") List<Integer> idList);
	
	/**
	 * 获取数量
	 * @param ydMerchantGift
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantGiftCount(YdMerchantGift ydMerchantGift);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantGift
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGift> findYdMerchantGiftListByPage(@Param("params") YdMerchantGift ydMerchantGift,
                                                             @Param("pageStart") Integer pageStart,
                                                             @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有商户礼品库YdMerchantGift
	 * @param ydMerchantGift
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantGift> getAll(YdMerchantGift ydMerchantGift);


	/**
	 * 添加商户礼品库YdMerchantGift
	 * @param ydMerchantGift
	 * @Description:
	 */
	public void insertYdMerchantGift(YdMerchantGift ydMerchantGift);
	

	/**
	 * 通过id修改商户礼品库YdMerchantGift
	 * @param ydMerchantGift
	 * @Description:
	 */
	public void updateYdMerchantGift(YdMerchantGift ydMerchantGift);

	/**
	 * 删除商户礼品
	 * @param id
	 */
    void deleteYdMerchantGift(Integer id);

}
