package com.yd.service.impl.integral;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.integral.YdIntegralRegisterResult;
import com.yd.api.service.integral.YdIntegralRegisterService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.integral.YdIntegralRegisterDao;
import com.yd.service.bean.integral.YdIntegralRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:积分登记Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-23 12:42:46
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdIntegralRegisterServiceImpl implements YdIntegralRegisterService {

	private static final Logger logger = LoggerFactory.getLogger(YdIntegralRegisterServiceImpl.class);

	@Resource
	private YdIntegralRegisterDao ydIntegralRegisterDao;

	@Override
	public YdIntegralRegisterResult getYdIntegralRegisterById(Integer id) {
		if (id == null || id <= 0) return null;
		YdIntegralRegisterResult ydIntegralRegisterResult = new YdIntegralRegisterResult();
		YdIntegralRegister ydIntegralRegister = this.ydIntegralRegisterDao.getYdIntegralRegisterById(id);
		if (ydIntegralRegister != null) {
			BeanUtilExt.copyProperties(ydIntegralRegisterResult, ydIntegralRegister);
		}
		return ydIntegralRegisterResult;
	}

	@Override
	public Page<YdIntegralRegisterResult> findYdIntegralRegisterListByPage(YdIntegralRegisterResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdIntegralRegisterResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdIntegralRegister ydIntegralRegister = new YdIntegralRegister();
		BeanUtilExt.copyProperties(ydIntegralRegister, params);
		
		int amount = this.ydIntegralRegisterDao.getYdIntegralRegisterCount(ydIntegralRegister);
		if (amount > 0) {
			List<YdIntegralRegister> dataList = this.ydIntegralRegisterDao.findYdIntegralRegisterListByPage(ydIntegralRegister,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdIntegralRegisterResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdIntegralRegisterResult> getAll(YdIntegralRegisterResult ydIntegralRegisterResult) {
		YdIntegralRegister ydIntegralRegister = null;
		if (ydIntegralRegisterResult != null) {
			ydIntegralRegister = new YdIntegralRegister();
			BeanUtilExt.copyProperties(ydIntegralRegister, ydIntegralRegisterResult);
		}
		List<YdIntegralRegister> dataList = this.ydIntegralRegisterDao.getAll(ydIntegralRegister);
		return DTOUtils.convertList(dataList, YdIntegralRegisterResult.class);
	}

	@Override
	public void insertYdIntegralRegister(YdIntegralRegisterResult ydIntegralRegisterResult) {
		if (null != ydIntegralRegisterResult) {
			ydIntegralRegisterResult.setCreateTime(new Date());
			ydIntegralRegisterResult.setUpdateTime(new Date());
			YdIntegralRegister ydIntegralRegister = new YdIntegralRegister();
			BeanUtilExt.copyProperties(ydIntegralRegister, ydIntegralRegisterResult);
			this.ydIntegralRegisterDao.insertYdIntegralRegister(ydIntegralRegister);
		}
	}

	@Override
	public void updateYdIntegralRegister(YdIntegralRegisterResult ydIntegralRegisterResult) {
		if (null != ydIntegralRegisterResult) {
			ydIntegralRegisterResult.setUpdateTime(new Date());
			YdIntegralRegister ydIntegralRegister = new YdIntegralRegister();
			BeanUtilExt.copyProperties(ydIntegralRegister, ydIntegralRegisterResult);
			this.ydIntegralRegisterDao.updateYdIntegralRegister(ydIntegralRegister);
		}
	}

}

