package com.yd.service.impl.material;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.yd.api.result.material.YdMaterialPictureResult;
import com.yd.api.service.material.YdMaterialPictureService;
import com.yd.core.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.material.YdMaterialPictureDao;
import com.yd.service.bean.material.YdMaterialPicture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:素材库Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-28 16:03:27
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMaterialPictureServiceImpl implements YdMaterialPictureService {

	private static final Logger logger = LoggerFactory.getLogger(YdMaterialPictureServiceImpl.class);

	@Resource
	private YdMaterialPictureDao ydMaterialPictureDao;

	@Override
	public YdMaterialPictureResult getYdMaterialPictureById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMaterialPictureResult ydMaterialPictureResult = null;
		YdMaterialPicture ydMaterialPicture = this.ydMaterialPictureDao.getYdMaterialPictureById(id);
		if (ydMaterialPicture != null) {
			ydMaterialPictureResult = new YdMaterialPictureResult();
			BeanUtilExt.copyProperties(ydMaterialPictureResult, ydMaterialPicture);
		}
		return ydMaterialPictureResult;
	}

	@Override
	public List<YdMaterialPictureResult> getAll(YdMaterialPictureResult ydMaterialPictureResult) {
		YdMaterialPicture ydMaterialPicture = null;
		if (ydMaterialPictureResult != null) {
			ydMaterialPicture = new YdMaterialPicture();
			BeanUtilExt.copyProperties(ydMaterialPicture, ydMaterialPictureResult);
		}
		List<YdMaterialPicture> dataList = this.ydMaterialPictureDao.getAll(ydMaterialPicture);
		List<YdMaterialPictureResult> resultList = DTOUtils.convertList(dataList, YdMaterialPictureResult.class);
		return resultList;
	}

	@Override
	public Page<YdMaterialPictureResult> findMaterialListByPage(PagerInfo pagerInfo) {
		Page<YdMaterialPictureResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = this.ydMaterialPictureDao.getMaterialCount();
		if (amount > 0) {
			List<YdMaterialPicture> dataList = this.ydMaterialPictureDao.findMaterialListByPage(
					pagerInfo.getStart() * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			List<YdMaterialPictureResult> resultList = DTOUtils.convertList(dataList, YdMaterialPictureResult.class);
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public void insertYdMaterialPicture(YdMaterialPictureResult ydMaterialPictureResult) {
		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydMaterialPictureResult.getPictureUrl()),
				"err_material_empty", "素材不可以为空");

		ydMaterialPictureResult.setCreateTime(new Date());
		ydMaterialPictureResult.setUpdateTime(new Date());
		YdMaterialPicture ydMaterialPicture = new YdMaterialPicture();
		BeanUtilExt.copyProperties(ydMaterialPicture, ydMaterialPictureResult);
		this.ydMaterialPictureDao.insertYdMaterialPicture(ydMaterialPicture);
	}

	@Override
	public void updateYdMaterialPicture(YdMaterialPictureResult ydMaterialPictureResult) {
		if (null != ydMaterialPictureResult) {
			ydMaterialPictureResult.setUpdateTime(new Date());
			YdMaterialPicture ydMaterialPicture = new YdMaterialPicture();
			BeanUtilExt.copyProperties(ydMaterialPicture, ydMaterialPictureResult);
			this.ydMaterialPictureDao.updateYdMaterialPicture(ydMaterialPicture);
		}
	}

	/**
	 * 通过id删除素材库YdMaterialPicture
	 * @param id
	 * @Description:
	 */
	@Override
	public void deleteYdMaterialPicture(Integer id) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0,
				"err_empty_id", "id不可以为空");
		YdMaterialPicture ydMaterialPicture = this.ydMaterialPictureDao.getYdMaterialPictureById(id);
		ValidateBusinessUtils.assertFalse(ydMaterialPicture == null,
				"err_not_exist", "素材不存在");
		this.ydMaterialPictureDao.deleteYdMaterialPicture(id);
	}

}

