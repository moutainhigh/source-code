package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdMerchantItemImage;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户商品详情Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-04-20 17:24:48
 * @Version:1.1.0
 */
public interface YdMerchantItemImageDao {

	/**
	 * 通过id得到商户商品详情YdMerchantItemImage
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantItemImage getYdMerchantItemImageById(Integer id);

	/**
	 * 通过itemId得到商户商品详情YdMerchantItemImage
	 * @param itemId
	 * @return
	 * @Description:
	 */
	public List<YdMerchantItemImage> getYdMerchantItemImageByItemId(Integer itemId);
	
	/**
	 * 获取数量
	 * @param ydMerchantItemImage
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantItemImageCount(YdMerchantItemImage ydMerchantItemImage);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantItemImage
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemImage> findYdMerchantItemImageListByPage(@Param("params") YdMerchantItemImage ydMerchantItemImage,
                                                                       @Param("pageStart") Integer pageStart,
                                                                       @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户商品详情YdMerchantItemImage
	 * @param ydMerchantItemImage
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantItemImage> getAll(YdMerchantItemImage ydMerchantItemImage);

	/**
	 * 添加商户商品详情YdMerchantItemImage
	 * @param ydMerchantItemImage
	 * @Description:
	 */
	public void insertYdMerchantItemImage(YdMerchantItemImage ydMerchantItemImage);

	/**
	 * 通过id修改商户商品详情YdMerchantItemImage
	 * @param ydMerchantItemImage
	 * @Description:
	 */
	public void updateYdMerchantItemImage(YdMerchantItemImage ydMerchantItemImage);

	/**
	 * 通过itemId修改商户商品详情YdMerchantItemImage
	 * @param itemId
	 * @Description:
	 */
	public void deleteYdMerchantItemImage(Integer itemId);

}
