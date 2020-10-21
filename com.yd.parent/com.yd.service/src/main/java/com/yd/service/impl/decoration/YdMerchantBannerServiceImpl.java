package com.yd.service.impl.decoration;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yd.api.result.decoration.YdMerchantBannerResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.service.decoration.YdMerchantBannerService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.ValidateBusinessUtils;
import com.yd.service.bean.decoration.YdMerchantBannerHistory;
import com.yd.service.dao.decoration.YdMerchantBannerDao;
import com.yd.service.dao.decoration.YdMerchantBannerHistoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.bean.decoration.YdMerchantBanner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户banner图Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-21 19:33:42
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantBannerServiceImpl implements YdMerchantBannerService {

	@Resource
	private YdMerchantBannerDao ydShopMerchantBannerDao;

    @Resource
    private YdMerchantBannerHistoryDao ydShopMerchantBannerHistoryDao;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantBannerResult getYdShopMerchantBannerById(Integer id) {
		if (id == null || id <= 0) return null;
		YdMerchantBannerResult ydShopMerchantBannerResult = null;
		YdMerchantBanner ydShopMerchantBanner = this.ydShopMerchantBannerDao.getYdShopMerchantBannerById(id);
		if (ydShopMerchantBanner != null) {
			ydShopMerchantBannerResult = new YdMerchantBannerResult();
			BeanUtilExt.copyProperties(ydShopMerchantBannerResult, ydShopMerchantBanner);
		}
		return ydShopMerchantBannerResult;
	}

	@Override
	public List<YdMerchantBannerResult> getAll(YdMerchantBannerResult ydShopMerchantBannerResult) {
		Integer merchantId = ydShopMerchantBannerResult.getMerchantId();
		ValidateBusinessUtils.assertIdNotNull(merchantId, "err_merchant_id", "非法的商户id");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		YdMerchantBanner ydShopMerchantBanner = new YdMerchantBanner();
		ydShopMerchantBanner.setMerchantId(storeInfo.getId());

		List<YdMerchantBanner> dataList = this.ydShopMerchantBannerDao.getAll(ydShopMerchantBanner);
		return DTOUtils.convertList(dataList, YdMerchantBannerResult.class);
	}

	@Override
	public void insertYdShopMerchantBanner(YdMerchantBannerResult ydShopMerchantBannerResult) {
		if (null != ydShopMerchantBannerResult) {
			ydShopMerchantBannerResult.setCreateTime(new Date());
			YdMerchantBanner ydShopMerchantBanner = new YdMerchantBanner();
			BeanUtilExt.copyProperties(ydShopMerchantBanner, ydShopMerchantBannerResult);
			this.ydShopMerchantBannerDao.insertYdShopMerchantBanner(ydShopMerchantBanner);
		}
	}

	@Override
	public void updateYdShopMerchantBanner(YdMerchantBannerResult ydShopMerchantBannerResult) {
		if (null != ydShopMerchantBannerResult) {
			ydShopMerchantBannerResult.setUpdateTime(new Date());
			YdMerchantBanner ydShopMerchantBanner = new YdMerchantBanner();
			BeanUtilExt.copyProperties(ydShopMerchantBanner, ydShopMerchantBannerResult);
			this.ydShopMerchantBannerDao.updateYdShopMerchantBanner(ydShopMerchantBanner);
		}
	}

    @Transactional(rollbackFor = Exception.class)
	@Override
	public void saveOrUpdate(Integer merchantId, List<YdMerchantBannerResult> bannerList) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(merchantId, "err_merchant_id", "非法的商户id");

		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertCollectionNotEmpty(bannerList, "err_empty_banner_list", "banner不可以为空");

		ValidateBusinessUtils.assertFalse(bannerList.size() > 8,
				"err_max_banner_list", "banner最多可以添加8条");

        bannerList.forEach(data -> {
            ValidateBusinessUtils.assertStringNotBlank(data.getPictureUrl(), "err_empty_banner_picture_url", "banner图不可以为空");
            ValidateBusinessUtils.assertStringNotBlank(data.getJumpType(), "err_empty_banner_jump_type", "banner跳转类型不可以为空");
            ValidateBusinessUtils.assertStringNotBlank(data.getJumpUrl(), "err_empty_banner_jump_url", "banner跳转地址不可以为空");
            ValidateBusinessUtils.assertNonNull(data.getSort(), "err_empty_sort", "排序号不可以为空");
			data.setId(null);
        });

        // 删除原本所有记录, 重新保存
        this.ydShopMerchantBannerDao.deleteYdShopMerchantBannerByMerchantId(merchantId);
        bannerList.forEach(data -> {
			data.setMerchantId(storeInfo.getId());
            this.insertYdShopMerchantBanner(data);
        });

        // 保存图片记录
        this.savePictureHistory(merchantId, bannerList);
	}

    private void savePictureHistory(Integer merchantId, List<YdMerchantBannerResult> bannerList) {
        Set<String> setList = bannerList.stream().map(YdMerchantBannerResult::getPictureUrl).collect(Collectors.toSet());
        if (!setList.isEmpty()) { // 查询历史记录，排除已经上传过的
            List<YdMerchantBannerHistory> historyList = ydShopMerchantBannerHistoryDao.findMerchantBannerPictureHistoryList(merchantId, setList);
            Date nowDate = new Date();
            if (CollectionUtils.isEmpty(historyList)) {
				setList.forEach(data -> {
					YdMerchantBannerHistory ydShopMerchantBannerHistory = new YdMerchantBannerHistory();
					ydShopMerchantBannerHistory.setCreateTime(nowDate);
					ydShopMerchantBannerHistory.setUpdateTime(nowDate);
					ydShopMerchantBannerHistory.setMerchantId(merchantId);
					ydShopMerchantBannerHistory.setPictureUrl(data);
					ydShopMerchantBannerHistoryDao.insertYdShopMerchantBannerHistory(ydShopMerchantBannerHistory);
				});
			} else {
				Set<String> historySet = historyList.stream().map(YdMerchantBannerHistory::getPictureUrl).collect(Collectors.toSet());
				setList.forEach(data -> {
					if (!historySet.contains(data)) {
						YdMerchantBannerHistory ydMerchantBannerHistory = new YdMerchantBannerHistory();
						ydMerchantBannerHistory.setCreateTime(nowDate);
						ydMerchantBannerHistory.setUpdateTime(nowDate);
						ydMerchantBannerHistory.setPictureUrl(data);
						ydMerchantBannerHistory.setMerchantId(merchantId);
						ydShopMerchantBannerHistoryDao.insertYdShopMerchantBannerHistory(ydMerchantBannerHistory);
					}
				});
			}
        }
    }

}

