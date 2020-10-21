package com.yd.service.impl.visitor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.visitor.YdVisitorLogResult;
import com.yd.api.service.visitor.YdVisitorLogService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.visitor.YdVisitorLogDao;
import com.yd.service.bean.visitor.YdVisitorLog;

/**
 * @Title:接口访问记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-09 19:17:07
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdVisitorLogServiceImpl implements YdVisitorLogService {

	@Resource
	private YdVisitorLogDao ydVisitorLogDao;

	@Override
	public YdVisitorLogResult getYdVisitorLogById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdVisitorLogResult ydVisitorLogResult = null;
		YdVisitorLog ydVisitorLog = this.ydVisitorLogDao.getYdVisitorLogById(id);
		if (ydVisitorLog != null) {
			ydVisitorLogResult = new YdVisitorLogResult();
			BeanUtilExt.copyProperties(ydVisitorLogResult, ydVisitorLog);
		}	
		return ydVisitorLogResult;
	}

	@Override
	public Page<YdVisitorLogResult> findYdVisitorLogListByPage(YdVisitorLogResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdVisitorLogResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdVisitorLog ydVisitorLog = new YdVisitorLog();
		BeanUtilExt.copyProperties(ydVisitorLog, params);
		
		int amount = this.ydVisitorLogDao.getYdVisitorLogCount(ydVisitorLog);
		if (amount > 0) {
			List<YdVisitorLog> dataList = this.ydVisitorLogDao.findYdVisitorLogListByPage(
				ydVisitorLog, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdVisitorLogResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdVisitorLogResult> getAll(YdVisitorLogResult ydVisitorLogResult) {
		YdVisitorLog ydVisitorLog = null;
		if (ydVisitorLogResult != null) {
			ydVisitorLog = new YdVisitorLog();
			BeanUtilExt.copyProperties(ydVisitorLog, ydVisitorLogResult);
		}
		List<YdVisitorLog> dataList = this.ydVisitorLogDao.getAll(ydVisitorLog);
		return DTOUtils.convertList(dataList, YdVisitorLogResult.class);
	}

	@Override
	public void insertYdVisitorLog(YdVisitorLogResult ydVisitorLogResult) {
		if (null != ydVisitorLogResult) {
			ydVisitorLogResult.setCreateTime(new Date());
			ydVisitorLogResult.setUpdateTime(new Date());
			YdVisitorLog ydVisitorLog = new YdVisitorLog();
			BeanUtilExt.copyProperties(ydVisitorLog, ydVisitorLogResult);
			this.ydVisitorLogDao.insertYdVisitorLog(ydVisitorLog);
		}
	}
	
	@Override
	public void updateYdVisitorLog(YdVisitorLogResult ydVisitorLogResult) {
		if (null != ydVisitorLogResult) {
			ydVisitorLogResult.setUpdateTime(new Date());
			YdVisitorLog ydVisitorLog = new YdVisitorLog();
			BeanUtilExt.copyProperties(ydVisitorLog, ydVisitorLogResult);
			this.ydVisitorLogDao.updateYdVisitorLog(ydVisitorLog);
		}
	}

}

