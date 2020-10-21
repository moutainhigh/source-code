package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantInfoAudit;
import org.apache.ibatis.annotations.Param;


/**
 * @Title:门店信息审核记录表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-05 18:31:13
 * @Version:1.1.0
 */
public interface YdMerchantInfoAuditDao {

	/**
	 * 通过id得到门店信息审核记录表YdMerchantInfoAudit
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantInfoAudit getYdMerchantInfoAuditById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantInfoAudit
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantInfoAuditCount(YdMerchantInfoAudit ydMerchantInfoAudit);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantInfoAudit
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantInfoAudit> findYdMerchantInfoAuditListByPage(@Param("params") YdMerchantInfoAudit ydMerchantInfoAudit,
                                                                       @Param("pageStart") Integer pageStart,
                                                                       @Param("pageSize") Integer pageSize);
	

	/**
	 * 得到所有门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAudit
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantInfoAudit> getAll(YdMerchantInfoAudit ydMerchantInfoAudit);

	/**
	 * 添加门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAudit
	 * @Description:
	 */
	public void insertYdMerchantInfoAudit(YdMerchantInfoAudit ydMerchantInfoAudit);
	
	/**
	 * 通过id修改门店信息审核记录表YdMerchantInfoAudit
	 * @param ydMerchantInfoAudit
	 * @Description:
	 */
	public void updateYdMerchantInfoAudit(YdMerchantInfoAudit ydMerchantInfoAudit);
	
}
