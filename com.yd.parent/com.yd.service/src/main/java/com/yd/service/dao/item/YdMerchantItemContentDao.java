package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdMerchantItemContent;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户商品详情Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-04-20 17:24:09
 * @Version:1.1.0
 */
public interface YdMerchantItemContentDao {

	/**
	 * 通过id得到商户商品详情YdMerchantItemContent
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemContent getYdMerchantItemContentById(Integer id);

	/**
	 * 通过itemId得到商户商品详情YdMerchantItemContent
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public YdMerchantItemContent getYdMerchantItemContentByItemId(Integer itemId);
	
	/**
	 * 获取数量
	 * @param ydMerchantItemContent
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantItemContentCount(YdMerchantItemContent ydMerchantItemContent);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantItemContent
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemContent> findYdMerchantItemContentListByPage(@Param("params") YdMerchantItemContent ydMerchantItemContent,
                                                                           @Param("pageStart") Integer pageStart,
                                                                           @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户商品详情YdMerchantItemContent
	 * @param ydMerchantItemContent
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemContent> getAll(YdMerchantItemContent ydMerchantItemContent);

	/**
	 * 添加商户商品详情YdMerchantItemContent
	 * @param ydMerchantItemContent
	 * @Description:
	 */
	public void insertYdMerchantItemContent(YdMerchantItemContent ydMerchantItemContent);
	
	/**
	 * 通过id修改商户商品详情YdMerchantItemContent
	 * @param ydMerchantItemContent
	 * @Description:
	 */
	public void updateYdMerchantItemContent(YdMerchantItemContent ydMerchantItemContent);

	/**
	 * 通过itemId删除商户商品详情YdMerchantItemContent
	 * @param itemId
	 * @Description:
	 */
	public void deleteYdMerchantItemContent(Integer itemId);
	
}
