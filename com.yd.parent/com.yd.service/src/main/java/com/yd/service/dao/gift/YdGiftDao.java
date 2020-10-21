package com.yd.service.dao.gift;

import java.util.List;
import com.yd.service.bean.gift.YdGift;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:平台礼品Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-30 20:22:34
 * @Version:1.1.0
 */
public interface YdGiftDao {

	/**
	 * 通过id得到平台礼品YdGift
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdGift getYdGiftById(Integer id);

	/**
	 * 通过idList 查询平台礼品
	 * @param giftIdList
	 * @Description:
	 */
	public List<YdGift> getYdGiftByIdList(@Param("list") List<Integer> giftIdList);
	
	/**
	 * 获取数量
	 * @param ydGift
	 * @return 
	 * @Description:
	 */
	public int getYdGiftCount(YdGift ydGift);
	
	/**
	 * 分页获取数据
	 * @param ydGift
	 * @return 
	 * @Description:
	 */
	public List<YdGift> findYdGiftListByPage(@Param("params") YdGift ydGift,
                                             @Param("pageStart") Integer pageStart,
                                             @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有平台礼品YdGift
	 * @param ydGift
	 * @return 
	 * @Description:
	 */
	public List<YdGift> getAll(YdGift ydGift);

	/**
	 * 添加平台礼品YdGift
	 * @param ydGift
	 * @Description:
	 */
	public void insertYdGift(YdGift ydGift);
	
	/**
	 * 通过id修改平台礼品YdGift
	 * @param ydGift
	 * @Description:
	 */
	public void updateYdGift(YdGift ydGift);

	/**
	 * 根据分类id查询礼品数量
	 * @param categoryId
	 * @return
	 */
	Integer getGiftCountByCategoryId(Integer categoryId);
}
