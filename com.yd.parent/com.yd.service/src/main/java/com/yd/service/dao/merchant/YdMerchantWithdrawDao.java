package com.yd.service.dao.merchant;

import java.util.List;
import com.yd.service.bean.merchant.YdMerchantWithdraw;
import org.apache.ibatis.annotations.Param;

/**
 * @Title:商户提现记录Dao接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-11 16:39:54
 * @Version:1.1.0
 */
public interface YdMerchantWithdrawDao {

	/**
	 * 通过id得到商户提现记录YdMerchantWithdraw
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantWithdraw getYdMerchantWithdrawById(Integer id);
	
	/**
	 * 获取数量
	 * @param ydMerchantWithdraw
	 * @return 
	 * @Description:
	 */
	public int getYdMerchantWithdrawCount(YdMerchantWithdraw ydMerchantWithdraw);
	
	/**
	 * 分页获取数据
	 * @param ydMerchantWithdraw
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantWithdraw> findYdMerchantWithdrawListByPage(@Param("params") YdMerchantWithdraw ydMerchantWithdraw,
                                                                     @Param("pageStart") Integer pageStart,
                                                                     @Param("pageSize") Integer pageSize);
	
	/**
	 * 得到所有商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdraw
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantWithdraw> getAll(YdMerchantWithdraw ydMerchantWithdraw);

	/**
	 * 添加商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdraw
	 * @Description:
	 */
	public void insertYdMerchantWithdraw(YdMerchantWithdraw ydMerchantWithdraw);
	
	/**
	 * 通过id修改商户提现记录YdMerchantWithdraw
	 * @param ydMerchantWithdraw
	 * @Description:
	 */
	public void updateYdMerchantWithdraw(YdMerchantWithdraw ydMerchantWithdraw);
	
}
