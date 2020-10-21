package com.yd.service.impl.rotary;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import com.yd.api.result.rotary.YdRotaryDrawShareRecordResult;
import com.yd.api.service.rotary.YdRotaryDrawShareRecordService;
import com.yd.service.dao.rotary.YdRotaryDrawShareRecordDao;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Title:转盘抽奖分享记录Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-17 15:53:47
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdRotaryDrawShareRecordServiceImpl implements YdRotaryDrawShareRecordService {

	@Resource
	private YdRotaryDrawShareRecordDao ydRotaryDrawShareRecordDao;


	@Override
	public YdRotaryDrawShareRecordResult getYdRotaryDrawShareRecordById(Integer id) {
		return null;
	}

	@Override
	public List<YdRotaryDrawShareRecordResult> getAll(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult) {
		return null;
	}

	@Override
	public void insertYdRotaryDrawShareRecord(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult) {

	}

	@Override
	public void updateYdRotaryDrawShareRecord(YdRotaryDrawShareRecordResult rotaryDrawShareRecordResult) {

	}
}

