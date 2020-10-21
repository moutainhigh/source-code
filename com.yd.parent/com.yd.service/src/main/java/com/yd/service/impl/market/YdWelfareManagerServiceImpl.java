package com.yd.service.impl.market;

import com.yd.api.result.market.YdWelfareManagerResult;
import com.yd.api.service.market.YdWelfareManagerService;
import com.yd.core.utils.*;
import com.yd.service.bean.decoration.YdVrManager;
import com.yd.service.bean.market.YdWelfareManager;
import com.yd.service.dao.market.YdWelfareManagerDao;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Title:福利管理Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-19 15:20:37
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdWelfareManagerServiceImpl implements YdWelfareManagerService {

	@Resource
	private YdWelfareManagerDao ydWelfareManagerDao;

	@Override
	public YdWelfareManagerResult getYdWelfareManagerById(Integer id) throws BusinessException {
		if (id == null || id <= 0) return null;
		YdWelfareManagerResult ydWelfareManagerResult = null;
		YdWelfareManager ydWelfareManager = this.ydWelfareManagerDao.getYdWelfareManagerById(id);
		if (ydWelfareManager != null) {
			ydWelfareManagerResult = new YdWelfareManagerResult();
			BeanUtilExt.copyProperties(ydWelfareManagerResult, ydWelfareManager);
		}	
		return ydWelfareManagerResult;
	}

	@Override
	public Page<YdWelfareManagerResult> findYdWelfareManagerListByPage(YdWelfareManagerResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdWelfareManagerResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdWelfareManager ydWelfareManager = new YdWelfareManager();
		BeanUtilExt.copyProperties(ydWelfareManager, params);
		
		int amount = this.ydWelfareManagerDao.getYdWelfareManagerCount(ydWelfareManager);
		if (amount > 0) {
			List<YdWelfareManager> dataList = this.ydWelfareManagerDao.findYdWelfareManagerListByPage(
				ydWelfareManager, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdWelfareManagerResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdWelfareManagerResult> getAll(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException {
		YdWelfareManager ydWelfareManager = null;
		if (ydWelfareManagerResult != null) {
			ydWelfareManager = new YdWelfareManager();
			BeanUtilExt.copyProperties(ydWelfareManager, ydWelfareManagerResult);
		}
		List<YdWelfareManager> dataList = this.ydWelfareManagerDao.getAll(ydWelfareManager);
		return DTOUtils.convertList(dataList, YdWelfareManagerResult.class);
	}

	@Override
	public void insertYdWelfareManager(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException {
		if (null != ydWelfareManagerResult) {
			ydWelfareManagerResult.setCreateTime(new Date());
			ydWelfareManagerResult.setUpdateTime(new Date());
			YdWelfareManager ydWelfareManager = new YdWelfareManager();
			BeanUtilExt.copyProperties(ydWelfareManager, ydWelfareManagerResult);
			this.ydWelfareManagerDao.insertYdWelfareManager(ydWelfareManager);
		}
	}
	
	@Override
	public void updateYdWelfareManager(YdWelfareManagerResult ydWelfareManagerResult) throws BusinessException {
		if (null != ydWelfareManagerResult) {
			ydWelfareManagerResult.setUpdateTime(new Date());
			YdWelfareManager ydWelfareManager = new YdWelfareManager();
			BeanUtilExt.copyProperties(ydWelfareManager, ydWelfareManagerResult);
			this.ydWelfareManagerDao.updateYdWelfareManager(ydWelfareManager);
		}
	}

	@Override
	public void deleteYdWelfareManager(Integer id) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0, "err_id_empty", "id不可以为空");

		YdWelfareManager ydWelfareManager = ydWelfareManagerDao.getYdWelfareManagerById(id);
		ValidateBusinessUtils.assertNonNull(ydWelfareManager, "err_id_not_exist", "id不存在");
		this.ydWelfareManagerDao.deleteYdWelfareManagerById(id);
	}

	@Override
	public void updateYdWelfareManagerStatus(Integer id, String isEnable) throws BusinessException {
		ValidateBusinessUtils.assertFalse(id == null || id <= 0, "err_id_empty", "id不可以为空");

		YdWelfareManager ydWelfareManager = ydWelfareManagerDao.getYdWelfareManagerById(id);
		ValidateBusinessUtils.assertNonNull(ydWelfareManager, "err_id_not_exist", "id不存在");
		ydWelfareManager.setIsEnable(isEnable);
		this.ydWelfareManagerDao.updateYdWelfareManager(ydWelfareManager);

	}

	@Override
	public void listSort(List<YdWelfareManagerResult> dataList) throws BusinessException {
		ValidateBusinessUtils.assertCollectionNotEmpty(dataList, "err_list_empty", "数据不可以为空");

		Integer sort = 0;
		for (YdWelfareManagerResult ydWelfareManagerResult : dataList) {
			ValidateBusinessUtils.assertStringNotBlank(ydWelfareManagerResult.getTitle(),
					"err_data_empty", "标题不可以为空");
			ValidateBusinessUtils.assertStringNotBlank(ydWelfareManagerResult.getMerchantIds(),
					"err_data_empty", "商户ids不可以为空");
			ValidateBusinessUtils.assertStringNotBlank(ydWelfareManagerResult.getImageUrl(),
					"err_data_empty", "图片地址不可以为空");
			ValidateBusinessUtils.assertStringNotBlank(ydWelfareManagerResult.getJumpUrl(),
					"err_data_empty", "跳转地址不可以为空");
			ydWelfareManagerResult.setSort(sort++);
		}

		this.ydWelfareManagerDao.deleteYdWelfareManager();

		for (YdWelfareManagerResult ydWelfareManagerResult : dataList) {
			YdWelfareManager ydWelfareManager = new YdWelfareManager();
			BeanUtilExt.copyProperties(ydWelfareManager, ydWelfareManagerResult);
			this.ydWelfareManagerDao.insertYdWelfareManager(ydWelfareManager);
		}
	}

}

