package com.yd.service.dao.user;

import java.util.List;
import com.yd.service.bean.user.YdUserMerchant;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:用户商户绑定关系表Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-05-29 10:11:01
 * @Version:1.1.0
 */
public interface YdUserMerchantDao {

	/**
	 * 通过id得到用户商户绑定关系表YdUserMerchant
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdUserMerchant getYdUserMerchantById(Integer id);

	/**
	 * 通过userId得到用户商户绑定关系表YdUserMerchant
	 * @param userId
	 * @return
	 * @Description:
	 */
	public YdUserMerchant getYdUserMerchantByUserId(Integer userId);

	/**
	 * 得到所有用户商户绑定关系表YdUserMerchant
	 * @param ydUserMerchant
	 * @return 
	 * @Description:
	 */
	public List<YdUserMerchant> getAll(YdUserMerchant ydUserMerchant);

	/**
	 * 添加用户商户绑定关系表YdUserMerchant
	 * @param ydUserMerchant
	 * @Description:
	 */
	public void insertYdUserMerchant(YdUserMerchant ydUserMerchant);
	

	/**
	 * 通过id修改用户商户绑定关系表YdUserMerchant
	 * @param ydUserMerchant
	 * @Description:
	 */
	public void updateYdUserMerchant(YdUserMerchant ydUserMerchant);
	
}
