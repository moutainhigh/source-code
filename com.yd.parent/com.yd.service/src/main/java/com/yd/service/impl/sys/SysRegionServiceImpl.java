package com.yd.service.impl.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.sys.SysRegionResult;
import com.yd.api.service.sys.SysRegionService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.sys.SysRegion;
import com.yd.service.dao.sys.SysRegionDao;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:地区表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-03 16:21:47
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class SysRegionServiceImpl implements SysRegionService {

	private static final Logger logger = LoggerFactory.getLogger(SysRegionServiceImpl.class);

	@Resource
	private SysRegionDao sysRegionDao;

	@Override
	public SysRegionResult getSysRegionById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		SysRegionResult sysRegionResult = null;
		SysRegion sysRegion = this.sysRegionDao.getSysRegionById(id);
		if (sysRegion != null) {
			sysRegionResult = new SysRegionResult();
			BeanUtilExt.copyProperties(sysRegionResult, sysRegion);
		}
		return sysRegionResult;
	}

	@Override
	public List<SysRegionResult> getAll(SysRegionResult sysRegionResult) {
		SysRegion sysRegion = null;
		if (sysRegionResult != null) {
			sysRegion = new SysRegion();
			BeanUtilExt.copyProperties(sysRegion, sysRegionResult);
		}
		List<SysRegion> dataList = this.sysRegionDao.getAll(sysRegion);
		return DTOUtils.convertList(dataList, SysRegionResult.class);
	}

	@Override
	public List<SysRegionResult> getChildList() {
		SysRegion sysRegion = new SysRegion();
		List<SysRegion> dataList = this.sysRegionDao.getAll(sysRegion);

		List<SysRegionResult> regionList = DTOUtils.convertList(dataList, SysRegionResult.class);

		Map<Integer, List<SysRegionResult>> groupMap = regionList.stream().collect(Collectors.groupingBy(SysRegionResult::getType));


		List<SysRegionResult> provinceList = groupMap.get(1);

		List<SysRegionResult> cityList = groupMap.get(2);

		List<SysRegionResult> distinctList = groupMap.get(3);

		setRegionChild(cityList, distinctList);

		setRegionChild(provinceList, cityList);
		logger.info("省市区json数据为 = " + JSON.toJSONString(provinceList));
		return provinceList;
	}

	/**
	 * 设置childList
	 * @param pList 高一级的type
	 * @param cList 第低一级的type
	 */
	private void setRegionChild(List<SysRegionResult> pList, List<SysRegionResult> cList) {
		pList.forEach(pData -> {
			List<SysRegionResult> childList = new ArrayList<>();
			cList.forEach(cData -> {
				if (pData.getId().equals(cData.getPid())) {
					childList.add(cData);
				}
			});
			pData.setChildList(childList);
		});
	}
}

