package com.yd.service.impl.user;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.yd.api.result.user.YdUserAddressResult;
import com.yd.api.service.user.YdUserAddressService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.sys.SysRegion;
import com.yd.service.dao.sys.SysRegionDao;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.user.YdUserAddressDao;
import com.yd.service.bean.user.YdUserAddress;

/**
 * @Title:用户收货地址Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-28 10:17:22
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserAddressServiceImpl implements YdUserAddressService {

	@Resource
	private SysRegionDao sysRegionDao;

	@Resource
	private YdUserAddressDao ydUserAddressDao;

	@Resource
	private YdUserService ydUserService;

	@Override
	public YdUserAddressResult getYdUserAddressById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdUserAddressResult ydUserAddressResult = null;
		YdUserAddress ydUserAddress = this.ydUserAddressDao.getYdUserAddressById(id);
		if (ydUserAddress != null) {
			ydUserAddressResult = new YdUserAddressResult();
			BeanUtilExt.copyProperties(ydUserAddressResult, ydUserAddress);
		}
		return ydUserAddressResult;
	}

	@Override
	public Page<YdUserAddressResult> findYdUserAddressListByPage(YdUserAddressResult params, PagerInfo pagerInfo) throws BusinessException {
		Page<YdUserAddressResult> pageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdUserAddress ydUserAddress = new YdUserAddress();
		BeanUtilExt.copyProperties(ydUserAddress, params);
		
		int amount = this.ydUserAddressDao.getYdUserAddressCount(ydUserAddress);
		if (amount > 0) {
			List<YdUserAddress> dataList = this.ydUserAddressDao.findYdUserAddressListByPage(ydUserAddress,
					pagerInfo.getStart(), pagerInfo.getPageSize());
			pageData.setData(DTOUtils.convertList(dataList, YdUserAddressResult.class));
		}
		pageData.setTotalRecord(amount);
		return pageData;
	}

	@Override
	public List<YdUserAddressResult> getAll(YdUserAddressResult ydUserAddressResult) {
		YdUserAddress ydUserAddress = null;
		if (ydUserAddressResult != null) {
			ydUserAddress = new YdUserAddress();
			BeanUtilExt.copyProperties(ydUserAddress, ydUserAddressResult);
		}
		List<YdUserAddress> dataList = this.ydUserAddressDao.getAll(ydUserAddress);
		List<YdUserAddressResult> resultList = DTOUtils.convertList(dataList, YdUserAddressResult.class);
		return resultList;
	}

	@Override
	public void insertYdUserAddress(YdUserAddressResult ydUserAddressResult) {
		// 校验新增地址入参
		checkAddressParams(ydUserAddressResult);

		if ("Y".equalsIgnoreCase(ydUserAddressResult.getIsDefault())) {
			// 将其他地址修改为非默认地址
			this.ydUserAddressDao.updateNoDefaultAddress(ydUserAddressResult.getUserId());
		}

		ydUserAddressResult.setCreateTime(new Date());
		YdUserAddress ydUserAddress = new YdUserAddress();
		BeanUtilExt.copyProperties(ydUserAddress, ydUserAddressResult);
		this.ydUserAddressDao.insertYdUserAddress(ydUserAddress);
	}
	
	@Override
	public void updateYdUserAddress(YdUserAddressResult ydUserAddressResult) {

		Integer addressId = ydUserAddressResult.getId();
		ValidateBusinessUtils.assertFalse(addressId == null || addressId <= 0,
				"err_address_id", "收货地址不可以为空");

		YdUserAddress userAddress = ydUserAddressDao.getYdUserAddressById(addressId);
		ValidateBusinessUtils.assertFalse(userAddress == null,
				"err_address_id", "收货地址不存在");

		if ("Y".equalsIgnoreCase(ydUserAddressResult.getIsDefault())) {
			// 将其他地址修改为非默认地址
			this.ydUserAddressDao.updateNoDefaultAddress(ydUserAddressResult.getUserId());
		}

		checkAddressParams(ydUserAddressResult);
		ydUserAddressResult.setUpdateTime(new Date());
		YdUserAddress ydUserAddress = new YdUserAddress();
		BeanUtilExt.copyProperties(ydUserAddress, ydUserAddressResult);
		this.ydUserAddressDao.updateYdUserAddress(ydUserAddress);
	}

	/**
	 * 删除用户收货地址
	 * @param userId	 用户id
	 * @param addressId	 收货地址id
	 * @throws BusinessException
	 */
	@Override
	public void deleteYdUserAddress(Integer userId, Integer addressId) throws BusinessException {
		ydUserService.checkUserInfo(userId);

		ValidateBusinessUtils.assertIdNotNull(addressId, "err_address_id_empty", "收货地址不可以为空");
		this.ydUserAddressDao.deleteYdUserAddress(userId, addressId);
	}

	@Override
	public YdUserAddressResult checkAddressId(Integer addressId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(addressId, "err_address_id_empty", "收货地址不可以为空");

		YdUserAddress ydUserAddress = this.ydUserAddressDao.getYdUserAddressById(addressId);
		ValidateBusinessUtils.assertNonNull(ydUserAddress, "err_no_addressId", "收货地址不存在");

		YdUserAddressResult ydUserAddressResult = new YdUserAddressResult();
		BeanUtilExt.copyProperties(ydUserAddressResult, ydUserAddress);
		return ydUserAddressResult;
	}

	// 校验保存修改参数
	private void checkAddressParams(YdUserAddressResult params) {
		ValidateBusinessUtils.assertFalse(params.getUserId() == null || params.getUserId() <= 0,
				"err_user_id_empty", "用户id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getRealname()),
				"err_real_name_empty", "联系人不可以为空");

		ValidateBusinessUtils.assertFalse(params.getRealname().length() > 10,
				"err_real_name_length", "联系人最多10个字符");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getMobile()),
				"err_mobile_empty", "手机不可以为空");

		ValidateBusinessUtils.assertFalse(params.getDistrictId() == null || params.getDistrictId() <= 0,
				"err_district_id_empty", "所属区id不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getAddress()),
				"err_address_empty", "详细地址不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(params.getIsDefault()),
				"err_default_empty", "是否设置为默认地址不可以为空");

		// 根据区去查询省市区信息
		SysRegion distinct = sysRegionDao.getSysRegionById(params.getDistrictId());
		ValidateBusinessUtils.assertFalse(distinct == null,
				"err_distinct_id", "请传入正确的地址id");

		params.setDistrict(distinct.getName());
		params.setDistrictId(distinct.getId());

		// 查询市的信息
		SysRegion city = sysRegionDao.getSysRegionById(distinct.getPid());
		params.setCity(city.getName());
		params.setCityId(city.getId());

		// 查询省的信息
		SysRegion province = sysRegionDao.getSysRegionById(city.getPid());
		params.setProvince(province.getName());
		params.setProvinceId(province.getId());
	}

}

