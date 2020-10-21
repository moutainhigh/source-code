package com.yd.service.impl.item;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.item.YdBrandResult;
import com.yd.api.service.item.YdBrandService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.item.YdBrandDao;
import com.yd.service.bean.item.YdBrand;

/**
 * @Title:商品品牌Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-19 14:09:38
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdBrandServiceImpl implements YdBrandService {

	@Resource
	private YdBrandDao ydBrandDao;

	@Override
	public YdBrandResult getYdBrandById(Integer id) {
		if (id == null || id <= 0) return null;
		YdBrandResult ydBrandResult = null;
		YdBrand ydBrand = this.ydBrandDao.getYdBrandById(id);
		if (ydBrand != null) {
			ydBrandResult = new YdBrandResult();
			BeanUtilExt.copyProperties(ydBrandResult, ydBrand);
		}
		return ydBrandResult;
	}

	@Override
	public Page<YdBrandResult> findYdBrandListByPage(YdBrandResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdBrandResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdBrand ydBrand = new YdBrand();
		BeanUtilExt.copyProperties(ydBrand, params);
		
		int amount = this.ydBrandDao.getYdBrandCount(ydBrand);
		if (amount > 0) {
			List<YdBrand> dataList = this.ydBrandDao.findYdBrandListByPage(ydBrand, 
				(pagerInfo.getPageIndex() - 1) * pagerInfo.getPageSize(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdBrandResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdBrandResult> getAll(YdBrandResult ydBrandResult) {
		YdBrand ydBrand = new YdBrand();
		if (ydBrandResult != null) {
			BeanUtilExt.copyProperties(ydBrand, ydBrandResult);
		}
		List<YdBrand> dataList = this.ydBrandDao.getAll(ydBrand);
		return DTOUtils.convertList(dataList, YdBrandResult.class);
	}

	@Override
	public void insertYdBrand(YdBrandResult ydBrandResult) {
		if (null != ydBrandResult) {
			ydBrandResult.setCreateTime(new Date());
			ydBrandResult.setUpdateTime(new Date());
			YdBrand ydBrand = new YdBrand();
			BeanUtilExt.copyProperties(ydBrand, ydBrandResult);
			this.ydBrandDao.insertYdBrand(ydBrand);
		}
	}
	
	@Override
	public void updateYdBrand(YdBrandResult ydBrandResult) {
		if (null != ydBrandResult) {
			ydBrandResult.setUpdateTime(new Date());
			YdBrand ydBrand = new YdBrand();
			BeanUtilExt.copyProperties(ydBrand, ydBrandResult);
			this.ydBrandDao.updateYdBrand(ydBrand);
		}
	}

}

