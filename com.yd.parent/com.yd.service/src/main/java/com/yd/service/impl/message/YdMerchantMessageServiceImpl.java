package com.yd.service.impl.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.message.YdMerchantMessageResult;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.message.YdMerchantMessageService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.message.YdMerchantMessageDao;
import com.yd.service.bean.message.YdMerchantMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:商户消息通知Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-17 17:33:49
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantMessageServiceImpl implements YdMerchantMessageService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantMessageServiceImpl.class);


	private final String order_message_content = "叮咚！店铺来新订单了，请及时处理";

	private final String gift_message_content = "礼品商城有新礼品，赶快看看吧";

	@Resource
	private YdMerchantMessageDao ydMerchantMessageDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantMessageResult getYdMerchantMessageById(Integer id) {
		if (id == null || id <= 0) { return null; }
		YdMerchantMessageResult ydMerchantMessageResult = null;
		YdMerchantMessage ydMerchantMessage = this.ydMerchantMessageDao.getYdMerchantMessageById(id);
		if (ydMerchantMessage != null) {
			ydMerchantMessageResult = new YdMerchantMessageResult();
			BeanUtilExt.copyProperties(ydMerchantMessageResult, ydMerchantMessage);
		}
		return ydMerchantMessageResult;
	}

	@Override
	public Page<YdMerchantMessageResult> findYdMerchantMessageListByPage(YdMerchantMessageResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdMerchantMessageResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdMerchantMessage ydMerchantMessage = new YdMerchantMessage();
		BeanUtilExt.copyProperties(ydMerchantMessage, params);
		
		int amount = this.ydMerchantMessageDao.getYdMerchantMessageCount(ydMerchantMessage);
		if (amount > 0) {
			List<YdMerchantMessage> dataList = this.ydMerchantMessageDao.findYdMerchantMessageListByPage(
					ydMerchantMessage, pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdMerchantMessageResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdMerchantMessageResult> getAll(YdMerchantMessageResult ydMerchantMessageResult) {
		YdMerchantMessage ydMerchantMessage = null;
		if (ydMerchantMessageResult != null) {
			ydMerchantMessage = new YdMerchantMessage();
			BeanUtilExt.copyProperties(ydMerchantMessage, ydMerchantMessageResult);
		}
		List<YdMerchantMessage> dataList = this.ydMerchantMessageDao.getAll(ydMerchantMessage);
		return DTOUtils.convertList(dataList, YdMerchantMessageResult.class);
	}

	@Override
	public void insertOrderMessage(String messageType, Integer outOrderId, Integer merchantId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(messageType),
				"err_empty_message_type", "消息类型不可以为空");

		ValidateBusinessUtils.assertFalse(outOrderId == null || outOrderId <= 0,
				"err_empty_out_order_id", "外部订单id不可以为空");

		ydMerchantService.getStoreInfo(merchantId);

		// 查询设置门店下的操作员
		List<YdMerchantResult> childList = ydMerchantService.findYdMerchantListByPid(merchantId);
		List<YdMerchantResult> merchantList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(childList)) {
			merchantList.addAll(childList);
		}
		batchInsertMessage(merchantList, messageType, order_message_content, outOrderId);
	}

	@Override
	public void insertGiftMessage(String messageType, Integer outOrderId) throws BusinessException {
		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(messageType),
				"err_empty_message_type", "消息类型不可以为空");

		ValidateBusinessUtils.assertFalse(outOrderId == null || outOrderId <= 0,
				"err_empty_out_order_id", "外部订单id不可以为空");

		// 查询所有商户
		YdMerchantResult params = new YdMerchantResult();
		params.setGroupCode(EnumSiteGroup.MERCHANT.getCode());
		List<YdMerchantResult> merchantList = ydMerchantService.getAll(params);
		if (CollectionUtils.isNotEmpty(merchantList)) {
			batchInsertMessage(merchantList, messageType, gift_message_content, outOrderId);
		}
	}

	@Override
	public void updateYdMerchantMessage(YdMerchantMessageResult ydMerchantMessageResult) {
		ydMerchantMessageResult.setUpdateTime(new Date());
		YdMerchantMessage ydMerchantMessage = new YdMerchantMessage();
		BeanUtilExt.copyProperties(ydMerchantMessage, ydMerchantMessageResult);
		this.ydMerchantMessageDao.updateYdMerchantMessage(ydMerchantMessage);
	}

	/**
	 * 商户消息已读
	 * @param merchantId
	 * @param id
	 */
	@Override
	public void updateMessage(Integer merchantId, Integer id) {
		if (merchantId == null || id == null) return;
		YdMerchantMessage ydMerchantMessage = this.ydMerchantMessageDao.getYdMerchantMessageById(id);
		if (ydMerchantMessage != null) {
			ydMerchantMessage.setMerchantId(merchantId);
			ydMerchantMessage.setIsRead("Y");
			this.ydMerchantMessageDao.updateYdMerchantMessage(ydMerchantMessage);
		}
	}

	/**
	 * 商户消息全部已读
	 * @param merchantId
	 */
	@Override
	public void updateAllMessage(Integer merchantId) throws BusinessException {
		YdMerchantMessage ydMerchantMessage = new YdMerchantMessage();
		ydMerchantMessage.setMerchantId(merchantId);
		ydMerchantMessage.setIsRead("Y");
		this.ydMerchantMessageDao.updateAllMessageRead(ydMerchantMessage);
	}

	private void batchInsertMessage(List<YdMerchantResult> merchantList, String messageType, String contents, Integer outOrderId) {
		merchantList.forEach(ydMerchantResult -> {
			YdMerchantMessage ydMerchantMessage = new YdMerchantMessage();
			ydMerchantMessage.setCreateTime(new Date());
			ydMerchantMessage.setIsRead("N");
			ydMerchantMessage.setContents(contents);
			ydMerchantMessage.setOutOrderId(outOrderId);
			ydMerchantMessage.setMessageType(messageType);
			ydMerchantMessage.setPid(ydMerchantResult.getPid());
			ydMerchantMessage.setMerchantId(ydMerchantResult.getId());
			this.ydMerchantMessageDao.insertYdMerchantMessage(ydMerchantMessage);
		});
	}

}

