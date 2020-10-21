package com.yd.service.dao.user;

import java.util.List;
import com.yd.service.bean.user.YdUserAddress;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:用户收货地址Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-28 10:17:22
 * @Version:1.1.0
 */
public interface YdUserAddressDao {

	/**
	 * 通过id得到用户收货地址YdUserAddress
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserAddress getYdUserAddressById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydUserAddress
	 * @return 
	 * @Description:
	 */
	public int getYdUserAddressCount(YdUserAddress ydUserAddress);
	
	/**
	 * 分页获取数据
	 * @param ydUserAddress
	 * @return 
	 * @Description:
	 */
	public List<YdUserAddress> findYdUserAddressListByPage(@Param("params") YdUserAddress ydUserAddress,
                                                           @Param("pageStart") Integer pageStart,
                                                           @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有用户收货地址YdUserAddress
	 * @param ydUserAddress
	 * @return 
	 * @Description:
	 */
	public List<YdUserAddress> getAll(YdUserAddress ydUserAddress);


	/**
	 * 添加用户收货地址YdUserAddress
	 * @param ydUserAddress
	 * @Description:
	 */
	public void insertYdUserAddress(YdUserAddress ydUserAddress);
	
	/**
	 * 通过id修改用户收货地址YdUserAddress
	 * @param ydUserAddress
	 * @Description:
	 */
	public void updateYdUserAddress(YdUserAddress ydUserAddress);

	/**
	 * 删除用户收货地址
	 * @param userId
	 * @param addressId
	 */
    void deleteYdUserAddress(@Param("userId") Integer userId, @Param("addressId") Integer addressId);

	/**
	 * 将其他地址修改为非默认地址
	 * @param userId
	 */
	void updateNoDefaultAddress(Integer userId);
}
