package com.yd.service.impl.item;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.item.YdItemBrowseHistoryResult;
import com.yd.api.service.item.YdItemBrowseHistoryService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.item.YdItemBrowseHistoryDao;
import com.yd.service.bean.item.YdItemBrowseHistory;

/**
 * @Title:商品浏览记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:29:34
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdItemBrowseHistoryServiceImpl implements YdItemBrowseHistoryService {

	@Resource
	private YdItemBrowseHistoryDao ydItemBrowseHistoryDao;

	@Override
	public YdItemBrowseHistoryResult getYdItemBrowseHistoryById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdItemBrowseHistoryResult ydItemBrowseHistoryResult = null;
		YdItemBrowseHistory ydItemBrowseHistory = this.ydItemBrowseHistoryDao.getYdItemBrowseHistoryById(id);
		if (ydItemBrowseHistory != null) {
			ydItemBrowseHistoryResult = new YdItemBrowseHistoryResult();
			BeanUtilExt.copyProperties(ydItemBrowseHistoryResult, ydItemBrowseHistory);
		}
		
		return ydItemBrowseHistoryResult;
	}

	@Override
	public List<YdItemBrowseHistoryResult> getAll(YdItemBrowseHistoryResult ydItemBrowseHistoryResult) {
		YdItemBrowseHistory ydItemBrowseHistory = null;
		if (ydItemBrowseHistoryResult != null) {
			ydItemBrowseHistory = new YdItemBrowseHistory();
			BeanUtilExt.copyProperties(ydItemBrowseHistory, ydItemBrowseHistoryResult);
		}
		List<YdItemBrowseHistory> dataList = this.ydItemBrowseHistoryDao.getAll(ydItemBrowseHistory);
		List<YdItemBrowseHistoryResult> resultList = DTOUtils.convertList(dataList, YdItemBrowseHistoryResult.class);
		return resultList;
	}


	@Override
	public void insertYdItemBrowseHistory(YdItemBrowseHistoryResult ydItemBrowseHistoryResult) {
		if (null != ydItemBrowseHistoryResult) {
			ydItemBrowseHistoryResult.setCreateTime(new Date());
			ydItemBrowseHistoryResult.setUpdateTime(new Date());
			YdItemBrowseHistory ydItemBrowseHistory = new YdItemBrowseHistory();
			BeanUtilExt.copyProperties(ydItemBrowseHistory, ydItemBrowseHistoryResult);
			this.ydItemBrowseHistoryDao.insertYdItemBrowseHistory(ydItemBrowseHistory);
		}
	}
	
	
	@Override
	public void updateYdItemBrowseHistory(YdItemBrowseHistoryResult ydItemBrowseHistoryResult) {
		if (null != ydItemBrowseHistoryResult) {
			ydItemBrowseHistoryResult.setUpdateTime(new Date());
			YdItemBrowseHistory ydItemBrowseHistory = new YdItemBrowseHistory();
			BeanUtilExt.copyProperties(ydItemBrowseHistory, ydItemBrowseHistoryResult);
			this.ydItemBrowseHistoryDao.updateYdItemBrowseHistory(ydItemBrowseHistory);
		}
	}

}

