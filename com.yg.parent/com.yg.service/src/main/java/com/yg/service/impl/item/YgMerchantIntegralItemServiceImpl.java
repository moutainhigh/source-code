package com.yg.service.impl.item;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yg.api.result.item.YgMerchantIntegralItemResult;
import com.yg.api.service.item.YgMerchantIntegralItemService;
import com.yg.core.utils.BeanUtilExt;
import com.yg.core.utils.*;
import org.apache.dubbo.config.annotation.Service;
import com.yg.service.dao.item.YgMerchantIntegralItemDao;
import com.yg.service.bean.item.YgMerchantIntegralItem;

/**
 * @Title:商户积分商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-27 13:34:01
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YgMerchantIntegralItemServiceImpl implements YgMerchantIntegralItemService {

	@Resource
	private YgMerchantIntegralItemDao ygMerchantIntegralItemDao;

	@Override
	public YgMerchantIntegralItemResult getYgMerchantIntegralItemById(Integer id) {
		if (id == null || id <= 0) return null;
		YgMerchantIntegralItemResult ygMerchantIntegralItemResult = null;
		YgMerchantIntegralItem ygMerchantIntegralItem = this.ygMerchantIntegralItemDao.getYgMerchantIntegralItemById(id);
		if (ygMerchantIntegralItem != null) {
			ygMerchantIntegralItemResult = new YgMerchantIntegralItemResult();
			BeanUtilExt.copyProperties(ygMerchantIntegralItemResult, ygMerchantIntegralItem);
		}	
		return ygMerchantIntegralItemResult;
	}

	@Override
	public Page<YgMerchantIntegralItemResult> findYgMerchantIntegralItemListByPage(YgMerchantIntegralItemResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YgMerchantIntegralItemResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YgMerchantIntegralItem ygMerchantIntegralItem = new YgMerchantIntegralItem();
		BeanUtilExt.copyProperties(ygMerchantIntegralItem, params);
		
		int amount = this.ygMerchantIntegralItemDao.getYgMerchantIntegralItemCount(ygMerchantIntegralItem);
		if (amount > 0) {
			List<YgMerchantIntegralItem> dataList = this.ygMerchantIntegralItemDao.findYgMerchantIntegralItemListByPage(
				ygMerchantIntegralItem, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YgMerchantIntegralItemResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YgMerchantIntegralItemResult> getAll(YgMerchantIntegralItemResult ygMerchantIntegralItemResult) {
		YgMerchantIntegralItem ygMerchantIntegralItem = null;
		if (ygMerchantIntegralItemResult != null) {
			ygMerchantIntegralItem = new YgMerchantIntegralItem();
			BeanUtilExt.copyProperties(ygMerchantIntegralItem, ygMerchantIntegralItemResult);
		}
		List<YgMerchantIntegralItem> dataList = this.ygMerchantIntegralItemDao.getAll(ygMerchantIntegralItem);
		return DTOUtils.convertList(dataList, YgMerchantIntegralItemResult.class);
	}

	@Override
	public void insertYgMerchantIntegralItem(YgMerchantIntegralItemResult ygMerchantIntegralItemResult) {
		if (null != ygMerchantIntegralItemResult) {
			ygMerchantIntegralItemResult.setCreateTime(new Date());
			ygMerchantIntegralItemResult.setUpdateTime(new Date());
			YgMerchantIntegralItem ygMerchantIntegralItem = new YgMerchantIntegralItem();
			BeanUtilExt.copyProperties(ygMerchantIntegralItem, ygMerchantIntegralItemResult);
			this.ygMerchantIntegralItemDao.insertYgMerchantIntegralItem(ygMerchantIntegralItem);
		}
	}

	@Override
	public void updateYgMerchantIntegralItem(YgMerchantIntegralItemResult ygMerchantIntegralItemResult) {
		if (null != ygMerchantIntegralItemResult) {
			ygMerchantIntegralItemResult.setUpdateTime(new Date());
			YgMerchantIntegralItem ygMerchantIntegralItem = new YgMerchantIntegralItem();
			BeanUtilExt.copyProperties(ygMerchantIntegralItem, ygMerchantIntegralItemResult);
			this.ygMerchantIntegralItemDao.updateYgMerchantIntegralItem(ygMerchantIntegralItem);
		}
	}

	/**
	 * 上下架商品
	 * @param itemId
	 * @param isEnable
	 * @throws BusinessException
	 */
	@Override
	public void updateMerchantItemStatus(Integer itemId, String isEnable) throws BusinessException {
		ValidateBusinessUtils.assertFalse(itemId == null || itemId <= 0,
				"err_item_id", "非法的商品id");
		YgMerchantIntegralItem merchantIntegralItem = this.ygMerchantIntegralItemDao.getYgMerchantIntegralItemById(itemId);
	}

	/**
	 * 删除商品
	 * @param itemId
	 * @param isEnable
	 * @throws BusinessException
	 */
	@Override
	public void deleteMerchantItemStatus(Integer itemId, String isEnable) throws BusinessException {
		ValidateBusinessUtils.assertFalse(itemId == null || itemId <= 0,
				"err_item_id", "非法的商品id");
		YgMerchantIntegralItem merchantIntegralItem = this.ygMerchantIntegralItemDao.getYgMerchantIntegralItemById(itemId);

	}

}

