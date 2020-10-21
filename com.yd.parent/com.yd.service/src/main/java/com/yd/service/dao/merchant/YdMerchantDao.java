package com.yd.service.dao.merchant;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yd.service.bean.merchant.YdMerchant;

/**
 * @Title:优度后台商户Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:06
 * @Version:1.1.0
 */
public interface YdMerchantDao {

	/**
	 * 通过id得到优度后台商户YdMerchant
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchant getYdMerchantById(Integer id);

	public List<YdMerchant> findYdMerchantListByPid(Integer id);

	/**
	 * 通过手机号得到优度后台商户YdMerchant
	 * @param mobile
	 * @return
	 * @Description:
	 */
	public YdMerchant getYdMerchantByMobile(String mobile);

	/**
	 * 获取数量
	 * @param ydMerchant
	 * @return
	 * @Description:
	 */
	public int getYdMerchantCount(YdMerchant ydMerchant);

	/**
	 * 分页获取数据
	 * @param ydMerchant
	 * @return
	 * @Description:
	 */
	public List<YdMerchant> findYdMerchantListByPage(@Param("params") YdMerchant ydMerchant,
													 @Param("pageStart") Integer pageStart,
													 @Param("pageSize") Integer pageSize);

	int getPlatformMerchantCount(YdMerchant ydMerchant);

	List<YdMerchant> findPlatformMerchantList(@Param("params") YdMerchant ydMerchant,
											  @Param("pageStart") Integer pageStart,
											  @Param("pageSize") Integer pageSize);
	/**
	 * 得到所有优度后台商户YdMerchant
	 * @param ydMerchant
	 * @return
	 * @Description:
	 */
	public List<YdMerchant> getAll(YdMerchant ydMerchant);

	/**
	 * 添加优度后台商户YdMerchant
	 * @param ydMerchant
	 * @Description:
	 */
	public void insertYdMerchant(YdMerchant ydMerchant);

	/**
	 * 通过id修改优度后台商户YdMerchant
	 * @param ydMerchant
	 * @Description:
	 */
	public void updateYdMerchant(YdMerchant ydMerchant);

	/**
	 * 删除门店的操作员
	 * @param merchantId
	 * @Description:
	 */
	public void deleteMerchantOperate(@Param("merchantId") Integer merchantId,
									  @Param("roleId") String roleId,
									  @Param("groupCode") String groupCode);

	public List<YdMerchant> getSysAccountList(@Param("offset")Integer offset, @Param("rows")Integer rows);

	public void updateSysAccount(@Param("param")YdMerchant merchant);

	/**
	 * 设置商户的支付密码
	 * @param merchantId
	 * @param payPassword
	 */
    int updatePayPassword(@Param("merchantId") Integer merchantId,
						  @Param("payPassword") String payPassword,
						  @Param("oldPassword") String oldPassword);

	/**
	 * 查询商户下操作员列表
	 * @param merchantId
	 */
	List<YdMerchant> findOperateList(@Param("merchantId") Integer merchantId,
									 @Param("pageStart") Integer pageStart,
									 @Param("pageSize") Integer pageSize);

	Integer getOperateCount(@Param("merchantId") Integer merchantId);

	public void updateMerchantBindWeixin(@Param("merchantId")Integer merchantId, @Param("wxOpenId")String wxOpenId, @Param("bindWxDetail")String bindWxDetail);

	/**
	 * 查询所有的门店
	 * @return
	 */
	List<YdMerchant> findStoreList(YdMerchant merchant);

	/**
	 * 条件分页查询所有的门店
	 * @return
	 */
	List<YdMerchant> findStoreListByPage(@Param("param") YdMerchant ydMerchant,
										 @Param("pageStart") Integer pageStart,
										 @Param("pageSize") Integer pageSize);

	// 禁用商户，将下面的操作员也禁用掉
    void updateMerchantAndOperate(@Param("merchantId") Integer merchantId,
								  @Param("isFlag") String isFlag);
}
