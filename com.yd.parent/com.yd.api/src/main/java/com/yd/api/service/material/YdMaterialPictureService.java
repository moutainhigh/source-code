package com.yd.api.service.material;

import java.util.List;

import com.yd.api.result.material.YdMaterialPictureResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:素材库Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-28 16:03:27
 * @Version:1.1.0
 */
public interface YdMaterialPictureService {

	/**
	 * 通过id得到素材库YdMaterialPicture
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMaterialPictureResult getYdMaterialPictureById(Integer id);

	/**
	 * 得到所有素材库YdMaterialPicture
	 * @param ydMaterialPictureResult
	 * @return 
	 * @Description:
	 */
	public List<YdMaterialPictureResult> getAll(YdMaterialPictureResult ydMaterialPictureResult);

	/**
	 * 分页查询素材库列表
	 * @param pagerInfo
	 * @return
	 */
	public Page<YdMaterialPictureResult> findMaterialListByPage(PagerInfo pagerInfo);

	/**
	 * 添加素材库YdMaterialPicture
	 * @param ydMaterialPictureResult
	 * @Description:
	 */
	public void insertYdMaterialPicture(YdMaterialPictureResult ydMaterialPictureResult);
	
	/**
	 * 通过id修改素材库YdMaterialPicture
	 * @param ydMaterialPictureResult
	 * @Description:
	 */
	public void updateYdMaterialPicture(YdMaterialPictureResult ydMaterialPictureResult);

	/**
	 * 通过id删除素材库YdMaterialPicture
	 * @param id
	 * @Description:
	 */
	void deleteYdMaterialPicture(Integer id) throws BusinessException;

}
