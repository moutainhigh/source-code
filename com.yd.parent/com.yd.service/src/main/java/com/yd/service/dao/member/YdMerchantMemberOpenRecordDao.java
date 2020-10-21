package com.yd.service.dao.member;

import java.util.List;
import com.yd.service.bean.member.YdMerchantMemberOpenRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:优度商户会员开通记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-03-20 16:58:04
 * @Version:1.1.0
 */
public interface YdMerchantMemberOpenRecordDao {

	/**
	 * 通过id得到优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantMemberOpenRecord getYdMerchantMemberOpenRecordById(Integer id);

	YdMerchantMemberOpenRecord getFirstValidEndTime(Integer merchantId);

	YdMerchantMemberOpenRecord getOpenValidEndTimeByTime(Integer merchantId);
	
	/**
	 * 获取数量
	 * @param ydMerchantMemberOpenRecord
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantMemberOpenRecordCount(YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantMemberOpenRecord
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberOpenRecord> findYdMerchantMemberOpenRecordListByPage(@Param("params") YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord,
                                                                                     @Param("pageStart") Integer pageStart,
                                                                                     @Param("pageSize") Integer pageSize);

	/**
	 * 得到所有优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecord
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantMemberOpenRecord> getAll(YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord);


	/**
	 * 添加优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecord
	 * @Description:
	 */
	public void insertYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord);
	
	/**
	 * 通过id修改优度商户会员开通记录YdMerchantMemberOpenRecord
	 * @param ydMerchantMemberOpenRecord
	 * @Description:
	 */
	public void updateYdMerchantMemberOpenRecord(YdMerchantMemberOpenRecord ydMerchantMemberOpenRecord);

}
