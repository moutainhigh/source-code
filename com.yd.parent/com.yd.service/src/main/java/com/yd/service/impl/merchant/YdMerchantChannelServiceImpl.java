package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.merchant.YdMerchantChannelResult;
import com.yd.api.service.merchant.YdMerchantChannelService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.merchant.YdMerchantChannelDao;
import com.yd.service.bean.merchant.YdMerchantChannel;

/**
 * @Title:商户渠道Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-30 13:02:38
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantChannelServiceImpl implements YdMerchantChannelService {

	@Resource
	private YdMerchantChannelDao ydMerchantChannelDao;

	@Override
	public YdMerchantChannelResult getYdMerchantChannelById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantChannelResult ydMerchantChannelResult = null;
		YdMerchantChannel ydMerchantChannel = this.ydMerchantChannelDao.getYdMerchantChannelById(id);
		if (ydMerchantChannel != null) {
			ydMerchantChannelResult = new YdMerchantChannelResult();
			BeanUtilExt.copyProperties(ydMerchantChannelResult, ydMerchantChannel);
		}	
		return ydMerchantChannelResult;
	}

	@Override
	public YdMerchantChannelResult getYdMerchantChannelByMerchantId(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantChannelResult ydMerchantChannelResult = null;
		YdMerchantChannel ydMerchantChannel = this.ydMerchantChannelDao.getYdMerchantChannelByMerchantId(id);
		if (ydMerchantChannel != null) {
			ydMerchantChannelResult = new YdMerchantChannelResult();
			BeanUtilExt.copyProperties(ydMerchantChannelResult, ydMerchantChannel);
		}
		return ydMerchantChannelResult;
	}

	@Override
	public Page<YdMerchantChannelResult> findYdMerchantChannelListByPage(YdMerchantChannelResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantChannelResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantChannel ydMerchantChannel = new YdMerchantChannel();
		BeanUtilExt.copyProperties(ydMerchantChannel, params);
		
		int amount = this.ydMerchantChannelDao.getYdMerchantChannelCount(ydMerchantChannel);
		if (amount > 0) {
			List<YdMerchantChannel> dataList = this.ydMerchantChannelDao.findYdMerchantChannelListByPage(
				ydMerchantChannel, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantChannelResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantChannelResult> getAll(YdMerchantChannelResult ydMerchantChannelResult) {
		YdMerchantChannel ydMerchantChannel = null;
		if (ydMerchantChannelResult != null) {
			ydMerchantChannel = new YdMerchantChannel();
			BeanUtilExt.copyProperties(ydMerchantChannel, ydMerchantChannelResult);
		}
		List<YdMerchantChannel> dataList = this.ydMerchantChannelDao.getAll(ydMerchantChannel);
		return DTOUtils.convertList(dataList, YdMerchantChannelResult.class);
	}

	@Override
	public Integer insertYdMerchantChannel(YdMerchantChannelResult ydMerchantChannelResult) {
		ydMerchantChannelResult.setCreateTime(new Date());
		ydMerchantChannelResult.setUpdateTime(new Date());
		YdMerchantChannel ydMerchantChannel = new YdMerchantChannel();
		BeanUtilExt.copyProperties(ydMerchantChannel, ydMerchantChannelResult);
		this.ydMerchantChannelDao.insertYdMerchantChannel(ydMerchantChannel);
		return ydMerchantChannel.getId();
	}
	
	@Override
	public Integer updateYdMerchantChannel(YdMerchantChannelResult ydMerchantChannelResult) {
		ydMerchantChannelResult.setUpdateTime(new Date());
		YdMerchantChannel ydMerchantChannel = new YdMerchantChannel();
		BeanUtilExt.copyProperties(ydMerchantChannel, ydMerchantChannelResult);
		this.ydMerchantChannelDao.updateYdMerchantChannel(ydMerchantChannel);
		return ydMerchantChannel.getId();
	}

}

