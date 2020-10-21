package com.yd.api.service.merchant;

import java.util.List;

import com.yd.api.enums.EnumSiteGroup;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:优度后台商户Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-16 16:39:06
 * @Version:1.1.0
 */
public interface YdMerchantService {

	/**
	 * 通过id得到优度后台商户YdMerchant
	 * @param id
	 * @return
	 * @Description:
	 */
	public YdMerchantResult getYdMerchantById(Integer id);

	/**
	 * 获取商户下面的操作员
	 * @param id
	 * @return
	 * @Description:
	 */
	public List<YdMerchantResult> findYdMerchantListByPid(Integer id);

	/**
	 * 查询全部供应商和门店
	 * @param ydMerchantResult
	 * @param pageInfo
	 * @return
	 */
	Page<YdMerchantResult> findPlatformMerchantList(YdMerchantResult ydMerchantResult, PagerInfo pageInfo) throws BusinessException;

	/**
	 * 分页查询优度后台商户YdMerchant
	 * @param ydMerchantResult
	 * @param pagerInfo
	 * @return
	 * @Description:
	 */
	public Page<YdMerchantResult> findYdMerchantListByPage(YdMerchantResult ydMerchantResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有优度后台商户YdMerchant
	 * @param ydMerchantResult
	 * @return
	 * @Description:
	 */
	public List<YdMerchantResult> getAll(YdMerchantResult ydMerchantResult);

	/**
	 * 添加优度后台商户YdMerchant
	 * @param ydMerchantResult
	 * @Description:
	 */
	public void insertYdMerchant(YdMerchantResult ydMerchantResult, EnumSiteGroup siteGroup) throws BusinessException;

	/**
	 * 通过id修改优度后台商户YdMerchant throws BusinessException;
	 * @param ydMerchantResult
	 * @Description:
	 */
	public void updateYdMerchant(YdMerchantResult ydMerchantResult, EnumSiteGroup siteGroup) throws BusinessException;

	/**
	 * 查询账户
	 * @param pagerInfo
	 * @param enumSiteGroup sys(系统账户) | merchant(商户账户)
	 * @return
	 */
	Page<YdMerchantResult> findMerchantList(PagerInfo pagerInfo, EnumSiteGroup enumSiteGroup) throws BusinessException;

	/**
	 *  查询门店下操作员
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	Page<YdMerchantResult> findOperateList(PagerInfo pagerInfo, Integer merchantId) throws BusinessException;

	/**
	 * 修改门店详细信息
	 * @param ydMerchantResult
	 */
	void updateMerchantInfo(YdMerchantResult ydMerchantResult) throws BusinessException;

	/**
	 * 更换手机号发送短信验证码
	 * @param merchantId
	 */
    void updateMerchantMobileSendSms(Integer merchantId) throws BusinessException;

	/**
	 * 校验更新手机号发送的验证码
	 * @param merchantId
	 * @param smsCode
	 * @return
	 * @throws BusinessException
	 */
	boolean checkUpdateMerchantMobileSmsCode(Integer merchantId, String smsCode) throws BusinessException;

	/**
	 * 开通线上支付
	 * @param merchantId
	 */
    void openPay(Integer merchantId) throws BusinessException;

	/**
	 * 设置开通比较功能
	 * @param merchantId
	 * @param type
	 * @throws BusinessException
	 */
	void setComparePrice(Integer merchantId, String type) throws BusinessException;

	/**
	 * 平台删除商户
	 * @param merchantId
	 * @throws BusinessException
	 */
	void deleteMerchant(Integer merchantId) throws BusinessException;

	/**
	 * 平台添加商户
	 * @param ydMerchantResult
	 */
	YdMerchantResult platformInsertMerchant(YdMerchantResult ydMerchantResult) throws BusinessException;

	/**
	 * 平台修改商户
	 * @param ydMerchantResult
	 */
	YdMerchantResult platformUpdateMerchant(YdMerchantResult ydMerchantResult) throws BusinessException;

	/**
	 * 校验商户是否存在
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	YdMerchantResult checkMerchantInfo(Integer merchantId) throws BusinessException;

	/**
	 * 根据门店id或者操作员id，获取门店信息
	 * @param merchantId
	 * @return 门店信息
	 * @throws BusinessException
	 */
	YdMerchantResult getStoreInfo(Integer merchantId) throws BusinessException;

	/**
	 * 门店删除操作员
	 * @param storeId	门店id
	 * @param operateId	操作员id
	 */
    void deleteStoreOperate(Integer storeId, Integer operateId) throws BusinessException;

	/**
	 * 查询供应商列表
	 * @return
	 */
	List<YdMerchantResult> getSupplierList() throws BusinessException;

	/**
	 * 商户开启关闭旧机抵扣功能
	 * @param merchantId
	 * @param type	Y | N
	 */
    void setOldMachineReduce(Integer merchantId, String type) throws BusinessException;

	/**
	 * 绑定微信账户
	 * @param merchantId
	 * @param openId
	 * @return
	 * @throws BusinessException
	 */
	Boolean bindWechatAccount(Integer merchantId, String openId) throws BusinessException;

	/**
	 * 解除绑定微信账号
	 * @param merchantId
	 * @return
	 * @throws BusinessException
	 */
	Boolean unbindWechatAccount(Integer merchantId) throws BusinessException;

	/**
	 * 平台升级商户会员
	 * @param merchantId
	 * @param memberLevel
	 * @throws BusinessException
	 */
    void merchantUpgrade(Integer merchantId, Integer memberLevel) throws BusinessException;

	/**
	 * 平台续费商户会员
	 * @param merchantId
	 * @param memberLevel
	 * @throws BusinessException
	 */
	void memberRenewal(Integer merchantId, Integer memberLevel) throws BusinessException;

	/**
	 * 平台续费商户会员
	 * @param merchantId
	 * @param memberLevel
	 * @throws BusinessException
	 */
	void openMemberAgain(Integer merchantId, Integer memberLevel) throws BusinessException;

	/**
	 * 平台启用禁用商户
	 * @param merchantId
	 * @param isFlag		Y代表禁用，N代表未禁用
	 * @throws BusinessException
	 */
	void updateMerchantStatus(Integer merchantId, String isFlag) throws BusinessException;

	/**
	 * 设置商户商城二维码
	 * @param merchantId
	 * @param shopQrcode
	 * @throws BusinessException
	 */
	void updateShopQrCode(Integer merchantId, String shopQrcode) throws BusinessException;

	/**
	 * 同步商户会员是否过期
	 * @throws Exception
	 */
	void synMemberValidTime() throws Exception;

	/**
	 * 查询平台所有的商户
	 * @return
	 * @throws BusinessException
	 */
    List<YdMerchantResult> findStoreList(YdMerchantResult ydMerchantResult) throws BusinessException;

	/**
	 * 条件分页搜索商户，仅仅是商户
	 * @param ydMerchantResult
	 * @param pageInfo
	 * @return
	 * @throws BusinessException
	 */
	public Page<YdMerchantResult> findStoreListByPage(YdMerchantResult ydMerchantResult, PagerInfo pageInfo) throws BusinessException;
}