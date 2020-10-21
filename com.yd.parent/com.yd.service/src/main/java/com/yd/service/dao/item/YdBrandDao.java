package com.yd.service.dao.item;

import java.util.List;
import com.yd.service.bean.item.YdBrand;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:商品品牌Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-19 14:09:38
 * @Version:1.1.0
 */
public interface YdBrandDao {

	/**
	 * 通过id得到商品品牌YdBrand
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdBrand getYdBrandById(Integer id);

	/**
	 * 通过brandAlias得到商品品牌YdBrand
	 * @param brandAlias
	 * @return
	 * @Description:
	 */
	public YdBrand getYdBrandByBrandAlias(String brandAlias);

	/**
	 * 获取数量
	 * @param ydBrand
	 * @return 
	 * @Description:
	 */
	public int getYdBrandCount(YdBrand ydBrand);
	
	/**
	 * 分页获取数据
	 * @param ydBrand
	 * @return 
	 * @Description:
	 */
	public List<YdBrand> findYdBrandListByPage(@Param("params") YdBrand ydBrand,
                                               @Param("pageStart") Integer pageStart,
                                               @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有商品品牌YdBrand
	 * @param ydBrand
	 * @return 
	 * @Description:
	 */
	public List<YdBrand> getAll(YdBrand ydBrand);

	/**
	 * 添加商品品牌YdBrand
	 * @param ydBrand
	 * @Description:
	 */
	public void insertYdBrand(YdBrand ydBrand);
	
	/**
	 * 通过id修改商品品牌YdBrand
	 * @param ydBrand
	 * @Description:
	 */
	public void updateYdBrand(YdBrand ydBrand);

}
