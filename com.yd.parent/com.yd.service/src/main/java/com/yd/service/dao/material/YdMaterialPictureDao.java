package com.yd.service.dao.material;

import java.util.List;
import com.yd.service.bean.material.YdMaterialPicture;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:素材库Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-28 16:03:27
 * @Version:1.1.0
 */
public interface YdMaterialPictureDao {

	/**
	 * 通过id得到素材库YdMaterialPicture
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMaterialPicture getYdMaterialPictureById(Integer id);

	/**
	 * 得到所有素材库YdMaterialPicture
	 * @param ydMaterialPicture
	 * @return 
	 * @Description:
	 */
	public List<YdMaterialPicture> getAll(YdMaterialPicture ydMaterialPicture);

	/**
	 * 查询素材数量
	 * @return
	 */
	int getMaterialCount();

	/**
	 * 分页查询素材
	 * @param pageStart
	 * @param pageSize
	 * @return
	 */
	List<YdMaterialPicture> findMaterialListByPage(@Param("pageStart") Integer pageStart,
												   @Param("pageSize") Integer pageSize);


	/**
	 * 添加素材库YdMaterialPicture
	 * @param ydMaterialPicture
	 * @Description:
	 */
	public void insertYdMaterialPicture(YdMaterialPicture ydMaterialPicture);

	/**
	 * 通过id修改素材库YdMaterialPicture
	 * @param ydMaterialPicture
	 * @Description:
	 */
	public void updateYdMaterialPicture(YdMaterialPicture ydMaterialPicture);

	/**
	 * 通过id修改素材库YdMaterialPicture
	 * @param id
	 * @Description:
	 */
    void deleteYdMaterialPicture(Integer id);
}
