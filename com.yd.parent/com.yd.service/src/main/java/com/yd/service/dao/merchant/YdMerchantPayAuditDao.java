package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantPayAudit;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户支付申请管理Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-25 11:17:40
 * @Version:1.1.0
 */
public interface YdMerchantPayAuditDao {

	/**
	 * 通过id得到商户支付申请管理YdMerchantPayAudit
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantPayAudit getYdMerchantPayAuditById(Integer id);

	/**
	 * 通过merchantId得到商户支付申请管理YdMerchantPayAudit
	 * @param merchantId
	 * @return
	 * @Description:
	 */
	public YdMerchantPayAudit getYdPayAuditByMerchantId(Integer merchantId);

	/**
	 * 获取数量
	 * @param ydMerchantPayAudit
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantPayAuditCount(YdMerchantPayAudit ydMerchantPayAudit);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantPayAudit
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantPayAudit> findYdMerchantPayAuditListByPage(@Param("params") YdMerchantPayAudit ydMerchantPayAudit,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAudit
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantPayAudit> getAll(YdMerchantPayAudit ydMerchantPayAudit);

	/**
	 * 添加商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAudit
	 * @Description:
	 */
	public void insertYdMerchantPayAudit(YdMerchantPayAudit ydMerchantPayAudit);

	/**
	 * 通过id修改商户支付申请管理YdMerchantPayAudit
	 * @param ydMerchantPayAudit
	 * @Description:
	 */
	public void updateYdMerchantPayAudit(YdMerchantPayAudit ydMerchantPayAudit);

}
