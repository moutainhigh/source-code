package com.yd.service.impl.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yd.api.result.user.YdUserAuthResult;
import com.yd.api.service.user.YdUserAuthService;
import com.yd.core.enums.YdUserAuthEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.user.YdUser;
import com.yd.service.dao.user.YdUserDao;
import org.apache.dubbo.config.annotation.Service;

import com.yd.service.dao.user.YdUserAuthDao;
import com.yd.service.bean.user.YdUserAuth;

/**
 * @Title:用户授权表Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 10:59:46
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserAuthServiceImpl implements YdUserAuthService {

	@Resource
	private YdUserDao ydUserDao;

	@Resource
	private YdUserAuthDao ydUserAuthDao;

	@Override
	public YdUserAuthResult getYdUserAuthById(Integer id) {
		if (id == null || id <= 0) return null;
		YdUserAuthResult ydUserAuthResult = null;
		YdUserAuth ydUserAuth = this.ydUserAuthDao.getYdUserAuthById(id);
		if (ydUserAuth != null) {
			ydUserAuthResult = new YdUserAuthResult();
			BeanUtilExt.copyProperties(ydUserAuthResult, ydUserAuth);
		}
		return ydUserAuthResult;
	}

	@Override
	public Page<YdUserAuthResult> findYdUserAuthListByPage(YdUserAuthResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdUserAuthResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdUserAuth ydUserAuth = new YdUserAuth();
		BeanUtilExt.copyProperties(ydUserAuth, params);

		int amount = this.ydUserAuthDao.getYdUserAuthCount(ydUserAuth);
		if (amount > 0) {
			List<YdUserAuth> dataList = this.ydUserAuthDao.findYdUserAuthListByPage(ydUserAuth,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdUserAuthResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdUserAuthResult> getAll(YdUserAuthResult ydUserAuthResult) {
		YdUserAuth ydUserAuth = null;
		if (ydUserAuthResult != null) {
			ydUserAuth = new YdUserAuth();
			BeanUtilExt.copyProperties(ydUserAuth, ydUserAuthResult);
		}
		List<YdUserAuth> dataList = this.ydUserAuthDao.getAll(ydUserAuth);
		return DTOUtils.convertList(dataList, YdUserAuthResult.class);
	}

	@Override
	public void insertYdUserAuth(YdUserAuthResult ydUserAuthResult) {
		ydUserAuthResult.setCreateTime(new Date());
		ydUserAuthResult.setUpdateTime(new Date());
		YdUserAuth ydUserAuth = new YdUserAuth();
		BeanUtilExt.copyProperties(ydUserAuth, ydUserAuthResult);
		this.ydUserAuthDao.insertYdUserAuth(ydUserAuth);
	}

	@Override
	public void updateYdUserAuth(YdUserAuthResult ydUserAuthResult) {
		ydUserAuthResult.setUpdateTime(new Date());
		YdUserAuth ydUserAuth = new YdUserAuth();
		BeanUtilExt.copyProperties(ydUserAuth, ydUserAuthResult);
		this.ydUserAuthDao.updateYdUserAuth(ydUserAuth);
	}

	@Override
	public YdUserAuthResult addUserAuth(String openId, String accessToken) {
		if (StringUtil.isEmptys(openId, accessToken)) return null;

		// 为空的话调用微信接口，保存用户信息，保存用户授权信息
		YdUserAuth ydUserAuth = this.ydUserAuthDao.getYdUserAuthByOpenId(openId);
		if (ydUserAuth == null) {
			Map<String, String> resultMap = WechatUtil.getUserInfo(accessToken, openId);
			if (resultMap == null) return null;

			Date nowDate = new Date();
			// 保存用户信息
			YdUser ydUser = new YdUser();
			ydUser.setNickname(resultMap.get("nickname"));
			ydUser.setImage(resultMap.get("headImage"));
			ydUser.setCreateTime(nowDate);
			ydUserDao.insertYdUser(ydUser);

			// 保存授权信息
			ydUserAuth = new YdUserAuth();
			ydUserAuth.setSex(0);
			ydUserAuth.setOpenId(openId);
			ydUserAuth.setCreateTime(nowDate);
			ydUserAuth.setUserId(ydUser.getId());
			ydUserAuth.setType(YdUserAuthEnum.WEIXIN.getCode());
			ydUserAuth.setUnionId(resultMap.get("unionId"));
			ydUserAuth.setNickname(resultMap.get("nickname"));
			ydUserAuth.setHeadImage(resultMap.get("headImage"));
			this.ydUserAuthDao.insertYdUserAuth(ydUserAuth);
		}

		YdUserAuthResult ydUserAuthResult = new YdUserAuthResult();
		BeanUtilExt.copyProperties(ydUserAuthResult, ydUserAuth);
		return ydUserAuthResult;
	}

}

