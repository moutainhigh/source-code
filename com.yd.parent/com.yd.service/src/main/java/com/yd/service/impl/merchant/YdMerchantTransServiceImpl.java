package com.yd.service.impl.merchant;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.merchant.YdMerchantTransResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.merchant.YdMerchantTransService;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.merchant.YdMerchantTransDao;
import com.yd.service.bean.merchant.YdMerchantTrans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户账单流水Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-22 20:33:42
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantTransServiceImpl implements YdMerchantTransService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantTransServiceImpl.class);

	@Resource
	private YdMerchantTransDao ydMerchantTransDao;

	@Resource
	private YdMerchantService ydMerchantService;


	@Override
	public YdMerchantTransResult getYdMerchantTransById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantTransResult ydMerchantTransResult = null;
		YdMerchantTrans ydMerchantGiftTrans = this.ydMerchantTransDao.getYdMerchantTransById(id);
		if (ydMerchantGiftTrans != null) {
			ydMerchantTransResult = new YdMerchantTransResult();
			BeanUtilExt.copyProperties(ydMerchantTransResult, ydMerchantGiftTrans);

			YdMerchantTransSourceEnum transSourceEnum = YdMerchantTransSourceEnum.getByCode(ydMerchantTransResult.getTransSource());
			if (transSourceEnum != null) {
				ydMerchantTransResult.setIcon(transSourceEnum.getIcon());
			}
		}
		return ydMerchantTransResult;
	}

	@Override
	public List<YdMerchantTransResult> getAll(YdMerchantTransResult ydMerchantTransResult) {
		YdMerchantTrans ydMerchantTrans = null;
		if (ydMerchantTransResult != null) {
			ydMerchantTrans = new YdMerchantTrans();
			BeanUtilExt.copyProperties(ydMerchantTrans, ydMerchantTransResult);
		}
		List<YdMerchantTrans> dataList = this.ydMerchantTransDao.getAll(ydMerchantTrans);
		List<YdMerchantTransResult> resultList = DTOUtils.convertList(dataList, YdMerchantTransResult.class);
		return resultList;
	}

	/**
	 * 分页查询商户交易列表
	 * @param merchantId
	 * @param orderId
	 * @param transStatus
	 * @param startTime
	 * @param endTime
	 * @param pagerInfo
	 * @return
	 */
	@Override
	public Page<YdMerchantTransResult> getMerchantTransListByPage(Integer merchantId, String orderId, String transStatus,
																  String startTime, String endTime, PagerInfo pagerInfo) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		Page<YdMerchantTransResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		int amount = ydMerchantTransDao.getMerchantTransListCount(merchantId, orderId, transStatus, startTime, endTime);
		if (amount > 0) {
			// 查询交易列表
			List<YdMerchantTrans> dataList = ydMerchantTransDao.getMerchantTransListByPage(merchantId, orderId, transStatus,
					startTime, endTime,pagerInfo.getStart(), pagerInfo.getPageSize());

			List<YdMerchantTransResult> resultList = DTOUtils.convertList(dataList, YdMerchantTransResult.class);
			resultList.forEach(ydMerchantGiftTransResult -> {
				YdMerchantTransSourceEnum transSourceEnum = YdMerchantTransSourceEnum.getByCode(ydMerchantGiftTransResult.getTransSource());
				if (transSourceEnum != null) {
					ydMerchantGiftTransResult.setIcon(transSourceEnum.getIcon());
				}
			});
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public void insertYdMerchantTrans(YdMerchantTransResult ydMerchantTransResult) {
		ydMerchantTransResult.setCreateTime(new Date());
		YdMerchantTrans ydMerchantTrans = new YdMerchantTrans();
		BeanUtilExt.copyProperties(ydMerchantTrans, ydMerchantTransResult);
		this.ydMerchantTransDao.insertYdMerchantTrans(ydMerchantTrans);
	}

	/**
	 * 添加商户账户交流流水
	 * @param merchantId	 	商户ID
	 * @param outOrderId	 	外部订单ID
	 * @param transSource	  	交易流水来源(MerchantTransSourceEnum对应code)
	 * @param transType		  	交易类型(IN|OUT)
	 * @param transDesc		  	交易流水描述信息
	 * @param outOrderStatus  	外部订单状态
	 * @param transAmount		交易金额(单位元)
	 * @param transBeforeAmount	交易前金额
	 * @param transAfterAmount	交易后金额
	 */
	@Override
	public void addMerchantTransDetail(Integer merchantId, String outOrderId, String transSource, String transType,
									   String transDesc, String outOrderStatus, Double transAmount, Double transBeforeAmount,
									   Double transAfterAmount, Double rateAmount) {
		YdMerchantTrans ydMerchantTrans = new YdMerchantTrans();
		Date nowDate = new Date();
		ydMerchantTrans.setCreateTime(nowDate);
		ydMerchantTrans.setUpdateTime(nowDate);
		ydMerchantTrans.setMerchantId(merchantId);
		ydMerchantTrans.setOutOrderId(outOrderId);
		ydMerchantTrans.setTransSource(transSource);
		ydMerchantTrans.setTransType(transType);
		ydMerchantTrans.setTransDesc(transDesc);
		ydMerchantTrans.setTransAmount(transAmount);
		ydMerchantTrans.setOutOrderStatus(outOrderStatus);
		ydMerchantTrans.setTransBeforeAmount(transBeforeAmount);
		ydMerchantTrans.setTransAfterAmount(transAfterAmount);
		ydMerchantTrans.setRateAmount(rateAmount);
		this.ydMerchantTransDao.insertYdMerchantTrans(ydMerchantTrans);
	}

}

