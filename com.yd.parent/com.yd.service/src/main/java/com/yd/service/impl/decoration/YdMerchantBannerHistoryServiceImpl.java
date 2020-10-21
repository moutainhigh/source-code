package com.yd.service.impl.decoration;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.decoration.YdMerchantBannerHistoryResult;
import com.yd.api.service.decoration.YdMerchantBannerHistoryService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import com.yd.service.dao.decoration.YdMerchantBannerHistoryDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.bean.decoration.YdMerchantBannerHistory;

/**
 * @Title:商户历史banner图Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:34:30
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantBannerHistoryServiceImpl implements YdMerchantBannerHistoryService {

	@Resource
	private YdMerchantBannerHistoryDao ydShopMerchantBannerHistoryDao;

	@Override
	public YdMerchantBannerHistoryResult getYdShopMerchantBannerHistoryById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult = null;
		YdMerchantBannerHistory ydShopMerchantBannerHistory = this.ydShopMerchantBannerHistoryDao.getYdShopMerchantBannerHistoryById(id);
		if (ydShopMerchantBannerHistory != null) {
			ydShopMerchantBannerHistoryResult = new YdMerchantBannerHistoryResult();
			BeanUtilExt.copyProperties(ydShopMerchantBannerHistoryResult, ydShopMerchantBannerHistory);
		}
		
		return ydShopMerchantBannerHistoryResult;
	}

	@Override
	public List<YdMerchantBannerHistoryResult> getAll(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult) {
		YdMerchantBannerHistory ydShopMerchantBannerHistory = null;
		if (ydShopMerchantBannerHistoryResult != null) {
			ydShopMerchantBannerHistory = new YdMerchantBannerHistory();
			BeanUtilExt.copyProperties(ydShopMerchantBannerHistory, ydShopMerchantBannerHistoryResult);
		}
		List<YdMerchantBannerHistory> dataList = this.ydShopMerchantBannerHistoryDao.getAll(ydShopMerchantBannerHistory);
		List<YdMerchantBannerHistoryResult> resultList = DTOUtils.convertList(dataList, YdMerchantBannerHistoryResult.class);
		return resultList;
	}


	@Override
	public void insertYdShopMerchantBannerHistory(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult) {
		if (null != ydShopMerchantBannerHistoryResult) {
			ydShopMerchantBannerHistoryResult.setCreateTime(new Date());
			ydShopMerchantBannerHistoryResult.setUpdateTime(new Date());
			YdMerchantBannerHistory ydShopMerchantBannerHistory = new YdMerchantBannerHistory();
			BeanUtilExt.copyProperties(ydShopMerchantBannerHistory, ydShopMerchantBannerHistoryResult);
			this.ydShopMerchantBannerHistoryDao.insertYdShopMerchantBannerHistory(ydShopMerchantBannerHistory);
		}
	}
	
	
	@Override
	public void updateYdShopMerchantBannerHistory(YdMerchantBannerHistoryResult ydShopMerchantBannerHistoryResult) {
		if (null != ydShopMerchantBannerHistoryResult) {
			ydShopMerchantBannerHistoryResult.setUpdateTime(new Date());
			YdMerchantBannerHistory ydShopMerchantBannerHistory = new YdMerchantBannerHistory();
			BeanUtilExt.copyProperties(ydShopMerchantBannerHistory, ydShopMerchantBannerHistoryResult);
			this.ydShopMerchantBannerHistoryDao.updateYdShopMerchantBannerHistory(ydShopMerchantBannerHistory);
		}
	}

}

