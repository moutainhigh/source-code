package com.yd.service.dao.member;

import java.util.List;
import com.yd.service.bean.member.YdMerchantMemberPayRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:优度商户会员注册，续费，升级支付记录表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:59:25
 * @Version:1.1.0
 */
public interface YdMerchantMemberPayRecordDao {

	/**
	 * 通过id得到优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMemberPayRecord getYdMerchantMemberPayRecordById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantMemberPayRecord
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantMemberPayRecordCount(YdMerchantMemberPayRecord ydMerchantMemberPayRecord);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantMemberPayRecord
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberPayRecord> findYdMerchantMemberPayRecordListByPage(@Param("params") YdMerchantMemberPayRecord ydMerchantMemberPayRecord,
                                                                                   @Param("pageStart") Integer pageStart,
                                                                                   @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecord
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberPayRecord> getAll(YdMerchantMemberPayRecord ydMerchantMemberPayRecord);

	/**
	 * 添加优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecord
	 * @Description:
	 */
	public void insertYdMerchantMemberPayRecord(YdMerchantMemberPayRecord ydMerchantMemberPayRecord);
	
	/**
	 * 通过id修改优度商户会员注册，续费，升级支付记录表YdMerchantMemberPayRecord
	 * @param ydMerchantMemberPayRecord
	 * @Description:
	 */
	public void updateYdMerchantMemberPayRecord(YdMerchantMemberPayRecord ydMerchantMemberPayRecord);
	
}
