package com.yd.service.impl.member;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.yd.api.result.member.YdMemberLevelConfigResult;
import com.yd.api.service.member.YdMemberLevelConfigService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.member.YdMemberLevelConfigDao;
import com.yd.service.bean.member.YdMemberLevelConfig;

/**
 * @Title:优度会员配置Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:33:56
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMemberLevelConfigServiceImpl implements YdMemberLevelConfigService {

	@Resource
	private YdMemberLevelConfigDao ydMemberLevelConfigDao;

	@Override
	public YdMemberLevelConfigResult getYdMemberLevelConfigById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMemberLevelConfigResult ydMemberLevelConfigResult = null;
		YdMemberLevelConfig ydMemberLevelConfig = this.ydMemberLevelConfigDao.getYdMemberLevelConfigById(id);
		if (ydMemberLevelConfig != null) {
			ydMemberLevelConfigResult = new YdMemberLevelConfigResult();
			BeanUtilExt.copyProperties(ydMemberLevelConfigResult, ydMemberLevelConfig);
		}	
		return ydMemberLevelConfigResult;
	}

	@Override
	public Page<YdMemberLevelConfigResult> findYdMemberLevelConfigListByPage(YdMemberLevelConfigResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMemberLevelConfigResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMemberLevelConfig ydMemberLevelConfig = new YdMemberLevelConfig();
		BeanUtilExt.copyProperties(ydMemberLevelConfig, params);
		
		int amount = this.ydMemberLevelConfigDao.getYdMemberLevelConfigCount(ydMemberLevelConfig);
		if (amount > 0) {
			List<YdMemberLevelConfig> dataList = this.ydMemberLevelConfigDao.findYdMemberLevelConfigListByPage(
				ydMemberLevelConfig, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMemberLevelConfigResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMemberLevelConfigResult> getAll(YdMemberLevelConfigResult ydMemberLevelConfigResult) {
		YdMemberLevelConfig ydMemberLevelConfig = null;
		if (ydMemberLevelConfigResult != null) {
			ydMemberLevelConfig = new YdMemberLevelConfig();
			BeanUtilExt.copyProperties(ydMemberLevelConfig, ydMemberLevelConfigResult);
		}
		List<YdMemberLevelConfig> dataList = this.ydMemberLevelConfigDao.getAll(ydMemberLevelConfig);
		return DTOUtils.convertList(dataList, YdMemberLevelConfigResult.class);
	}

	@Override
	public void insertYdMemberLevelConfig(YdMemberLevelConfigResult ydMemberLevelConfigResult) {
		if (null != ydMemberLevelConfigResult) {
			ydMemberLevelConfigResult.setCreateTime(new Date());
			ydMemberLevelConfigResult.setUpdateTime(new Date());
			YdMemberLevelConfig ydMemberLevelConfig = new YdMemberLevelConfig();
			BeanUtilExt.copyProperties(ydMemberLevelConfig, ydMemberLevelConfigResult);
			this.ydMemberLevelConfigDao.insertYdMemberLevelConfig(ydMemberLevelConfig);
		}
	}
	
	@Override
	public void updateYdMemberLevelConfig(YdMemberLevelConfigResult ydMemberLevelConfigResult) {
		if (null != ydMemberLevelConfigResult) {
			ydMemberLevelConfigResult.setUpdateTime(new Date());
			YdMemberLevelConfig ydMemberLevelConfig = new YdMemberLevelConfig();
			BeanUtilExt.copyProperties(ydMemberLevelConfig, ydMemberLevelConfigResult);
			this.ydMemberLevelConfigDao.updateYdMemberLevelConfig(ydMemberLevelConfig);
		}
	}

}

