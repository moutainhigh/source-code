package com.yd.service.dao.message;

import java.util.List;
import com.yd.service.bean.message.YdMerchantMessage;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户消息通知Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-17 17:33:49
 * @Version:1.1.0
 */
public interface YdMerchantMessageDao {

	/**
	 * 通过id得到商户消息通知YdMerchantMessage
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMessage getYdMerchantMessageById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantMessage
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantMessageCount(YdMerchantMessage ydMerchantMessage);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantMessage
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMessage> findYdMerchantMessageListByPage(@Param("params") YdMerchantMessage ydMerchantMessage,
                                                                   @Param("pageStart") Integer pageStart,
                                                                   @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有商户消息通知YdMerchantMessage
	 * @param ydMerchantMessage
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMessage> getAll(YdMerchantMessage ydMerchantMessage);

	/**
	 * 添加商户消息通知YdMerchantMessage
	 * @param ydMerchantMessage
	 * @Description:
	 */
	public void insertYdMerchantMessage(YdMerchantMessage ydMerchantMessage);
	
	/**
	 * 通过id修改商户消息通知YdMerchantMessage
	 * @param ydMerchantMessage
	 * @Description:
	 */
	public void updateYdMerchantMessage(YdMerchantMessage ydMerchantMessage);

	/**
	 * 设置消息全部已读
	 * @param ydMerchantMessage
	 * @Description:
	 */
	public void updateAllMessageRead(YdMerchantMessage ydMerchantMessage);

}
