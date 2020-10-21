package com.yd.service.impl.rotary;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.rotary.YdRotaryDrawRecordResult;
import com.yd.api.service.rotary.YdRotaryDrawRecordService;
import com.yd.core.utils.DTOUtils;
import com.yd.service.bean.rotary.YdRotaryDrawRecord;
import com.yd.service.dao.rotary.YdRotaryDrawRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Title:转盘抽奖记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:47:52
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdRotaryDrawRecordServiceImpl implements YdRotaryDrawRecordService {

	@Resource
	private YdRotaryDrawRecordDao ydRotaryDrawRecordDao;

	@Override
	public List<YdRotaryDrawRecordResult> findUserDrawRecordList(Integer userId, Integer activityId, String startTime, String endTime) {
		List<YdRotaryDrawRecord> recordList = ydRotaryDrawRecordDao.findUserDrawRecordList(userId, activityId, startTime, endTime);
		return DTOUtils.convertList(recordList, YdRotaryDrawRecordResult.class);
	}

	@Override
	public YdRotaryDrawRecordResult getYdRotaryDrawRecordById(Integer id) throws Exception {
		return null;
	}

	@Override
	public List<YdRotaryDrawRecordResult> getAll(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception {
		return null;
	}

	@Override
	public void insertYdRotaryDrawRecord(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception {

	}

	@Override
	public void updateYdRotaryDrawRecord(YdRotaryDrawRecordResult rotaryDrawRecordResult) throws Exception {

	}
}

