package com.yd.api.service.draw;

import java.util.List;

import com.yd.api.req.MerchantDrawReq;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.draw.YdMerchantDrawActivityResult;
import com.yd.core.utils.BusinessException;
import com.yd.core.utils.Page;
import com.yd.core.utils.PagerInfo;

/**
 * @Title:商户抽奖活动Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-12-04 11:33:33
 * @Version:1.1.0
 */
public interface YdMerchantDrawActivityService {

	/**
	 * 通过id得到商户抽奖活动YdMerchantDrawActivity
	 * @param id
	 * @return 
	 * @Description:
	 */
	public YdMerchantDrawActivityResult getYdMerchantDrawActivityById(Integer id);

	/**
	 * 通过uuid得到商户抽奖活动YdMerchantDrawActivity
	 * @param uuid
	 * @return
	 * @Description:
	 */
	public YdMerchantDrawActivityResult getYdMerchantDrawActivityByUdid(String uuid);

	/**
	 * 分页查询商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivityResult
	 * @param pagerInfo
	 * @return 
	 * @Description:
	 */
	public Page<YdMerchantDrawActivityResult> findYdMerchantDrawActivityListByPage(YdMerchantDrawActivityResult ydMerchantDrawActivityResult, PagerInfo pagerInfo) throws BusinessException;

	/**
	 * 得到所有商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivityResult
	 * @return 
	 * @Description:
	 */
	public List<YdMerchantDrawActivityResult> getAll(YdMerchantDrawActivityResult ydMerchantDrawActivityResult);

	/**
	 * 添加商户抽奖活动YdMerchantDrawActivity
	 * @param ydMerchantDrawActivityResult
	 * @Description:
	 */
	public void insertYdMerchantDrawActivity(YdMerchantDrawActivityResult ydMerchantDrawActivityResult) throws BusinessException;
	
	/**
	 * 通过id修改商户抽奖活动YdMerchantDrawActivity throws BusinessException;
	 * @param ydMerchantDrawActivityResult
	 * @Description:
	 */
	public void updateYdMerchantDrawActivity(YdMerchantDrawActivityResult ydMerchantDrawActivityResult) throws BusinessException;

	/**
	 * 查询活动详情
	 * @param id
	 * @return
	 */
	YdMerchantDrawActivityResult getActivityDetail(Integer id) throws BusinessException;

	/**
	 * 商户抽奖活动删除
	 * @param currMerchantId
	 * @param id
	 */
	void deleteYdMerchantDrawActivity(Integer currMerchantId, Integer id) throws BusinessException;

	/**
	 * 商户抽奖活动新增修改
	 * @param currMerchantId
	 * @param req
	 */
	void saveOrUpdate(Integer currMerchantId, MerchantDrawReq req) throws BusinessException;

	/**
	 * 上下架活动
	 * @param currMerchantId	商户id
	 * @param id	活动id
	 * @param isEnable	Y | N
	 * @throws BusinessException
	 */
	void upOrDownActivity(Integer currMerchantId, Integer id, String isEnable) throws BusinessException;

	/**
	 * 用户点击抽奖
	 * @param userId	 用户id
	 * @param uuid		 活动uuid
	 */
	Integer clickDraw(Integer userId, String uuid) throws BusinessException;

	/**
	 * 获取用户可抽奖次数
	 * @param userId	 用户id
	 * @param uuid		 活动uuid
	 * @return integer
	 * @throws BusinessException
	 */
    Integer getUserDrawCount(Integer userId, String uuid) throws BusinessException;

}
