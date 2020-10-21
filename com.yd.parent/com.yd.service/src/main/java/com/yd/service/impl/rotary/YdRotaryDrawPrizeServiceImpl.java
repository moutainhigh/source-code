package com.yd.service.impl.rotary;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.rotary.YdRotaryDrawPrizeResult;
import com.yd.api.service.rotary.YdRotaryDrawPrizeService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import com.yd.service.bean.rotary.YdRotaryDrawPrize;
import com.yd.service.dao.rotary.YdRotaryDrawPrizeDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Title:优度转盘抽奖奖品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:36:56
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdRotaryDrawPrizeServiceImpl implements YdRotaryDrawPrizeService {

	@Resource
	private YdRotaryDrawPrizeDao ydRotaryDrawPrizeDao;

	@Override
	public YdRotaryDrawPrizeResult getYdRotaryDrawPrizeById(Integer id) {
		return null;
	}

	@Override
	public List<YdRotaryDrawPrizeResult> getAll(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) {
		YdRotaryDrawPrize rotaryDrawPrize = new YdRotaryDrawPrize();
		BeanUtilExt.copyProperties(rotaryDrawPrize, rotaryDrawPrizeResult);
		List<YdRotaryDrawPrize> list = ydRotaryDrawPrizeDao.getAll(rotaryDrawPrize);
		return DTOUtils.convertList(list, YdRotaryDrawPrizeResult.class);
	}

	@Override
	public void insertYdRotaryDrawPrize(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) {

	}

	@Override
	public void updateYdRotaryDrawPrize(YdRotaryDrawPrizeResult rotaryDrawPrizeResult) {

	}
}

