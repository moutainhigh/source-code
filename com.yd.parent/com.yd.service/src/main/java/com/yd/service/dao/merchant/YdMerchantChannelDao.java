package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantChannel;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商户渠道Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-30 13:02:38
 * @Version:1.1.0
 */
public interface YdMerchantChannelDao {

	/**
	 * 通过id得到商户渠道YdMerchantChannel
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantChannel getYdMerchantChannelById(Integer id);

	/**
	 * 通过merchantId得到商户渠道YgMerchantChannel
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantChannel getYdMerchantChannelByMerchantId(Integer merchantId);
	
	/**
	 * 获取数量
	 * @param ydMerchantChannel
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantChannelCount(YdMerchantChannel ydMerchantChannel);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantChannel
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantChannel> findYdMerchantChannelListByPage(@Param("params") YdMerchantChannel ydMerchantChannel,
                                                                   @Param("pageStart") Integer pageStart,
                                                                   @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有商户渠道YdMerchantChannel
	 * @param ydMerchantChannel
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantChannel> getAll(YdMerchantChannel ydMerchantChannel);


	/**
	 * 添加商户渠道YdMerchantChannel
	 * @param ydMerchantChannel
	 * @Description:
	 */
	public void insertYdMerchantChannel(YdMerchantChannel ydMerchantChannel);
	

	/**
	 * 通过id修改商户渠道YdMerchantChannel
	 * @param ydMerchantChannel
	 * @Description:
	 */
	public void updateYdMerchantChannel(YdMerchantChannel ydMerchantChannel);
	
}
